package com.github.furubarug.intellij.external.binary.viewer.services

import com.github.furubarug.intellij.external.binary.viewer.fileType.FileTypeConfig
import com.github.furubarug.intellij.external.binary.viewer.fileType.FileTypeConfigProvider
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileTypes.BinaryFileTypeDecompilers
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx
import com.intellij.psi.FileTypeFileViewProviders

class RegistrationManageService {
    private var configList: List<FileTypeConfig> = emptyList()

    fun register() {
        delete(configList)
        configList = FileTypeConfigProvider.getConfigList()
        create(configList)
    }

    fun unregister() {
        delete(configList)
        configList = emptyList()
    }

    private fun create(target: List<FileTypeConfig>) {
        if (target.isEmpty()) return
        ApplicationManager.getApplication().invokeAndWait {
            ApplicationManager.getApplication().runWriteAction {
                FileTypeManagerEx.getInstanceEx().apply {
                    target.forEach {
                        it.extensions.forEach { extension ->
                            associateExtension(it.fileType, extension)
                        }
                    }
                }
            }
            BinaryFileTypeDecompilers.getInstance().apply {
                target.forEach {
                    addExplicitExtension(it.fileType, it.decompiler)
                }
            }
            FileTypeFileViewProviders.INSTANCE.apply {
                target.forEach {
                    addExplicitExtension(it.fileType, it.fileViewProviderFactory)
                }
            }
        }
    }

    private fun delete(target: List<FileTypeConfig>) {
        if (target.isEmpty()) return
        ApplicationManager.getApplication().invokeAndWait {
            BinaryFileTypeDecompilers.getInstance().apply {
                target.forEach {
                    removeExplicitExtension(it.fileType, it.decompiler)
                }
            }
            FileTypeFileViewProviders.INSTANCE.apply {
                target.forEach {
                    removeExplicitExtension(it.fileType, it.fileViewProviderFactory)
                }
            }
            ApplicationManager.getApplication().runWriteAction {
                FileTypeManagerEx.getInstanceEx().apply {
                    target.forEach {
                        it.extensions.forEach { extension ->
                            removeAssociatedExtension(it.fileType, extension)
                        }
                    }
                }
            }
        }
    }
}
