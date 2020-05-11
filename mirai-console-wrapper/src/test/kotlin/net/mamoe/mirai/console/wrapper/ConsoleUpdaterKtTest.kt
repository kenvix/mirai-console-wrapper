package net.mamoe.mirai.console.wrapper

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ConsoleUpdaterKtTest {
    @Test
    fun testVersionCompare() {
        assertEquals(
                listOf(
                        "1.0.0",
                        "1.0-RC3",
                        "1.0-RC-2",
                        "1.0-RC2",
                        "1.0-RC",
                        "1.0-EA2",
                        "1.0-EA-2",
                        "1.0-EA",
                        "0.40.0"
                ),
                listOf(
                        "1.0.0",
                        "0.40.0",
                        "1.0-EA-2",
                        "1.0-EA",
                        "1.0-EA2",
                        "1.0-RC",
                        "1.0-RC2",
                        "1.0-RC-2",
                        "1.0-RC3"
                ).sortByVersion()
        )
    }
}

fun main() {
    ConsoleUpdaterKtTest().testVersionCompare()
}