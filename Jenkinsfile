node {
    stage("Checkout") {
        checkout scm
    }

    stage("Clean") {
        sh "./gradlew --info clean"
    }

    stage("Build") {
        sh "./gradlew --info build"
    }

    stage("Publish reports") {
        sh "./gradlew --info sonarqube"
        publishUnitTestResults()
    }

    stage("Upload archives") {
        sh "./gradlew --info uploadArchives"
    }

    if (env.BRANCH_NAME == "master") {
        stage("Publish plugin") {
            sh "./gradlew --info publishPlugins"
        }
    }
}

def publishUnitTestResults() {
    step([$class: "JUnitResultArchiver", testResults: "build/**/TEST-*.xml"])
}
