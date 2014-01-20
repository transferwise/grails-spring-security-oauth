package grails.plugin.springsecurity.oauth

import grails.transaction.Transactional

import grails.plugin.springsecurity.oauth.OAuthToken
import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.security.core.authority.GrantedAuthorityImpl
import grails.plugin.springsecurity.userdetails.GrailsUser

@Transactional
class SpringSecurityOAuthService {

	def grailsApplication

    public OAuthToken createAuthToken(providerName, scribeToken) {
        def providerService = grailsApplication.mainContext.getBean("${providerName}SpringSecurityOAuthService")
        OAuthToken oAuthToken = providerService.createAuthToken(scribeToken)
        def OAuthID = lookupOAuthIdClass()
        def oAuthID = OAuthID.findByProviderAndAccessToken(oAuthToken.providerName, oAuthToken.socialId)
        if (oAuthID) {
            updateOAuthToken(oAuthToken, oAuthID.user)
        }
        return oAuthToken
    }

    public OAuthToken updateOAuthToken(OAuthToken oAuthToken, user) {
        def conf = SpringSecurityUtils.securityConfig

        // user

        String usernamePropertyName = conf.userLookup.usernamePropertyName
        String passwordPropertyName = conf.userLookup.passwordPropertyName
        String enabledPropertyName = conf.userLookup.enabledPropertyName
        String accountExpiredPropertyName = conf.userLookup.accountExpiredPropertyName
        String accountLockedPropertyName = conf.userLookup.accountLockedPropertyName
        String passwordExpiredPropertyName = conf.userLookup.passwordExpiredPropertyName

        String username = user."${usernamePropertyName}"
        String password = user."${passwordPropertyName}"
        boolean enabled = enabledPropertyName ? user."${enabledPropertyName}" : true
        boolean accountExpired = accountExpiredPropertyName ? user."${accountExpiredPropertyName}" : false
        boolean accountLocked = accountLockedPropertyName ? user."${accountLockedPropertyName}" : false
        boolean passwordExpired = passwordExpiredPropertyName ? user."${passwordExpiredPropertyName}" : false

        // authorities

        String authoritiesPropertyName = conf.userLookup.authoritiesPropertyName
        String authorityPropertyName = conf.authority.nameField
        Collection<?> userAuthorities = user."${authoritiesPropertyName}"
        def authorities = userAuthorities.collect { new GrantedAuthorityImpl(it."${authorityPropertyName}") }

        oAuthToken.principal = new GrailsUser(username, password, enabled, !accountExpired, !passwordExpired,
                !accountLocked, authorities ?: [GormUserDetailsService.NO_ROLE], user.id)
        oAuthToken.authorities = authorities
        oAuthToken.authenticated = true

        return oAuthToken
    }

    def getAskToLinkOrCreateAccountUri() {
        return SpringSecurityUtils.securityConfig.oauth.registration.askToLinkOrCreateAccountUri
    }

    protected String lookupUserClassName() {
        SpringSecurityUtils.securityConfig.userLookup.userDomainClassName
    }

    protected Class<?> lookupUserClass() {
        grailsApplication.getDomainClass(lookupUserClassName()).clazz
    }

    protected String lookupRoleClassName() {
        SpringSecurityUtils.securityConfig.authority.className
    }

    protected Class<?> lookupRoleClass() {
        grailsApplication.getDomainClass(lookupRoleClassName()).clazz
    }

    protected String lookupUserRoleClassName() {
        SpringSecurityUtils.securityConfig.userLookup.authorityJoinClassName
    }

    protected Class<?> lookupUserRoleClass() {
        grailsApplication.getDomainClass(lookupUserRoleClassName()).clazz
    }

    protected String lookupOAuthIdClassName() {
        SpringSecurityUtils.securityConfig.oauth.domainClass
    }

    protected Class<?> lookupOAuthIdClass() {
        grailsApplication.getDomainClass(lookupOAuthIdClassName()).clazz
    }
}
