
grails.project.work.dir = 'target'

grails.project.target.level = 1.6

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {

    inherits "global"
    log "warn"

    repositories {
        mavenLocal()
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
        mavenRepo 'http://repo.spring.io/milestone'
    }

    dependencies {
        runtime "org.scribe:scribe:1.3.5"
    }

    plugins {
        compile ":spring-security-core:2.0-RC2"
        compile ":oauth:2.3", {
            transitive = false
        }

        build ":release:3.0.1", ":rest-client-builder:2.0.0", {
            export = false
        }
    }

}
