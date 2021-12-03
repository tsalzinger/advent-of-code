package me.salzinger

data class Submarine(
    val position: Position = Position(0, 0)
)

fun Submarine.executeCommand(command: Command): Submarine {
    return copy(
        position = command(position)
    )
}

data class Position(
    val horizontal: Int,
    val depth: Int
)

sealed interface Command {
    val amount: Int
    operator fun invoke(position: Position): Position

    data class Forward(
        override val amount: Int
    ) : Command {
        override fun invoke(position: Position): Position {
            return position.copy(
                horizontal = position.horizontal + amount
            )
        }
    }

    data class Down(
        override val amount: Int
    ) : Command {
        override fun invoke(position: Position): Position {
            return position.copy(
                depth = position.depth + amount
            )
        }
    }

    data class Up(
        override val amount: Int
    ) : Command {
        override fun invoke(position: Position): Position {
            return position.copy(
                depth = position.depth - amount
            )
        }
    }

    companion object {
        fun formCommandInput(input: String): Command {
            val (commandType, amountInput) = input.split(" ")
            val amount = amountInput.toInt()

            return when (commandType) {
                "forward" -> Forward(amount)
                "up" -> Up(amount)
                "down" -> Down(amount)
                else -> TODO("Unsupported command type $commandType")
            }
        }
    }
}
