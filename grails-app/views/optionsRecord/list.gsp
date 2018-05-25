<%@ page import="util.CommonUtil" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="page" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div id="list-optionsRecord" class="content scaffold-list" role="main">
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
%{--  <f:table collection="${optionsRecordList}" />--}%
    <div class="tip-container">
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active list"><a href="#">期权池</a></li>
        </ul>
    </div>
    <div class="table-container">
        <div class="operation">
            <a class="small-btn refresh glyphicon glyphicon-refresh" title="刷新" href="javascript:void(0)" onclick="location.reload()" > </a>
        </div>
        <table class="table  table-hover table-list">
            <tbody>
            <tr>
                <th>序号</th>
                <th>姓名</th>
                <th>当前总股数</th>
                <th>当前股价</th>
                <th>当前股票总值(元)</th>
<sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>
                <th>股份占比</th>
            </sec:ifAnyGranted>
                <th>操作</th>
            </tr>
            </tbody>
            <%
                int index = 0
                String realName =''
                long totalShare = 0
                long totalPrice = 0
                String sharePercent = '0%'
            %>
            <g:each in="${resultList}" var="optionsRecord">
                <%
                    index ++
                    realName = optionsRecord.realName
                    totalShare += optionsRecord.totalShare
                    totalPrice += optionsRecord.totalPrice
                    sharePercent = util.CommonUtil.percentPlus(sharePercent, optionsRecord.sharePercent)
                %>
                <tr>
                    <td>${index}</td>
                    <td>${optionsRecord.realName}</td>
                    <td>${optionsRecord.totalShare}</td>
                    <td>${optionsRecord.sharePrice}</td>
                    <td>${optionsRecord.totalPrice}</td>
                    <sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>
                        <td>${optionsRecord.sharePercent}</td>
                    </sec:ifAnyGranted>

                    <td>
                        <a class="td-btn glyphicon glyphicon-list-alt" title="编辑" href="/optionsRecord/index/${optionsRecord.userId}"></a>
                    </td>
                </tr>
            </g:each>
            <sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>
                <tr>
                    <td>--</td>
                    <td>总计</td>
                    <td>${totalShare}</td>
                    <td></td>
                    <td>${totalPrice}</td>
                    <td>${sharePercent}</td>
                </tr>
            </sec:ifAnyGranted>
        </table>

        %{--        <div class="pagination">
                    <g:paginate total="${totalCount ?: 0}" />
                </div>--}%
    </div>
</div>
</body>
</html>