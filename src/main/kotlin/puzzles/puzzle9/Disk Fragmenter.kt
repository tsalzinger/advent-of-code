package me.salzinger.puzzles.puzzle9

object `Disk Fragmenter` {
    fun Sequence<String>.compactedFilesystemChecksum(): Long {
        return single()
            .map { "$it".toInt() }
            .toDiskBlocks()
            .compact()
            .checksum()
    }

    fun Sequence<String>.compactUnfragmentedUnfragmentedFilesystemChecksum(): Long {
        val diskMap = single()
            .map { "$it".toInt() }

        val fileList = diskMap
            .chunked(2)
            .mapIndexed { index, chunk ->
                File(
                    id = index,
                    length = chunk.first(),
                ).apply {
                    freeSpaceAfter = chunk.getOrNull(1) ?: 0
                }
            }
            .windowed(2)
            .onEach { (first, second) ->
                first.nextFile = second
                second.previousFile = first
            }
            .first()
            .first()

        fileList
            .reversed()
            .onEach { file ->
                fileList
                    .firstOrNull {
                        if (it == file) {
                            return@onEach
                        }
                        it.freeSpaceAfter >= file.length
                    }
                    ?.also {
                        file.moveTo(it)
                    }
            }

        return fileList.flatMap { file ->
            List(file.length) {
                DiskBlock.File(file.id)
            } + List(file.freeSpaceAfter) {
                DiskBlock.FreeSpace
            }
        }.checksum()
    }

    class File(
        val id: Int,
        val length: Int,
    ) : Iterable<File> {
        var nextFile: File? = null
        var freeSpaceAfter: Int = 0

        var previousFile: File? = null
        val freeSpaceBefore: Int
            get() = previousFile?.freeSpaceAfter ?: 0

        override fun toString(): String {
            val file = List(length) { id }
            val free = List(freeSpaceAfter) { "." }
                .takeUnless { it.isEmpty() }

            return "$file${free ?: ""}${nextFile ?: ""}"
        }

        override fun iterator(): Iterator<File> {
            return object : Iterator<File> {
                private var currentFile: File? = this@File

                override fun hasNext() = currentFile != null

                override fun next(): File = currentFile!!.also {
                    currentFile = currentFile?.nextFile
                }
            }
        }

        fun moveTo(space: File) {
            check(space.freeSpaceAfter >= length) {}

            // cut out
            nextFile?.previousFile = previousFile
            previousFile?.nextFile = nextFile
            previousFile?.freeSpaceAfter += (freeSpaceAfter + length)

            // insert
            freeSpaceAfter = space.freeSpaceAfter - length
            previousFile = space
            nextFile = space.nextFile
            nextFile?.previousFile = this
            space.nextFile = this
            space.freeSpaceAfter = 0
        }
    }

    private fun List<DiskBlockGroup>.compactUnfragmented(): List<DiskBlockGroup> {
        val alreadyEvaluatedBlocks = mutableSetOf<DiskBlockGroup>()
        var fileBlocKToMove = lastOrNull { it.diskBlock is DiskBlock.File }
        val compacted = toMutableList()

        while (fileBlocKToMove != null) {
            alreadyEvaluatedBlocks += fileBlocKToMove
            val canFit = compacted.firstOrNull { it.canFit(fileBlocKToMove) }

            if (canFit != null) {
                val indexToMove = compacted.indexOf(fileBlocKToMove)
                compacted.removeAt(indexToMove)
                compacted.add(indexToMove, DiskBlockGroup(DiskBlock.FreeSpace, fileBlocKToMove.length))

                val indexToReplace = compacted.indexOf(canFit)
                compacted.removeAt(indexToReplace)
                compacted.addAll(indexToReplace, canFit.insert(fileBlocKToMove))
            }

            fileBlocKToMove = lastOrNull { it.diskBlock is DiskBlock.File && it !in alreadyEvaluatedBlocks }
        }

        return compacted
    }

    fun List<Int>.toDiskBlocks(): List<DiskBlock> {
        val diskMap = this
        return buildList {
            diskMap
                .forEachIndexed { index, blockLength ->
                    if (index % 2 == 0) {
                        val file = DiskBlock.File(id = index / 2)
                        repeat(blockLength) {
                            add(file)
                        }
                    } else {
                        repeat(blockLength) {
                            add(DiskBlock.FreeSpace)
                        }
                    }
                }
        }
    }

    fun List<Int>.toDiskBlockGroups(): List<DiskBlockGroup> {
        val diskMap = this
        return buildList {
            diskMap.forEachIndexed { index, blockLength ->
                if (index % 2 == 0) {
                    add(
                        DiskBlockGroup(
                            diskBlock = DiskBlock.File(id = index / 2),
                            length = blockLength,
                        )
                    )
                } else {
                    add(
                        DiskBlockGroup(
                            diskBlock = DiskBlock.FreeSpace,
                            length = blockLength,
                        )
                    )
                }
            }
        }
    }

    fun List<DiskBlock>.compact(): List<DiskBlock> {
        val reverse = toMutableList()
        var forwardIndex = 0
        var reverseIndex = size - 1

        val compactedList = mutableListOf<DiskBlock>()

        while (forwardIndex < reverseIndex && forwardIndex < size) {
            when (val next = get(forwardIndex)) {
                is DiskBlock.File -> compactedList += next
                DiskBlock.FreeSpace -> {
                    var diskBlock = reverse[reverseIndex--]

                    while (diskBlock == DiskBlock.FreeSpace) {
                        diskBlock = reverse[reverseIndex--]
                    }

                    compactedList += diskBlock
                }
            }
            forwardIndex++
        }

        return compactedList
    }

    fun List<DiskBlock>.checksum(): Long {
        return foldIndexed(0L) { index, checksum, diskBlock ->
            checksum + when (diskBlock) {
                is DiskBlock.File -> diskBlock.id * index
                DiskBlock.FreeSpace -> 0
            }
        }
    }

    sealed interface DiskBlock {
        object FreeSpace : DiskBlock

        data class File(val id: Int) : DiskBlock
    }

    data class DiskBlockGroup(
        val diskBlock: DiskBlock,
        val length: Int,
    ) {
        fun canFit(diskBlockGroup: DiskBlockGroup): Boolean {
            check(diskBlockGroup.diskBlock is DiskBlock.File) {
                "Only Files can fit into free blocks"
            }

            return when (diskBlock) {
                DiskBlock.FreeSpace -> length >= diskBlockGroup.length
                is DiskBlock.File -> false
            }
        }

        fun insert(diskBlockGroup: DiskBlockGroup): List<DiskBlockGroup> {
            check(canFit(diskBlockGroup)) {
                "does not fit"
            }

            return if (length == diskBlockGroup.length) {
                listOf(diskBlockGroup)
            } else {
                listOf(
                    diskBlockGroup,
                    copy(length = length - diskBlockGroup.length)
                )
            }
        }
    }
}