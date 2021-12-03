package me.salzinger

fun main() {
    2.solve(1) {
        map {
            Command.formCommandInput(it)
        }.fold(Submarine(), Submarine::executeCommand)
            .run {
                position.depth * position.horizontal
            }
            .toString()
    }
}
