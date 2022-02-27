package com.github.furubarug.intellij.external.binary.viewer.settings

import com.github.furubarug.intellij.external.binary.viewer.ExternalBinaryViewerBundle
import com.github.furubarug.intellij.external.binary.viewer.utils.CommandParser
import com.github.furubarug.intellij.external.binary.viewer.utils.ExtensionsSplitter
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import javax.swing.*
import javax.swing.table.AbstractTableModel
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer

class SettingsPanel : JPanel() {
    private val table = JBTable(TableModel(mutableListOf()))

    val configList: List<SettingsState.SettingsConfig>
        get() = (table.model as TableModel).configList

    init {
        layout = BorderLayout()
        val button = JButton("+").apply {
            toolTipText = ExternalBinaryViewerBundle.message("settings.settingsConfig.addButton.description")
            addActionListener {
                (table.model as TableModel).addNewRow()
            }
        }
        val toolbar = JToolBar(JToolBar.HORIZONTAL).apply {
            background = Color(0, 0, 0, 0)
            add(button)
        }
        add(toolbar, BorderLayout.NORTH)
        add(JScrollPane(table), BorderLayout.CENTER)
    }

    fun loadSettings() {
        table.model = TableModel(SettingsState.instance.deepCopy().configList.toMutableList())
        with(table.columnModel) {
            getColumn(0).cellRenderer = TextCellRenderer { ExtensionsSplitter.split(it) }.apply {
                toolTipText =
                    ExternalBinaryViewerBundle.message("settings.settingsConfig.semicolonDelimitedExtensions.description")
            }
            getColumn(1).cellRenderer = TextCellRenderer { CommandParser.parse(it) }.apply {
                toolTipText = ExternalBinaryViewerBundle.message("settings.settingsConfig.command.description")
            }
            getColumn(2).cellRenderer = TextCellRenderer { /* no validator*/ }.apply {
                toolTipText =
                    ExternalBinaryViewerBundle.message("settings.settingsConfig.convertedExtension.description")
            }
            getColumn(3).apply {
                val cell = DeleteButtonCell()
                cellRenderer = cell
                cellEditor = cell
                maxWidth = 30
            }
        }
    }

    private class TableModel(
        val configList: MutableList<SettingsState.SettingsConfig>,
    ) : AbstractTableModel() {
        override fun getRowCount() = configList.size

        override fun getColumnCount() = 4

        override fun getColumnName(columnIndex: Int): String {
            return when (columnIndex) {
                0 -> ExternalBinaryViewerBundle.message("settings.settingsConfig.semicolonDelimitedExtensions")
                1 -> ExternalBinaryViewerBundle.message("settings.settingsConfig.command")
                2 -> ExternalBinaryViewerBundle.message("settings.settingsConfig.convertedExtension")
                3 -> ""
                else -> throw IllegalArgumentException("Illegal columnIndex: $columnIndex")
            }
        }

        override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
            return when (columnIndex) {
                0 -> configList[rowIndex].semicolonDelimitedExtensions
                1 -> configList[rowIndex].command
                2 -> configList[rowIndex].convertedExtension
                3 -> ""
                else -> throw IllegalArgumentException("Illegal columnIndex: $columnIndex")
            }
        }

        override fun setValueAt(value: Any?, rowIndex: Int, columnIndex: Int) {
            if (value !is String) return
            when (columnIndex) {
                0 -> configList[rowIndex].semicolonDelimitedExtensions = value
                1 -> configList[rowIndex].command = value
                2 -> configList[rowIndex].convertedExtension = value
            }
        }

        override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = true

        fun addNewRow() {
            configList.add(SettingsState.SettingsConfig())
            fireTableRowsInserted(configList.size - 1, configList.size - 1)
        }
    }

    private class TextCellRenderer(
        private val validator: (String) -> Unit,
    ) : DefaultTableCellRenderer() {
        override fun getTableCellRendererComponent(
            table: JTable?,
            value: Any?,
            isSelected: Boolean,
            hasFocus: Boolean,
            row: Int,
            column: Int
        ): Component {
            val renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
            if (value !is String) return renderer
            val isValid = try {
                validator(value)
                true
            } catch (_: RuntimeException) {
                false
            }
            background = if (isValid) Color(0, 0, 0, 0) else Color(255, 0, 0, 50)
            return renderer
        }
    }

    private class DeleteButtonCell : AbstractCellEditor(), TableCellEditor, TableCellRenderer {
        private fun createButton(model: TableModel, rowIndex: Int) = object : JButton() {
            override fun updateUI() {
                super.updateUI()
                border = BorderFactory.createEmptyBorder()
                isFocusable = false
                isRolloverEnabled = false
                text = "X"
            }
        }.apply {
            addActionListener {
                fireEditingStopped()
                model.configList.removeAt(rowIndex)
                model.fireTableRowsDeleted(rowIndex, rowIndex)
            }
            toolTipText = ExternalBinaryViewerBundle.message("settings.settingsConfig.deleteButton.description")
        }

        override fun getCellEditorValue() = ""

        override fun getTableCellEditorComponent(
            table: JTable,
            value: Any?,
            isSelected: Boolean,
            rowIndex: Int,
            columnIndex: Int
        ) = createButton(table.model as TableModel, rowIndex)

        override fun getTableCellRendererComponent(
            table: JTable,
            value: Any?,
            isSelected: Boolean,
            hasFocus: Boolean,
            rowIndex: Int,
            columnIndex: Int
        ) = createButton(table.model as TableModel, rowIndex)
    }
}
