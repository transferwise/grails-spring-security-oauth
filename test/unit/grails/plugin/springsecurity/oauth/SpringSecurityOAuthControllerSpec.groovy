package grails.plugin.springsecurity.oauth

import spock.lang.Specification
import org.scribe.model.Token
import grails.plugin.springsecurity.oauth.OAuthToken
import grails.plugin.springsecurity.userdetails.GrailsUser

@TestFor(SpringSecurityOAuthController)
class SpringSecurityOAuthControllerSpec extends Specification {

    def "onSuccess should respond with error code if request is missing data"() {
        given:
            params.provider = provider
            def oauthService = [findSessionKeyForAccessToken:{p -> 'no-such-key-in-session'}]
            controller.oauthService = oauthService
        and:
            controller.onSuccess()
        expect:
            response.status == responseCode
        where:
            provider      |  responseCode
            ''            |  400
            null          |  400
            'facebook'    |  500
    }

    def "onSuccess should respond 500 if askToLinkOrCreateAccountUri is not set"() {
        given:
            OAuthToken authToken = Mock()

            controller.springSecurityOAuthService = [createAuthToken: {p, t -> authToken}, getAskToLinkOrCreateAccountUri: { null } ]
            params.provider = provider
            def providerkey = "${provider}_oauth_session_key"
            session[providerkey] = "${provider}_oauth_session_key"
            def oauthService = [findSessionKeyForAccessToken:{p -> providerkey}]
            controller.oauthService = oauthService
        and:
            controller.onSuccess()
        expect:
            response.status == responseCode
        where:
            provider      |  responseCode
            'facebook'    |  500
    }

    def "onSuccess should redirect to askToLinkOrCreateAccountUri if the user is not logged in"() {
        given:
            OAuthToken authToken = Mock()
            controller.springSecurityOAuthService = [createAuthToken: {p, t -> authToken}, getAskToLinkOrCreateAccountUri: { "/askToLinkOrCreateAccountUri" } ]
            params.provider = provider
            def providerkey = "${provider}_oauth_session_key"
            session[providerkey] = "${provider}aaa"
            def oauthService = [findSessionKeyForAccessToken:{p -> providerkey}]
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
            controller.springSecurityOAuthService = [createAuthToken: {p, t -> authToken}, getAskToLinkOrCreateAccountUri: { "/askToLinkOrCreateAccountUri" } ]
            params.provider = provider
            def providerkey = "${provider}_oauth_session_key"
            session[providerkey] = "${provider}aaa"
            def oauthService = [findSessionKeyForAccessToken:{p -> providerkey}]
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
}

/*
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
