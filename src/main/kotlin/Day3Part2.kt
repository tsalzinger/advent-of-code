package me.salzinger

fun main() {
    3.solve(2) {
        map { reportLine ->
            reportLine
                .toCharArray()
                .map {
                    it.digitToInt().toByte()
                }
                .toByteArray()
        }.run {
            DiagnosticReport(this)
        }.run {
            oxygenGeneratorRating * co2SrubberRaiting
        }.toString()
    }
}
