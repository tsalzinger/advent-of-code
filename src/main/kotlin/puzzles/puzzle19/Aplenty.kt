package me.salzinger.puzzles.puzzle19

import common.extensions.product
import me.salzinger.common.extensions.ChunkEvaluation
import me.salzinger.common.extensions.chunkedBy
import me.salzinger.puzzles.puzzle19.Aplenty.Rule.Unconditional.Companion.toRule
import me.salzinger.puzzles.puzzle19.Aplenty.RuleCondition.Companion.toRuleCondition
import me.salzinger.puzzles.puzzle19.Aplenty.RuleResult.Companion.toRuleResult
import me.salzinger.puzzles.puzzle19.Aplenty.Workflow.Companion.toWorkflows
import java.math.BigInteger

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

    data class RangePart(
        val x: IntRange,
        val m: IntRange,
        val a: IntRange,
        val s: IntRange,
    ) {
        val possibilities: BigInteger = listOf(x, m, a, s).map { it.count().toBigInteger() }.product()

        operator fun get(type: String): IntRange {
            return when (type) {
                "x" -> x
                "m" -> m
                "a" -> a
                "s" -> s
                else -> throw RuntimeException("Unknown type $type")
            }
        }

        companion object {
            val EMPTY =
                RangePart(
                    x = IntRange.EMPTY,
                    m = IntRange.EMPTY,
                    a = IntRange.EMPTY,
                    s = IntRange.EMPTY,
                )
        }
    }

    sealed interface RuleResult {
        data object Accepted : RuleResult {
            override fun toString(): String {
                return "A"
            }
        }

        data object Rejected : RuleResult {
            override fun toString(): String {
                return "R"
            }
        }

        data class WorkflowTrigger(val workflowName: String) : RuleResult {
            override fun toString(): String {
                return workflowName
            }
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

    sealed interface RuleCondition {
        fun evaluate(part: Part): Boolean

        fun getAcceptedRangePart(rangePart: RangePart): RangePart

        data class LessThen(val type: String, val value: Int) : RuleCondition {
            override fun evaluate(part: Part): Boolean {
                return part[type] < value
            }

            override fun getAcceptedRangePart(rangePart: RangePart): RangePart {
                return when (type) {
                    "x" -> rangePart.copy(x = rangePart.x.first..<value.coerceAtMost(rangePart.x.last))
                    "m" -> rangePart.copy(m = rangePart.m.first..<value.coerceAtMost(rangePart.m.last))
                    "a" -> rangePart.copy(a = rangePart.a.first..<value.coerceAtMost(rangePart.a.last))
                    "s" -> rangePart.copy(s = rangePart.s.first..<value.coerceAtMost(rangePart.s.last))
                    else -> throw RuntimeException("Unknown type $type")
                }
            }

            override fun withoutAcceptedRange(rangePart: RangePart): RangePart {
                return GreaterThen(type, value - 1).getAcceptedRangePart(rangePart)
            }

            override fun toString(): String {
                return "$type<$value"
            }
        }

        data class GreaterThen(val type: String, val value: Int) : RuleCondition {
            override fun evaluate(part: Part): Boolean {
                return part[type] < value
            }

            override fun getAcceptedRangePart(rangePart: RangePart): RangePart {
                return when (type) {
                    "x" -> rangePart.copy(x = (value + 1).coerceAtLeast(rangePart.x.first)..rangePart.x.last)
                    "m" -> rangePart.copy(m = (value + 1).coerceAtLeast(rangePart.m.first)..rangePart.m.last)
                    "a" -> rangePart.copy(a = (value + 1).coerceAtLeast(rangePart.a.first)..rangePart.a.last)
                    "s" -> rangePart.copy(s = (value + 1).coerceAtLeast(rangePart.s.first)..rangePart.s.last)
                    else -> throw RuntimeException("Unknown type $type")
                }
            }

            override fun withoutAcceptedRange(rangePart: RangePart): RangePart {
                return LessThen(type, value + 1).getAcceptedRangePart(rangePart)
            }

            override fun toString(): String {
                return "$type>$value"
            }
        }

        companion object {
            fun String.toRuleCondition(): RuleCondition {
                return when {
                    "<" in this -> {
                        val (type, value) = split("<")

                        LessThen(type, value.toInt())
                    }

                    ">" in this -> {
                        val (type, value) = split(">")

                        GreaterThen(type, value.toInt())
                    }

                    else -> throw RuntimeException("Unsupported rule condition $this")
                }
            }
        }

        fun withoutAcceptedRange(rangePart: RangePart): RangePart
    }

    operator fun RuleCondition.invoke(part: Part) = evaluate(part)

    sealed interface Rule {
        val ruleResult: RuleResult

        fun matches(part: Part): Boolean

        fun accept(rangePart: RangePart): Pair<RangePart, RuleResult>

        fun passOn(rangePart: RangePart): RangePart

        fun withResult(ruleResult: RuleResult): Rule

        data class Unconditional(
            override val ruleResult: RuleResult,
        ) : Rule {
            override fun matches(part: Part): Boolean {
                return true
            }

            override fun accept(rangePart: RangePart): Pair<RangePart, RuleResult> {
                return rangePart to ruleResult
            }

            override fun passOn(rangePart: RangePart): RangePart {
                return RangePart.EMPTY
            }

            override fun withResult(ruleResult: RuleResult): Rule {
                return Unconditional(ruleResult)
            }

            override fun toString(): String {
                return ruleResult.toString()
            }

            companion object {
                fun String.toRule(): Rule {
                    return when {
                        ':' !in this ->
                            Unconditional(
                                toRuleResult(),
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

            override fun withResult(ruleResult: RuleResult): Rule {
                return Conditional(condition, ruleResult)
            }

            override fun accept(rangePart: RangePart): Pair<RangePart, RuleResult> {
                return condition.getAcceptedRangePart(rangePart) to ruleResult
            }

            override fun passOn(rangePart: RangePart): RangePart {
                return condition.withoutAcceptedRange(rangePart)
            }

            override fun toString(): String {
                return "$condition:$ruleResult"
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

        fun simplify(): Workflow {
            if (rules.count() == 1) {
                return this
            }

            val results =
                rules.map {
                    it.ruleResult
                }

            val unconditionalRule = rules.last()
            val unconditionalResult = unconditionalRule.ruleResult
            val conditionalRules = rules.dropLastWhile { it.ruleResult == unconditionalResult }

            return Workflow(
                name,
                conditionalRules + unconditionalRule,
            )
        }

        fun substituteWorkflowResult(
            workflowName: String,
            result: RuleResult,
        ): Workflow {
            return Workflow(
                name,
                rules.map {
                    val ruleResult = it.ruleResult
                    if (ruleResult is RuleResult.WorkflowTrigger && ruleResult.workflowName == workflowName) {
                        it.withResult(result)
                    } else {
                        it
                    }
                },
            )
        }

        override fun toString(): String {
            return "$name{${rules.joinToString(",")}}"
        }

        companion object {
            fun String.toWorkflow(): Workflow {
                val (name, rulesSpec) = trim('}').split("{")

                return Workflow(
                    name,
                    rulesSpec.split(",").map {
                        it.toRule()
                    },
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
        val (workflows, parts) =
            toList()
                .chunkedBy {
                    when {
                        it.isEmpty() -> ChunkEvaluation.END_CHUNK_AND_DISCARD
                        else -> ChunkEvaluation.APPEND_TO_CHUNK
                    }
                }
                .let { (workflows, parts) ->
                    workflows.toWorkflows() to parts.toParts()
                }

        val workflowByName =
            workflows.associateBy {
                it.name
            }

        val acceptedParts =
            parts.filter { part ->
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

    fun Sequence<String>.getNumberOfPossibleAcceptedCombinations(): BigInteger {
        val workflows =
            takeWhile {
                it.isNotEmpty()
            }.toList()
                .toWorkflows()

        var workflowsByName =
            workflows
                .associateBy {
                    it.name
                }
                .toMutableMap()

        do {
            val workflowCount = workflowsByName.count()
            val unconditionalWorkflows =
                workflowsByName
                    .values
                    .map {
                        it.simplify()
                    }
                    .filter {
                        it.rules.count() == 1
                    }
                    .map {
                        it.name to it.rules.single().ruleResult
                    }
                    .onEach {
                        workflowsByName.remove(it.first)
                    }

            workflowsByName =
                workflowsByName
                    .mapValues { entry ->
                        unconditionalWorkflows
                            .fold(entry.value) { workflow, (workflowName, result) ->
                                workflow.substituteWorkflowResult(workflowName, result)
                            }
                            .simplify()
                    }
                    .toMutableMap()
        } while (workflowsByName.count() < workflowCount)

        val fullRange = 1..4000

        var ranges =
            listOf(
                RangePart(
                    x = fullRange,
                    m = fullRange,
                    a = fullRange,
                    s = fullRange,
                ) to RuleResult.WorkflowTrigger("in"),
            )

        val accepted = mutableListOf<RangePart>()

        while (ranges.isNotEmpty()) {
            ranges =
                ranges.flatMap { (rangePart, workflowTrigger) ->
                    var remainder = rangePart

                    workflowsByName
                        .getValue(workflowTrigger.workflowName)
                        .rules
                        .map { rule ->
                            rule.accept(remainder)
                                .also {
                                    remainder = rule.passOn(remainder)
                                }
                        }
                        .mapNotNull { (range, result) ->
                            when (result) {
                                RuleResult.Accepted -> {
                                    accepted += range
                                    null
                                }

                                RuleResult.Rejected -> null
                                is RuleResult.WorkflowTrigger -> {
                                    result
                                }
                            }?.run {
                                range to this
                            }
                        }
                }
        }

        return accepted.sumOf {
            it.possibilities
        }
    }
}
