package com.github.furubarug.intellij.external.binary.viewer.settings

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.Converter
import com.intellij.util.xmlb.XmlSerializerUtil.copyBean
import com.intellij.util.xmlb.annotations.OptionTag

@State(
    name = "com.github.furubarug.intellij.external.binary.viewer.settings.SettingsState",
    storages = [(Storage("external_binary_viewer.xml"))],
)
data class SettingsState(
    // FIXME use XML instead of JSON
    @OptionTag(converter = SettingsConfigListConverter::class)
    var configList: List<SettingsConfig> = emptyList(),
) : PersistentStateComponent<SettingsState> {
    companion object {
        val instance: SettingsState
            get() = service()
    }

    override fun getState() = this

    override fun loadState(state: SettingsState) = copyBean(state, this)

    fun deepCopy(): SettingsState {
        return SettingsState(
            configList = configList.map { it.copy() },
        )
    }

    data class SettingsConfig(
        var semicolonDelimitedExtensions: String = "",
        var command: String = "",
        var convertedExtension: String = "",
    )

    private class SettingsConfigListConverter : Converter<List<SettingsConfig>>() {
        override fun fromString(values: String): List<SettingsConfig>? {
            return jacksonObjectMapper().readValue(values)
        }

        override fun toString(values: List<SettingsConfig>): String? {
            return jacksonObjectMapper().writeValueAsString(values)
        }
    }
}
