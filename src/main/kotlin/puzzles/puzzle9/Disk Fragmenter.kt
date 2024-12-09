package me.salzinger.puzzles.puzzle9

object `Disk Fragmenter` {
    fun Sequence<String>.compactedFilesystemChecksum(): Long {
        return single()
            .map { "$it".toInt() }
            .toDiskBlocks()
            .compact()
            .checksum()
    }

    fun Sequence<String>.compactedUnfragmentedFilesystemChecksum(): Long {
        return single()
            .map { "$it".toInt() }
            .toDiskBlockGroups()
            .compactUnfragmented()
            .flatMap { diskBlockGroup ->
                List(diskBlockGroup.length) {
                    diskBlockGroup.diskBlock
                }
            }
            .checksum()
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