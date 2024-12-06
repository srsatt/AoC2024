sealed class Result {
    object Cycle : Result()
    data class Break(val value: Int) : Result()
}

fun main() {
    fun getStart(input: List<List<String>>): Pair<Int, Int> {
        var row: Int? = null
        var col: Int? = null
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == "^") {
                    row = i
                    col = j
                }
            }
        }
        return Pair(row!!, col!!)
    }

    //   -1
    // -1   1
    //    1
    fun getNextDir(dir: Pair<Int, Int>): Pair<Int, Int> {
        return when (dir) {
            Pair(-1, 0) -> Pair(0, 1)
            Pair(0, 1) -> Pair(1, 0)
            Pair(1, 0) -> Pair(0, -1)
            Pair(0, -1) -> Pair(-1, 0)
            else -> throw Exception("Invalid direction")
        }
    }

    fun iterateField(field: List<List<String>>): Result {
        val start = getStart(field)
        val visited = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>()

        var curr = Pair(start, Pair(-1, 0))
        while (true) {
            val (coord, dir) = curr
            if (visited.computeIfAbsent(coord) { mutableSetOf() }.contains(dir)) {
                return Result.Cycle
            }
            visited[coord]!!.add(dir)
            val nextCoord = field.getOrNull(coord.first + dir.first)?.getOrNull(coord.second + dir.second)
                ?: return Result.Break(visited.size)
            if (nextCoord == "#") {
                curr = Pair(coord, getNextDir(dir))
                continue
            }
            curr = Pair(Pair(coord.first + dir.first, coord.second + dir.second), dir)
        }
    }

    fun part1(input: List<String>): Int {
        val field = input.map { it.split("") }
        val result = iterateField(field)

        when (result) {
            Result.Cycle -> return 0
            is Result.Break -> return result.value
        }
    }

    fun part2(input: List<String>): Int {
        var cycleCount = 0
        val field = input.map { it.toList().map(Char::toString) }
        for (row in field.indices) {
            for (col in field[row].indices) {
                if (field[row][col] == "#" || field[row][col] == "^") continue
                val newField = field.map { it.toMutableList() }.toMutableList()
                newField[row][col] = "#"
                val result = iterateField(newField)
                if (result is Result.Cycle) {
                    cycleCount++
                }
            }
        }
        println("""res of the part 2 is: $cycleCount""")
        return cycleCount
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
