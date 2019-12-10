package me.salzinger

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
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
}