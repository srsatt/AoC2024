class Region(val name: Char) {
    var area: Int = 0;
    var perimeter: Int = 0;
    val points: MutableSet<Pair<Int, Int>> = mutableSetOf()


    fun grow(point: Pair<Int, Int>, field: List<List<Char>>, visited: MutableSet<Pair<Int, Int>>) {
        val value = field[point.first][point.second]
        if (value == this.name) {
            visited.add(point)
            points.add(point)
            val neighbours = getNeighbours(point, field)
            this.area++
            val neighbourCount = neighbours.count { it == null || it.first != this.name }
            this.perimeter += neighbourCount
            neighbours.forEach {
                if (it != null && !visited.contains(it.second)) grow(it.second, field, visited)
            }
        }
    }

    fun calculateSides(): Int {
        val processedCoords = mutableSetOf<Pair<Int, Int>>()
        var cornersCount = 0;
        val corners = mutableSetOf<Pair<Int, Int>>()

        fun getCoordSurrounding(coord: Pair<Int, Int>): List<Pair<Int, Int>> {
            return listOf(Pair(-1, -1), Pair(-1, 0), Pair(0, -1), Pair(0, 0)).map {
                Pair(
                    coord.first + it.first,
                    coord.second + it.second
                )
            }.filter { this.points.contains(it) }
        }

        this.points.forEach { point ->
            listOf(Pair(1, 1), Pair(1, 0), Pair(0, 1), Pair(0, 0)).forEach { coordDir ->
                val coord = Pair(point.first + coordDir.first, point.second + coordDir.second)
                if (!processedCoords.contains(coord)) {
                    val surrounding = getCoordSurrounding(coord)
                    if (surrounding.size % 2 != 0) {
                        corners.add(coord)
                        cornersCount++
                    } else {
                        val (first, second) = surrounding
                        if (first.first != second.first && first.second != second.second) cornersCount += 2
                    }

                    processedCoords.add(coord)
                }
            }
        }

        return cornersCount
    }

    companion object {
        fun growFromPoint(point: Pair<Int, Int>, field: List<List<Char>>, visited: MutableSet<Pair<Int, Int>>): Region {
            val region = Region(field[point.first][point.second])
            visited.add(point)
            region.grow(point, field, visited)
            return region
        }
    }
}

fun getNeighbours(point: Pair<Int, Int>, field: List<List<Char>>): List<Pair<Char, Pair<Int, Int>>?> {
    val directions = listOf(Pair(0, -1), Pair(-1, 0), Pair(1, 0), Pair(0, 1))
    return directions.map {
        val row = point.first + it.first
        val col = point.second + it.second
        val newPoint = field.getOrNull(row)?.getOrNull(col)
        if (newPoint != null) Pair(newPoint, Pair(row, col)) else null
    }
}

fun main() {


    fun part1(input: List<String>): Int {
        val field = input.map { it.toList() }
        val processedPoints = mutableSetOf<Pair<Int, Int>>()
        val regions = mutableSetOf<Region>()
        field.forEachIndexed { row, chars ->
            chars.forEachIndexed { col, char ->
                if (!processedPoints.contains(Pair(row, col))) {
                    val region = Region.growFromPoint(Pair(row, col), field, processedPoints)
                    regions.add(region)
                }
            }
        }

        regions
            .forEach {
                val region = it
                println("""A region of ${region.name} plants with price ${region.area} * ${region.perimeter} = ${region.area * region.perimeter}""")
            }
        val res = regions.sumOf {
            it.area * it.perimeter
        }

        println("""res of the part 1 is: $res""")
        return res
    }

    fun part2(input: List<String>): Int {
        val field = input.map { it.toList() }
        val processedPoints = mutableSetOf<Pair<Int, Int>>()
        val regions = mutableSetOf<Region>()
        field.forEachIndexed { row, chars ->
            chars.forEachIndexed { col, char ->
                if (!processedPoints.contains(Pair(row, col))) {
                    val region = Region.growFromPoint(Pair(row, col), field, processedPoints)
                    regions.add(region)
                }
            }
        }

        val res = regions.sumOf {
            val size = it.calculateSides()
            println("""A region of ${it.name} plants with price ${it.area} * ${size} = ${it.area * size}""")

            size * it.area
        }

        println("""res of the part 2 is: $res""")
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 1930)
    check(part2(testInput) == 1206)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
