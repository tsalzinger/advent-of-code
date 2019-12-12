package me.salzinger

import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    23.solve {
        var moonSystem = toLocations()
            .map {
                Moon(
                    it,
                    Velocity(0, 0, 0)
                )
            }
            .run(::MoonSystem)

        repeat(1000) {
            moonSystem = moonSystem.progress()
        }

        moonSystem
            .moons
            .map {
                it.totalEnergy
            }
            .sum()
            .toString()
    }
}

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
    fun progress(): MoonSystem {
        val newMoons = mutableListOf<Moon>()

        moons.forEach { moon ->
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