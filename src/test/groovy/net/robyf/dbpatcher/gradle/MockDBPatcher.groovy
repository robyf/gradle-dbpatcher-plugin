package net.robyf.dbpatcher.gradle

import net.robyf.dbpatcher.DBPatcher
import net.robyf.dbpatcher.Parameters

class MockDBPatcher implements DBPatcher {

    private boolean invoked = false
    private Parameters params = null

    def void patch(Parameters params) {
        this.params = params
        invoked = true
    }
    
    def boolean wasInvoked() {
        return invoked
    }
    
    def Parameters getParams() {
        return params
    }

}