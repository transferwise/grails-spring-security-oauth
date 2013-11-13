package grails.plugin.springsecurity.oauth

import spock.lang.Specification

@TestFor(SpringSecurityOAuthController)
class SpringSecurityOAuthControllerSpec extends Specification {

    def setup() {
    }

    /*
    def "onSuccess should respond with error code if request is missing data"() {
        given:
            def iain = new Author(name: 'Iain Banks').save(failOnError: true)
            def bret = new Author(name: 'Bret Easton Ellis').save(failOnError: true)
            def book1 = new Book(title: 'Excession', author: iain).save(failOnError: true)
            def book2 = new Book(title: 'American psycho', author: bret).save(failOnError: true)
        when:
            request.method = "GET"
            params.provider = 'v1'
            params.entity = 'book'
            params.id = book1.id
            controller.show()
        then:
            def json = JSON.parse(response.text)
            json.id == book1.id
            json.title == 'Excession'
            json.author.name == 'Iain Banks'
    }
    */

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
}
