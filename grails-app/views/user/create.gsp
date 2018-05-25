<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="page" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <g:link class="list create-btn  glyphicon glyphicon-arrow-left" action="index"></g:link>
        <div id="create-user" class="content scaffold-create" role="main">
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
%{--            <g:form action="save">
                <fieldset class="form">
                    <f:all bean="user"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>--}%
            <form action="/user/save" method="post">
                <div class="input-group">
                    <span class="input-group-addon">用户名</span>
                    <input type="text" name="username" value="" required="" id="username" class="form-control" >
                </div>
                <div class="input-group">
                    <span class="input-group-addon">
                        姓名
                    </span>
                    <input type="text" name="realName" value="" required="" id="realName" class="form-control" >
                </div>
                <div class="input-group">
                    <span class="input-group-addon">
                        密码
                    </span>
                    <input type="text" name="password" value="123456" required="" id="password" class="form-control" >
                </div>
                <div class="input-group">
                    <select class="form-control" name="role">
                        <option value="2" selected="selected">成员</option>
                        <option value="1">管理员</option>
                    </select>
                </div>

                <div class="page-new-bottom">
                    <input type="submit" name="create" class="save btn btn-default btn-group-sm" value="保存" id="create" />
                </div>
            </form>
        </div>
    </body>
</html>
