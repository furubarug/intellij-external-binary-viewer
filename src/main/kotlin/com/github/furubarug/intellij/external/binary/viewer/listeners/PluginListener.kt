package com.github.furubarug.intellij.external.binary.viewer.listeners

import com.github.furubarug.intellij.external.binary.viewer.services.RegistrationManageService
import com.intellij.ide.AppLifecycleListener
import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.application.ApplicationManager

class PluginListener : AppLifecycleListener, DynamicPluginListener {
    private val service: RegistrationManageService
        get() = ApplicationManager.getApplication().getService(RegistrationManageService::class.java)

    override fun appStarted() = service.register()

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) = service.register()

    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) = service.unregister()
}
