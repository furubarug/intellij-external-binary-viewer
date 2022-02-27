package com.github.furubarug.intellij.external.binary.viewer.settings

import com.github.furubarug.intellij.external.binary.viewer.ExternalBinaryViewerBundle
import com.github.furubarug.intellij.external.binary.viewer.services.RegistrationManageService
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class SettingsConfigurable : Configurable {
    private var component: SettingsPanel? = null
    private val settings = SettingsState.instance

    override fun isModified(): Boolean {
        return component?.configList != settings.configList
    }

    override fun getDisplayName() = ExternalBinaryViewerBundle.message("settings.displayName")

    override fun apply() {
        val wasModified = isModified
        component?.run {
            settings.configList = configList.map { it.copy() }
        }
        if (wasModified) {
            ApplicationManager.getApplication().getService(RegistrationManageService::class.java).register()
        }
    }

    override fun reset() {
        component?.loadSettings()
    }

    override fun createComponent(): JComponent? {
        component = component ?: SettingsPanel()
        return component
    }

    override fun disposeUIResources() {
        component = null
    }
}
