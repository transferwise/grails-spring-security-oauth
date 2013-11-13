package grails.plugin.springsecurity.oauth

import spock.lang.*

// import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
// import org.codehaus.groovy.grails.plugins.web.mimes.MimeTypesFactoryBean
// import org.codehaus.groovy.grails.web.mime.DefaultMimeUtility

@Unroll
class SpringSecurityOAuthControllerIntegrationSpec extends Specification {
  
    SpringSecurityOAuthController controller

    def setup() {
        controller = new SpringSecurityOAuthController()
    }
      
    def "should work"() {
        given:
            controller.params.test = 'Atest'
        when:
            def res = controller.index()
        then:
            res == "index-Atest"
    }

}