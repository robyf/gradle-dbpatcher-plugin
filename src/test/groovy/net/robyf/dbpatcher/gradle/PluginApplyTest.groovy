package net.robyf.dbpatcher.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue

class PluginApplyTest {

    Project project

    @Before    
    void setUp() {
        project = ProjectBuilder.builder().build()
    }

    @Test
    void applyPlugin() {
        project.apply plugin: 'net.robyf.dbpatcher'
        assertTrue("Plugin missing", project.plugins.hasPlugin(DBPatcherPlugin))
        assertNotNull("Plugin configuration missing", project.dbpatcher)
        assertNotNull("dbpatcher task missing", project.tasks.dbpatcher)
    }
/*
    @Test
    void defaultConfigurationWithoutJavaPlugin() {
        project.apply plugin: 'net.robyf.jarinjar'
        assertEquals("Configuration not runtime", project.configurations.runtime, project.executableJar.configuration)
    }

    @Test
    void defaultConfigurationWithJavaPlugin() {
        project.apply plugin: 'java'
        project.apply plugin: 'net.robyf.jarinjar'
        assertEquals("Configuration not runtime", project.configurations.runtime, project.executableJar.configuration)
    }
*/
}