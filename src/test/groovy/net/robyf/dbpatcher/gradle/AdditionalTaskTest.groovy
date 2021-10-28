package net.robyf.dbpatcher.gradle

import net.robyf.dbpatcher.DBPatcherFactory
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.After
import org.junit.Before
import org.junit.Test

import java.nio.charset.Charset

import static org.junit.Assert.*

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

    @Test (expected = NullPointerException.class)
    void missingUsername() {
        task.password = 'x'
        task.database = 'x'
        task.schemaRoot = 'x'
        
        task.patch()
    }

    @Test (expected = NullPointerException.class)
    void missingPassword() {
        task.username = 'x'
        task.database = 'x'
        task.schemaRoot = 'x'
        
        task.patch()
    }

    @Test (expected = NullPointerException.class)
    void missingDatabase() {
        task.username = 'x'
        task.password = 'x'
        task.schemaRoot = 'x'
        
        task.patch()
    }

    @Test (expected = NullPointerException.class)
    void missingSchemaRoot() {
        task.username = 'x'
        task.password = 'x'
        task.database = 'x'
        
        task.patch()
    }

    @Test
    void executePluginWithMandatoryParametersOnly() {
        def mock = new MockDBPatcher()
        DBPatcherFactory.setDBPatcher(mock)
    
        task.username = 'x'
        task.password = 'y'
        task.database = 'z'
        task.schemaRoot = 'w'
        
        task.patch()
        
        assertTrue("DBPatcher not invoked", mock.wasInvoked())
        
        assertEquals("Username", task.username, mock.getParams().getUsername())
        assertEquals("Password", task.password, mock.getParams().getPassword())
        assertEquals("Database", task.database, mock.getParams().getDatabaseName())
        assertEquals("Schema root", task.schemaRoot, mock.getParams().getSchemaPath())
        assertNull("Target version", mock.getParams().getTargetVersion())
        assertFalse("Rollback if error", mock.getParams().rollbackIfError())
        assertFalse("Simulation mode", mock.getParams().isSimulationMode())
        assertFalse("Insecure mode", mock.getParams().isInsecureMode())
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
        task.insecureMode = true
        task.charset = 'ISO-8859-15'
        
        task.patch()
        
        assertTrue("DBPatcher not invoked", mock.wasInvoked())
        
        assertEquals("Username", task.username, mock.getParams().getUsername())
        assertEquals("Password", task.password, mock.getParams().getPassword())
        assertEquals("Database", task.database, mock.getParams().getDatabaseName())
        assertEquals("Schema root", task.schemaRoot, mock.getParams().getSchemaPath())
        assertEquals("Target version", task.targetVersion, mock.getParams().getTargetVersion())
        assertTrue("Rollback if error", mock.getParams().rollbackIfError())
        assertTrue("Simulation mode", mock.getParams().isSimulationMode())
        assertTrue("Insecure mode", mock.getParams().isInsecureMode())
        assertEquals("Charset", Charset.forName(task.charset), mock.getParams().getCharset())
    }

}
