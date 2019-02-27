<%@ page import="util.ConstantUtil" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="page" />
    <g:set var="entityName" value="${message(code: 'optionsRecord.label', default: 'OptionsRecord')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<div class="tip-container">
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active list"><a href="#">期权池</a></li>
    </ul>
</div>
<div class="table-container">
    <g:link class="list create-btn back glyphicon glyphicon-arrow-left" action="index" params="[id:userId]"></g:link>
    <g:link class="list create-btn back  glyphicon glyphicon-home" action="list"></g:link>
    <div id="show-optionsRecord" class="content scaffold-create" role="main">
        <form action="/stock/optionsRecord/save" method="post">
            <input type="hidden" name="userId" id="userId" value="${userId}">
            <div class="input-group">
                <span class="input-group-addon">
                    买入数量
                </span>
                <input type="text" readonly="readonly" name="buyShareNum" value="${optionsRecord?.buyShareNum}" required="" id="buyShareNum" class="form-control" >
            </div>
            <div class="input-group">
                <span class="input-group-addon">
                    卖出数量
                </span>
                <input type="text" readonly="readonly"  name="sellShareNum" value="${optionsRecord?.sellShareNum}" required="" id="sellShareNum" class="form-control" >
            </div>
            <div class="input-group">
                <span class="input-group-addon">
                    交易时间
                </span>
                <input type="text" readonly="readonly"  name="transactionDate" value="${optionsRecord&&optionsRecord.transactionDate?  optionsRecord.transactionDate.format("yyyy-MM-dd"):''}" required="" id="transactionDate" class="form-control" >
            </div>
            <div class="input-group">
                <span class="input-group-addon">
                    买卖单价
                </span>
                <input type="text"  readonly="readonly"  name="tradingPrice" value="${optionsRecord.tradingPrice}" required="" id="tradingPrice" class="form-control" >
            </div>
            <div class="input-group">
                <span class="input-group-addon">
                    当前股价
                </span>
                <input type="text"  readonly="readonly"  name="sharePrice" value="${sharePrice}" required="" id="sharePrice" class="form-control" >
            </div>
            <div class="input-group">
                <span class="input-group-addon">
                    成交金额
                </span>
                <input type="text"  readonly="readonly"  name="turnover" value="${optionsRecord && optionsRecord.turnover ? optionsRecord.turnover:0}" required="" id="turnover" class="form-control" >
            </div>
            <div class="textArea-group">
                <span class="input-group-addon">
                    备注
                </span>
                <textarea class="form-control"  readonly="readonly"  name="remark" id="remark" rows="3">${optionsRecord&&optionsRecord.remark?optionsRecord.remark:''}</textarea>
            </div>
        </form>
    </div>
</div>
</body>
</html>
