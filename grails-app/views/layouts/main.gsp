<%@ page import="util.ConstantUtil; grails.plugin.springsecurity.ControllerMixin" %>
<!doctype html>
<html lang="en" class="no-js">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="日事清合伙人"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:javascript src="jquery-2.2.0.min.js"/>
    <asset:javascript src="bootstrap-all.js"/>
    <asset:stylesheet src="bootstrap-all.css"/>
    <asset:javascript src="alertify.js"/>
    <asset:javascript src="main/init.js"/>
    <asset:javascript src="main/main.js"/>
    <asset:stylesheet src="main/main.css"/>
    <asset:stylesheet src="animate.css"/>
    <asset:stylesheet src="main/component.css"/>
    <asset:stylesheet src="alertify.css"/>
    <style type="text/css">

    </style>
    <script type="text/javascript">
    </script>
    <g:layoutHead/>
</head>
<body>
<nav class="n1 navbar navbar-default noSelected" role="navigation">
    <div class="logo_c">
        <asset:image src="logo@40.png" class="logo_img"/>
        <a class="logo_t">日事清合伙人</a>
    </div>
    <div class="h-center">
        <ul class="nav-ul noSelected">
            <li class="functionMenu active">融资历程</li>
            <li class="functionMenu">股权池</li>
            <li class="functionMenu">期权池</li>
            <sec:ifAnyGranted roles='${util.ConstantUtil.ROLE_ADMIN}'>
            <li class="dropdown" >
                <a href="#" class="dropdown-toggle noSelected" data-toggle="dropdown"> 系统管理 <span class="caret"></span></a>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="#"  data-toggle="modal" data-target="#systemOptions" >系统参数</a></li>
                    <li class="divider"></li>
                    <li><a href="#"  data-toggle="modal" data-target="#userManager">用户管理</a></li>
                </ul>
            </li>
            </sec:ifAnyGranted>
            <li class="bulletin">公告</li>
        </ul>
    </div>
    <div class="h-right">
        <div class="dropdown right-name">
            <a href="#" class="dropdown-toggle noSelected" data-toggle="dropdown"><sec:loggedInUserInfo field='realName'/><span class="caret"></span></a>
            <ul class="dropdown-menu" role="menu">
                <li><a href="#">个人信息</a></li>
                <li class="divider"></li>
                <li class="changePassword"><a  data-toggle="modal" data-target="#passwordManager" href="#">修改密码</a></li>
                <li class="divider"></li>
                <li><a href="/logout">退出</a></li>
            </ul>
        </div>
        <asset:image src="avatar.png" class="avatar"/>
    </div>
    </div>
</nav>
<div class="container-main">
    <div class="shift left">
        <asset:image src="button/btn-left1.png" class="left btn1"/>
        <div class="btn"></div>
    </div>
    <div class="center">
        <g:layoutBody/>
    </div>
    <div class="shift right">
        <asset:image src="button/btn-right1.png"  class="right btn1"/>
    </div>
</div>
<div class="footer navbar navbar-default noSelected">
    <div class="content">
        <p>京ICP备12028373 Copyright 2012 by Beijing Chance Create</p>
        <p>联系方式： 610320681@qq.com</p>
    </div>
</div>

<!---------------弹窗组件----------------->
<!-------------------------------系统信息--------------------------------->
<div class="modal fade" id="systemOptions">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">系统信息</h4>
            </div>
            <div class="modal-body">
                <div class="input-group">
                    <span class="input-group-addon">
                        当前股价
                    </span>
                    <input type="text" name="sharePrice" value="" required="" id="sharePrice" class="form-control" >
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary save">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<!-------------------------------用户管理--------------------------------->
<div class="modal fade" id="userManager">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">用户管理</h4>
            </div>
            <div class="modal-body">
                <iframe  class="p1" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes" src="/user"></iframe>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary"  data-dismiss="modal">关闭</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<!-------------------------------公告--------------------------------->
<div class="modal fade" id="bulletinManager">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h3 class="modal-title">日事清股权系统(alpha) — 使用说明</h3>
            </div>
            <div class="modal-body">
                <h4>使用注意事项：</h4>
                <p>1.由于是内测版，请轻点使用 </p>
                <p>2.管理员权限下，系统设置中可以设置当前股价</p>
                <p>3.管理员权限下，系统设置中可以添加成员及角色</p>
                <p>4.输入信息时，保证输入数字区域不要输入汉字</p>
                <p>5.暂不提供条件搜索功能</p>
                <p>6.个人信息功能暂未开放</p>
                <p>7.首次登录后，建议修改密码</p>
                <p>8.如果遇到bug，不需要惊慌，将问题记录在日事清系统“股权系统BUG收集”看板中，如果有紧急问题，请联系维护人员</p>
                <p>9.维护人员联系方式：15646500377 610320681@qq.com</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary"  data-dismiss="modal">关闭</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<div class="modal fade" id="passwordManager">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h3 class="modal-title">修改密码</h3>
            </div>
            <div class="modal-body">
                <div class="input-group">
                    <span class="input-group-addon">
                        新密码
                    </span>
                    <input type="password" name="newPassword" value="" required="" id="newPassword" class="form-control" >
                </div>
                <div class="input-group">
                    <span class="input-group-addon">
                        新密码
                    </span>
                    <input type="password" name="newPassword-repeat" value="" required="" id="newPassword-repeat" class="form-control" >
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"  data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary save">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
</body>
</html>
