<html>
<head>
%{--	<meta name="layout" content="${gspLayout ?: 'main'}"/>--}%
	<title>日事清合伙人</title>
	<asset:link rel="icon" href="icon.png" type="image/x-ico" />
	<style type="text/css" media="screen">
*{
	font-family:"Microsoft YaHei",
	微软雅黑,"MicrosoftJhengHei",
	华文细黑,STHeiti,MingLiu
}
body  {
	font-size: 12px;
/*	background: url("/assets/background2.jpg") no-repeat center center fixed;*/
	background:  rgba(221,241,255, 0.6) url("/assets/background/bg1.png") no-repeat center center fixed;
	background-size: contain;
}
ul {
	margin: 0;
	padding: 0;
}
.t1{
	color: #FFF
}
.popupContainer {
	width: 300px;
	height: 360px;
	background: #f0f0f0;
	border: 5px solid #D9D9D9;
	position: fixed;
	right: 100px;
	top: 40%;
	margin-top: -170px;
}
.switcher{
	width: 100%;
	height: 80px;
	line-height: 80px;
	text-align: center;
	font-size: 16px;
}
.popInput {
	box-sizing: border-box;
	display: block;
	outline: none;
	width: 250px;
	height: 48px;
	margin: 10px auto;
	padding: 0 20px 0 50px;
	font-size: 14px;
	color: #262626;
	-webkit-border-radius: 24px;
	-o-border-radius: 24px;
	-ms-border-radius: 24px;
	-moz-border-radius: 24px;
	border-radius: 24px;
	border: 1px solid #c9c9c9;
	text-align: left;
}

.popInput::-webkit-input-placeholder {
	/* WebKit browsers */
	color: #939393; }
.popInput:-moz-placeholder {
	color: #939393; }
.popInput::-moz-placeholder {
	color: #939393; }
.popInput:-ms-input-placeholder {
	color: #939393; }
.posR {
	position: relative;
}
.posR i.iconUser {
	position: absolute;
	left: 45px;
	top: 14px;
	width: 18px;
	height: 21px;
	background: url("/assets/login-sprite-1.png") -5px -61px no-repeat;
}
.posR i.iconPsw {
	position: absolute;
	left: 45px;
	top: 14px;
	width: 18px;
	height: 21px;
	background: url("/assets/login-sprite-1.png") -7px -104px no-repeat;}
.btnS.blueBtn {
	background: #007eff;
	border: 1px solid #007eff;
	margin: 0 auto;
}
.btnS.blueBtn:hover {
	background: #158dff; }
.btnS.blueBtn:active {
	background: #158dff;
	box-shadow: 0 0 8px rgba(0, 0, 0, 0.2) inset; }

.btnS {
	display: block;
	width: 135px;
	height: 38px;
	line-height: 38px;
	-webkit-border-radius: 3px;
	-o-border-radius: 3px;
	-ms-border-radius: 3px;
	-moz-border-radius: 3px;
	border-radius: 3px;
	color: white;
	text-align: center;
	cursor: pointer;
	outline: none;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
	-webkit-transition: all .3s;
	-moz-transition: all .3s;
	-ms-transition: all .3s;
	-o-transition: all .3s; }
.btnS[disabled] {
	background: #aaa;
	border: 1px solid #aaa;
	cursor: not-allowed; }
.btnS[disabled]:hover {
	background: #aaa; }
.btnS[disabled]:active {
	background: #aaa;
	box-shadow: 0 0 8px rgba(0, 0, 0, 0.2) inset; }
.popupContainer#pageRegLog #loginForm .aboutPsw {
	width: 250px;
	height: 30px;
	line-height: 30px;
	margin: 15px auto; }
.popupContainer#pageRegLog #loginForm .aboutPsw ul.rememberPsw {
	float: left;
	height: 30px; }
	.popupContainer#pageRegLog #loginForm .aboutPsw ul.rememberPsw li.checkboxLoginLi {
			float: left;
			display: block;
			margin: 0;
			padding: 0;
	}
	.popupContainer#pageRegLog #loginForm .aboutPsw ul.rememberPsw li.checkboxLoginLi input {
		cursor: pointer;
		width: 12px;
		height: 12px;
		display: block;
		margin-top: 9px;
	}
.popupContainer#pageRegLog #loginForm .aboutPsw ul.rememberPsw li.checkboxTextLi {
	float: left;
	display: block;
	font-size: 12px;
	color: #939393;
	margin-left: 5px; }
.popupContainer#pageRegLog #loginForm .aboutPsw ul.rememberPsw li.checkboxTextLi label {
	cursor: pointer; }
