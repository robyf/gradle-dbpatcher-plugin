package net.robyf.dbpatcher.gradle

import java.nio.charset.Charset

import net.robyf.dbpatcher.DBPatcherFactory

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskExecutionException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.After
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue

class AdditionalTaskTest {

    Project project
    Task task

    @Before    
    void setUp() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'net.robyf.dbpatcher'
        
        task = project.tasks.create([ name: "db", type: DBPatcher ])
    }
    
    @After
    void tearDown() {
        DBPatcherFactory.reset()
    }

    @Test (expected = TaskExecutionException.class)
    void missingUsername() {
        task.password = 'x'
        task.database = 'x'
        task.schemaRoot = 'x'
        
        task.execute()
    }

    @Test (expected = TaskExecutionException.class)
    void missingPassword() {
        task.username = 'x'
        task.database = 'x'
        task.schemaRoot = 'x'
        
        task.execute()
    }

    @Test (expected = TaskExecutionException.class)
    void missingDatabase() {
        task.username = 'x'
        task.password = 'x'
        task.schemaRoot = 'x'
        
        task.execute()
    }

    @Test (expected = TaskExecutionException.class)
    void missingSchemaRoot() {
        task.username = 'x'
        task.password = 'x'
        task.database = 'x'
        
        task.execute()
    }

    @Test
    void executePluginWithMandatoryParametersOnly() {
        def mock = new MockDBPatcher()
        DBPatcherFactory.setDBPatcher(mock)
    
        task.username = 'x'
        task.password = 'y'
        task.database = 'z'
        task.schemaRoot = 'w'
        
        task.execute()
        
        assertTrue("DBPatcher not invoked", mock.wasInvoked())
        
        assertEquals("Username", task.username, mock.getParams().getUsername())
        assertEquals("Password", task.password, mock.getParams().getPassword())
        assertEquals("Database", task.database, mock.getParams().getDatabaseName())
        assertEquals("Schema root", task.schemaRoot, mock.getParams().getSchemaPath())
        assertNull("Target version", mock.getParams().getTargetVersion())
        assertFalse("Rollback if error", mock.getParams().rollbackIfError())
        assertFalse("Simulation mode", mock.getParams().isSimulationMode())
        assertEquals("Charset", Charset.defaultCharset(), mock.getParams().getCharset())
    }

    @Test
    void executePluginWithAllParameters() {
        def mock = new MockDBPatcher()
        DBPatcherFactory.setDBPatcher(mock)
    
        task.username = 'x'
        task.password = 'y'
        task.database = 'z'
        task.schemaRoot = 'w'
        task.targetVersion = 25
        task.rollbackIfError = true
        task.simulationMode = true
        task.charset = 'ISO-8859-15'
        
        task.execute()
        
        assertTrue("DBPatcher not invoked", mock.wasInvoked())
        
        assertEquals("Username", task.username, mock.getParams().getUsername())
        assertEquals("Password", task.password, mock.getParams().getPassword())
        assertEquals("Database", task.database, mock.getParams().getDatabaseName())
        assertEquals("Schema root", task.schemaRoot, mock.getParams().getSchemaPath())
        assertEquals("Target version", task.targetVersion, mock.getParams().getTargetVersion())
        assertTrue("Rollback if error", mock.getParams().rollbackIfError())
        assertTrue("Simulation mode", mock.getParams().isSimulationMode())
        assertEquals("Charset", Charset.forName(task.charset), mock.getParams().getCharset())
    }

}