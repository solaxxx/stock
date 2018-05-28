<%@ page import="util.CommonUtil" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="page" />
    <g:set var="entityName" value="${message(code: 'tradingRecord.label', default: 'TradingRecord')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body class="page">
    <div class="operation">
        <a class="small-btn refresh glyphicon glyphicon-refresh" title="刷新" href="javascript:void(0)" onclick="location.reload()" > </a>
    <sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>
        <a class="small-btn refresh glyphicon glyphicon-plus" title="添加" href="/tradingRecord/create"> </a>
    </sec:ifAnyGranted>
    </div>
    <table class="table  table-hover table-list">
        <tbody>
        <tr>
            <th>序号</th>
            <th>交易时间</th>
            <th>卖出人</th>
            <th>卖出数量</th>
            <th>卖出类型</th>
            <th>买入人</th>
            <th>买入数量</th>
            <th>买入类型</th>
            <th>成交金额</th>
            <th>当前股票总值（元）</th>
            <th>备注</th>
            <sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>
                <th>操作</th>
            </sec:ifAnyGranted>

        </tr>
        </tbody>
        <g:each in="${tradingRecordList}" var="tradingRecord">
            <tr>
                <!-- 交易号id -->
                <td>${tradingRecord.id}</td>
                <!-- 交易时间  -->
                <td><g:formatDate date="${tradingRecord.transactionDate}" format="yyyy-MM-dd"></g:formatDate> </td>
                <!-- 卖出人  -->
                <td>${tradingRecord.sellUser.realName}</td>
                <!-- 卖出数量 -->
                <td>${tradingRecord.sellShareNum}</td>
                <!-- 卖出类型 -->
                <g:if test="${tradingRecord.sellType == 1}">
                    <td>股权</td>
                </g:if>
                <g:if test="${tradingRecord.sellType == 2}">
                    <td>期权</td>
                </g:if>
                <!-- 买入人 -->
                <td>${tradingRecord.buyUser.realName}</td>
                <!-- 买入数量 -->
                <td>${tradingRecord.buyShareNum}</td>
                <!-- 买入类型 -->
                <g:if test="${tradingRecord.buyType == 1}">
                    <td>股权</td>
                </g:if>
                <g:if test="${tradingRecord.buyType == 2}">
                    <td>期权</td>
                </g:if>
                <!-- 成交金额 -->
                <td>${tradingRecord.turnover}</td>
                <!-- 当前股票总值 -->
                <td>${tradingRecord.totalShare}</td>
                <td>${util.CommonUtil.subString(tradingRecord.remark)}</td>
                <sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>
                    <td>
                        <g:form resource="${tradingRecord}" method="DELETE">
                            <a class="td-btn glyphicon glyphicon-wrench" title="编辑" href="/tradingRecord/edit/${tradingRecord.id}"></a>
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