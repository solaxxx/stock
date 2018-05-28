<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>日事清合伙人</title>

    <asset:stylesheet src="main/index.css"/>
    <asset:link rel="icon" href="icon.png" type="image/x-ico" />
</head>
<body>
<div class="page1 index-page">
    <iframe  class="p1" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes" src="/stockHistory"></iframe>
</div>
<div class="page2 index-page" style="display: none" >
    <iframe  class="p1" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes" src="/shareRecord/list"></iframe>
</div>
<div class="page3 index-page" style="display: none">
    <iframe  class="p1" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes" src="/optionsRecord/list"></iframe>
</div>
<sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>
<div class="page4 index-page" style="display: none">
    <iframe  class="p1" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes" src="/tradingRecord"></iframe>
</div>
</sec:ifAnyGranted>
</body>
</html>
