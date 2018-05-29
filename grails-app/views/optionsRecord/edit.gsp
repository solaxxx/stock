<%@ page import="util.ConstantUtil" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="page" />
    <g:set var="entityName" value="${message(code: 'optionsRecord.label', default: 'OptionsRecord')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<div class="tip-container">
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active list"><a href="#">期权池</a></li>
    </ul>
</div>
<div class="table-container">
    <g:link class="list create-btn back glyphicon glyphicon-arrow-left" action="index"  params="[id:userId]"></g:link>
    <g:link class="list create-btn back  glyphicon glyphicon-home" action="list"></g:link>
    <div id="edit-optionsRecord" class="content scaffold-create" role="main">
        <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
        </g:if>
        <g:hasErrors bean="${this.optionsRecord}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.optionsRecord}" var="error">
                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
        </g:hasErrors>
        <form action="/optionsRecord/update" method="put">
            <input type="hidden" name="id" id="id" value="${optionsRecord.id}">
            <div class="input-group">
                <span class="input-group-addon">
                    买入数量
                </span>
                <input type="text" name="buyShareNum" value="${optionsRecord?.buyShareNum}" required="" id="buyShareNum" class="form-control" >
            </div>
            <div class="input-group">
                <span class="input-group-addon">
                    卖出数量
                </span>
                <input type="text" name="sellShareNum" value="${optionsRecord?.sellShareNum}" required="" id="sellShareNum" class="form-control" >
            </div>
            <div class="input-group date date-show" data-provide="datepicker" data-date-format="${util.ConstantUtil.JS_DATE_FORMATE_STR}">
                <input type="text" class="form-control" value="${optionsRecord&&optionsRecord.transactionDate?  optionsRecord.transactionDate.format("yyyy-MM-dd"): new Date().format(util.ConstantUtil.JAVA_DATE_FORMATE_STR)}" name="transactionDate" value="" required="" id="transactionDate"  placeholder="交易日期">
                <div class="input-group-addon">
                    <span class="glyphicon glyphicon-th"></span>
                </div>
            </div>
            <div class="input-group">
                <span class="input-group-addon">
                    买卖单价
                </span>
                <input type="text" name="tradingPrice" value="${optionsRecord.tradingPrice}" required="" id="tradingPrice" class="form-control" >
            </div>
            <div class="input-group">
                <span class="input-group-addon">
                    当前股价
                </span>
                <input type="text" name="sharePrice" value="${sharePrice}" required="" id="sharePrice" class="form-control" >
            </div>
            <div class="input-group">
                <span class="input-group-addon">
                    成交金额
                </span>
                <input type="text" name="turnover" value="${optionsRecord && optionsRecord.turnover ? optionsRecord.turnover:0}" required="" id="turnover" class="form-control" >
            </div>
            <div class="textArea-group">
                <span class="input-group-addon">
                    备注
                </span>
                <textarea class="form-control"name="remark" id="remark" rows="3">${optionsRecord&&optionsRecord.remark?optionsRecord.remark:''}</textarea>
            </div>
            <div class="page-new-bottom">
                <input type="submit" name="create" class="save btn btn-default btn-group-sm" value="保存" id="create" />
            </div>
        </form>
    </div>
</div>
</body>
</html>
