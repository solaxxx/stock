package base

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import groovy.sql.Sql

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
/*@Secured('ROLE_ADMIN')*/
class UserController {
    def dataSource
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
     /*   def list = User.findAllByIsDeleted(false)*/
        def list = User.createCriteria().list (params){
           eq('isDeleted', false)
        }
        def count = User.countByIsDeleted(false)
        respond list, model:[userCount: count ]
    }

    def show(User user) {
        respond user
    }

    def create() {
        respond new User(params)
    }

    @Transactional
    def save() {
        User user = new User(
                username: params.username,
                realName: params.realName,
                password: params.password
        )
        if (user == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
        if (user.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond user.errors, view:'create'
            return
        }
        flash.message = '创建成功'
        if (user.save(flush: true)) {
            // 保存 userRole
            Role role = Role.findById(params.long('role'))
            UserRole userRole = new UserRole(
                    user: user,
                    role: role
            )
            userRole.save(flush: true)
        } else {
            println(user.errors)
            flash.message = user.errors
        }
/*        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), user.id])
                redirect user
            }
            '*' { respond user, [status: CREATED] }
        }*/


        redirect action: 'index'
    }

    def edit(User user) {
        respond user
    }

    @Transactional
    def update(User user) {
        if (user == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (user.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond user.errors, view:'edit'
            return
        }

        // 保存角色信息
        if (user.save(flush: true)) {
            Role role = Role.findById(params.long('role'))
            UserRole userRole = UserRole.findByUser(user)
            Sql sql = new Sql(dataSource)
            sql.execute('update user_role ur set ur.role_id = '+ role.id +' where user_id=' + user.id)
/*            UserRole.executeUpdate('update UserRole ur set ur.role = :role where id=:id', [
                    role : role.id,
                    id: user.id
            ])*/
           /* userRole.delete(flush:true)
                userRole = new UserRole(
                        user: user,
                        role: role
                )
                userRole.save(flush: true)
            }*/
        }


        request.withFormat {
            form multipartForm {
/*                flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), user.id])
                redirect user*/
                redirect action:"index", method:"GET"
            }
            '*'{ respond user, [status: OK] }
        }
    }

    @Transactional
    def delete(User user) {

        if (user == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        user.isDeleted = true

        user.save flush:true

        request.withFormat {
            form multipartForm {
                //flash.message = message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), user.id])
                flash.message = '删除成功'
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    @Transactional
    def changePassword () {
        String newPassword = params.newPassword
        User currentUser  = getAuthenticatedUser()
        currentUser.password = newPassword
        if (currentUser.save(flush:true)) {
            render ([success:true, message: '密码修改成功'] as JSON)
        } else {
            render ([success:false, message: '密码修改失败'] as JSON)
        }
    }
}
