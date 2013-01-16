grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.repos.default = "quonb-snapshot"

//grails.plugin.location.'infra-file-storage' = "../infra-file-storage"

grails.project.dependency.distribution = {
    String serverRoot = "http://mvn.quonb.org"
    remoteRepository(id: 'quonb-snapshot', url: serverRoot + '/plugins-snapshot-local/')
    remoteRepository(id: 'quonb-release', url: serverRoot + '/plugins-release-local/')
}

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()

        mavenCentral()
        mavenLocal()

        mavenRepo "http://mvn.quonb.org/repo"
        grailsRepo "http://mvn.quonb.org/repo", "quonb"
    }
    dependencies {
        compile "net.coobird:thumbnailator:latest.release"

        test("org.spockframework:spock-grails-support:0.7-groovy-2.0") {
            export = false
        }
    }

    plugins {
        compile ":infra-file-storage:0.2-SNAPSHOT"

        build(":tomcat:$grailsVersion",
                ":release:latest.release") {
            export = false
        }
        runtime(":hibernate:$grailsVersion") {
            export = false
        }

        //compile ":platform-core:latest.release"

        test(":spock:latest.release") {
            exclude "spock-grails-support"
            export = false
        }
    }
}
