fun main() {
    fun generatePermutations(length: Int, value: List<String>): Sequence<List<String>> {
        return sequence<List<String>> {
            if (length == 1) {
                yieldAll(value.map { listOf(it) })
            } else {
                generatePermutations(length - 1, value).forEach { seq ->
                    yieldAll(value.map { listOf(it) + seq })
                }
            }
        }
    }

    fun parseInput(input: List<String>): List<Pair<Long, List<Long>>> {
        return input.map { s ->
            val split = s.split(": ")
            Pair(split[0].toLong(), split[1].split(" ").map { it.toLong() })
        }
    }

    fun operationSequenceMatchesTarget(pair: Pair<Long, List<Long>>, operators: List<String>): Boolean {
        return generatePermutations(pair.second.size - 1, operators).any { seq ->
            seq.zip(pair.second.drop(1)).fold(pair.second[0]) { acc, (action, value) ->
                when (action) {
                    "+" -> acc + value
                    "*" -> acc * value
                    "|" -> (acc.toString() + value.toString()).toLong()
                    else -> acc
                }
            } == pair.first
        }
    }

    fun part1(input: List<String>): Long {
        val values = parseInput(input)
        val operators = listOf("+", "*")
        val result = values.filter { operationSequenceMatchesTarget(it, operators) }.sumOf { it.first }
        println("""Result of part 1 is: $result""")
        return result
    }

    fun part2(input: List<String>): Long {
        val values = parseInput(input)
        val operators = listOf("+", "*", "|")
        val result = values.filter { operationSequenceMatchesTarget(it, operators) }.sumOf { it.first }
        println("""Result of part 2 is: $result""")
        return result
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
