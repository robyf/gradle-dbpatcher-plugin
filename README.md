gradle-dbpatcher-plugin
=======================

A gradle plugin for the [dbpatcher](https://github.com/robyf/dbpatcher) database patching tool.

Usage
-----

### Apply the plugin
To use the plugin with Gradle 2.1 or later, add the following to your build.gradle file:
```groovy
plugins {
    id "net.robyf.dbpatcher" version "1.0.1"
}
```

To use the plugin with Gradle 2.0 or older, or to use a snapshot release of the
plugin, add the following to build.gradle:

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "gradle.plugin.net.robyf:gradle-dbpatcher-plugin:1.0.1"
    }
}

apply plugin: "net.robyf.dbpatcher"
```

### Configuration
The behavior of this plugin is controlled by setting various options in the ```dbpatcher```
block of your build.gradle file.
- ```username```: database username (mandatory).
- ```password```: database password (mandatory).
- ```database```: database name (mandatory).
- ```schemaRoot```: database definition directory or zip file (mandatory).
- ```targetVersion```: target version number. If not specified the database is patched to the latest available version.
- ```rollbackIfError```: roll back the entire operation in case of errors. Default: ```false```.
- ```simulationMode```: simulate the operation without touching the actual database. Default: ```false```.
- ```charset```: character set used in the patch definition files. Default: the system default one.

### Examples

#### Basic scenario
```groovy
plugins {
    id "net.robyf.dbpatcher" version "1.0.1"
}

dbpatcher {
    username = "username"
    password = "password"
    database = "database"
    schemaRoot = "database"
}
```
and run ```./gradlew dbpatcher```

#### Advanced scenario
```groovy
plugins {
    id "net.robyf.dbpatcher" version "1.0.1"
}

dbpatcher {
    username = "username"
    password = "password"
    database = "database"
    schemaRoot = "database"
    targetVersion = 10
    rollbackIfError = true
    simulationMode = true
    charset = UTF-8
}
```
and run ```./gradlew dbpatcher```

#### Additional task
```groovy
plugins {
    id "net.robyf.dbpatcher" version "1.0.1"
}

dbpatcher {
    username = "username"
    password = "password"
    database = "database"
    schemaRoot = "database"
}

task productionDatabase(type: net.robyf.dbpatcher.gradle.DBPatcher) {
    username = "prodUsername"
    password = "prodPassword"
    database = "prodDatabase"
    schemaRoot = "database"
    rollbackIfError = true
    simulationMode = true
}
```
and run ```./gradlew dbpatcher``` for the development database and ```./gradlew productionDatabase``` for the production one.
