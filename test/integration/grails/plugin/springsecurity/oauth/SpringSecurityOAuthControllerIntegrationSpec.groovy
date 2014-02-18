package grails.plugin.springsecurity.oauth

import spock.lang.*

/**
 * Integration test for SpringSecurityOAuthController.
 */
@Unroll
class SpringSecurityOAuthControllerIntegrationSpec extends Specification {

    SpringSecurityOAuthController controller

    def oauthService

    def setup() {
        controller = new SpringSecurityOAuthController()
        controller.oauthService = oauthService
    }

    def "onSuccess throws exception if provider is missing"() {
        given:
            controller.params.provider = ''
        when:
            controller.onSuccess()
        then:
            thrown OAuthLoginException
    }

}