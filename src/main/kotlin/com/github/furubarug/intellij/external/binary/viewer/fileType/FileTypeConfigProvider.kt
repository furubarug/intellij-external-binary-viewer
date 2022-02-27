package com.github.furubarug.intellij.external.binary.viewer.fileType

import com.github.furubarug.intellij.external.binary.viewer.ExternalBinaryViewerBundle
import com.github.furubarug.intellij.external.binary.viewer.settings.SettingsState
import com.github.furubarug.intellij.external.binary.viewer.utils.CommandExecutor
import com.github.furubarug.intellij.external.binary.viewer.utils.CommandParser
import com.github.furubarug.intellij.external.binary.viewer.utils.ExtensionsSplitter
import com.intellij.lang.Language
import com.intellij.lang.LanguageUtil
import com.intellij.openapi.fileTypes.BinaryFileDecompiler
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import javax.swing.Icon

object FileTypeConfigProvider {
    private val icon: Icon? by lazy {
        IconLoader.findIcon("icons/16.svg")
    }

    fun getConfigList(): List<FileTypeConfig> {
        return SettingsState.instance.configList.mapNotNull {
            try {
                it.buildConfig()
            } catch (e: Exception) {
                // TODO show [e.message] in dialog
                null
            }
        }
    }

    private fun SettingsState.SettingsConfig.buildConfig(): FileTypeConfig {
        val extensions = ExtensionsSplitter.split(semicolonDelimitedExtensions)
        val commands = CommandParser.parse(command)
        return FileTypeConfig(
            FileTypeImpl(semicolonDelimitedExtensions, extensions.first(), icon),
            extensions,
            DecompilerImpl(commands),
            FileViewProviderFactoryImpl(convertedExtension.ifEmpty { "txt" }),
        )
    }

    private class FileTypeImpl(
        private val name: String,
        private val defaultExtension: String,
        private val icon: Icon?,
    ) : FileType {
        private val description = ExternalBinaryViewerBundle.message("fileType.fileType.description", defaultExtension)

        override fun getName() = name

        override fun getDescription() = description

        override fun getDefaultExtension() = defaultExtension

        override fun getIcon(): Icon? = icon

        override fun isBinary() = true

        override fun isReadOnly() = true

        override fun getCharset(file: VirtualFile, content: ByteArray?): String? = null
    }

    // TODO support charset
    private class DecompilerImpl(
        private val commands: Array<String>,
    ) : BinaryFileDecompiler {
        override fun decompile(file: VirtualFile): CharSequence {
            try {
                return CommandExecutor.exec(commands, file.inputStream)
            } catch (e: Exception) {
                // TODO show [e.message] in dialog
                throw IllegalArgumentException(e)
            }
        }
    }

    private class FileViewProviderFactoryImpl(
        private val convertedExtension: String,
    ) : FileViewProviderFactory {
        override fun createFileViewProvider(
            file: VirtualFile,
            language: Language?,
            manager: PsiManager,
            eventSystemEnabled: Boolean
        ): FileViewProvider {
            val fileType = FileTypeManager.getInstance().getFileTypeByExtension(convertedExtension)
            if (fileType != file.fileType) {
                val lang = LanguageUtil.getFileTypeLanguage(fileType)
                val factory: FileViewProviderFactory? = if (lang == null) {
                    FileTypeFileViewProviders.INSTANCE.forFileType(fileType)
                } else {
                    LanguageFileViewProviders.INSTANCE.forLanguage(lang)
                }
                if (factory != null) {
                    return factory.createFileViewProvider(file, lang, manager, eventSystemEnabled)
                }
            }
            return SingleRootFileViewProvider(manager, file, eventSystemEnabled)
        }
    }
}
