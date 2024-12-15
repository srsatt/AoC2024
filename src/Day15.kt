class Field(val field: MutableList<MutableList<Char>>, val commands: MutableList<Char>) {

    var robot: Pair<Int, Int>? = null

    fun findRobot(): Pair<Int, Int> {
        var coords: Pair<Int, Int>? = null
        field.forEachIndexed { row, it ->
            it.forEachIndexed { col, char ->
                if (char == '@') {
                    coords = Pair(row, col)
                }
            }
        }

        return coords!!
    }

    fun tick() {
        val command = commands.removeFirst()
        val direction = when (command) {
            '>' -> Pair(0, 1)
            '^' -> Pair(-1, 0)
            '<' -> Pair(0, -1)
            'v' -> Pair(1, 0)
            else -> throw Exception("Unknown command $command")
        }
        val r = robot ?: return;
        val robotMoved = moveInDirection(r, direction)
        if (robotMoved) robot = Pair(
            r.first + direction.first,
            r.second + direction.second
        )
    }

    fun tickWide() {
        val command = commands.removeFirst()
        val direction = when (command) {
            '>' -> Pair(0, 1)
            '^' -> Pair(-1, 0)
            '<' -> Pair(0, -1)
            'v' -> Pair(1, 0)
            else -> throw Exception("Unknown command $command")
        }
        val r = robot ?: return;
        if (canMoveInWide(r, direction)) {
            robot = Pair(r.first + direction.first, r.second + direction.second)
            moveInWideDirection(r, direction)
        }
    }

    fun run(isWide: Boolean = false) {
        while (commands.isNotEmpty()) {
            if (isWide) tickWide() else tick()
        }
    }


    fun moveInDirection(coord: Pair<Int, Int>, direction: Pair<Int, Int>): Boolean {
        val nextCoord = Pair(coord.first + direction.first, coord.second + direction.second)
        val target = field[nextCoord.first][nextCoord.second]
        val canMove = target == '.' || target == 'O' && moveInDirection(nextCoord, direction)
        if (canMove) {
            field[nextCoord.first][nextCoord.second] = field[coord.first][coord.second]
            field[coord.first][coord.second] = '.'
        }
        return canMove
    }

    fun canMoveInWide(coord: Pair<Int, Int>, direction: Pair<Int, Int>, checkOther: Boolean = true): Boolean {
        val nextCoord = Pair(coord.first + direction.first, coord.second + direction.second)
        val target = field[nextCoord.first][nextCoord.second]
        return target == '.'
                || (target == '['
                && canMoveInWide(nextCoord, direction)
                && (direction.first == 0 || !checkOther || canMoveInWide(
            Pair(coord.first, coord.second + 1),
            direction, false
        )))
                || (target == ']'
                && canMoveInWide(nextCoord, direction)
                && (direction.first == 0 || !checkOther || canMoveInWide(
            Pair(coord.first, coord.second - 1),
            direction, false
        )))
    }

    fun moveInWideDirection(coord: Pair<Int, Int>, direction: Pair<Int, Int>, moveOther: Boolean = true) {
        val nextCoord = Pair(coord.first + direction.first, coord.second + direction.second)
        val target = field[nextCoord.first][nextCoord.second]
        val current = field[coord.first][coord.second]
        if (target == '[') {
            moveInWideDirection(nextCoord, direction)
            if (direction.first != 0) moveInWideDirection(
                Pair(nextCoord.first, nextCoord.second + 1),
                direction,
                false
            )
        }
        if (target == ']') {
            moveInWideDirection(nextCoord, direction)
            if (direction.first != 0) moveInWideDirection(
                Pair(nextCoord.first, nextCoord.second - 1),
                direction,
                false
            )
        }
        field[nextCoord.first][nextCoord.second] = current
        field[coord.first][coord.second] = '.'
    }

}


fun main() {
    fun parse(input: List<String>): Field {
        var isField = true
        val field = Field(mutableListOf(), mutableListOf())
        input.forEachIndexed { i, it ->
            if (isField && it.isNotEmpty()) {
                field.field.add(it.toList().toMutableList())
            }
            if (it.isEmpty()) {
                isField = false
            }
            if (it.isNotEmpty() && !isField) {
                field.commands.addAll(it.toList())
            }
        }

        field.robot = field.findRobot()
        return field
    }

    fun parseWide(input: List<String>): Field {
        var isField = true
        val field = Field(mutableListOf(), mutableListOf())
        input.forEachIndexed { i, it ->
            if (isField && it.isNotEmpty()) {
                field.field.add(it.toList().flatMap {
                    when (it) {
                        '#' -> "##".toList()
                        'O' -> "[]".toList()
                        '@' -> "@.".toList()
                        '.' -> "..".toList()
                        else -> throw Exception("Unknown input")
                    }
                }.toMutableList())
            }
            if (it.isEmpty()) {
                isField = false
            }
            if (it.isNotEmpty() && !isField) {
                field.commands.addAll(it.toList())
            }
        }

        field.robot = field.findRobot()
        return field
    }


    fun part1(input: List<String>): Int {
        val field = parse(input)
        field.run()

        field.field.forEach { println(it) }
        var res = 0;
        field.field.forEachIndexed { row, it ->
            it.forEachIndexed { col, char ->
                if (char == 'O') {
                    res += row * 100 + col
                }
            }
        }
        println("""res of the part 1 is: $res""")
        return res
    }

    fun part2(input: List<String>): Int {
        val field = parseWide(input)
        field.run(true)
        var res = 0;
        field.field.forEachIndexed { row, it ->
            it.forEachIndexed { col, char ->
                if (char == '[') {
                    res += row * 100 + col
                }
            }
        }


        println("""res of the part 2 is: $res""")
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 10092)
    check(part2(testInput) == 9021)


    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
