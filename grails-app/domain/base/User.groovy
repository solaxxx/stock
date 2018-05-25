package base

import com.rishiqing.OptionsRecord
import com.rishiqing.ShareRecord
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

	private static final long serialVersionUID = 1

	transient springSecurityService
	transient def roleSelected

	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	boolean isDeleted = false

	String realName

	static hasMany =[
	        shareRecord : ShareRecord,
			optionsRecord :OptionsRecord
	]

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this)*.role
	}

	String getAuthorityValue () {
		UserRole userRole = UserRole.findByUser(this)
		if (!userRole) return ''
		Role role = userRole.role
		if (!role) return ''
		return role.authority
	}

	Integer getAuthorityId () {
		UserRole userRole = UserRole.findByUser(this)
		if (!userRole) return null
		Role role = userRole.role
		if (!role) return null
		return  role.id
	}

	String getAuthoritiesString () {
		UserRole userRole = UserRole.findByUser(this)
		if (!userRole) return ''
		Role role = userRole.role
		if (!role) return ''
		if (role.authority == 'ROLE_ADMIN') {
			return '管理员'
		} else if (role.authority == 'ROLE_USER') {
			return  '成员'
		}
		return  ''
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}

	static transients = ['springSecurityService']

	static constraints = {
		password blank: false, password: true
		username blank: false, unique: true
	/*	realName nullable: true*/
	}

	static mapping = {
		password column: '`password`'
	}

	def toMap () {
		return [
				id:id,
				username : username,
		        realName : realName,
				role : this.getAuthorities()
		]
	}
}
