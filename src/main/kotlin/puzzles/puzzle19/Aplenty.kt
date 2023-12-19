package me.salzinger.puzzles.puzzle19

import me.salzinger.common.extensions.ChunkEvaluation
import me.salzinger.common.extensions.chunkedBy
import me.salzinger.puzzles.puzzle19.Aplenty.Rule.Unconditional.Companion.toRule
import me.salzinger.puzzles.puzzle19.Aplenty.RuleCondition
import me.salzinger.puzzles.puzzle19.Aplenty.RuleCondition.Companion.toRuleCondition
import me.salzinger.puzzles.puzzle19.Aplenty.RuleResult.Companion.toRuleResult
import me.salzinger.puzzles.puzzle19.Aplenty.Workflow.Companion.toWorkflows

object Aplenty {

    data class Part(
        val x: Int,
        val m: Int,
        val a: Int,
        val s: Int,
    ) {

        val value: Int = x + m + a + s
        operator fun get(type: String): Int {
            return when (type) {
                "x" -> x
                "m" -> m
                "a" -> a
                "s" -> s
                else -> throw RuntimeException("Unknown type $type")
            }
        }
    }

    sealed interface RuleResult {

        val terminal: Boolean

        data object Accepted : RuleResult {
            override val terminal = true
        }

        data object Rejected : RuleResult {
            override val terminal = true
        }

        data class WorkflowTrigger(val workflowName: String) : RuleResult {
            override val terminal = false
        }

        companion object {
            fun String.toRuleResult(): RuleResult {
                return when (this) {
                    "A" -> Accepted
                    "R" -> Rejected
                    else -> WorkflowTrigger(this)
                }
            }
        }
    }

    fun interface RuleCondition {

        fun evaluate(part: Part): Boolean

        companion object {
            fun String.toRuleCondition(): RuleCondition {
                return when {
                    "<" in this -> {
                        val (type, value) = split("<")

                        RuleCondition {
                            it[type] < value.toInt()
                        }
                    }

                    ">" in this -> {
                        val (type, value) = split(">")

                        RuleCondition {
                            it[type] > value.toInt()
                        }
                    }

                    else -> throw RuntimeException("Unsupported rule condition $this")
                }
            }
        }
    }

    operator fun RuleCondition.invoke(part: Part) = evaluate(part)

    sealed interface Rule {
        val ruleResult: RuleResult

        fun matches(part: Part): Boolean
        data class Unconditional(
            override val ruleResult: RuleResult,
        ) : Rule {
            override fun matches(part: Part): Boolean {
                return true
            }

            companion object {
                fun String.toRule(): Rule {
                    return when {
                        ':' !in this -> Unconditional(
                            toRuleResult()
                        )

                        else -> {
                            val (conditionSpec, resultSpec) = split(":")

                            Conditional(
                                conditionSpec.toRuleCondition(),
                                resultSpec.toRuleResult(),
                            )
                        }
                    }
                }
            }
        }

        data class Conditional(
            val condition: RuleCondition,
            override val ruleResult: RuleResult,
        ) : Rule {
            override fun matches(part: Part): Boolean {
                return condition(part)
            }
        }
    }

    data class Workflow(
        val name: String,
        val rules: List<Rule>,
    ) {

        fun evaluate(part: Part): RuleResult {
            return rules.first {
                it.matches(part)
            }.ruleResult
        }

        companion object {
            fun String.toWorkflow(): Workflow {
                val (name, rulesSpec) = trim('}').split("{")

                return Workflow(
                    name,
                    rulesSpec.split(",").map {
                        it.toRule()
                    }
                )
            }

            fun List<String>.toWorkflows(): List<Workflow> {
                return map { it.toWorkflow() }
            }
        }
    }

    fun Part.evaluate(workflow: Workflow): RuleResult {
        return workflow
            .rules
            .first {
                it.matches(this)
            }
            .ruleResult
    }

    fun List<String>.toParts(): List<Part> {
        return map { partSpec ->
            partSpec.trim('{', '}')
                .split(",")
                .associate { typeAndValue ->
                    typeAndValue
                        .split("=")
                        .let { (type, value) ->
                            type to value.toInt()
                        }
                }
                .let { typeToValue ->
                    Part(
                        x = typeToValue.getValue("x"),
                        m = typeToValue.getValue("m"),
                        a = typeToValue.getValue("a"),
                        s = typeToValue.getValue("s"),
                    )
                }
        }
    }

    fun Sequence<String>.getTotalSumOfPartRatings(): Long {
        val (workflows, parts) = toList()
            .chunkedBy {
                when {
                    it.isEmpty() -> ChunkEvaluation.END_CHUNK_AND_DISCARD
                    else -> ChunkEvaluation.APPEND_TO_CHUNK
                }
            }
            .let { (workflows, parts) ->
                workflows.toWorkflows() to parts.toParts()
            }

        val workflowByName = workflows.associateBy {
            it.name
        }

        val acceptedParts = parts.filter { part ->
            var currentResult: RuleResult = RuleResult.WorkflowTrigger("in")

            while (currentResult is RuleResult.WorkflowTrigger) {
                currentResult = workflowByName.getValue(currentResult.workflowName).evaluate(part)
            }

            currentResult is RuleResult.Accepted
        }

        return acceptedParts
            .sumOf {
                it.value.toLong()
            }
    }
}
