package com.github.furubarug.intellij.external.binary.viewer.fileType

import com.intellij.openapi.fileTypes.BinaryFileDecompiler
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProviderFactory

data class FileTypeConfig(
    val fileType: FileType,
    val extensions: List<String>,
    val decompiler: BinaryFileDecompiler,
    val fileViewProviderFactory: FileViewProviderFactory,
)
