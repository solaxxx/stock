<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="page" />
        <g:set var="entityName" value="${message(code: 'stockHistory.label', default: 'StockHistory')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body class="page">
    <div class="tip-container">
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active list"><a href="#">融资历程</a></li>
        %{--    <li role="presentation" class="new"><a href="#">new</a></li>--}%
        </ul>
    </div>
    <div class="table-container">
        <iframe  class="iframe-list" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes" src="/stockHistory/list"></iframe>
        <iframe  class="iframe-create" frameborder="no" style="display: none" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes" src="/stockHistory/create"></iframe>
    </div>
    </body>
</html>