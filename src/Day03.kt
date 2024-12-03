fun main() {
    fun parse(input: String): List<Pair<Int, Int>> {
        val regex = """(mul\(\d+,\d+\))""".toRegex()
        val numberRegex = """mul\((\d+),(\d+)\)""".toRegex()
        return regex.findAll(input).mapNotNull {
            val match = numberRegex.find(it.value)
            if (match != null) {
                val (first, second) = match.destructured
                Pair(first.toInt(), second.toInt())
            } else {
                null
            }
        }.toList()
    }


    fun part1(input: List<String>): Int {
        val res = parse(input.joinToString(" ")).sumOf { it.first * it.second }
        println("""res of the part 1 is: $res""")
        return res
    }

    fun part2(input: List<String>): Int {
        val regex = """do\(\)|don't\(\)|(mul\(\d+,\d+\))""".toRegex()
        val (_, res) = regex.findAll(input.joinToString(" ")).fold(Pair(true, 0)) { (enabled, sum), curr ->
            println(curr.value)
            when (curr.value) {
                "do()" -> Pair(true, sum)
                "don't()" -> Pair(false, sum)
                else -> {
                    val numberRegex = """mul\((\d+),(\d+)\)""".toRegex()
                    val match = numberRegex.find(curr.value)
                    if (match != null && enabled) {
                        val (first, second) = match.destructured
                        Pair(true, sum + first.toInt() * second.toInt())
                    } else {
                        Pair(enabled, sum)
                    }
                }
            }
        }
        println("""res of the part 2 is: $res""")
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    val testInput2 = readInput("Day03_2_test")
    check(part1(testInput) == 161)
    check(part2(testInput2) == 48)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
