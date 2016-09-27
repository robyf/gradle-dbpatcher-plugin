package net.robyf.dbpatcher.gradle

class DBPatcherExtension {

    String version = '1.0.1'

    String username
    
    String password
    
    String database
    
    String schemaRoot
    
    Long targetVersion
    
    boolean rollbackIfError
    
    boolean simulationMode
    
    String charset

}