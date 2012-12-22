grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.repos.default = "internal-snapshot"

grails.project.dependency.distribution = {

    String serverRoot = "http://artifactory.dev/"

    remoteRepository(id: 'internal-snapshot', url: serverRoot + '/plugins-snapshot-local/') {
        authentication username: 'admin', password: 'password'
    }

    remoteRepository(id: 'internal-release', url: serverRoot + '/plugins-release-local/') {
        authentication username: 'admin', password: 'password'
    }
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
        mavenRepo "http://artifactory.dev/repo"
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenCentral()
        mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"

        grailsRepo "http://artifactory.dev/repo", "dev"

    }
    dependencies {
        compile "net.coobird:thumbnailator:latest.release"
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.5'
    }

    plugins {
        compile ":infra-file-storage:0.2-SNAPSHOT"
    }
}
