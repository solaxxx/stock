<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="page" />
        <g:set var="entityName" value="${message(code: 'stockHistory.label', default: 'StockHistory')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div id="create-stockHistory" class="content scaffold-create" role="main">
            <g:if test="${flash.message}">
                <div class="alert alert-info alert-dismissible" role="alert">
                    <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span></button>
                    ${flash.message}
                </div>
          %{--  <div class="message" role="status">${flash.message}</div>--}%
            </g:if>
            <g:hasErrors bean="${this.stockHistory}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.stockHistory}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <div class="operation">
                <a class="create-btn back glyphicon glyphicon-arrow-left" title="返回" href="/stockHistory/list"  > </a>
            </div>
            <form action="/stockHistory/save" method="post">
                <div class="input-group">
                    <span class="input-group-addon">增发事由</span>
                    <input type="text" name="description" value="" required="" id="description" class="form-control" >
                </div>
                <div class="input-group">
                    <span class="input-group-addon">
                        增发数量
                    </span>
                    <input type="text" name="addShares" value="" required="" id="addShares" class="form-control" >
                </div>
                <div class="page-new-bottom">
                    <input type="submit" name="create" class="save btn btn-default btn-group-sm" value="保存" id="create" />
                </div>
            </form>
        </div>
    </body>
</html>
