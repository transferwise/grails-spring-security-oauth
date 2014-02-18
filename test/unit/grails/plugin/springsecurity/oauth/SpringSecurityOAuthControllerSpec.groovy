package grails.plugin.springsecurity.oauth

import spock.lang.Specification
import org.scribe.model.Token
import grails.plugin.springsecurity.userdetails.GrailsUser

/**
 * Unit test for SpringSecurityOAuthController.
 */
@TestFor(SpringSecurityOAuthController)
class SpringSecurityOAuthControllerSpec extends Specification {

    def "onSuccess should throw exception if request is missing data"() {
        given:
            params.provider = provider
            def oauthService = [ findSessionKeyForAccessToken:{ p -> 'no-such-key-in-session' } ]
            controller.oauthService = oauthService
        when:
            controller.onSuccess()
        then:
            thrown OAuthLoginException
        where:
            provider      |  _
            ''            |  _
            null          |  _
            'facebook'    |  _
    }

    def "onSuccess should throw exception if askToLinkOrCreateAccountUri is not set"() {
        given:
            OAuthToken authToken = Mock()
            controller.springSecurityOAuthService = [ createAuthToken: { p, t -> authToken }, getAskToLinkOrCreateAccountUri: { null } ]
            params.provider = provider
            def providerkey = "${provider}_oauth_session_key"
            session[providerkey] = "${provider}_oauth_session_key"
            def oauthService = [ findSessionKeyForAccessToken:{ p -> providerkey } ]
            controller.oauthService = oauthService
        when:
            controller.onSuccess()
        then:
            thrown OAuthLoginException
        where:
            provider      |  _
            'facebook'    |  _
    }

    def "onSuccess should redirect to askToLinkOrCreateAccountUri if the user is not logged in"() {
        given:
            OAuthToken authToken = Mock()
            controller.springSecurityOAuthService = [ createAuthToken: { p, t -> authToken }, getAskToLinkOrCreateAccountUri: { "/askToLinkOrCreateAccountUri" } ]
            params.provider = provider
            def providerkey = "${provider}_oauth_session_key"
            session[providerkey] = "${provider}aaa"
            def oauthService = [ findSessionKeyForAccessToken:{ p -> providerkey } ]
            controller.oauthService = oauthService
        and:
            controller.onSuccess()
        expect:
            response.status == responseCode
            response.redirectedUrl == "/askToLinkOrCreateAccountUri"
        where:
            provider      |  responseCode
            'facebook'    |  302
    }

    def "onSuccess should redirect to defaultTargeturl if user is logged in"() {
        given:
            def token = Stub(Token) {
                getRawResponse() >> "a=1&b=2"
            }
            OAuthToken authToken = new TestOAuthToken(token, false)
            controller.springSecurityOAuthService = [ createAuthToken: { p, t -> authToken }, getAskToLinkOrCreateAccountUri: { "/askToLinkOrCreateAccountUri" } ]
            params.provider = provider
            def providerkey = "${provider}_oauth_session_key"
            session[providerkey] = "${provider}aaa"
            def oauthService = [ findSessionKeyForAccessToken:{ p -> providerkey } ]
            controller.oauthService = oauthService
        and:
            controller.onSuccess()
        expect:
            response.status == responseCode
            response.redirectedUrl == controller.getDefaultTargetUrl().uri
        where:
            provider      |  responseCode
            'facebook'    |  302
    }

    def "askToLinkOrCreateAccount should return view if user is not logged in"() {
        given:
            controller.springSecurityService = [ isLoggedIn: { false } ]
        when:
            def mav = controller.askToLinkOrCreateAccount()
        then:
            mav.viewName == "/springSecurityOAuth/askToLinkOrCreateAccount"
    }
}

/**
 * A basic implementation for oauth token for a loggedin user.
 */
class TestOAuthToken extends OAuthToken {
    TestOAuthToken(def token, def json) {
        super(token, json)
        this.principal = new GrailsUser("username", "password", true, true, true, true, [], 1L)
    }
    String getProviderName() {
        return "provider"
    }
    String getSocialId() {
        return "socialId"
    }
    String getScreenName() {
        return "screenName"
    }
}
