<%@ page import="util.ConstantUtil; util.CommonUtil" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="page" />
    <g:set var="entityName" value="${message(code: 'optionsRecord.label', default: 'OptionsRecord')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div id="list-optionsRecord" class="content scaffold-list" role="main">
    <div class="tip-container">
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active list"><a href="#">期权池</a></li>
        </ul>
    </div>
    <div class="table-container">
        <div class="operation">
            <a class="small-btn back glyphicon glyphicon-arrow-left" title="返回" href="/optionsRecord/list"  > </a>
            <a class="small-btn refresh glyphicon glyphicon-refresh" title="刷新" href="javascript:void(0)" onclick="location.reload()" > </a>
            <sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>
                <a class="small-btn refresh glyphicon glyphicon-plus" title="交易" href="/optionsRecord/create/${userId}"> </a>
            </sec:ifAnyGranted>
        </div>
        <table class="table  table-hover table-list">
            <tbody>
            <tr>
                <th>序号</th>
                <th>姓名</th>
                <th>交易时间</th>
                <th>买入数量</th>
                <th>卖出数量</th>
                <th>买卖单价(元)</th>
                <th>当前股价（元）</th>
                <th>成交金额(元)</th>
                <th>当前股票总值（元）</th>
                <th>备注</th>
             %{--   <sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>--}%
                    <th>操作</th>
            %{--    </sec:ifAnyGranted>--}%
            </tr>
            </tbody>
            <%
                int index = 0
                String name
                Date  transactionDate
                long buyShareNum = 0
                long sellShareNum = 0
                double turnover = 0.0
                double totalShare = 0.0
            %>
            <g:each in="${optionsRecordList}" var="oneEntity">
                <%
                    index ++;
                    name = oneEntity.user.realName;
                    transactionDate = oneEntity.transactionDate;
                    buyShareNum += oneEntity.buyShareNum;
                    sellShareNum += oneEntity.sellShareNum;
                    turnover += Double.parseDouble(oneEntity.turnover);
                    totalShare += oneEntity.totalShare;
                %>
                <tr>
                    <td>${index}</td>
                    <td>${oneEntity.user.realName}</td>
                    <td><g:formatDate date="${oneEntity.transactionDate}" format="yyyy-MM-dd"></g:formatDate> </td>
                    <td>${oneEntity.buyShareNum}</td>
                    <td>${oneEntity.sellShareNum}</td>
                    <td>${oneEntity.tradingPrice}</td>
                    <td>${oneEntity.sharePrice}</td>
                    <td>${oneEntity.turnover}</td>
                    <td>${oneEntity.totalShare}</td>
                    <td>${CommonUtil.subString(oneEntity.remark)}</td>

                        <td>
                            <g:form resource="${oneEntity}" method="DELETE">
                                <a class="td-btn glyphicon glyphicon-list-alt" title="详细信息" href="/optionsRecord/show?id=${oneEntity.id}&userId=${userId}"></a>
                                <sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>
                                <a class="td-btn glyphicon glyphicon-wrench" title="编辑" href="/optionsRecord/edit?id=${oneEntity.id}&userId=${userId}"></a>
                                <a class="td-btn  glyphicon glyphicon-trash" title="删除" onclick="$(this).next().click()"></a>
                                <input class="delete" style="display:none" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('确定删除吗');" />
                                </sec:ifAnyGranted>
                                </g:form>
                        </td>

                </tr>
            </g:each>
            <tr class="total">
                <td>总计</td>
                <td> ${name}</td>
                <td></td>
                <td> ${buyShareNum}</td>
                <td> ${sellShareNum}</td>
                <td> </td>
                <td> </td>
                <td> ${util.CommonUtil.formatLong(turnover)}</td>
                <td> ${util.CommonUtil.formatLong(totalShare)}</td>
                <td></td>
               %{-- <sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>--}%
                    <td> </td>
               %{-- </sec:ifAnyGranted>--}%
            </tr>
        </table>

        %{--            <div class="pagination">
                        <g:paginate total="${totalCount ?: 0}" />
                    </div>--}%
    </div>
</div>
</body>
</html>