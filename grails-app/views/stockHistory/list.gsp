<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="page" />
    <g:set var="entityName" value="${message(code: 'stockHistory.label', default: 'StockHistory')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body class="page">
    <div class="operation">
        <a class="small-btn refresh glyphicon glyphicon-refresh" title="刷新" href="javascript:void(0)" onclick="location.reload()" > </a>
<sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>
        <a class="small-btn refresh glyphicon glyphicon-plus" title="添加" href="/stock/stockHistory/create/${userId}"> </a>
    </sec:ifAnyGranted>
    </div>
    <table class="table  table-hover table-list">
        <tbody>
        <tr>
            <th>序号</th>
            <th>增发事由</th>
            <th>增发数量</th>
            <th>总股数</th>
            <sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>
                <th>操作</th>
            </sec:ifAnyGranted>

        </tr>
        </tbody>
        <% long share = 0%>
        <g:each in="${stockHistoryList}" var="stockHistory">
            <%  share += stockHistory.addShares%>
            <tr>
                <td>${stockHistory.id}</td>
                <td>${stockHistory.description}</td>
                <td>${stockHistory.addShares}</td>
                <td>${share}</td>
                <sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>
                    <td>
                        <g:form resource="${stockHistory}" method="DELETE">
                            <a class="td-btn glyphicon glyphicon-wrench" title="编辑" href="/stock/stockHistory/edit/${stockHistory.id}"></a>
                            <a class="td-btn  glyphicon glyphicon-trash" title="删除" onclick="$(this).next().click()"></a>
                            <input class="delete" style="display:none" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('确定删除吗');" />
                        </g:form>
                    </td>
                </sec:ifAnyGranted>
            </tr>
        </g:each>
    </table>
</div>
</body>
</html>