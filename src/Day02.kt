private data class Position(val horizontal: Int, val depth: Int)
private data class AimedPosition(val horizontal: Int, val depth: Int, val aim: Int)

private sealed interface Command {
    fun applyTo(position: Position): Position
    fun applyTo(position: AimedPosition): AimedPosition
}

private class Forward(val amount: Int) : Command {
    override fun applyTo(position: Position): Position {
        return position.copy(horizontal = position.horizontal + amount)
    }

    override fun applyTo(position: AimedPosition): AimedPosition {
        return position.copy(
            horizontal = position.horizontal + amount,
            depth = position.depth + position.aim * amount,
        )
    }
}

private class Down(val amount: Int) : Command {
    override fun applyTo(position: Position): Position {
        return position.copy(depth = position.depth + amount)
    }

    override fun applyTo(position: AimedPosition): AimedPosition {
        return position.copy(aim = position.aim + amount)
    }
}

private class Up(val amount: Int) : Command {
    override fun applyTo(position: Position): Position {
        return position.copy(depth = position.depth - amount)
    }

    override fun applyTo(position: AimedPosition): AimedPosition {
        return position.copy(aim = position.aim - amount)
    }
}

private fun String.readCommand(): Command {
    val (cmd, amount) = split(" ", limit = 2)
    return when (cmd) {
        "forward" -> Forward(amount.toInt())
        "up" -> Up(amount.toInt())
        "down" -> Down(amount.toInt())
        else -> throw IllegalArgumentException("unknown command: $cmd $amount")
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            it.readCommand()
        }.fold(Position(0, 0)) { pos, cmd ->
            cmd.applyTo(pos)
        }.let {
            it.horizontal * it.depth
        }
    }

    fun part2(input: List<String>): Int {
        return input.map {
            it.readCommand()
        }.fold(AimedPosition(0, 0, 0)) { pos, cmd ->
            cmd.applyTo(pos)
        }.let {
            it.horizontal * it.depth
        }
    }

    val testInput = readInput("tests/Day02.test01")
    test(part1(testInput), 150)
    test(part2(testInput), 900)

    val input = readInput("inputs/Day02")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
