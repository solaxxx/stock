<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="page" />
        <g:set var="entityName" value="${message(code: 'stockHistory.label', default: 'StockHistory')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        %{--<div class="edit-top"> </div>--}%
    <div class="operation">
        <a class="create-btn back glyphicon glyphicon-arrow-left" title="返回" href="/stockHistory/list"  > </a>
    </div>
        <div id="edit-stockHistory" class="content scaffold-edit" role="main">
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.stockHistory}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.stockHistory}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
          <form action="/stockHistory/update/${this.stockHistory.id}" method="post">
     <input type="hidden" name="_method" value="PUT" id="_method">
     <input type="hidden" name="version" value="${this.stockHistory.version}" id="version">
                <div class="input-group">
                    <span class="input-group-addon">增发事由</span>
                    <input type="text" name="description" value="${this.stockHistory.description}" required="" id="description" class="form-control" >
                </div>
                <div class="input-group">
                    <span class="input-group-addon">
                        增发数量
                    </span>
                    <input type="text" name="addShares" value="${this.stockHistory.addShares}" required="" id="addShares" class="form-control" >
                </div>
                <div class="page-new-bottom">
                    <input type="submit" name="create" class="save btn btn-default btn-group-sm" value="保存" id="create" />
                </div>
            </form>
           %{-- /stockHistory/update/2--}%
%{--           <g:form resource="${this.stockHistory}" method="PUT">
                <g:hiddenField name="version" value="${this.stockHistory?.version}" />
                <fieldset class="form">
                    <f:all bean="stockHistory"/>
                </fieldset>
                <fieldset class="buttons">
                    <input class="save" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>--}%
        </div>
    </body>
</html>
