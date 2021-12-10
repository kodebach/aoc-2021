private val uniqueDigitLengths = setOf(2, 3, 4, 7)

/**
 *  aaaa
 * b    c
 * b    c
 *  dddd
 * e    f
 * e    f
 *  gggg
 */
private enum class SevenSegmentDigit {
    Zero,
    One,
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,
}

private fun findCorrectMapping(inputs: List<Set<Char>>): Map<Set<Char>, SevenSegmentDigit> {
    val jumbledOne = inputs.single { it.size == 2 }
    val jumbledSeven = inputs.single { it.size == 3 }
    val jumbledFour = inputs.single { it.size == 4 }
    val jumbledEight = inputs.single { it.size == 7 }

    val jumbledThree = inputs.single { it.size == 5 && it.containsAll(jumbledOne) }
    val jumbledNine = inputs.single { it.size == 6 && it.containsAll(jumbledThree) }
    val jumbledFive = inputs.single { it.size == 5 && jumbledNine.containsAll(it) && it != jumbledThree }
    val jumbledTwo = inputs.single { it.size == 5 && it != jumbledFive && it != jumbledThree }
    val jumbledZero = inputs.single { it.size == 6 && it.containsAll(jumbledOne) && it != jumbledNine }
    val jumbledSix = inputs.single { it.size == 6 && it != jumbledNine && it != jumbledZero }

    return mapOf(
        jumbledZero to SevenSegmentDigit.Zero,
        jumbledOne to SevenSegmentDigit.One,
        jumbledTwo to SevenSegmentDigit.Two,
        jumbledThree to SevenSegmentDigit.Three,
        jumbledFour to SevenSegmentDigit.Four,
        jumbledFive to SevenSegmentDigit.Five,
        jumbledSix to SevenSegmentDigit.Six,
        jumbledSeven to SevenSegmentDigit.Seven,
        jumbledEight to SevenSegmentDigit.Eight,
        jumbledNine to SevenSegmentDigit.Nine,
    )
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val (_, output) = line.split(" | ", limit = 2).map { it.split(" ") }

            output.count { digitString ->
                digitString.length in uniqueDigitLengths
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val (inputs, outputs) = line.split(" | ", limit = 2).map { it.split(" ") }

            val correctMapping = findCorrectMapping(inputs.map { it.toSet() })

            val number = outputs.map { digitString ->
                val digit = correctMapping.getValue(digitString.toSet())
                digit.ordinal
            }.reduce { acc, i ->
                10 * acc + i
            }
            number
        }
    }

    val testInput = readInput("tests/Day08.test01")
    test(part1(testInput), 26)
    test(part2(testInput), 61229)

    val input = readInput("inputs/Day08")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
