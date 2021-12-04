private data class Board(
    val rows: List<Row>,
) {
    init {
        require(rows.size == 5 && rows.all { it.cells.size == 5 }) { "not 5x5 board" }
    }

    fun isComplete(): Boolean {
        val cols = List(5) { i ->
            rows.map { it.cells[i] }
        }
        return rows.any { row -> row.cells.all { it.marked } } || cols.any { col -> col.all { it.marked } }
    }

    fun mark(number: Int): Board {
        return Board(
            rows = rows.map {
                it.mark(number)
            }
        )
    }

    fun calculateScore(lastDraw: Int): Int {
        return rows.sumOf { row -> row.cells.filterNot { it.marked }.sumOf { it.value } } * lastDraw
    }

    data class Row(val cells: List<Cell>) {
        fun mark(number: Int): Row {
            return Row(
                cells = cells.map { it.copy(marked = it.marked || it.value == number) },
            )
        }

        companion object {
            fun parse(input: String): Row {
                val cells = input.split(" ").filterNot { it.isEmpty() }
                require(cells.size == 5) { "wrong number of cells: $input" }
                return Row(
                    cells = cells.map { Cell(it.toInt(), false) },
                )
            }
        }
    }

    data class Cell(val value: Int, val marked: Boolean)

    companion object {
        fun parse(input: List<String>): Board {
            require(input.size >= 5) { "not enough lines" }
            return Board(
                rows = input.take(5).map { Row.parse(it) }
            )
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val draws = input.first().split(",").map {
            it.toInt()
        }.asSequence()
        val boards = input.drop(1).chunked(6) { board ->
            board.drop(1) // empty line
                .let { Board.parse(it) }
        }

        val (lastDraw, winningBoard) = draws.scan(emptyList<Int>()) { list, cur ->
            list + cur
        }.drop(1).flatMap { numbers ->
            boards.map { board ->
                numbers.last() to numbers.fold(board) { b, number ->
                    b.mark(number)
                }
            }
        }.first { (_, board) ->
            board.isComplete()
        }

        return winningBoard.calculateScore(lastDraw)
    }

    fun part2(input: List<String>): Int {
        val draws = input.first().split(",").map {
            it.toInt()
        }
        val boards = input.drop(1).chunked(6) { board ->
            board.drop(1) // empty line
                .let { Board.parse(it) }
        }

        val (penultimateDraw, finalBoards) = draws.asSequence().withIndex()
            .scan(emptyList<IndexedValue<Int>>()) { list, cur ->
                list + cur
            }.drop(1).map { numbers ->
            numbers.last() to boards.map { board ->
                numbers.fold(board) { b, (_, number) ->
                    b.mark(number)
                }
            }
        }.first { (_, boards) ->
            boards.count { it.isComplete() } == boards.size - 1
        }

        val lastDraw = draws[penultimateDraw.index + 1]
        val lastWinningBoard = finalBoards.single {
            !it.isComplete()
        }.mark(lastDraw)

        return lastWinningBoard.calculateScore(lastDraw)
    }

    val testInput = readInput("tests/Day04.test01")
    test(part1(testInput), 4512)
    test(part2(testInput), 1924)

    val input = readInput("inputs/Day04")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
