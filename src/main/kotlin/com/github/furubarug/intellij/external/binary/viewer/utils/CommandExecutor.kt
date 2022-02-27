package com.github.furubarug.intellij.external.binary.viewer.utils

import com.github.furubarug.intellij.external.binary.viewer.ExternalBinaryViewerBundle
import com.github.pgreze.process.InputSource
import com.github.pgreze.process.Redirect
import com.github.pgreze.process.process
import com.github.pgreze.process.unwrap
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import java.io.InputStream

object CommandExecutor {
    /**
     * Execute external commands
     * @return execution results
     */
    fun exec(commands: Array<String>, input: InputStream): CharSequence {
        return runBlocking {
            withTimeoutOrNull(10000) {
                // TODO support charset
                process(
                    command = commands,
                    stdin = InputSource.fromInputStream(input),
                    stdout = Redirect.CAPTURE,
                    stderr = Redirect.SILENT,
                    destroyForcibly = true,
                ).unwrap().joinToString("\n")
            }
        } ?: throw RuntimeException(ExternalBinaryViewerBundle.message("utils.commands.timeout"))
    }
}
