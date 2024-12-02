fun main() {

    fun strictValid(list: List<Int>): Boolean {
        val diffs = list.zipWithNext { a, b -> a - b }
        return diffs.all { it * diffs[0] > 0 && Math.abs(it) >= 1 && Math.abs(it) <= 3 }
    }

    fun listIsValid(list: List<Int>): Boolean {
        for (i in list.indices) {
            val poped = listOf(list.slice(0..<i), list.slice(i + 1..<list.size)).flatten()
            if (strictValid(poped)) return true
        }
        return false
    }

    fun part1(input: List<String>): Int {
        val res = input.map { it.split(" ").map { it.toInt() } }.count { strictValid(it) }
        println("""res of the part 1 is: $res""")
        return res
    }

    fun part2(input: List<String>): Int {
        val res = input.map { it.split(" ").map { it.toInt() } }.count { list ->
            listIsValid(list)
        }
        println("""res of the part 2 is: $res""")
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 5)

    val input = readInput("Day02")

    part1(input).println()
    part2(input).println()
}
