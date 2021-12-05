import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import kotlin.math.abs
import kotlin.math.sign

private data class Point(
    val x: Int,
    val y: Int,
)

private data class LineSegment(
    val start: Point,
    val end: Point,
) {
    object Parser : Grammar<LineSegment>() {
        val arrow by literalToken("->")
        val ws by regexToken("\\s+", ignore = true)

        val sep by ws and arrow and ws

        val comma by literalToken(",")
        val digits by regexToken("""\d+""")

        val coord by digits use { text.toInt() }

        val point by (coord and comma and coord) use {
            Point(this.t1, this.t3)
        }

        override val rootParser = (point and sep and point) use {
            LineSegment(this.t1, this.t3)
        }
    }
}

private fun readLineSegments(input: List<String>): List<LineSegment> {
    return input.map {
        LineSegment.Parser.parseToEnd(it)
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val lines = readLineSegments(input)

        fun LineSegment.coveredPoints(): List<Point> {
            return if (start.x == end.x) {
                if (start.y < end.y) {
                    (start.y..end.y).map { Point(start.x, it) }
                } else {
                    (end.y..start.y).map { Point(start.x, it) }
                }
            } else if (start.y == end.y) {
                if (start.x < end.x) {
                    (start.x..end.x).map { Point(it, start.y) }
                } else {
                    (end.x..start.x).map { Point(it, start.y) }
                }
            } else {
                // ignored
                emptyList()
            }
        }

        val linesAtPoint = lines.asSequence().flatMap {
            it.coveredPoints()
        }.groupingBy { it }.eachCount()

        return linesAtPoint.count { it.value >= 2 }
    }

    fun part2(input: List<String>): Int {
        val lines = readLineSegments(input)

        fun LineSegment.coveredPoints(): List<Point> {
            return if (start.x == end.x) {
                if (start.y < end.y) {
                    (start.y..end.y).map { Point(start.x, it) }
                } else {
                    (end.y..start.y).map { Point(start.x, it) }
                }
            } else if (start.y == end.y) {
                if (start.x < end.x) {
                    (start.x..end.x).map { Point(it, start.y) }
                } else {
                    (end.x..start.x).map { Point(it, start.y) }
                }
            } else if (abs(start.x - end.x) ==  abs(start.y - end.y)) {
                val dist = abs(start.x - end.x)
                val stepX = (end.x - start.x).sign
                val stepY = (end.y - start.y).sign
                (0..dist).map {
                    Point(
                        x = start.x + it * stepX,
                        y = start.y + it * stepY,
                    )
                }
            } else {
                throw IllegalArgumentException("line at wrong angle: $this")
            }
        }

        val linesAtPoint = lines.asSequence().flatMap {
            it.coveredPoints()
        }.groupingBy { it }.eachCount()

        return linesAtPoint.count { it.value >= 2 }
    }

    val testInput = readInput("tests/Day05.test01")
    test(part1(testInput), 5)
    test(part2(testInput), 12)

    val input = readInput("inputs/Day05")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
