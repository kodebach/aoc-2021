private data class BitStats(val mostCommon: String, val leastCommon: String)
private data class BitCount(val zeros: Int, val ones: Int)

fun main() {
    fun getBitStats(input: List<String>): BitStats {
        val bitCounts = input.map { line ->
            line.map {
                when (it) {
                    '0' -> BitCount(1, 0)
                    '1' -> BitCount(0, 1)
                    else -> throw IllegalArgumentException("not binary: $it")
                }
            }
        }.reduce { accList, curList ->
            accList.zip(curList) { acc, cur ->
                BitCount(
                    zeros = acc.zeros + cur.zeros,
                    ones = acc.ones + cur.ones,
                )
            }
        }

        val mostCommon = bitCounts.joinToString(separator = "") {
            if (it.zeros > it.ones) {
                "0"
            } else {
                "1"
            }
        }

        val leastCommon = bitCounts.joinToString(separator = "") {
            if (it.zeros <= it.ones) {
                "0"
            } else {
                "1"
            }
        }

        return BitStats(
            mostCommon = mostCommon,
            leastCommon = leastCommon,
        )
    }

    fun part1(input: List<String>): Int {
        val bitStats = getBitStats(input)
        val gammaRate = bitStats.mostCommon.toInt(2)
        val epsilonRate = bitStats.leastCommon.toInt(2)
        return gammaRate * epsilonRate
    }

    fun part2(input: List<String>): Int {
        tailrec fun part2Rec(data: List<String>, index: Int, mostCommon: Boolean): String {
            val bitStats = getBitStats(data)
            val filtered = data.filter {
                if (mostCommon) {
                    it[index] == bitStats.mostCommon[index]
                } else {
                    it[index] == bitStats.leastCommon[index]
                }
            }

            return filtered.singleOrNull() ?: part2Rec(filtered, index + 1, mostCommon)
        }

        val oxygenRating = part2Rec(input, 0, true).toInt(2)
        val co2Rating = part2Rec(input, 0, false).toInt(2)

        return oxygenRating * co2Rating
    }

    val testInput = readInput("tests/Day03.test01")
    test(part1(testInput), 198)
    test(part2(testInput), 230)

    val input = readInput("inputs/Day03")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
