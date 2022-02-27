package com.github.furubarug.intellij.external.binary.viewer.utils

import com.github.furubarug.intellij.external.binary.viewer.ExternalBinaryViewerBundle

object ExtensionsSplitter {
    /**
     * Split a string consisting of semicolon-delimited extensions
     * @return extensions splitted with semicolon
     * @throws RuntimeException when splitted extensions are empty
     */
    fun split(semicolonDelimitedExtensions: String): List<String> {
        val extensions = semicolonDelimitedExtensions.split(";").filter { it.isNotBlank() }
        if (extensions.isEmpty()) {
            throw RuntimeException(ExternalBinaryViewerBundle.message("utils.extensions.empty"))
        }
        return extensions
    }
}
