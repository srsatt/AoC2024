data class Queue(
    val order: Map<Int, Set<Int>>,
    val updates: List<List<Int>>
)

fun main() {

    fun parse(input: List<String>): Queue {
        val order = mutableMapOf<Int, MutableSet<Int>>()
        val pages = mutableListOf<List<Int>>()
        var parsingOrder = true
        input.forEach {
            if (it.isEmpty()) {
                parsingOrder = false
            } else {

                if (parsingOrder) {
                    val (first, second) = it.split("|").map { it.toInt() }
                    order.computeIfAbsent(second) { mutableSetOf() }.add(first)
                } else {
                    pages.add(it.split(",").map { it.toInt() })
                }
            }
        }
        return Queue(order, pages)
    }

    fun part1(input: List<String>): Int {
        val queue = parse(input)

        val res = queue.updates.filter {
            it.foldIndexed(true) { index, curr, page ->
                curr && it.slice(index..it.size - 1).all { p -> !(queue.order[page]?.contains(p) ?: false) }
            }
        }.sumOf {
            it[(it.size - 1) / 2]
        }

        println("""res of the part 1 is: $res""")
        return res
    }

    fun part2(input: List<String>): Int {
        val queue = parse(input)

        val incorrect = queue.updates.filter {
            !it.foldIndexed(true) { index, curr, page ->
                curr && it.slice(index..it.size - 1).all { p -> !(queue.order[page]?.contains(p) ?: false) }
            }
        }
        val correct = incorrect.map {
            val indexMap = it.mapIndexed { index, i -> i to index }.toMap()
            it.sortedWith { a, b ->
                if (queue.order[a]?.contains(b) ?: false) {
                    -1
                } else {
                    indexMap[a]!! - indexMap[b]!!
                }
            }
        }

        val res = correct.sumOf { it[(it.size - 1) / 2] }
        println("""res of the part 2 is: $res""")
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
