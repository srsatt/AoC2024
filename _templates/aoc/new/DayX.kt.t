---
to: src/Day<%= day %>.kt
---
fun main() {
    fun part1(input: List<String>): Int {
        val res = input.size
        println("""res of the part 1 is: $res""")
        return res
    }

    fun part2(input: List<String>): Int {
        val res = input.size
        println("""res of the part 2 is: $res""")
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day<%= day %>_test")
    check(part1(testInput) == 1)

    val input = readInput("Day<%= day %>")
    part1(input).println()
    part2(input).println()
}
