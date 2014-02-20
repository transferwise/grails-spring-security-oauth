grails-spring-security-oauth
============================

[![Build Status](https://travis-ci.org/enr/grails-spring-security-oauth.png?branch=master)](https://travis-ci.org/enr/grails-spring-security-oauth)

Adds OAuth-based authentication to the [Spring Security plugin][spring-security-plugin] using the [OAuth plugin][oauth-plugin].

This plugin provides an OAuth realm that can easily be integrated into existing applications and a host of utility functions to make things like "log in with Twitter" almost trivial.

**This README refers to the code currently in the master branch, maybe not yet released.**

Please, see [Grails plugin portal][s2oauth-grails-website] for instructions about the actual released version.


Changelog
---------

**2.1 (not yet released)**

Controller and view are not anymore created in the app but are available as plugin artefacts.

View has a configurable layout via `grails.plugin.springsecurity.oauth.layout` configuration key.

**2.0.2**

Version provider's service and token are moved into separate plugin, example:

    ':spring-security-oauth-google:0.1'


Installation
------------

In `BuildConfig.groovy`, add the dependency to "plugins" section:

```groovy

    plugins {
        //...
        compile ':spring-security-oauth:2.0.2'

        // and also you need add at least one of extensions:
        compile ':spring-security-oauth-facebook:0.1'
        compile ':spring-security-oauth-google:0.1'
        compile ':spring-security-oauth-linkedin:0.1'
        compile ':spring-security-oauth-twitter:0.1'
        compile ':spring-security-oauth-yahoo:0.1'
        //...
    }
```

Change the version to reflect the actual version you would like to use.


Usage
-----

Install the plugin as described above by adding a dependency in BuildConfig.groovy. Then follow Spring Security Core and OAuth plugins documentation.

Sample configuration for Spring Security Core request mapping:

```groovy
grails.plugin.springsecurity.securityConfigType = "InterceptUrlMap"
grails.plugin.springsecurity.interceptUrlMap = [
    '/':                ['permitAll'],
    '/index':           ['permitAll'],
    '/index.gsp':       ['permitAll'],
    '/**/js/**':        ['permitAll'],
    '/**/css/**':       ['permitAll'],
    '/**/images/**':    ['permitAll'],
    '/**/favicon.ico':  ['permitAll'],
    '/login/**':        ['permitAll'],
    '/logout/**':       ['permitAll'],
    '/oauth/**':        ['permitAll']
]
```

Sample configuration for OAuth plugin (each provider needs the proper spring-security-oauth-* plugin):

```groovy
def appName = grails.util.Metadata.current.'app.name'
def baseURL = grails.serverURL ?: "http://localhost:${System.getProperty('server.port', '8080')}/${appName}"
oauth {
    debug = true
    providers {
        facebook {
            api = org.scribe.builder.api.FacebookApi
            key = 'oauth_facebook_key'
            secret = 'oauth_facebook_secret'
            successUri = '/oauth/facebook/success'
            failureUri = '/oauth/facebook/error'
            callback = "${baseURL}/oauth/facebook/callback"
        }
        twitter {
            api = org.scribe.builder.api.TwitterApi
            key = 'oauth_twitter_key'
            secret = 'oauth_twitter_secret'
            successUri = '/oauth/twitter/success'
            failureUri = '/oauth/twitter/error'
            callback = "${baseURL}/oauth/twitter/callback"
        }
        linkedin {
            api = org.scribe.builder.api.LinkedInApi
            key = 'oauth_linkedin_key'
            secret = 'oauth_linkedin_secret'
            successUri = '/oauth/linkedin/success'
            failureUri = '/oauth/linkedin/error'
            callback = "${baseURL}/oauth/linkedin/callback"
        }

        // for Google OAuth 1.0 DEPRECATED
        google {
            api = org.scribe.builder.api.GoogleApi
            key = 'oauth_google_key'
            secret = 'oauth_google_secret'
            successUri = '/oauth/google/success'
            failureUri = '/oauth/google/error'
            callback = "${baseURL}/oauth/google/callback"
            scope = 'https://www.googleapis.com/auth/userinfo.email'
        }

        // for Google OAuth 2.0
        google {
            api = org.grails.plugin.springsecurity.oauth.GoogleApi20
            key = 'oauth_google_key'
            secret = 'oauth_google_secret'
            successUri = '/oauth/google/success'
            failureUri = '/oauth/google/error'
            callback = "${baseURL}/oauth/google/callback"
            scope = 'https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email'
        }
    }
}
```

Other configuration keys you can use, are (with their default values):

```groovy
grails.plugin.springsecurity.oauth.active = true
grails.plugin.springsecurity.oauth.domainClass = 'OAuthID'
grails.plugin.springsecurity.oauth.userLookup.oAuthIdsPropertyName = 'oAuthIDs'
grails.plugin.springsecurity.oauth.registration.askToLinkOrCreateAccountUri = '/oauth/askToLinkOrCreateAccount'
grails.plugin.springsecurity.oauth.registration.roleNames = ['ROLE_USER']
```

Once you have an user domain and configured provider names, go with:

    grails s2-init-oauth [domain-class-package] [oauthid-class-name]

Example:

    grails s2-init-oauth com.yourapp OAuthID

that creates the domain class `com.yourapp.OAuthID`

Finally, add:

```groovy
static hasMany = [oAuthIDs: OAuthID]
```

to you user domain class.

In your view you can use the taglib exposed from this plugin and from OAuth plugin to create links and to know if the user is authenticated with a given provider:

```xml
<oauth:connect provider="twitter" id="twitter-connect-link">Twitter</oauth:connect>
<oauth:connect provider="facebook" id="facebook-connect-link">Facebook</oauth:connect>
<oauth:connect provider="google" id="google-connect-link">Google</oauth:connect>
<oauth:connect provider="linkedin" id="linkedin-connect-link">Linkedin</oauth:connect>
<oauth:connect provider="yahoo" id="yahoo-connect-link">Yahoo</oauth:connect>
Logged with facebook? <s2o:ifLoggedInWith provider="facebook">yes</s2o:ifLoggedInWith><s2o:ifNotLoggedInWith provider="facebook">no</s2o:ifNotLoggedInWith>
Logged with twitter? <s2o:ifLoggedInWith provider="twitter">yes</s2o:ifLoggedInWith><s2o:ifNotLoggedInWith provider="twitter">no</s2o:ifNotLoggedInWith>
Logged with google? <s2o:ifLoggedInWith provider="google">yes</s2o:ifLoggedInWith><s2o:ifNotLoggedInWith provider="google">no</s2o:ifNotLoggedInWith>
Logged with linkedin? <s2o:ifLoggedInWith provider="linkedin">yes</s2o:ifLoggedInWith><s2o:ifNotLoggedInWith provider="linkedin">no</s2o:ifNotLoggedInWith>
Logged with yahoo? <s2o:ifLoggedInWith provider="yahoo">yes</s2o:ifLoggedInWith><s2o:ifNotLoggedInWith provider="yahoo">no</s2o:ifNotLoggedInWith>
```

Extensions
----------

* [Facebook][spring-security-oauth-facebook-plugin]
* [Google][spring-security-oauth-google-plugin]
* [LinkedIn][spring-security-oauth-linkedin-plugin]
* [Twitter][spring-security-oauth-twitter-plugin]
* [VK][spring-security-oauth-vkontakte-plugin]
* [Weibo][spring-security-oauth-weibo-plugin]
* [Yahoo][spring-security-oauth-yahoo-plugin]
* [Dailymotion][spring-security-oauth-dailymotion-plugin]

That's it!

[s2oauth-grails-website]: http://www.grails.org/plugin/spring-security-oauth
[spring-security-plugin]: http://grails.org/plugin/spring-security-core
[oauth-plugin]: http://grails.org/plugin/oauth
[spring-security-oauth-facebook-plugin]: https://github.com/donbeave/grails-spring-security-oauth-facebook
[spring-security-oauth-google-plugin]: https://github.com/donbeave/grails-spring-security-oauth-google
[spring-security-oauth-linkedin-plugin]: https://github.com/donbeave/grails-spring-security-oauth-linkedin
[spring-security-oauth-twitter-plugin]: https://github.com/donbeave/grails-spring-security-oauth-twitter
[spring-security-oauth-vkontakte-plugin]: https://github.com/donbeave/grails-spring-security-oauth-vkontakte
[spring-security-oauth-weibo-plugin]: https://github.com/donbeave/grails-spring-security-oauth-weibo
[spring-security-oauth-yahoo-plugin]: https://github.com/donbeave/grails-spring-security-oauth-yahoo
[spring-security-oauth-dailymotion-plugin]: https://github.com/tamershahin/grails-spring-security-oauth-dailymotion
