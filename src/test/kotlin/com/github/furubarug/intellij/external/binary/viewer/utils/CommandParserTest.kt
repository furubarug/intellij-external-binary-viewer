package com.github.furubarug.intellij.external.binary.viewer.utils

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly

/**
 * test class for [CommandParser]
 */
class CommandParserTest : StringSpec() {
    init {
        "parse: valid" {
            val result = CommandParser.parse(" test   test \" test test \" test ")
            result shouldContainExactly arrayOf("test", "test", " test test ", "test")
        }

        "parse: invalid" {
            shouldThrow<RuntimeException> {
                CommandParser.parse("")
            }
            shouldThrow<RuntimeException> {
                CommandParser.parse("   ")
            }
        }
    }
}
