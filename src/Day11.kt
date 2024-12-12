import kotlin.math.floor
import kotlin.math.log10

fun main() {

    val cacheV = mutableMapOf<Long, List<Long>>()
    fun mutate(value: Long): List<Long> {
        return cacheV.getOrPut(value) {

            if (value == 0L) {
                return listOf(1L)
            }
            val digits = (floor(log10(value.toDouble())) + 1).toInt()
            if (digits % 2 == 0) {
                val powered = Math.pow(10.0, (digits / 2).toDouble())
                val modValue = (value % powered).toLong()
                return listOf(
                    modValue, ((value - modValue) / powered).toLong(),
                )
            }
            return listOf(value * 2024L)
        }
    }

    val cache = mutableMapOf<Pair<Int, Long>, List<Long>>()

    fun mutateN(value: Long, n: Int): List<Long> {
        return cache.getOrPut(Pair(n, value)) {
            var res = listOf<Long>(value)
            for (i in 1..n) {
                res = res.flatMap { mutate(it) }
            }
            res
        }
    }

    fun updateResult(res: MutableMap<Long, Long>, values: List<Long>, count: Long) {
        values.forEach { value ->
            res.compute(value) { _, existing -> existing?.plus(count) ?: count }
        }
    }

    // Main function refactored
    fun mutateDistinctN(list: List<Pair<Long, Long>>, n: Int): List<Pair<Long, Long>> {
        val mutatedValuesWithCounts = list.map { it.first to mutateN(it.second, n) }
        val result = mutableMapOf<Long, Long>()

        mutatedValuesWithCounts.forEach { (count, mutatedValues) ->
            updateResult(result, mutatedValues, count)
        }

        return result.map { Pair(it.value, it.key) }
    }


    fun part1(input: List<String>): Int {
        var stones = input[0].split(" ").map { it.toLong() }
        stones = stones.flatMap { mutateN(it, 25) }
        val res = stones.size
        println("""res of the part 1 is: $res""")
        return res
    }

    fun part2(input: List<String>): Long {
        var stones = input[0].split(" ").map { Pair(1L, it.toLong()) }
        for (i in 1..25) {
            stones = mutateDistinctN(stones, 3)
            val size = stones.size
            println("step $i: $size")
        }
        val res = stones.sumOf { it.first }
        println("""res of the part 1 is: $res""")
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 55312)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
