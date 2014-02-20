
grails.project.work.dir = 'target'

grails.project.target.level = 1.6
grails.project.source.level = 1.6

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
        compile ":oauth:2.4", {
            transitive = false
        }
        compile ":codenarc:0.20"

        build ":release:3.0.1", ":rest-client-builder:1.0.3", {
            export = false
        }
    }
}

codenarc {
    reports = {
        CodenarcXmlReport('xml') {
            outputFile = 'target/CodeNarc-Report.xml'
            title = 'Spring Security OAuth plugin CodeNarc Report'
        }
        CodenarcHtmlReport('html') {
            outputFile = 'target/CodeNarc-Report.html'
            title = 'Spring Security OAuth plugin CodeNarc Report'
        }
    }
    ruleSetFiles='file:grails-app/conf/CodeNarcRuleSet.groovy'
    maxPriority1Violations = 0
    maxPriority2Violations = 5  // FIX BooleanMethodReturnsNull in controller
    maxPriority3Violations = 5
}