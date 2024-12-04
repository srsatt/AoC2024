fun main() {
    val length = "XMAS".length
    fun getWord(field: List<List<String>>, point: Pair<Int, Int>, vector: Pair<Int, Int>): String {
        var res = ""
        for (i in 0..<length) {
            res += field.getOrNull(point.first + vector.first * i)?.getOrNull(point.second + vector.second * i) ?: ""
        }
        return res
    }

    fun getVectors(): List<Pair<Int, Int>> {
        val values = listOf(-1, 0, 1)
        val vectors = mutableListOf<Pair<Int, Int>>()
        for (x in values) {
            for (y in values) {
                vectors.add(Pair(x, y))
            }
        }
        return vectors
    }

    fun getDiagonalsVectors(): List<Pair<Int, Int>> {
        val values = listOf(-1, 1)
        val vectors = mutableListOf<Pair<Int, Int>>()
        for (x in values) {
            for (y in values) {
                vectors.add(Pair(x, y))
            }
        }
        return vectors
    }

    fun getPointValue(field: List<List<String>>, point: Pair<Int, Int>): String? {
        return field.getOrNull(point.first)?.getOrNull(point.second)
    }

    fun getDiagonalValues(field: List<List<String>>, point: Pair<Int, Int>): List<String> {
        val res = mutableListOf<String>()
        for (vector in getDiagonalsVectors()) {
            val value = getPointValue(field, Pair(point.first + vector.first, point.second + vector.second))
            if (value != null) res.add(
                value
            )
        }
        return res
    }

    fun part1(input: List<String>): Int {
        val field = input.map { it.split("") }
        var res = 0
        field.forEachIndexed { row, list ->
            list.forEachIndexed { col, s ->
                for (vector in getVectors()) {
                    if (getWord(field, Pair(row, col), vector) == "XMAS") {
                        res++
                    }
                }

            }
        }

        println("""res of the part 2 is: $res""")
        return res
    }

    fun part2(input: List<String>): Int {
        val field = input.map { it.split("") }
        var res = 0
        field.forEachIndexed { row, list ->
            list.forEachIndexed { col, s ->
                val point = Pair(col, row)
                val value = getPointValue(field, point)
                if (value == "A") {
                    val diagonals = getDiagonalValues(field, point)
                    if (diagonals.count { it == "M" } == 2 && diagonals.count { it == "S" } == 2 && diagonals[0] != diagonals[3]) {
                        res++
                    }
                }
            }
        }

        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    println(getDiagonalsVectors().joinToString(" "))
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)


    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
