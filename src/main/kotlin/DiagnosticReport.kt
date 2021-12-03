package me.salzinger

import kotlin.experimental.xor

data class DiagnosticReport(
    val reports: List<ByteArray>
) {
    val binaryGammaRate by lazy {
        reports.first().mapIndexed { index, _ ->
            reports.getMaxCommonAtIndex(index)
        }.toByteArray()
    }

    val gammaRate by lazy {
        binaryGammaRate.toInt()
    }

    val binaryEpsilonRate by lazy {
        binaryGammaRate.map { it.xor(1) }.toByteArray()
    }

    val epsilonRate by lazy {
        binaryEpsilonRate.toInt()
    }

    val powerConsumption by lazy {
        gammaRate * epsilonRate
    }

    val oxygenGeneratorRating by lazy {
        var currentIndex = 0

        var remainingReports = reports

        while (remainingReports.size > 1) {
            val maxCommonAtIndex = remainingReports.getMaxCommonAtIndex(currentIndex)
            remainingReports = remainingReports.filter { report ->
                report[currentIndex] == maxCommonAtIndex
            }
            currentIndex++
        }

        remainingReports.single().toInt()
    }

    val co2SrubberRaiting by lazy {
        var currentIndex = 0

        var remainingReports = reports

        while (remainingReports.size > 1) {
            val maxCommonAtIndex = remainingReports.getMaxCommonAtIndex(currentIndex)
            remainingReports = remainingReports.filter {
                it[currentIndex] != maxCommonAtIndex
            }
            currentIndex++
        }

        remainingReports.single().toInt()
    }

    private fun List<ByteArray>.getMaxCommonAtIndex(index: Int): Byte {
        val occurrences = map {
            it[index]
        }.countOccurrences(1)

        return if (2 * occurrences >= size) {
            1
        } else {
            0
        }
    }
}
