<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="page" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <g:link class="list" action="index" style="margin-left: 20px">返回</g:link>
        <div id="edit-user" class="content scaffold-edit" role="main">
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.user}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.user}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <form action="/user/update/${this.user.id}" method="post">
                <input type="hidden" name="_method" value="PUT" id="_method">
                <input type="hidden" name="version" value="${this.user.version}" id="version">
                <div class="input-group">
                    <span class="input-group-addon">用户名</span>
                    <input type="text" name="username" value="${this.user.username}" required="" id="username" class="form-control" >
                </div>
                <div class="input-group">
                    <span class="input-group-addon">
                        姓名
                    </span>
                    <input type="text" name="realName" value="${this.user.realName}" required="" id="realName" class="form-control" >
                </div>
                <div class="input-group">
                    <select class="form-control" name="role">
                        <option value="2" ${this.user.getAuthorityId() == 2 ? "selected='selected'" : ""}>成员</option>
                        <option value="1" ${this.user.getAuthorityId() == 1 ? "selected='selected'" : ""}>管理员</option>
                    </select>
                </div>
%{--                <div class="input-group">
                    <span class="input-group-addon">

                    </span>
                    <input type="text" name="realName" value="${this.user.realName}" required="" id="realName" class="form-control" >
                </div>--}%

                <div class="page-new-bottom">
                    <input type="submit" name="create" class="save btn btn-default btn-group-sm" value="保存" id="create" />
                </div>
            </form>
        </div>
    </body>
</html>
