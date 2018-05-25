import base.Role
import base.User
import base.UserRole
import com.rishiqing.GlobalSystemOptions
import com.rishiqing.SystemOptions

class BootStrap {

    def init = { servletContext ->
/*        def adminRole = new Role(authority: 'ROLE_ADMIN').save()
        def userRole = new Role(authority: 'ROLE_USER').save()

        def testUser = new User(username: 'me', password: 'password').save()
        def testUser1 = new User(username: 'solax', password: 'ted5612689').save()

        UserRole.create testUser1, userRole
        UserRole.create testUser, adminRole

        UserRole.withSession {
            it.flush()
            it.clear()
        }*/

        //  初始化角色
        def adminRole = Role.findByAuthority('ROLE_ADMIN')
        if (!adminRole) {
            def role = new Role(
                    authority: 'ROLE_ADMIN'
            )
            role.save(flush:true)
        }

        def userRole = Role.findByAuthority('ROLE_USER')
        if (!userRole) {
            def role = new Role(
                    authority: 'ROLE_USER'
            )
            role.save(flush:true)
        }

        // 初始化人员
        def u1 = User.findByUsername('bit.liulei@163.com')
        if (!u1) {
            def testUser = new User(username: 'bit.liulei@163.com', password: '123456',realName: '刘磊')
            if (testUser.save(flush: true)) {
                new UserRole(
                        user: testUser,
                        role: adminRole
                ).save(flush: true)
            }
        }
        def u2 = User.findByUsername('610320681@qq.com')
        if (!u2) {
            def testUser1 = new User(username: '610320681@qq.com', password: 'ted5612689',realName: '张君毅')
            if (testUser1.save(flush: true)) {
                new UserRole(
                        user: testUser1,
                        role: adminRole
                ).save(flush: true)
            }
        }

        // 初始化系统数据
        def systemOption1 = SystemOptions.findByType('share_price')
        if (!systemOption1) {
            systemOption1 = new SystemOptions(type: 'share_price', value: '0.5')
            systemOption1.save(flush:true)
        }
    }
    def destroy = {
    }
}
