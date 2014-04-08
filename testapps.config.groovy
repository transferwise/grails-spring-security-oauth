
String projectDirCommon = new File('target').absolutePath

// app to test s2oauth plugin
s2oauth {

    //grailsVersion = '2.4.0.M1'
    //grailsHome = "/home/enrico/.gvm/grails/2.4.0.M1"

    // only if this file is in the s2oauth plugin dir
    //pluginVersion = '2.0.2'

    projectDir = projectDirCommon

    // if true the app name name will hava a -$timestamp suffix
    //timestamp = false

    // custom repositories
    customRepos = ['https://raw.github.com/fernandezpablo85/scribe-java/mvn-repo']

    plugins {
        compile = [':spring-security-oauth:2.1.0-RC2',
        ':spring-security-oauth-facebook:0.1', ':spring-security-oauth-google:0.2', ':spring-security-oauth-linkedin:0.1', ':spring-security-oauth-twitter:0.1']
    }

    
    dependencies {
        compile = ['org.scribe:scribe:1.3.6']
    }
    
    
    // will be appended to grails-app/conf/Config.groovy
    customConfig = 'oauth { debug = true }'

    log {
        debug = ['grails.plugin.springsecurity.oauth', 's2oauth.testapp']        
        info = ['grails.app.conf.BootStrap']
    }

    scripts = [
        [name:'s2-quickstart', args:['s2oauth.testapp', 'User', 'Role']], 
        [name:'s2-init-oauth', args:['s2oauth.testapp', 'OAuthID']]
    ]

}
