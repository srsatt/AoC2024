fun main() {
    fun mutate(value: ULong): List<ULong> {
        if (value == 0UL) {
            return listOf(1U)
        }
        val stringed = value.toString()
        if (stringed.length % 2 == 0) {
            return listOf(
                stringed.take(stringed.length / 2).toULong(),
                stringed.takeLast(stringed.length / 2).toULong()
            )
        }
        return listOf(value * 2024U)
    }

    val cache = mutableMapOf<Pair<Int, ULong>, List<ULong>>()

    fun mutateN(value: ULong, n: Int): List<ULong> {
        return cache.getOrPut(Pair(n, value)) {
            var res = listOf<ULong>(value)
            for (i in 1..n) {
                res = res.flatMap { mutate(it) }
            }
            res
        }
    }

    fun mutateDistinctN(list: List<Pair<Long, ULong>>, n: Int): List<Pair<Long, ULong>> {
        val mutatedValues = list.map { it.second }.map { mutateN(it, n) }
        val res = mutableMapOf<ULong, Long>()
        println("max value is ${list.maxOf { it.second }}")
        mutatedValues.zip(list.map { it.first }).forEach { (mutated, count) ->
            mutated.forEach { res.compute(it) { _, v -> v?.plus(count) ?: count } }
        }
        return res.map { Pair(it.value, it.key) }
    }


    fun part1(input: List<String>): Int {
        var stones = input[0].split(" ").map { it.toULong() }
        stones = stones.flatMap { mutateN(it, 25) }
        val res = stones.size
        println("""res of the part 1 is: $res""")
        return res
    }

    fun part2(input: List<String>): Long {
        var stones = input[0].split(" ").map { Pair(1L, it.toULong()) }
        for (i in 1..5) {
            println("step $i")
            stones = mutateDistinctN(stones, 15)
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
