package grails.plugin.springsecurity.oauth

import spock.lang.*

// import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
// import org.codehaus.groovy.grails.plugins.web.mimes.MimeTypesFactoryBean
// import org.codehaus.groovy.grails.web.mime.DefaultMimeUtility

@Unroll
class SpringSecurityOAuthControllerIntegrationSpec extends Specification {
  
    SpringSecurityOAuthController controller

    def oauthService

    def setup() {
        controller = new SpringSecurityOAuthController()
        controller.oauthService = oauthService
    }
      
    def "onSuccess gives 400 if provider is missing"() {
        given:
            controller.params.provider = ''
        when:
            controller.onSuccess()
        then:
            controller.response.status == 400
    }

}