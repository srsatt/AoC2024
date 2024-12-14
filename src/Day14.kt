data class Robot(var coord: Pair<Int, Int>, val velocity: Pair<Int, Int>)

fun main() {
    fun parse(input: List<String>): List<Robot> {
        val regex = """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".toRegex()
        return input.map {
            val match = regex.find(it)
            if (match != null) {
                val (x, y, vx, vy) = match.destructured
                Robot(Pair(x.toInt(), y.toInt()), Pair(vx.toInt(), vy.toInt()))
            } else {
                println(it)
                throw IllegalArgumentException("Invalid input")
            }
        }
    }

    fun tick(r: Robot, fieldSize: Pair<Int, Int>) {
        r.coord = Pair(
            (r.coord.first + r.velocity.first).mod(fieldSize.first),
            (r.coord.second + r.velocity.second).mod(fieldSize.second)
        )
    }

    fun printField(robots: List<Robot>, fieldSize: Pair<Int, Int>) {
        val c = robots.map { it.coord }.groupingBy { it }.eachCount()
        for (j in 0..<fieldSize.second) {
            (0..<fieldSize.first).map {
                val p = Pair(it, j)
                c.getOrDefault(p, ".")
            }.joinToString("").println()
        }
        println("--------")
    }


    fun part1(input: List<String>, fieldSize: Pair<Int, Int>): Int {
        val robots = parse(input)

//        printField(robots, fieldSize)
        for (i in 0..<100) {
            for (r in robots) {
                tick(r, fieldSize)
            }
        }

        printField(robots, fieldSize)

        //0, 1
        //2, 3


        val q = mutableListOf(0, 0, 0, 0)
        robots.forEach {
            var index: Int? = null
            val middleX = (fieldSize.first) / 2
            val middleY = (fieldSize.second) / 2
            if (it.coord.first < middleX && it.coord.second < middleY) {
                index = 0
            }
            if (it.coord.first < middleX && it.coord.second > middleY) {
                index = 2
            }
            if (it.coord.first > middleX && it.coord.second < middleY) {
                index = 1
            }
            if (it.coord.first > middleX && it.coord.second > middleY) {
                index = 3
            }
            if (index != null) {
                q[index]++
            }
        }
        println(q)


        val res = q.fold(1) { acc, i -> acc * i }
        println("""res of the part 1 is: $res""")
        return res
    }

    fun part2(input: List<String>): Int {
        val robots = parse(input)
        var count = 0
        while (true) {
            if ((count % 1000) == 0) {
                println("count: $count")
            }
            count++
            robots.forEach {
                tick(it, Pair(101, 103))
            }

            val coords = robots.map { it.coord }.toSet()
            fun hasTree(coord: Pair<Int, Int>, size: Int): Boolean {
                return (0..<size).all {
                    coords.contains(Pair(coord.first + it, coord.second)) && coords.contains(
                        Pair(
                            coord.first - it,
                            coord.second
                        )
                    )
                }
            }

            val tree = coords.find { hasTree(it, 5) }
            if (tree != null) {
                printField(robots, Pair(101, 103))
                return count
            }
        }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day14_test")
//    check(part1(testInput, Pair(11, 7)) == 12)


    val input = readInput("Day14")
//    part1(input, Pair(101, 103)).println()
    part2(input).println()
}
