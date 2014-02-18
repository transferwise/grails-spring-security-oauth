package grails.plugin.springsecurity.oauth

import grails.test.mixin.*
import spock.lang.*
import grails.persistence.Entity

/**
 * Unit test for SpringSecurityOAuthService.
 */
@TestFor(SpringSecurityOAuthService)
@Mock(TestUser)
class SpringSecurityOAuthServiceSpec extends Specification  {

    def "Should tell if username is taken"() {

        given: "a user named joe is in db"
            def joe = new TestUser(username: 'joe', password: 'secret')
            //assert !springSecurityOAuthService.usernameTaken(joe.username)
            joe.save()
            assert joe.errors.errorCount == 0
            assert joe.id != null
            assert  TestUser.get(joe.id).username == joe.username
        when: "service is asked usernameTaken joe"
            SpringSecurityOAuthService.metaClass.lookupUserClass = { ->
                TestUser
            }
            TestUser.metaClass.static.withNewSession = { Closure c -> c.call() }
            def springSecurityOAuthService = new SpringSecurityOAuthService()
            def taken = springSecurityOAuthService.usernameTaken(joe.username)
        then: "service responds true"
            taken
    }
}

@Entity
class TestUser {
    String username
    String password
}
