package me.salzinger

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class PasswordTests {

    @TestFactory
    fun `meets password criteria`() =
        listOf(
            122345 to true,
            111123 to true,
            135679 to false,
            111111 to true,
            223450 to false,
            123789 to false
        ).map {
            DynamicTest.dynamicTest("Password  ${it.first} matches rules is ${it.second}") {
                Assertions.assertEquals(
                    it.second,
                    it.first
                        .meetsPasswordCriteria()
                )
            }
        }
    @TestFactory
    fun `meets password criteria strict`() =
        listOf(
            122345 to true,
            111123 to false,
            135679 to false,
            111111 to false,
            223450 to false,
            123789 to false,
            112233 to true,
            123444 to false,
            111122 to true
        ).map {
            DynamicTest.dynamicTest("Password  ${it.first} matches strict rules is ${it.second}") {
                Assertions.assertEquals(
                    it.second,
                    it.first
                        .meetsPasswordCriteriaStrict()
                )
            }
        }
}