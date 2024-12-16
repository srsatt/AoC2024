fun main() {


    fun parse(input: List<String>): List<List<Char>> {
        return input.map { it.toCharArray().toList() }
    }

    fun findField(input: List<List<Char>>, target: Char): Pair<Int, Int> {
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == target) return Pair(i, j)
            }
        }
        error("no start found")
    }

    fun getMetric(stack: List<Char>): Int {
        return stack.sumOf { if (it == 'F') 1 else 1000.toInt() }
    }

    data class Moment(
        val coord: Pair<Int, Int>,
        val direction: Pair<Int, Int>,
        val visited: Set<Pair<Int, Int>>,
        val weight: Int = 0
    )

    data class Record(val value: Int, val visited: MutableSet<Pair<Int, Int>>)


    fun solve(input: List<String>): MutableMap<Pair<Int, Int>, Record> {
        val field = parse(input)


        val start = findField(field, 'S')
        val end = findField(field, 'E')


        var edges = setOf<Moment>(Moment(start, Pair(0, 1), setOf(start)))
        val records = mutableMapOf<Pair<Int, Int>, Record>()

        while (edges.isNotEmpty()) {
            val newEdges = edges.flatMap { edge ->
                listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0).filter {
                    val next = edge.coord.first + it.first to edge.coord.second + it.second
                    val nextValue = field[next.first][next.second]
                    nextValue != '#'
                }.map {
                    val s = edge.direction.first * it.second - edge.direction.second * it.first
                    val step = if (s > 0) "LF" else if (s < 0) "RF" else "F"
                    val next = edge.coord.first + it.first to edge.coord.second + it.second
                    if (next == end) {
                        println("found end in map!")
                    }
                    Moment(
                        next,
                        it,
                        edge.visited + next,
                        edge.weight + if (edge.direction == it) 1 else 1001
                    )
                }.filter {
                    val curr = records[it.coord]
                    if (it.coord == end) {
                        println("updating end value $curr")
                    }
                    if (curr == null) {
                        records[it.coord] = Record(it.weight, it.visited.toMutableSet())
                        if (it.coord == end) println("found end!")
                        true
                    } else {
                        if (it.weight < curr.value) {
                            records[it.coord] = Record(it.weight, it.visited.toMutableSet())
                            true
                        } else if (it.weight == curr.value) {
                            println("updating weight ${it.coord} to ${it.weight}")
                            records[it.coord]!!.visited.addAll(it.visited)
                            true
                        } else {
                            false
                        }
                    }
                }
            }.toSet()
            edges = newEdges
        }


        return records
    }

    fun part1(input: List<String>): Int {

        val records = solve(input)

        val end = findField(parse(input), 'E')
        val res = records[end]?.value ?: error("no end found")
        println("""res of the part 1 is: ${res}""")

        return res
    }

    fun part2(input: List<String>): Int {
        val records = solve(input)

        val res = mutableSetOf<Pair<Int, Int>>()
        var curr = records[findField(parse(input), 'E')]!!.visited
        while (curr.isNotEmpty()) {
            val new = curr.flatMap { records[it]!!.visited }.toSet().filter { it !in res }
            println("curr! ${curr.size} new! ${new.size}")
            new.forEach {
                res.add(it)
            }
            curr = new.toMutableSet()
        }

        input.forEachIndexed { i, s ->
            s.mapIndexed { j, c ->
                if (res.contains(i to j)) 'O' else c
            }.joinToString("").println()
        }

        println("""res of the part 2 is: ${res.size}""")
        return res.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 7036)
    check(part2(testInput) == 45)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
