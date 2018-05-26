<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache,must-revalidate">
		<title>修改密码</title>
		<link type="text/css" rel="stylesheet"
			href="${pageContext.request.contextPath}/css/regist.personal.css" />
		<link type="text/css" rel="stylesheet"
			href="${pageContext.request.contextPath}/css/passport.base.css" />
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.6.4.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery.cookie.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/safety.js"></script>
</head>
<body>
	<div class="w" id="logo">
		<div>
			<a href="http://localhost:8082"> <img
				src="${pageContext.request.contextPath}/images/safety-logo.gif"
				alt="安全管理信息平台" width="170" height="60" />
			</a> <b></b>
		</div>
	</div>

	<div class="w" id="regist">
		<div class="mt">
			<ul class="tab">
				<li class="curr" id="personName"></li>
			</ul>
			<div class="extra">
				<span><a href="http://localhost:8082/safety-portal/" class="flk13">首页</a></span>
			</div>
		</div>

		<div class="mc">
			<form id="personRegForm" method="post" onsubmit="return false;">
				<div class="form" onselectstart="return false;">
					<div class="item" id="select-regName">
						<span class="label"><b class="ftx04">*</b>请输入原密码：</span>

						<div class="fl item-ifo">
							<div class="o-intelligent-regName">
								<input type="text" id="regName" name="username" class="text"
									tabindex="1" autoComplete="off" onpaste="return false;"
									value=""
									onfocus="if(this.value=='') this.value='';this.style.color='#333'"
									onblur="if(this.value=='') {this.value='';this.style.color='#999999'}" />
								<input type="hidden" id="personId" name="personId"/>
								<i class="i-name"></i>
								<ul id="intelligent-regName" class="hide"></ul>
								<label id="regName_succeed" class="blank"></label> <label
									id="regName_error" class="hide"></label>
							</div>
						</div>
					</div>
					<div id="o-password">
						<div class="item">
							<span class="label"><b class="ftx04">*</b>请设置新密码：</span>

							<div class="fl item-ifo">
								<input type="password" id="pwd" name="password" class="text"
									tabindex="2" style="ime-mode:disabled;" onpaste="return  false"
									autocomplete="off" /> <i class="i-pass"></i> <label
									id="pwd_succeed" class="blank"></label> <label id="pwd_error"></label>
								<span class="clr"></span>
							</div>
						</div>

						<div class="item">
							<span class="label"><b class="ftx04">*</b>请确认密码：</span>

							<div class="fl item-ifo">
								<input type="password" id="pwdRepeat" name="pwdRepeat"
									class="text" tabindex="3" onpaste="return  false"
									autocomplete="off" /> <i class="i-pass"></i> <label
									id="pwdRepeat_succeed" class="blank"></label> <label
									id="pwdRepeat_error"></label>
							</div>
						</div>
					</div>
					<div class="item">
						<span class="label">&nbsp;</span> <input type="button"
							class="btn-img btn-regist" id="registsubmit" value="立即修改"
							tabindex="8" clstag="regist|keycount|personalreg|07"
							onclick="REGISTER.reg();" />
					</div>
				</div>
				<div class="phone">
					<img width="180" height="180" src="${pageContext.request.contextPath}/images/safetyShield.png">
				</div>
				<span class="clr"></span>
			</form>
		</div>
		<script type="text/javascript">
			var REGISTER = {
				param : {
					//单点登录系统的url
					surl : "${pageContext.request.contextPath}"
				},
				inputcheck : function() {
					//不能为空检查
					if ($("#personId").val() == "") {
						alert("必须从首页登录！");
						return false;
					}
					
					//不能为空检查
					if ($("#regName").val() == "") {
						alert("原密码不能为空");
						$("#regName").focus();
						return false;
					}
					
					//密码不为空检查
					if ($("#pwd").val() == "") {
						alert("新密码不能为空");
						$("#pwd").focus();
						return false;
					}
					
					//密码重复验证
					if ($("#pwd").val() == $("#regName").val()) {
						alert("新密码不能与原密码相同！");
						$("#pwd").focus();
						return false;
					}
					
					//密码检查
					if ($("#pwd").val() != $("#pwdRepeat").val()) {
						alert("确认密码和新密码不一致，请重新输入！");
						$("#pwdRepeat").select();
						$("#pwdRepeat").focus();
						return false;
					}
					return true;
				},
				beforeSubmit : function() {
					//检查密码是否正确
					$.ajax({
						url : REGISTER.param.surl + "/user/check/" + $("#personId").val() + "/" + $("#regName").val() 
								+ "?r=" + Math.random(),
						success : function(data) {
							if (data.data) {
								REGISTER.doSubmit();
							} else {
								alert("密码错误，请重新输入！");
								$("#regName").select();
							}
						}
					});
		
				},
				doSubmit : function() {
					$.post("${pageContext.request.contextPath}/user/modifyPassword", $("#personRegForm").serialize(), function(data) {
						if (data.status == 200) {
							alert('修改密码成功，请登录！');
							REGISTER.login();
						} else {
							alert("注册失败！");
						}
					});
				},
				login : function() {
					location.href = "${pageContext.request.contextPath}/page/login";
					return false;
				},
				reg : function() {
					if (this.inputcheck()) {
						this.beforeSubmit();
					}
				}
			};
		</script>
</body>
</html>
