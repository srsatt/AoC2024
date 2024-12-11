fun main() {
    fun mutate(value: Long): List<Long> {
        if (value == 0L) {
            return listOf(1L)
        }
        val stringed = value.toString()
        if (stringed.length % 2 == 0) {
            return listOf(
                stringed.take(stringed.length / 2).toLong(),
                stringed.takeLast(stringed.length / 2).toLong()
            )
        }
        return listOf(value * 2024L)
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

    fun mutateDistinctN(list: List<Pair<Long, Long>>, n: Int): List<Pair<Long, Long>> {
        val mutatedValues = list.map { it.second }.map { mutateN(it, n) }
        val res = mutableMapOf<Long, Long>()
        mutatedValues.zip(list.map { it.first }).forEach { (mutated, count) ->
            mutated.forEach { res.compute(it) { _, v -> v?.plus(count) ?: count } }
        }
        return res.map { Pair(it.value, it.key) }
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
        for (i in 1..15) {
            stones = mutateDistinctN(stones, 5)
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
