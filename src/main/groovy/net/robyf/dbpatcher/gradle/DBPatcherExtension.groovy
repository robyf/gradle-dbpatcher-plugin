package net.robyf.dbpatcher.gradle

class DBPatcherExtension {

    String version = '1.0.3'

    String username
    
    String password
    
    String database
    
    String schemaRoot
    
    Long targetVersion
    
    boolean rollbackIfError
    
    boolean simulationMode

    boolean insecureMode
    
    String charset

}
