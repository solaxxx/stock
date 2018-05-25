

import grails.plugin.springsecurity.annotation.Secured

class IndexController {

    def index() {
        if (!isLoggedIn()) {
            redirect (controller: 'login', action: 'auth', params: params)
        }
        render view: 'index', model:[user:authenticatedUser]
    }
}
