fun main() {

    fun parse(input: List<String>): Map<String, List<Pair<Int, Int>>> {
        val res = mutableMapOf<String, MutableList<Pair<Int, Int>>>()
        input.forEachIndexed { index, s ->
            s.toList().forEachIndexed { index2, char ->
                val s2 = char.toString()
                if (s2 != "." && s2 != "#") {
                    res.getOrPut(s2, { emptyList<Pair<Int, Int>>().toMutableList() }).add(Pair(index, index2))
                }
            }
        }
        return res
    }

    fun getNodesForList(list: List<Pair<Int, Int>>): Set<Pair<Int, Int>> {
        val res = mutableSetOf<Pair<Int, Int>>()
        list.forEach { nodeA ->

            list.forEach { nodeB ->
                if (nodeA != nodeB) {
                    res.add(Pair(nodeA.first - nodeB.first + nodeA.first, nodeA.second - nodeB.second + nodeA.second))
                    res.add(Pair(nodeB.first - nodeA.first + nodeB.first, nodeB.second - nodeA.second + nodeB.second))
                }
            }
        }
        return res
    }

    fun getNodesForDirection(
        nodeA: Pair<Int, Int>,
        nodeB: Pair<Int, Int>,
        ranges: Pair<IntRange, IntRange>
    ): Set<Pair<Int, Int>> {
        val res = mutableSetOf<Pair<Int, Int>>()
        val dxy = Pair(nodeB.first - nodeA.first, nodeB.second - nodeA.second)
        var count = 0
        while (true) {
            val newNode = Pair(nodeB.first + dxy.first * count, nodeB.second + dxy.second * count)
            if (newNode.first !in ranges.first || newNode.second !in ranges.second) {
                break
            }
            count++
            res.add(newNode)
        }
        return res
    }

    fun getNodesForList2(list: List<Pair<Int, Int>>, ranges: Pair<IntRange, IntRange>): Set<Pair<Int, Int>> {
        val res = mutableSetOf<Pair<Int, Int>>()
        list.forEach { nodeA ->
            list.forEach { nodeB ->
                if (nodeA != nodeB) {
                    getNodesForDirection(nodeA, nodeB, ranges).forEach { res.add(it) }
                    getNodesForDirection(nodeB, nodeA, ranges).forEach { res.add(it) }
                }
            }
        }
        return res
    }

    fun part1(input: List<String>): Int {
        val map = parse(input)
        val nodes = mutableSetOf<Pair<Int, Int>>()
        map.forEach { (_, list) ->
            nodes.addAll(getNodesForList(list).filter {
                it.first in input.indices && it.second in 0 until input[0].length
            })
        }
        val res = nodes.size
        println("""res of the part 1 is: $res""")
        return res
    }

    fun part2(input: List<String>): Set<Pair<Int, Int>> {
        val map = parse(input)
        val nodes = mutableSetOf<Pair<Int, Int>>()
        map.forEach { (_, list) ->
            nodes.addAll(getNodesForList2(list, Pair(input.indices, 0 until input[0].length)))
        }
        val res = nodes.size
        println("""res of the part 2 is: $res""")

        return nodes
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput).size == 34)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
