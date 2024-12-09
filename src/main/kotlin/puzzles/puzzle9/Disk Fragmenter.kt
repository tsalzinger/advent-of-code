package me.salzinger.puzzles.puzzle9

object `Disk Fragmenter` {
    fun Sequence<String>.compactedFilesystemChecksum(): Long {
        return single()
            .map { "$it".toInt() }
            .toDiskBlocks()
            .compact()
            .checksum()
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
}