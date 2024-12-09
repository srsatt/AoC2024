sealed class Disk {
    object Free : Disk()
    data class Block(val value: Int) : Disk()
}

fun main() {
    fun parse(input: String): Triple<MutableList<Disk>, MutableMap<Int, Int>, MutableMap<Int, Int>> {
        val freeIndxes = mutableMapOf<Int, Int>()
        val blockIndexes = mutableMapOf<Int, Int>()
        val blocks = mutableListOf<Disk>()
        var fileId = 0
        input.toList().map { it.toString().toInt() }.mapIndexed { indx, value ->
            if (indx % 2 == 0) {
                blockIndexes[blocks.size] = value
                for (i in 0..<value) {
                    blocks.add(Disk.Block(fileId))
                }
                fileId++
            } else {
                freeIndxes[blocks.size] = value
                for (i in 0..<value) {
                    blocks.add(Disk.Free)
                }
            }
        }
        return Triple(blocks, freeIndxes, blockIndexes)
    }

    fun hash(list: List<Disk>): Long {
        return list.foldIndexed(0) { index: Int, acc: Long, disk: Disk ->
            when (disk) {
                is Disk.Block -> acc + disk.value * index
                is Disk.Free -> acc
            }
        }
    }

    fun part1(input: List<String>): Long {
        val (blocks, freeIndxesMap) = parse(input[0])
        val freeIndxes = freeIndxesMap.toSortedMap().toList().fold(mutableListOf<Int>()) { acc, (indx, value) ->
            for (i in 0..<value) {
                acc.add(indx + i)
            }
            acc
        }
        while (freeIndxes.isNotEmpty()) {
            var block = blocks.removeLast()
            while (block is Disk.Free) {
                block = blocks.removeLast()
            }
            val nextFreeIndex = freeIndxes.removeFirst()
            if (nextFreeIndex < blocks.size) {
                blocks[nextFreeIndex] = block
            } else {
                blocks.add(block)
            }
        }


        val res = hash(blocks)
        println("""res of the part 1 is: $res""")
        return res
    }

    fun part2(input: List<String>): Long {
        var (blocks, freeIndexes, blockIndexes) = parse(input[0])
        val blockMap = blockIndexes.toSortedMap()

        while (blockMap.isNotEmpty()) {
            val blockOffset = blockMap.lastKey()
            val blockSize = blockMap[blockOffset]!!
            blockMap.remove(blockOffset)
            var newOffset = 0
            for (i in 0..<blockOffset) {
                if (blocks.slice(i..<i + blockSize).all { it is Disk.Free }) {
                    newOffset = i
                    break
                }
            }
            if (newOffset == 0) continue
            for (i in 0..<blockSize) {
                val value = blocks[blockOffset + i]
                blocks[newOffset + i] = value
                blocks[blockOffset + i] = Disk.Free
            }
        }



        return hash(blocks)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
