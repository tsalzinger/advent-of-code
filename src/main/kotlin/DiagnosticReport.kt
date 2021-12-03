package me.salzinger

data class DiagnosticReport(
    val reports: List<ByteArray>
) {
    val binaryGammaRate by lazy {
        reports.first().mapIndexed { index, _ ->
            getMaxCommonAtIndex(index)
        }
    }

    val gammaRate by lazy {
        binaryGammaRate.joinToString(separator = "").toInt(2)
    }

    val binaryEpsilonRate by lazy {
        binaryGammaRate.map { it.xor(1) }
    }

    val epsilonRate by lazy {
        binaryEpsilonRate.joinToString(separator = "").toInt(2)
    }

    val powerConsumption by lazy {
        gammaRate * epsilonRate
    }

    private fun getMaxCommonAtIndex(index: Int): Int {
        val occurrences = reports.map {
            it[index]
        }.countOccurrences(1)

        return if (2 * occurrences > reports.size) {
            1
        } else {
            0
        }
    }
}
