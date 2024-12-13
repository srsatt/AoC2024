import kotlin.math.floor

fun main() {
    data class Game(val A: Pair<Long, Long>, val B: Pair<Long, Long>, val prize: Pair<Long, Long>)

    fun parse(input: List<String>, margin: Long = 0L): List<Game> {
        val games = mutableListOf<Game>()
        input.chunked(4) {
            val (strA, strB, strPrize) = it
            val regex = """X[\+=](\d+), Y[\+=](\d+)""".toRegex()
            val (aX, aY) = regex.find(strA)!!.destructured
            val (bX, bY) = regex.find(strB)!!.destructured
            println("$strA $strB $strPrize")
            val (prizeX, prizeY) = regex.find(strPrize)!!.destructured
            games.add(
                Game(
                    Pair(aX.toLong(), aY.toLong()),
                    Pair(bX.toLong(), bY.toLong()),
                    Pair(prizeX.toLong() + margin, prizeY.toLong() + margin)
                )
            )
        }
        return games
    }

    fun solveLinearEquations(
        first: Triple<Long, Long, Long>,
        second: Triple<Long, Long, Long>
    ): Pair<Long, Long>? {
        val (a1, b1, c1) = first
        val (a2, b2, c2) = second
        val det = a1 * b2 - a2 * b1
        if (det == 0L) return null

        val x = (c1 * b2 - c2 * b1) / det
        val y = (a1 * c2 - a2 * c1) / det

        return Pair(x, y)
    }


    fun solveGame(game: Game): List<Pair<Long, Long>> {
        val wins = mutableListOf<Pair<Long, Long>>()
        var i = Math.max(floor((game.prize.first / game.B.first).toDouble()) - 300000000L, 0.0).toLong()
        while (wins.isEmpty() && i * game.B.first <= game.prize.first && i * game.B.second <= game.prize.second) {
            val modeFirst = (game.prize.first - game.B.first * i).mod(game.A.first)
            val modeSecond = (game.prize.second - game.B.second * i).mod(game.A.second)
            if (modeFirst == 0L && modeSecond == 0L && (game.prize.first - game.B.first * i) / game.A.first ==
                (game.prize.second - game.B.second * i) / game.A.second
            ) {
                wins.add(Pair((game.prize.second - game.B.second * i) / game.A.second, i))
            }
            i++
        }
        println("res of game: $wins")
        return wins
    }

    fun solveGame2(game: Game): List<Pair<Long, Long>> {
        val wins = mutableListOf<Pair<Long, Long>>()
        val res = solveLinearEquations(
            Triple(game.A.first, game.B.first, game.prize.first),
            Triple(game.A.second, game.B.second, game.prize.second)
        )
        if (res == null) {
            return wins
        }
        if (res.first * game.A.first + res.second * game.B.first == game.prize.first && res.first * game.A.second + res.second * game.B.second == game.prize.second) {
            println("res of game: $res")
            return listOf(res)
        }
        return wins
    }


    fun part1(input: List<String>): Long {
        val games = parse(input)

        val res = games.mapIndexed { index, it ->
            println("""solving game $index""")
            solveGame(it)
        }.sumOf {
            if (it.isEmpty()) 0 else it.minOf { win -> 3 * win.first + win.second }
        }
        println("""res of the part 1 is: $res""")
        return res
    }

    fun part2(input: List<String>): Long {
        val games = parse(input, 10000000000000L)

        val res = games.mapIndexed { index, it ->
            println("""solving game $index""")
            solveGame2(it)
        }.sumOf {
            if (it.isEmpty()) 0 else it.minOf { win -> 3 * win.first + win.second }
        }

        println("""res of the part 2 is: $res""")
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 480L)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
