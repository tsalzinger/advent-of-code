package me.salzinger

import kotlin.math.ceil

fun main() {
    27.solve {
        toChemicalReactionsMap()
            .run {
                getValue(Fuel)
                    .inputs
                    .map {
                        getNecessaryOre(it)
                    }

                chemicalStore[Ore]
            }
            .toString()
    }
}

val chemicalStore = mutableMapOf<Chemical, Int>().withDefault { 0 }

typealias Recipes = Map<Chemical, Reaction>

fun Map<Chemical, Reaction>.getNecessaryOre(chemicalInputs: Set<ChemicalInput>, level: Int = 0) {
    chemicalInputs
        .forEach {
            getNecessaryOre(it, level)
        }
}

fun Map<Chemical, Reaction>.getNecessaryOre(chemicalInput: ChemicalInput, level: Int = 0) {
    val requiredChemical = chemicalInput.chemical
    val requiredAmount = chemicalInput.amount
    val availableAmount = chemicalStore.getValue(requiredChemical)
    val prefix = List(level) { "|---" }.joinToString("")

    if (requiredChemical == Ore) {
        println("$prefix$chemicalInput requires ${chemicalInput.amount} ORE")
        chemicalStore[requiredChemical] = availableAmount + requiredAmount
        println("Total required ORE: ${chemicalStore[requiredChemical]}")
    } else {
        if (availableAmount >= requiredAmount) {
            println("$prefix$chemicalInput still $availableAmount available in store, consuming $requiredAmount")
            chemicalStore[requiredChemical] = availableAmount - requiredAmount
            println("$prefix$chemicalInput has ${chemicalStore[requiredChemical]} remaining in store")
        } else {
            println("$prefix$chemicalInput only $availableAmount available in store")
            val necessaryAmount = requiredAmount - availableAmount
            val reaction = getValue(requiredChemical)
            val productionAmount = reaction.output.amount
            val multiple = ceil(necessaryAmount.toFloat() / productionAmount.toFloat()).toInt()

            println("$prefix$chemicalInput producing $multiple times $reaction to get $necessaryAmount")
            chemicalStore[requiredChemical] = availableAmount + multiple * productionAmount - requiredAmount

            getNecessaryOre(
                reaction
                    .inputs
                    .map {
                        it.copy(
                            amount = it.amount * multiple
                        )
                    }.toSet(),
                level = level + 1
            )
        }

    }
}

fun List<String>.toChemicalReactionsMap(): Recipes {
    return map { reaction ->
        val (inputs, outputs) = reaction.split(" => ")

        val chemicalInputs = inputs.toChemicalInputs()
        val chemicalOutput = outputs.toChemicalOutput()

        chemicalOutput.chemical to Reaction(
            chemicalOutput,
            chemicalInputs
        )
    }.toMap()
}

inline class Chemical(val name: String) {
    override fun toString() = name
}

val Ore = Chemical("ORE")
val Fuel = Chemical("FUEL")

data class ChemicalOutput(
    val amount: Int,
    val chemical: Chemical
)

fun String.toChemicalOutput(): ChemicalOutput {
    val (amount, chemicalName) = split(" ")
    return ChemicalOutput(amount.trim().toInt(), Chemical(chemicalName.trim()))
}

data class ChemicalInput(
    val amount: Int,
    val chemical: Chemical
)

fun String.toChemicalInputs(): Set<ChemicalInput> {
    val inputs = split(", ")
    return inputs.map(String::toChemicalInput).toSet()
}

fun String.toChemicalInput(): ChemicalInput {
    val (amount, chemicalName) = split(" ")
    return ChemicalInput(amount.trim().toInt(), Chemical(chemicalName.trim()))
}

data class Reaction(
    val output: ChemicalOutput,
    val inputs: Set<ChemicalInput>
)
