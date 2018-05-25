<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="page" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
     %{--   <asset:stylesheet src="application.css"/>--}%
    </head>
    <body>
       <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
        </g:if>
        <div class="nav" role="navigation" style="margin: 10px 0">
            <g:link class="create small-btn glyphicon glyphicon-plus" style="padding-top:0" action="create" ></g:link>
        </div>
        <div id="list-user" style="overflow-y: auto " class="content scaffold-list" role="main">
            <table class="table  table-hover table-list">
                <tbody>
                <tr>
               %{--     <th>序号</th>--}%
                    <th>姓名</th>
                    <th>用户名</th>
                    <th>角色</th>
                    <th>操作</th>
                </tr>
                </tbody>
                <g:each in="${userList}" var="user">
                    <tr>
                      %{--  <td>${user.id}</td>--}%
                        <td>${user.realName}</td>
                        <td>${user.username}</td>
                        <td>${user.getAuthoritiesString()}</td>
                        <td>
                            <g:form resource="${user}" method="DELETE">
                                <a class="td-btn glyphicon glyphicon-wrench" title="编辑" href="/user/edit/${user.id}"></a>
                                <a class="td-btn  glyphicon glyphicon-trash" title="删除" onclick="$(this).next().click()"></a>
                                <input class="delete" style="display:none" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('确定删除吗');" />
                            </g:form>
                        </td>
                    </tr>
                </g:each>
            </table>
            <div class="pagination">
                <g:paginate total="${userCount ?: 0}" />
            </div>
        </div>
    </body>
</html>