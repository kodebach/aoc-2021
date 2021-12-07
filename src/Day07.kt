import kotlin.math.abs

@JvmInline
private value class CrabSubmarine(val position: Int)

private fun fuelUse(
    from: List<CrabSubmarine>,
    to: List<CrabSubmarine>,
    distance: (CrabSubmarine, CrabSubmarine) -> Int
): Int {
    return from.zip(to) { f, t -> distance(f, t) }.sum()
}

private fun solvePuzzle(input: List<String>, distance: (CrabSubmarine, CrabSubmarine) -> Int): Int {
    val crabs = input.single().split(",").mapTo(mutableListOf()) { CrabSubmarine(it.toInt()) }

    val maxPosition = crabs.maxOf { it.position }
    val goals = (0..maxPosition).map { goal ->
        List(crabs.size) { CrabSubmarine(position = goal) }
    }

    return goals.minOf { goal ->
        fuelUse(crabs, goal, distance)
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return solvePuzzle(input) { a, b -> abs(a.position - b.position) }
    }

    fun part2(input: List<String>): Int {
        return solvePuzzle(input) { a, b ->
            val dist = abs(a.position - b.position)
            (dist * (dist + 1)) / 2
        }
    }

    val testInput = readInput("tests/Day07.test01")
    test(part1(testInput), 37)
    test(part2(testInput), 168)

    val input = readInput("inputs/Day07")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
