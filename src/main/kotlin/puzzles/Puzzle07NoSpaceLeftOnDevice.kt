package puzzles

import me.salzinger.common.solve
import me.salzinger.common.streamInput
import me.salzinger.common.writeToFile
import puzzles.Puzzle07NoSpaceLeftOnDevice.Part1.parseTerminalOutput
import java.nio.file.Path
import kotlin.io.path.Path

sealed interface TerminalCommand {
    data class ChangeDirectory(
        val targetDirectory: Path,
        val initialDirectory: Path? = null,
    ) : TerminalCommand {
        val fullPath: Path
            get() = if (initialDirectory == null) {
                targetDirectory.normalize()
            } else {
                initialDirectory.resolve(targetDirectory).normalize()
            }
    }

    sealed interface WithDirectoryContext : TerminalCommand {
        val directoryPath: Path

        data class List(
            override val directoryPath: Path,
            val entries: kotlin.collections.List<Entry>,
        ) : WithDirectoryContext {

            sealed interface Entry {
                data class Directory(val name: String) : Entry
                data class File(val name: String, val size: Int) : Entry
            }
        }
    }
}


inline fun <T> MutableList<T>.removeWhile(predicate: (T) -> Boolean): List<T> {
    return takeWhile(predicate).also {
        repeat(it.count()) {
            removeFirst()
        }
    }
}

data class DirectorySize(val path: Path, val size: Int)

class DirectorySizeCalculator(private val listCommands: List<TerminalCommand.WithDirectoryContext.List>) {
    private val directorySizes = mutableMapOf<Path, Int>()

    fun getDirectorySizeOf(path: Path): Int {
        return directorySizes[path] ?: listCommands
            .single { it.directoryPath == path }
            .let { listCommand ->
                listCommand
                    .entries
                    .sumOf {
                        when (it) {
                            is TerminalCommand.WithDirectoryContext.List.Entry.Directory -> getDirectorySizeOf(
                                path.resolve(
                                    it.name
                                )
                            )

                            is TerminalCommand.WithDirectoryContext.List.Entry.File -> it.size
                        }
                    }
                    .also {
                        directorySizes[path] = it
                    }

            }
    }
}

fun List<TerminalCommand.WithDirectoryContext.List>.getDirectorySizes(): List<DirectorySize> {
    val directorySizeCalculator = DirectorySizeCalculator(this)

    return map {
        DirectorySize(
            it.directoryPath,
            directorySizeCalculator.getDirectorySizeOf(it.directoryPath),
        )
    }
}

object Puzzle07NoSpaceLeftOnDevice {
    private const val MAX_DIRECTORY_SIZE = 100_000

    object Part1 {
        fun Sequence<String>.parseTerminalOutput(): Iterable<TerminalCommand> {
            val inputList = this@parseTerminalOutput.toMutableList()
            var currentPath: Path? = null

            return buildList {
                while (inputList.isNotEmpty()) {
                    val commandAndArguments = inputList.removeFirst().split(" ")

                    val (terminalCommand, nextPath) = parseTerminalCommand(commandAndArguments, currentPath, inputList)
                    add(terminalCommand)
                    println(terminalCommand)
                    currentPath = nextPath
                }
            }
        }

        fun Sequence<String>.solve(): Int {
            return parseTerminalOutput()
                .filterIsInstance<TerminalCommand.WithDirectoryContext.List>()
                .getDirectorySizes()
                .filter { it.size <= MAX_DIRECTORY_SIZE }
                .sumOf { it.size }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            7.solve(1) {
                asSequence().solve().toString()
            }
        }
    }


    object Part2 {
        private const val TOTAL_DISK_SPACE = 70000000
        private const val REQUIRED_FREE_SPACE = 30000000
        fun Sequence<String>.solve(): Int {
            return parseTerminalOutput()
                .filterIsInstance<TerminalCommand.WithDirectoryContext.List>()
                .getDirectorySizes()
                .run {
                    val freeDiskSpace = TOTAL_DISK_SPACE - single { it.path == Path("/") }.size

                    val minimumDirectorySize = REQUIRED_FREE_SPACE - freeDiskSpace

                    filter {
                        it.size >= minimumDirectorySize
                    }
                }
                .minBy { it.size }
                .size
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-7-2.in"
                .streamInput()
                .solve()
                .writeToFile("puzzle-7-2.out")
        }
    }

    private fun parseTerminalCommand(
        commandAndArguments: List<String>,
        currentPath: Path?,
        inputList: MutableList<String>,
    ): Pair<TerminalCommand, Path> {
        check(commandAndArguments.first() == "$") {
            "Input does not start with a command: $commandAndArguments"
        }

        return when (val command = commandAndArguments[1]) {
            "cd" -> {
                TerminalCommand.ChangeDirectory(
                    targetDirectory = Path(commandAndArguments[2]),
                    initialDirectory = currentPath
                ).run {
                    this to this.fullPath
                }
            }

            "ls" -> {
                TerminalCommand.WithDirectoryContext.List(
                    directoryPath = currentPath
                        ?: throw RuntimeException("List command requires a directory path but non is available"),
                    entries = inputList
                        .removeWhile {
                            !it.startsWith("$ ")
                        }
                        .map { it.split(" ") }
                        .map { (first, second) ->
                            if (first == "dir") {
                                TerminalCommand.WithDirectoryContext.List.Entry.Directory(name = second)
                            } else {
                                TerminalCommand.WithDirectoryContext.List.Entry.File(
                                    name = second,
                                    size = first.toInt()
                                )
                            }
                        }
                ) to currentPath
            }

            else -> throw RuntimeException("Unknown command encountered: $command")
        }
    }
}
