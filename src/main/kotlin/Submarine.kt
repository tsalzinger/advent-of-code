package me.salzinger

data class Submarine(
    val position: Position = Position()
)

fun Submarine.executeCommand(command: Command): Submarine {
    return copy(
        position = command(position)
    )
}

data class Position(
    val horizontal: Int = 0,
    val depth: Int = 0,
    val aim: Int = 0,
)

sealed interface Command {
    val amount: Int
    operator fun invoke(position: Position): Position

    data class Forward(
        override val amount: Int
    ) : Command {
        override fun invoke(position: Position): Position {
            return position.copy(
                horizontal = position.horizontal + amount,
                depth = position.depth + position.aim * amount,
            )
        }
    }

    data class Down(
        override val amount: Int
    ) : Command {
        override fun invoke(position: Position): Position {
            return position.copy(
                aim = position.aim + amount
            )
        }
    }

    data class Up(
        override val amount: Int
    ) : Command {
        override fun invoke(position: Position): Position {
            return position.copy(
                aim = position.aim - amount
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
