@JvmInline
private value class Lanternfish(
    val daysUntilReproduction: Int,
)

private fun simulate(input: List<Lanternfish>, totalDays: Int): Long {
    // holds the number of Lanternfish that will reproduce after 'index' days
    val fishes = Array(9) { days ->
        input.count { it.daysUntilReproduction == days }.toLong()
    }

    repeat(totalDays) {
        val reproducing = fishes[0]
        for (i in 1..8) {
            fishes[i - 1] = fishes[i] // reduce days
        }
        fishes[8] = reproducing // new fishes
        fishes[6] += reproducing // reset reproducing fishes
    }

    return fishes.sumOf { it }
}

private fun MutableList<Lanternfish>.doSimulationStep() {
    for (i in indices) {
        if (this[i].daysUntilReproduction == 0) {
            this[i] = Lanternfish(daysUntilReproduction = 6)
            this.add(Lanternfish(daysUntilReproduction = 8))
        } else {
            this[i] = Lanternfish(daysUntilReproduction = this[i].daysUntilReproduction - 1)
        }
    }
}

fun main() {
    val memoMap = mutableMapOf<Pair<Lanternfish, Int>, List<Lanternfish>>()

    for (i in 0..8) {
        val fish = Lanternfish(daysUntilReproduction = i)
        val fishes = mutableListOf(fish)
        repeat(128) {
            memoMap[fish to it] = fishes.toList()
            fishes.doSimulationStep()
        }
        memoMap[fish to 128] = fishes.toList()
    }

    fun part1(input: List<String>): Long {
        val fishes = input.single().split(",").mapTo(mutableListOf()) { Lanternfish(it.toInt()) }
        return simulate(fishes, 80)
    }

    fun part2(input: List<String>): Long {
        val fishes = input.single().split(",").mapTo(mutableListOf()) { Lanternfish(it.toInt()) }
        return simulate(fishes, 256)

    }

    val testInput = readInput("tests/Day06.test01")
    test(part1(testInput), 5934L)
    test(part2(testInput), 26_984_457_539L)

    val input = readInput("inputs/Day06")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
