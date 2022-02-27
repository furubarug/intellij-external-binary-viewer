package com.github.furubarug.intellij.external.binary.viewer.utils

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder

/**
 * test class for [ExtensionsSplitter]
 */
class ExtensionsSplitterTest : StringSpec() {
    init {
        "split: valid" {
            val result = ExtensionsSplitter.split(";a;bc;;def")
            result shouldContainExactlyInAnyOrder listOf("a", "bc", "def")
        }

        "split: invalid" {
            shouldThrow<RuntimeException> {
                ExtensionsSplitter.split("")
            }
            shouldThrow<RuntimeException> {
                ExtensionsSplitter.split(";;;")
            }
        }
    }
}
