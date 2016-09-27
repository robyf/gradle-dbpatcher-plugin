package net.robyf.dbpatcher.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.PluginAware

class DBPatcherPlugin implements Plugin<PluginAware> {

    def void apply(PluginAware pluginAware) {
        if (pluginAware instanceof Project) {
            doApply(pluginAware);
        } else if (pluginAware instanceof Settings) {
            pluginAware.gradle.allprojects { p ->
                p.plugins.apply(DBPatcherPlugin)
            }
        } else if (pluginAware instanceof Gradle) {
            pluginAware.allprojects { p ->
                p.plugins.apply(DBPatcherPlugin)
            }
        } else {
            throw new IllegalArgumentException("${pluginAware.getClass()} is currently not supported as an apply target, please report if you need it")
        }
    }

    def void doApply(Project project) {
        if (project.plugins.hasPlugin(DBPatcherPlugin)) {
            project.logger.info("Project ${project.name} already has the dbpatcher plugin")
            return
        }
        
        project.logger.info("Applying dbpatcher plugin to ${project.name}")
        
        def extension = project.extensions.create('dbpatcher', DBPatcherExtension)
        if (!project.configurations.asMap["dbpatcher"]) {
            project.configurations.create("dbpatcher")
            project.afterEvaluate {
                project.dependencies {
                    dbpatcher group: 'net.robyf', name: 'dbpatcher', version: project.extensions.dbpatcher.version 
                }
            }
        }
        
        project.tasks.create(name: 'dbpatcher', type: DBPatcher,
                             {
                                configuration = project.extensions.dbpatcher
                                classpath = project.configurations.dbpatcher
                             })
    }

}