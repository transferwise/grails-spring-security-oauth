import grails.plugin.springsecurity.SpringSecurityUtils

/**
* @author <a href='mailto:cazacugmihai@gmail.com'>Mihai Cazacu</a>
* @author <a href='mailto:enrico@comiti.name'>Enrico Comiti</a>
* @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
*/
class SpringSecurityOauthGrailsPlugin {
    def version = "2.1.0-RC1"
    def grailsVersion = "2.0 > *"
    def pluginExcludes = [
            "web-app/*"
    ]

    def title = "Spring Security OAuth plugin"
    def description = '''Adds OAuth-based authentication to the
[Spring Security plugin|http://grails.org/plugin/spring-security-core] using the
[OAuth plugin|http://grails.org/plugin/oauth]. This plugin provides an OAuth realm that can easily be integrated
into existing applications and a host of utility functions to make things like "log in with Twitter" almost trivial.'''

    def documentation = "http://grails.org/plugin/spring-security-oauth"

    def license = "APACHE"
    def organization = [name: "Macrobit Software", url: "http://macrobit.ro/"]
    def developers = [
        [name: "Mihai Cazacu", email: "cazacugmihai@gmail.com"],
        [name: "Enrico Comiti", email: "enrico@comiti.name"],
        [name: "Alexey Zhokhov", email: "donbeave@gmail.com"]]
    def issueManagement = [system: 'GITHUB', url: 'https://github.com/enr/grails-spring-security-oauth/issues']
    def scm = [url: 'https://github.com/enr/grails-spring-security-oauth/']

    def doWithSpring = {
        def conf = SpringSecurityUtils.securityConfig

        boolean printStatusMessages = (conf.printStatusMessages instanceof Boolean) ? conf.printStatusMessages : true

        if (!conf || !conf.active) {
            return
        }

        SpringSecurityUtils.loadSecondaryConfig 'DefaultSpringSecurityOAuthConfig'
        // have to get again after overlaying DefaultSpringSecurityOAuthConfig
        conf = SpringSecurityUtils.securityConfig

        if (!conf.oauth.active) {
            return
        }

        if (printStatusMessages) {
            println '\nConfiguring Spring Security OAuth ...'

            println '... finished configuring Spring Security OAuth\n'
        }
    }
}
