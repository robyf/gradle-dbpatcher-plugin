package net.robyf.dbpatcher.gradle

import net.robyf.dbpatcher.DBPatcherFactory
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import java.nio.charset.Charset

import static org.junit.Assert.*

class DefaultTaskTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    Project project

    @Before    
    void setUp() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'net.robyf.dbpatcher'
    }
    
    @After
    void tearDown() {
        DBPatcherFactory.reset()
    }

    @Test (expected = DBPatcherException.class)
    void missingUsername() {
        project.dbpatcher.password = 'x'
        project.dbpatcher.database = 'x'
        project.dbpatcher.schemaRoot = 'x'

        project.tasks.dbpatcher.patch()
    }

    @Test (expected = DBPatcherException.class)
    void missingPassword() {
        project.dbpatcher.username = 'x'
        project.dbpatcher.database = 'x'
        project.dbpatcher.schemaRoot = 'x'

        project.tasks.dbpatcher.patch()
    }

    @Test (expected = DBPatcherException.class)
    void missingDatabase() {
        project.dbpatcher.username = 'x'
        project.dbpatcher.password = 'x'
        project.dbpatcher.schemaRoot = 'x'

        project.tasks.dbpatcher.patch()
    }

    @Test (expected = DBPatcherException.class)
    void missingSchemaRoot() {
        project.dbpatcher.username = 'x'
        project.dbpatcher.password = 'x'
        project.dbpatcher.database = 'x'

        project.tasks.dbpatcher.patch()
    }

    @Test
    void executePluginWithMandatoryParametersOnly() {
        def mock = new MockDBPatcher()
        DBPatcherFactory.setDBPatcher(mock)

        project.dbpatcher.username = 'x'
        project.dbpatcher.password = 'y'
        project.dbpatcher.database = 'z'
        project.dbpatcher.schemaRoot = 'w'

        project.tasks.dbpatcher.patch()
        
        assertTrue("DBPatcher not invoked", mock.wasInvoked())
        
        assertEquals("Username", project.dbpatcher.username, mock.getParams().getUsername())
        assertEquals("Password", project.dbpatcher.password, mock.getParams().getPassword())
        assertEquals("Database", project.dbpatcher.database, mock.getParams().getDatabaseName())
        assertEquals("Schema root", project.dbpatcher.schemaRoot, mock.getParams().getSchemaPath())
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
    
        project.dbpatcher.username = 'x'
        project.dbpatcher.password = 'y'
        project.dbpatcher.database = 'z'
        project.dbpatcher.schemaRoot = 'w'
        project.dbpatcher.targetVersion = 25
        project.dbpatcher.rollbackIfError = true
        project.dbpatcher.simulationMode = true
        project.dbpatcher.insecureMode = true
        project.dbpatcher.charset = 'ISO-8859-15'
        
        project.tasks.dbpatcher.patch()
        
        assertTrue("DBPatcher not invoked", mock.wasInvoked())
        
        assertEquals("Username", project.dbpatcher.username, mock.getParams().getUsername())
        assertEquals("Password", project.dbpatcher.password, mock.getParams().getPassword())
        assertEquals("Database", project.dbpatcher.database, mock.getParams().getDatabaseName())
        assertEquals("Schema root", project.dbpatcher.schemaRoot, mock.getParams().getSchemaPath())
        assertEquals("Target version", project.dbpatcher.targetVersion, mock.getParams().getTargetVersion())
        assertTrue("Rollback if error", mock.getParams().rollbackIfError())
        assertTrue("Simulation mode", mock.getParams().isSimulationMode())
        assertTrue("Insecure mode", mock.getParams().isInsecureMode())
        assertEquals("Charset", Charset.forName(project.dbpatcher.charset), mock.getParams().getCharset())
    }

    private void writeFile(File destination, String content) throws IOException {
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(destination))
            output.write(content)
        } finally {
            if (output != null) {
                output.close()
            }
        }
    }

}
