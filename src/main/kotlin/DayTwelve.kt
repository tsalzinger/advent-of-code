package me.salzinger

import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    23.solve {
        var moonSystem = toMoonSystem()

        repeat(1000) {
            moonSystem = moonSystem.progress()
        }

        moonSystem
            .totalEnergy
            .toString()
    }

//    24.solve {
//        val moonSystem = toMoonSystem

//    moonSystem = MoonSystem(
//            moonSystem.moons.map {
//                Moon(
//                    it.location.run {Location(this.x + 5, this.y - 6, this.z + 11)},
//                    it.velocity
//                )
//            }
//        )
//
//        var currentMoonSystem = moonSystem.progress()
//        var count = 1
//        while (currentMoonSystem.totalEnergy != moonSystem.totalEnergy) {
//            count++
//            currentMoonSystem = moonSystem.progress()
//        }
//
//        count.toString()
//    }
}

fun List<String>.toMoonSystem() =
    toLocations()
        .map {
            Moon(
                it,
                Velocity(0, 0, 0)
            )
        }
        .run(::MoonSystem)

fun String.toCoordinatesList(): List<Int> {
    return Regex("<x=(.+), y=(.+), z=(.+)>")
        .matchEntire(this)!!
        .groupValues
        .drop(1)
        .map { it.toInt() }
        .toList()
}

fun List<String>.toLocations(): List<Location> {
    return map {
        val (x, y, z) = it.toCoordinatesList()
        Location(x, y, z)
    }
}

class MoonSystem(
    val moons: List<Moon>
) {
    val totalEnergy by lazy {
        moons
            .map {
                it.totalEnergy
            }
            .sum()
    }

    fun progress(): MoonSystem {
        val newMoons = mutableListOf<Moon>()
        println("=================")

        moons.forEach { moon ->
            println(moon)
            var currentVelocity = moon.velocity
            moons.forEach { secondMoon ->
                if (moon != secondMoon) {
                    currentVelocity += moon.getVelocityDelta(secondMoon)
                }
            }

            newMoons.add(moon.moveBy(currentVelocity))
        }

        return MoonSystem(
            newMoons.toList()
        )
    }
}

data class Location(
    val x: Int,
    val y: Int,
    val z: Int
) {
    val values by lazy { listOf(x, y, z) }
}

fun Moon.moveBy(velocity: Velocity) =
    Moon(
        Location(
            location.x + velocity.dx,
            location.y + velocity.dy,
            location.z + velocity.dz
        ),
        velocity
    )

fun Moon.getVelocityDelta(moon: Moon) = location.getVelocityDelta(moon.location)

fun Location.getVelocityDelta(location: Location): Velocity {
    return Velocity(
        (location.x - x).sign,
        (location.y - y).sign,
        (location.z - z).sign
    )
}

data class Velocity(
    val dx: Int,
    val dy: Int,
    val dz: Int
) {
    val values by lazy { listOf(dx, dy, dz) }

    operator fun plus(velocity: Velocity) =
        Velocity(
            dx + velocity.dx,
            dy + velocity.dy,
            dz + velocity.dz
        )
}

data class Moon(
    val location: Location,
    val velocity: Velocity
) {
    val potentialEnergy by lazy { (location.values.map { it.absoluteValue }.sum()) }
    val kineticEnergy by lazy { (velocity.values.map { it.absoluteValue }.sum()) }
    val totalEnergy by lazy { potentialEnergy * kineticEnergy }
}