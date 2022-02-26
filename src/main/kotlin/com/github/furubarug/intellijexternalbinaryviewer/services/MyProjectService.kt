package com.github.furubarug.intellijexternalbinaryviewer.services

import com.intellij.openapi.project.Project
import com.github.furubarug.intellijexternalbinaryviewer.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
