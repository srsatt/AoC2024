fun main() {
    fun parse(input: List<String>, size: Int, take: Int): List<List<Char>> {
        val field = mutableListOf<MutableList<Char>>()
        for (i in 0..<size) {
            field.add((0..<size).map { '.' }.toMutableList())
        }
        val regex = """(\d+),(\d+)""".toRegex()
        input.take(take).forEach {
            val (firstNum, secondNum) = regex.find(it)!!.destructured
            val (x, y) = Pair(firstNum.toInt(), secondNum.toInt())
            field[y][x] = '#'
        }
        return field

    }

    fun print(field: List<List<Char>>) {
        field.forEach { println(it.joinToString("")) }
    }

    fun getNeighbors(coord: Pair<Int, Int>): List<Pair<Int, Int>> {
        return listOf(
            Pair(coord.first - 1, coord.second),
            Pair(coord.first + 1, coord.second),
            Pair(coord.first, coord.second - 1),
            Pair(coord.first, coord.second + 1)
        )
    }

    fun getPossibleDirections(coord: Pair<Int, Int>, field: List<List<Char>>): List<Pair<Int, Int>> {
        return getNeighbors(coord).filter {
            field.getOrNull(it.first)?.getOrNull(it.second) == '.'
        }
    }

    fun part1(input: List<String>, size: Int, take: Int): Int? {
        val field = parse(input, size, take)
        print(field)

        val visited = mutableSetOf<Pair<Int, Int>>()
        val current = mutableSetOf(Pair(0, 0))
        val finish = Pair(size - 1, size - 1)
        var length = 0


        while (visited.contains(finish).not()) {
            val next = current.flatMap { getPossibleDirections(it, field) }.filter { !visited.contains(it) }.toSet()
            current.clear()
            current.addAll(next)
            println(next)
            if (next.size == 0) {
                return null
            }
            visited.addAll(current)
            length++
        }


        return length
    }

    fun part2(input: List<String>, size: Int): String {

        var floor = 0
        var ceil = input.size - 1

        while (ceil - floor > 1) {
            val mid = floor + (ceil - floor) / 2
            val res = part1(input, size, mid)
            if (res == null) {
                ceil = mid
            } else {
                floor = mid
            }
        }


        val res = floor
        println("""res of the part 2 is: ${input[res]}""")
        return input[res]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput, 7, 12) == 22)
    check(part2(testInput, 7) == "6,1")

    val input = readInput("Day18")
    part1(input, 71, 1024).println()
    part2(input, 71).println()
}
