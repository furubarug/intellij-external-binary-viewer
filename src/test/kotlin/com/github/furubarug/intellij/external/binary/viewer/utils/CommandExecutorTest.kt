package com.github.furubarug.intellij.external.binary.viewer.utils

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import java.io.IOException
import java.io.InputStream

/**
 * test class for [CommandExecutor]
 */
class CommandExecutorTest : StringSpec() {
    init {
        "exec :valid" {
            shouldNotThrowAny {
                CommandExecutor.exec(arrayOf("java", "-version"), InputStream.nullInputStream())
            }
        }

        "exec :invalid" {
            shouldThrow<IOException> {
                CommandExecutor.exec(arrayOf(" "), InputStream.nullInputStream())
            }
        }
    }
}
