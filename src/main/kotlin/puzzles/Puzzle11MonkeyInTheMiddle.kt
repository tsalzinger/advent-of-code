package puzzles

import common.extensions.product
import me.salzinger.common.extensions.toIntList
import me.salzinger.common.streamInput
import puzzles.Puzzle11MonkeyInTheMiddle.Part1.toMonkeys
import java.math.BigInteger

@JvmInline
value class MonkeyIndex(val index: Int)

@JvmInline
value class WorryLevel(val value: BigInteger)

fun <T, R> MutableList<T>.drainAndMap(operation: (T) -> R): List<R> {
    return toList()
        .also {
            clear()
        }
        .map(operation)

}

object Puzzle11MonkeyInTheMiddle {

    data class ItemTrow(
        val itemWorryLevel: WorryLevel,
        val targetMonkey: MonkeyIndex,
    )

    class Monkey(
        initialItems: List<WorryLevel>,
        private val operation: (WorryLevel) -> WorryLevel,
        private val throwDecision: (WorryLevel) -> MonkeyIndex,
    ) {
        private var inspectedItemsCount: BigInteger = BigInteger.ZERO
        private val items = initialItems.toMutableList()

        fun throwItems(): List<ItemTrow> {
            return items
                .drainAndMap(operation)
                .onEach { inspectedItemsCount++ }
                .map { worryLevel ->
                    ItemTrow(
                        worryLevel,
                        worryLevel.let(throwDecision),
                    )
                }
        }

        fun transformWorryLevel(transform: (WorryLevel) -> WorryLevel) {
            items.replaceAll(transform)
        }

        fun addItem(item: WorryLevel) {
            items += item
        }

        fun getInspectedItemsCount(): BigInteger {
            return inspectedItemsCount
        }
    }

    object Part1 {

        sealed interface Operator {
            object Plus : Operator
            object Times : Operator
        }

        sealed interface OperationValue {

            object Old : OperationValue

            data class Number(val value: BigInteger) : OperationValue
        }

        data class Operation(
            val first: OperationValue,
            val operator: Operator,
            val second: OperationValue,
        ) {
            fun evaluate(worryLevel: WorryLevel): WorryLevel {
                val firstValue = first.substitute(worryLevel)
                val secondValue = second.substitute(worryLevel)
                return when (operator) {
                    Operator.Plus -> firstValue + secondValue
                    Operator.Times -> firstValue * secondValue
                }.let(::WorryLevel)
            }

            private fun OperationValue.substitute(worryLevel: WorryLevel): BigInteger {
                return when (this) {
                    is OperationValue.Number -> value
                    OperationValue.Old -> worryLevel.value
                }
            }
        }

        fun String.toOperationValue(): OperationValue {
            return if (equals("old")) {
                OperationValue.Old
            } else {
                OperationValue.Number(toBigInteger())
            }
        }

        fun String.toOperator(): Operator {
            return when (this) {
                "+" -> Operator.Plus
                "*" -> Operator.Times
                else -> throw RuntimeException("Unknown operator: $this")
            }
        }

        fun String.parseOperation(): Operation {
            val (first, operator, second) = substring("  Operation: new = ".count()).split(" ")

            return Operation(
                first.toOperationValue(),
                operator.toOperator(),
                second.toOperationValue()
            )
        }

        fun List<String>.toThrowDecision(): (WorryLevel) -> MonkeyIndex {
            val divisor = first().substring("  Test: divisible by ".count()).toBigInteger()
            val monkeyIndexOnTrue = get(1)
                .substring("    If true: throw to monkey ".count())
                .toInt()
                .let(::MonkeyIndex)
            val monkeyIndexOnFalse = get(2)
                .substring("    If false: throw to monkey ".count())
                .toInt()
                .let(::MonkeyIndex)
            return {
                if (it.value % divisor == BigInteger.ZERO) {
                    monkeyIndexOnTrue
                } else {
                    monkeyIndexOnFalse
                }
            }
        }

        fun Sequence<String>.toMonkeys(generalWorryLevelModifier: (WorryLevel) -> WorryLevel): Sequence<Monkey> {
            return chunked(7) { monkeyInputLines ->
                val initialItems = monkeyInputLines[1].substring("  Starting items: ".count())
                    .toIntList(", ")
                    .map { WorryLevel(it.toBigInteger()) }

                val operation: (WorryLevel) -> WorryLevel = monkeyInputLines[2]
                    .parseOperation()
                    .let {
                        { worryLevel -> it.evaluate(worryLevel).let(generalWorryLevelModifier) }
                    }

                val throwDecision = monkeyInputLines.subList(3, 6).toThrowDecision()

                Monkey(
                    initialItems = initialItems,
                    operation = operation,
                    throwDecision = throwDecision,
                )
            }
        }

        fun Sequence<String>.solve(): Int {
            val monkeys = toMonkeys { WorryLevel(it.value / 3.toBigInteger()) }
                .mapIndexed { index, monkey ->
                    MonkeyIndex(index) to monkey
                }.toMap()

//            val product = listOf(11, 3, 5, 7, 19, 2, 13, 17).product().toBigInteger()
            val product = listOf(23, 19, 13, 17).product().toBigInteger()

            repeat(20) {
                for (index in 0 until monkeys.size) {
                    monkeys.getValue(MonkeyIndex(index))
                        .throwItems()
                        .forEach { itemThrow ->
                            monkeys
                                .getValue(itemThrow.targetMonkey)
                                .addItem(itemThrow.itemWorryLevel)
                        }
                }

                monkeys.values.forEach { monkey ->
                    monkey.transformWorryLevel {
//                    if (it.value > product) {
//                        WorryLevel(it.value % product)
//                    } else {
//                        it
//                    }
                        println(it.value)
                        it
                    }
                }
            }

            return monkeys
                .values
                .map { it.getInspectedItemsCount() }
                .sortedDescending()
                .take(2)
                .product()
                .toInt()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-11.in"
                .streamInput()
                .solve()
                .run(::println)
        }
    }

    object Part2 {
        fun Sequence<String>.solve(): BigInteger {
            val monkeys = toMonkeys { it }
                .mapIndexed { index, monkey ->
                    MonkeyIndex(index) to monkey
                }.toMap()

            repeat(10_000) {
                for (index in 0 until monkeys.size) {
                    monkeys.getValue(MonkeyIndex(index))
                        .throwItems()
                        .forEach { itemThrow ->
                            monkeys
                                .getValue(itemThrow.targetMonkey)
                                .addItem(itemThrow.itemWorryLevel)
                        }
                }
            }

            return monkeys
                .values
                .map { it.getInspectedItemsCount() }
                .sortedDescending()
                .take(2)
                .product()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-11.in"
                .streamInput()
                .solve()
                .run(::println)
        }
    }
}
