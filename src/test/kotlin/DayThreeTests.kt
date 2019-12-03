package me.salzinger

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class WireTests {

    @TestFactory
    fun `input parsing`() =
        listOf(
            "R10,U5,L8,D4" to listOf(
                Movement(Direction.RIGHT, 10),
                Movement(Direction.UP, 5),
                Movement(Direction.LEFT, 8),
                Movement(Direction.DOWN, 4)
            )
        ).map {
            DynamicTest.dynamicTest("parse ${it.first}") {
                Assertions.assertEquals(it.second, it.first.convertToMovements())
            }
        }

    @TestFactory
    fun `movement tests`() =
        listOf(
            Movement.fromString("U3") to listOf(Point(0, 1), Point(0, 2), Point(0, 3)),
            Movement.fromString("D2") to listOf(Point(0, -1), Point(0, -2)),
            Movement.fromString("L3") to listOf(Point(-1, 0), Point(-2, 0), Point(-3, 0)),
            Movement.fromString("R2") to listOf(Point(1, 0), Point(2, 0))
        ).map {
            DynamicTest.dynamicTest("Movements ${it.first} yields points ${it.second}") {
                assertEquals(it.second, Point(0, 0).getPointsTo(it.first))
            }
        }


    @TestFactory
    fun `wire tests`() =
        listOf(
            "U3" to listOf(Point(0, 1), Point(0, 2), Point(0, 3)),
            "D2" to listOf(Point(0, -1), Point(0, -2)),
            "L3" to listOf(Point(-1, 0), Point(-2, 0), Point(-3, 0)),
            "R2" to listOf(Point(1, 0), Point(2, 0)),
            "R10,D10,L5,U7,R6" to listOf(
                Point(0, 0).getPointsTo("R10"),
                Point(10, 0).getPointsTo("D10"),
                Point(10, -10).getPointsTo("L5"),
                Point(5, -10).getPointsTo("U7"),
                Point(5, -3).getPointsTo("R6")
            ).flatten()
        ).map {
            DynamicTest.dynamicTest("Movements ${it.first} yields wire ${it.second}") {
                assertEquals(
                    Wire(it.second),
                    Wire.fromMovements(it.first.convertToMovements())
                )
            }
        }

    @TestFactory
    fun `wire overlap`() =
        listOf(
            listOf(
                "R8,U5,L5,D3",
                "U7,R6,D4,L4"
            ) to 6,

            listOf(
                "R75,D30,R83,U83,L12,D49,R71,U7,L72",
                "U62,R66,U55,R34,D71,R55,D58,R83"
            ) to 159,

            listOf(
                "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",
                "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"
            ) to 135
        ).map {
            DynamicTest.dynamicTest("Closet overlap of  ${it.first.joinToString { "[$it]" }} has distance ${it.second}") {
                Assertions.assertEquals(
                    it.second,
                    it.first
                        .convertToMovements()
                        .map(List<Movement>::toWire)
                        .toGrid()
                        .getClosestOverlapDistanceToCenter()
                )
            }
        }

}