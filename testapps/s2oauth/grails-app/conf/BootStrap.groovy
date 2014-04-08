
import s2oauth.testapp.*

class BootStrap {

    def grailsApplication

    def init = { servletContext ->

        log.info 'Starting with OAuth configuration:'
        log.info grailsApplication.config.oauth.toString()
        log.info ''
        
        def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
        def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
                
        def admin = new User(username:'admin', password:'admin'*2, email:'admin@yourapp.com', 'enabled':true)
        if (admin.save(flush:true)) {
            UserRole.create admin, adminRole
            log.info 'created user admin/adminadmin'
        }
        
        def user = new User(username:'user', password:'user'*2, email:'user@yourapp.com', 'enabled':true)
        if (user.save(flush:true)) {
            UserRole.create user, userRole
            log.info 'created user user/useruser'
        }
        
    }
    def destroy = {
    }
}
