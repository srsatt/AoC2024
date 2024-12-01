import kotlin.math.abs

fun main() {
    fun parse(input: List<String>): Pair<List<Int>, List<Int>> {
        val first = mutableListOf<Int>()
        val second = mutableListOf<Int>()
        val regex = """(\d+)\s+(\d+)""".toRegex()
        input.forEach {
            val match = regex.find(it)
            if (match != null) {
                val (firstNum, secondNum) = match.destructured
                first.add(firstNum.toInt())
                second.add(secondNum.toInt())
            }
        }
        return Pair(first.toList(), second.toList())
    }

    fun part1(input: List<String>): Int {
        val (first, second) = parse(input)
        val firstSorted = first.sorted()
        val secondSorted = second.sorted()

        val res = firstSorted.zip(secondSorted).sumOf { (first, second) -> abs(first - second) }

        println("""res of the part 1 is: $res""")
        return res
    }

    fun part2(input: List<String>): Int {
        val (first, second) = parse(input)
        val firstSorted = first.sorted()
        val secondMap = second.groupingBy { it }.eachCount()
        val res = firstSorted.sumOf { it -> (secondMap[it] ?: 0) * it }
        return res
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)

    val input = readInput("Day01")
    part1(input).println()

    check(part2(testInput) == 31)

    part2(input).println()
}