.popupContainer#pageRegLog #loginForm .aboutPsw .findPsw {
	cursor: pointer;
	float: right;
	font-size: 12px;
	color: #939393; }
.popupContainer#pageRegLog #loginForm .aboutPsw .findPsw:hover {
	color: #333; }
.login_message {
	padding: 0 35px;
	color: red;
}
	</style>
</head>

<body>
%{--<div id="login">--}%
%{--	<div class="inner">
		<div class="fheader"><g:message code='springSecurity.login.header'/></div>

		<g:if test='${flash.message}'>
			<div class="login_message">${flash.message}</div>
		</g:if>

--}%%{--		<form action="${postUrl ?: '/login/authenticate'}" method="POST" id="loginForm" class="cssform" autocomplete="off">
			<p>
				<label for="username">用户名:</label>
				<input type="text" class="text_" name="${usernameParameter ?: 'username'}" id="username"/>
			</p>

			<p>
				<label for="password">密码:</label>
				<input type="password" class="text_" name="${passwordParameter ?: 'password'}" id="password"/>
			</p>

			<p id="remember_me_holder">
				<input type="checkbox" class="chk" name="${rememberMeParameter ?: 'remember-me'}" id="remember_me" <g:if test='${hasCookie}'>checked="checked"</g:if>/>
				<label for="remember_me">记住密码</label>
			</p>

			<p>
				<input type="submit" id="submit" value="${message(code: 'springSecurity.login.button')}"/>
			</p>
		</form>--}%%{--


	</div>
</div>--}%
%{--<h1 class="t1">日事清股权</h1>
<h3 class="t2">为成为全球领先的协作管理工具而奋斗</h3>--}%
<div class="popupContainer" id="pageRegLog">

	<div class="switcher">
		日事清股权系统登录
	</div>
	<form  action="${postUrl ?: '/stock/login/authenticate'}" method="POST" id="loginForm" class="cssform" autocomplete="off">
		<div class="posR">
			<!--<ul class="searchResult"></ul>-->
			<input name="${usernameParameter ?: 'username'}" value="${username}" id="username" type="text" class="popInput emails" placeholder="输入您的账号">
			<i class="iconUser"></i>
		</div>
		<div class="posR">
			<input  name="${passwordParameter ?: 'password'}" id="password" type="password" class="popInput passwords" placeholder="输入您的密码">
			<i class="iconPsw"></i>
		</div>
		<g:if test='${flash.message}'>
			<div class="login_message">${flash.message}</div>
		</g:if>
		<div class="aboutPsw">
			<ul class="rememberPsw">
				<li class="checkboxLoginLi">
					<input type="checkbox" name="${rememberMeParameter ?: 'remember-me'}" id="remember_me" <g:if test='${hasCookie}'>checked="checked"</g:if>/>
				</li>
				<li class="checkboxTextLi">
					<label for="remember_me">记住密码</label>
				</li>
			</ul>
			<div class="findPsw">
				忘记密码
			</div>
		</div>
		<div class="reg-error error-place-hack"></div>
		<input type="submit" class="btnS blueBtn popupBtn" id="submit" value="登录"/>
		%{--<div id="logBtn" class="btnS blueBtn popupBtn">登　录</div>--}%
	</form>
</div>
</div>
<script>
(function() {
	document.forms['loginForm'].elements['${usernameParameter ?: 'username'}'].focus();
})();
</script>
</body>
</html>
