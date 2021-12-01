fun main() {
    fun part1(input: List<String>): Int {
        return input.map { line ->
            line.toInt()
        }.windowed(2) { (a, b) ->
            a < b
        }.count { it }
    }

    fun part2(input: List<String>): Int {
        return input.map { line ->
            line.toInt()
        }.windowed(3) { window ->
            window.sum()
        }.windowed(2) { (a, b) ->
            a < b
        }.count { it }
    }

    val testInput = readInput("tests/Day01.test01")
    test(part1(testInput), 7)
    test(part2(testInput), 5)

    val input = readInput("inputs/Day01")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
