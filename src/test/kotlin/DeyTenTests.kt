package me.salzinger

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class AsteroidMapTests {

    @TestFactory
    fun `find best location`() =
        listOf(
            """
                .#..#
                .....
                #####
                ....#
                ...##
            """.trimIndent() to MonitoringStation(
                Point(3, 4),
                8
            ),
            """
                ......#.#.
                #..#.#....
                ..#######.
                .#.#.###..
                .#..#.....
                ..#....#.#
                #..#....#.
                .##.#..###
                ##...#..#.
                .#....####
            """.trimIndent() to MonitoringStation(
                Point(5, 8),
                33
            ),
            """
                #.#...#.#.
                .###....#.
                .#....#...
                ##.#.#.#.#
                ....#.#.#.
                .##..###.#
                ..#...##..
                ..##....##
                ......#...
                .####.###.
            """.trimIndent() to MonitoringStation(
                Point(1, 2),
                35
            ),
            """
                .#..#..###
                ####.###.#
                ....###.#.
                ..###.##.#
                ##.##.#.#.
                ....###..#
                ..#.#..#.#
                #..#.#.###
                .##...##.#
                .....#.#..
            """.trimIndent() to MonitoringStation(
                Point(6, 3),
                41
            ),
            """
                .#..##.###...#######
                ##.############..##.
                .#.######.########.#
                .###.#######.####.#.
                #####.##.#.##.###.##
                ..#####..#.#########
                ####################
                #.####....###.#.#.##
                ##.#################
                #####.##.###..####..
                ..######..##.#######
                ####.##.####...##..#
                .#####..#.######.###
                ##...#.##########...
                #.##########.#######
                .####.#.###.###.#.##
                ....##.##.###..#####
                .#.#.###########.###
                #.#.#.#####.####.###
                ###.##.####.##.#..##
            """.trimIndent() to MonitoringStation(
                Point(11, 13),
                210
            )
        ).map { (map, result) ->
            val input = map.split("\n").map(String::trim)
            DynamicTest.dynamicTest("Map ${input.joinToString()} has best asteroid location at $result") {
                assertEquals(
                    result,
                    input.toAsteroidMap().monitoringStation
                )
            }
        }

    @Test
    fun `astroid elimination`() {
        val map = """
                .#..##.###...#######
                ##.############..##.
                .#.######.########.#
                .###.#######.####.#.
                #####.##.#.##.###.##
                ..#####..#.#########
                ####################
                #.####....###.#.#.##
                ##.#################
                #####.##.###..####..
                ..######..##.#######
                ####.##.####...##..#
                .#####..#.######.###
                ##...#.####X#####...
                #.##########.#######
                .####.#.###.###.#.##
                ....##.##.###..#####
                .#.#.###########.###
                #.#.#.#####.####.###
                ###.##.####.##.#..##
            """.trimIndent()
        val eliminationOrder = listOf(
            1 to Point(11, 12),
            2 to Point(12, 1),
            3 to Point(12, 2),
            10 to Point(12, 8),
            20 to Point(16, 0),
            50 to Point(16, 9),
            100 to Point(10, 16),
            199 to Point(9, 6),
            200 to Point(8, 2),
            201 to Point(10, 9),
            299 to Point(11, 1)
        )

        val input = map.split("\n").map(String::trim)
        val asteroidMap = input.toAsteroidMap()

        val destroyable = asteroidMap.destroyAllAsteroids()

        eliminationOrder.forEach {
            assertEquals(
                it.second,
                destroyable[it.first - 1]
            )
        }
    }

    @Test
    fun `with marked monitoring station`() {
        val monitoringStation = """
            .X..#
            .....
            #####
            ....#
            ...##
        """.trimIndent().split("\n").map(String::trim).toAsteroidMap().monitoringStation

        assertEquals(
            MonitoringStation(
                Point(1, 0),
                7
            ),
            monitoringStation
        )
    }

    @Test
    fun `vector sort`() {
        val sorted = listOf(
            Vector(0, -2),
            Vector(1, -2),
            Vector(2, -2),
            Vector(2, -1),
            Vector(2, 0),
            Vector(2, 1),
            Vector(2, 2),
            Vector(1, 2),
            Vector(0, 2),
            Vector(-1, 2),
            Vector(-2, 2),
            Vector(-2, 1),
            Vector(-2, 0),
            Vector(-2, -1),
            Vector(-2, -2),
            Vector(-1, -2)
        )

        assertEquals(sorted, sorted.shuffled().sortedBy(Vector::angle))
    }
}