package me.salzinger

fun main() {
    3.solve(1) {
        map { reportLine ->
            reportLine
                .toCharArray()
                .map {
                    it.digitToInt().toByte()
                }
                .toByteArray()
        }.run {
            DiagnosticReport(this)
        }.powerConsumption
            .toString()
    }
}
