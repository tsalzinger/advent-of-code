package me.salzinger.puzzles.puzzle20

object PulsePropagation {
    enum class Pulse {
        LOW,
        HIGH,
    }

    data class PulseEvent(
        val source: String,
        val target: String,
        val pulse: Pulse,
    ) {
        override fun toString(): String {
            return "$source -${pulse.name.lowercase()}-> $target"
        }
    }

    sealed class Module(
        val name: String,
        val inputs: List<String>,
        val outputs: List<String>,
    ) {
        class Builder(
            val name: String,
        ) {
            private val outputs = mutableListOf<String>()
            private val inputs = mutableListOf<String>()
            private var type: Char? = null

            fun withOutputs(outputs: List<String>): Builder {
                this.outputs.addAll(outputs)
                return this
            }

            fun addInput(input: String): Builder {
                inputs.add(input)
                return this
            }

            fun withType(type: Char): Builder {
                this.type = type
                return this
            }

            fun build(): Module {
                return when (
                    type
                ) {
                    null -> Broadcaster(name, outputs)
                    '%' -> FlipFlop(name, inputs, outputs)
                    '&' -> Conjunction(name, inputs, outputs)
                    else -> throw RuntimeException("Unsupported module type: $type")
                }
            }
        }

        abstract fun activate(
            source: String,
            pulse: Pulse,
        ): List<PulseEvent>

        protected fun generatePulseEvents(pulse: Pulse): List<PulseEvent> {
            return outputs.map {
                PulseEvent(name, it, pulse)
            }
        }

        override fun toString(): String {
            return "(${inputs.joinToString(", ")}) -> $name -> (${outputs.joinToString(", ")})"
        }

        class Button : Module(
            "Button",
            emptyList(),
            listOf("broadcaster"),
        ) {
            override fun activate(
                source: String,
                pulse: Pulse,
            ): List<PulseEvent> {
                return generatePulseEvents(Pulse.LOW)
            }

            fun activate(): List<PulseEvent> {
                return activate("--ButtonPush--", Pulse.HIGH)
            }
        }

        class Broadcaster(
            name: String,
            outputs: List<String>,
        ) : Module(
                name,
                listOf("button"),
                outputs,
            ) {
            override fun activate(
                source: String,
                pulse: Pulse,
            ): List<PulseEvent> {
                return generatePulseEvents(pulse)
            }
        }

        class FlipFlop(name: String, inputs: List<String>, outputs: List<String>) : Module(
            name,
            inputs,
            outputs,
        ) {
            var state = false

            override fun activate(
                source: String,
                pulse: Pulse,
            ): List<PulseEvent> {
                return if (pulse == Pulse.LOW) {
                    generatePulseEvents(
                        if (state) {
                            Pulse.LOW
                        } else {
                            Pulse.HIGH
                        },
                    ).also {
                        state = !state
                    }
                } else {
                    emptyList()
                }
            }
        }

        class Conjunction(name: String, inputs: List<String>, outputs: List<String>) : Module(
            name,
            inputs,
            outputs,
        ) {
            var state =
                inputs
                    .associateWith {
                        Pulse.LOW
                    }
                    .toMutableMap()

            override fun activate(
                source: String,
                pulse: Pulse,
            ): List<PulseEvent> {
                state[source] = pulse

                return generatePulseEvents(
                    if (state.values.any { it == Pulse.LOW }) {
                        Pulse.HIGH
                    } else {
                        Pulse.LOW
                    },
                )
            }
        }
    }

    fun String.getTypeAndName(): Pair<Char?, String> {
        return if (first() in setOf('%', '&')) {
            first() to drop(1)
        } else {
            null to this
        }
    }

    fun Sequence<String>.toModulesMap(): Map<String, Module> {
        val moduleBuilders =
            mutableMapOf<String, Module.Builder>()

        fun getBuilder(name: String): Module.Builder {
            return moduleBuilders.computeIfAbsent(
                name,
            ) {
                Module.Builder(name)
            }
        }

        forEach {
            val (moduleTypeAndName, outputs) = it.split(" -> ")

            val (typeOrNull, inputName) =
                moduleTypeAndName.getTypeAndName()

            getBuilder(inputName)
                .apply {
                    if (typeOrNull != null) {
                        withType(typeOrNull)
                    }

                    val outputList = outputs.split(", ")
                    withOutputs(outputList)

                    outputList.forEach { outputName ->
                        getBuilder(outputName).addInput(inputName)
                    }
                }
        }

        return moduleBuilders
            .values.associate {
                it.name to it.build()
            }
    }

    fun Sequence<String>.getNumberOfPulsesAfter1000Iterations(): Long {
        val button = Module.Button()

        val modules = toModulesMap()
        var lowPulseCount = 0L
        var highPulseCount = 0L

        repeat(1000) {
            var nextEvents = button.activate()

            while (nextEvents.isNotEmpty()) {
                nextEvents =
                    nextEvents
                        .onEach {
//                            println(it)
                            if (it.pulse == Pulse.LOW) {
                                lowPulseCount++
                            } else {
                                highPulseCount++
                            }
                        }
                        .flatMap {
                            modules.getValue(it.target).activate(it.source, it.pulse)
                        }
            }
        }

        return lowPulseCount * highPulseCount
    }
}
