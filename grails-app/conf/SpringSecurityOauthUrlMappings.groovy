import grails.plugin.springsecurity.SpringSecurityUtils
import org.scribe.exceptions.OAuthException
import grails.util.Holders

class SpringSecurityOauthUrlMappings {

    static mappings = {
        def active = Holders.grailsApplication.config.grails?.plugin?.springsecurity?.oauth?.active
        boolean enabled = (active instanceof Boolean) ? active : true
        if (enabled) {
        	"/oauth/$provider/success"(controller: "springSecurityOAuth", action: "onSuccess")
        	"/oauth/$provider/failure"(controller: "springSecurityOAuth", action: "onFailure")
        	"/oauth/askToLinkOrCreateAccount"(controller: "springSecurityOAuth", action: "askToLinkOrCreateAccount")
        	"/oauth/linkaccount"(controller: "springSecurityOAuth", action: "linkAccount")
        	"/oauth/createaccount"(controller: "springSecurityOAuth", action: "createAccount")

        	"500"(controller: "login", action: "auth", exception: OAuthException)
    	}
    }

}
