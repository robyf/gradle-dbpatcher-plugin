package net.robyf.dbpatcher.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.TaskAction

import java.lang.reflect.Method
import java.nio.charset.Charset

class DBPatcher extends DefaultTask {

    DBPatcherExtension configuration
    Configuration classpath

    String username
    String password
    String database
    String schemaRoot
    Long targetVersion
    boolean rollbackIfError
    boolean simulationMode
    boolean insecureMode
    String charset

    @TaskAction
    def patch() {
        ClassLoader threadLoader = Thread.currentThread().getContextClassLoader()
   
        try {
            def path
            if (classpath) {
                path = classpath
            } else {
                path = project.configurations.dbpatcher
            }
            def urls = path.collect { it.toURI().toURL() }
            ClassLoader newLoader = new URLClassLoader(urls as URL[], threadLoader)
            Thread.currentThread().setContextClassLoader(newLoader)
            
            Class paramsClass = Thread.currentThread().getContextClassLoader().loadClass("net.robyf.dbpatcher.Parameters")
            Object params = paramsClass.getConstructor().newInstance()
            if (configuration == null) {
                params.setUsername(username)
                params.setPassword(password)
                params.setDatabaseName(database)
                params.setSchemaPath(schemaRoot)
                params.setTargetVersion(targetVersion)
                params.setRollbackIfError(rollbackIfError)
                params.setSimulationMode(simulationMode)
                params.setInsecureMode(insecureMode)
                if (charset) {
                    params.setCharset(Charset.forName(charset))
                } else {
                    params.setCharset(Charset.defaultCharset())
                }
            } else {
                params.setUsername(configuration.username)
                params.setPassword(configuration.password)
                params.setDatabaseName(configuration.database)
                params.setSchemaPath(configuration.schemaRoot)
                params.setTargetVersion(configuration.targetVersion)
                params.setRollbackIfError(configuration.rollbackIfError)
                params.setSimulationMode(configuration.simulationMode)
                params.setInsecureMode(configuration.insecureMode)
                if (configuration.charset) {
                    params.setCharset(Charset.forName(configuration.charset))
                } else {
                    params.setCharset(Charset.defaultCharset())
                }

            }
            
            if (params.getUsername() == null) {
                throw new NullPointerException("Username cannot be null");
            }
            if (params.getPassword() == null) {
                throw new NullPointerException("Password cannot be null");
            }
            if (params.getDatabaseName() == null) {
                throw new NullPointerException("Database cannot be null");
            }
            if (params.getSchemaPath() == null) {
                throw new NullPointerException("Schema root cannot be null");
            }
            
            Class factory = Thread.currentThread().getContextClassLoader().loadClass("net.robyf.dbpatcher.DBPatcherFactory")
            Method patcher = factory.getMethod("getDBPatcher")
            patcher.invoke(null).patch(params)
         } finally {
            Thread.currentThread().setContextClassLoader(threadLoader)
         }
    }
}
