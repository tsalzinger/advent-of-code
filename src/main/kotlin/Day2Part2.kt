package me.salzinger

fun main() {
    2.solve(2) {
        map {
            Command.formCommandInput(it)
        }.fold(Submarine(), Submarine::executeCommand)
            .run {
                position.depth * position.horizontal
            }
            .toString()
    }
}
