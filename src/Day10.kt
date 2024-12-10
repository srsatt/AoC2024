fun main() {
    data class Point(val height: Int, val coords: Pair<Int, Int>)

    fun parse(input: List<String>): List<List<Point>> {
        return input.mapIndexed(
            { index, line ->
                line.split("").filter(String::isNotBlank).mapIndexed(
                    { index2, s ->
                        Point(
                            s.toInt(), Pair(index, index2)
                        )
                    }
                )
            })
    }

    fun nextDirection(point: Point, map: List<List<Point>>): Sequence<Point> {
        val dir = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))
        return sequence {
            dir.forEach {
                val next = map.getOrNull(point.coords.first + it.first)?.getOrNull(point.coords.second + it.second)
                if (next != null && next.height == point.height + 1) {
                    yield(next)
                }
            }
        }
    }


    fun getDestinations(point: Point, map: List<List<Point>>, filterDistinct: Boolean): Int {
        var curr = listOf(point)
        while (curr.isNotEmpty() && curr[0].height != 9) {
            curr = curr.flatMap { nextDirection(it, map) }.let { if (filterDistinct) it.distinct() else it }
        }
        return curr.size
    }

    fun part1(input: List<String>): Int {
        val map = parse(input)
        var res = 0
        map.forEach { row ->
            row.forEach { point ->
                if (point.height == 0) {
                    res += getDestinations(point, map, true)
                }
            }
        }

        println("""res of the part 1 is: $res""")
        return res
    }

    fun part2(input: List<String>): Int {
        val map = parse(input)
        var res = 0
        map.forEach { row ->
            row.forEach { point ->
                if (point.height == 0) {
                    res += getDestinations(point, map, false)
                }
            }
        }

        println("""res of the part 2 is: $res""")
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
