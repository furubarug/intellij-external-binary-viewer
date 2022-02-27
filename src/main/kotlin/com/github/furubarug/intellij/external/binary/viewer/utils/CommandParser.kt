package com.github.furubarug.intellij.external.binary.viewer.utils

import com.github.furubarug.intellij.external.binary.viewer.ExternalBinaryViewerBundle
import com.opencsv.CSVParserBuilder

object CommandParser {
    private val parser = CSVParserBuilder().withSeparator(' ').build()

    /**
     * parse a string consisting of whitespace-delimited commands
     * @return commands parsed with whitespace
     * @throws RuntimeException when parsed commands are empty
     */
    fun parse(command: String): Array<String> {
        // TODO support for tab and other whitespaces
        val commands = parser.parseLine(command).filter { it.isNotBlank() }.toTypedArray()
        if (commands.isEmpty()) {
            throw RuntimeException(ExternalBinaryViewerBundle.message("utils.commands.empty"))
        }
        return commands
    }
}
