import kotlin.math.pow

class Processor(var A: Long, var B: Long, var C: Long, var pointer: Long = 0, val program: List<Long>) {

    val output = mutableListOf<Long>()


    fun getCombo(input: Long): Long {
        if (input in 0L..3L) {
            return input
        }
        return when (input) {
            4L -> A
            5L -> B
            6L -> C
            else -> throw error("invalid combo code")
        }
    }

    fun adv(combo: Long) {
        A /= Math.pow(2.0, getCombo(combo).toDouble()).toInt()
    }

    fun bdv(combo: Long) {
        B = A / Math.pow(2.0, getCombo(combo).toDouble()).toInt()
    }

    fun cdv(combo: Long) {
        C = A / Math.pow(2.0, getCombo(combo).toDouble()).toInt()
    }

    fun bxc(input: Long) {
        B = B.xor(C)
    }

    fun bxl(input: Long) {
        B = B.xor(input)
    }

    fun bst(input: Long) {
        B = getCombo(input) % 8
    }

    fun jnz(input: Long) {
        if (A != 0L) {
            pointer = input - 2
        }
    }


    fun out(input: Long) {
        val combo = getCombo(input).mod(8).toLong()
        output.add(combo)
    }

    fun apply(opcode: Long, param: Long) {
        when (opcode) {
            0L -> adv(param)
            1L -> bxl(param)
            2L -> bst(param)
            3L -> jnz(param)
            4L -> bxc(param)
            5L -> out(param)
            6L -> bdv(param)
            7L -> cdv(param)
            else -> throw error("invalid opcode")
        }

    }

    fun tick(): Boolean {
        val op = program.getOrNull(pointer.toInt()) ?: return true
        val param = program.getOrNull(pointer.toInt() + 1) ?: return true
        apply(op, param)
        pointer += 2
        return false
    }

    fun execute() {
        while (!tick()) {
        }
    }


}

fun main() {


    fun parse(input: List<String>): Processor {
        val re = """(\d+)""".toRegex()
        val A = re.find(input[0])!!.value.toLong()
        val B = re.find(input[1])!!.value.toLong()
        val C = re.find(input[2])!!.value.toLong()
        val program = input[4].drop(9).split(",").map { it.toLong() }
        return Processor(A, B, C, 0, program)
    }


    fun part1(input: List<String>): String {
        val processor = parse(input)
        println("A=${processor.A}, B=${processor.B}, C=${processor.C}")
        processor.execute()
        val res = processor.output.joinToString(",")
        println("""res of the part 1 is: $res""")
        return res
    }


    fun part2(input: List<String>): Int {
        var A: Int = 0
        var B = 0
        var C = 0
        while (A > 0) {
            //2,4,1,1,7,5,0,3,1,4,4,4,5,5,3,0
            B = A % 8 // 2, 4
            B = B xor 1 // 1, 1
            
            C = A / 2.0.pow(B).toInt() // 7, 5

            A = A / 2.0.pow(3).toInt() // 0, 3

            B = B xor 4 // 1, 4

            B = B xor C // 4, 4

            println(B % 8) // 5, 5
        }


        val output = listOf(2, 4, 1, 1, 7, 5, 0, 3, 1, 4, 4, 4, 5, 5, 3, 0)
        while (output.isNotEmpty()) {
            B = output.last()
//            C= B xor 1
            B = B xor 4


        }

        println("""res of the part 2 is: $res""")
        return res
    }

//    val test1 = Processor(0, 0, 9, program = listOf(2, 6))
//    test1.execute()
//    check(test1.B == 1L)
//
//    val test2 = Processor(10, 0, 0, program = listOf(5, 0, 5, 1, 5, 4))
//    test2.execute()
//    check(test2.output == listOf(0L, 1L, 2L))
//
//    val test3 = Processor(2024, 0, 0, program = listOf(0, 1, 5, 4, 3, 0))
//    test3.execute()
//    check(test3.output == listOf(4, 2, 5, 6, 7, 7, 7, 7, 3, 1, 0).map(Int::toLong))
//    check(test3.A == 0L)
//
//    val test4 = Processor(0, 29, 0, program = listOf(1, 7))
//    test4.execute()
//    check(test4.B == 26L)
//
//    val test5 = Processor(0, 2024, 43690, program = listOf(4, 0))
//    test5.execute()
//    check(test5.B == 44354L)
//
//    val test6 = Processor(0, 14, 0, program = listOf(1, 3))
//    test6.execute()
//    check(test6.B == 13L)


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")

    val input = readInput("Day17")



    part1(input).println()
    part2(testInput).println()
}
