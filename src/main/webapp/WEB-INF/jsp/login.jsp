<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>登录三作业区安全生产信息平台</title>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/css/login.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.6.4.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.cookie.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/safety.js"></script>
</head>
<body>
	<div class="w">
		<div id="logo">
			<a href="http://www.baidu.com" clstag="passport|keycount|login|01">
				<img src="${pageContext.request.contextPath}/images/safety-logo.gif"
				alt="安全生产信息平台" width="170" height="60" />
			</a><b></b>
		</div>
	</div>
	<form id="formlogin" method="post" onsubmit="return false;">
		<div class=" w1" id="entry">
			<div class="mc " id="bgDiv">
				<div id="entry-bg" clstag="passport|keycount|login|02"
					style="width: 511px; height: 455px; position: absolute; left: -44px; top: -44px; background: url(${pageContext.request.contextPath}/images/safetyShield.png) 0px 0px no-repeat;">
				</div>
				<div class="form ">
					<div class="item fore1">
						<span>用户名</span>
						<div class="item-ifo">
							<input type="text" id="loginname" name="username" class="text"
								tabindex="1" autocomplete="off"/>
							<div class="i-name ico"></div>
							<label id="loginname_succeed" class="blank invisible"></label> <label
								id="loginname_error" class="hide"><b></b></label>
						</div>
					</div>
					<script type="text/javascript">
						setTimeout(function() {
							if (!$("#loginname").val()) {
								$("#loginname").get(0).focus();
							}
						}, 0);
					</script>
					<div id="capslock">
						<i></i><s></s>键盘大写锁定已打开，请注意大小写
					</div>
					<div class="item fore2">
						<span>密码</span>
						<div class="item-ifo">
							<input type="password" id="nloginpwd" name="password"
								class="text" tabindex="2" autocomplete="off" />
							<div class="i-pass ico"></div>
							<label id="loginpwd_succeed" class="blank invisible"></label> <label
								id="loginpwd_error" class="hide"></label>
						</div>
					</div>
					<div class="item login-btn2013">
						<input type="button" class="btn-img btn-entry" id="loginsubmit"
							value="登录" tabindex="8" clstag="passport|keycount|login|06" />
					</div>
				</div>
			</div>
		</div>
	</form>
	<script type="text/javascript">
		var redirectUrl = "${redirect}";
		var flag = "${flag}";
		var LOGIN = {
			checkInput : function() {
				if ($("#loginname").val() == "") {
					alert("用户名不能为空");
					$("#loginname").focus();
					return false;
				}
				
				if(!IdentityCodeValid($("#loginname").val())) {
					$("#loginname").focus();
					return false;
				}
				
				if ($("#nloginpwd").val() == "") {
					alert("密码不能为空");
					$("#nloginpwd").focus();
					return false;
				}
				return true;
			},
			doLogin : function() {
				if (flag) {
					$.post("${pageContext.request.contextPath}/user/loginManager", $("#formlogin").serialize(), function(data) {
						if (data.status == 200) {
							alert("登录成功！");
							if (redirectUrl == "") {
								location.href = "http://localhost:8080/safety-manager-web";
							} else {
								location.href = redirectUrl;
							}
						} else {
							alert("登录失败，原因是：" + data.msg);
							$("#loginname").select();
						}
					});
				} else {
					$.post("${pageContext.request.contextPath}/user/login", $("#formlogin").serialize(), function(data) {
							if (data.status == 200) {
								alert("登录成功！");
								if (redirectUrl == "") {
									location.href = "http://localhost:8082/safety-portal";
								} else {
									location.href = redirectUrl;
								}
							} else {
								alert("登录失败，原因是：" + data.msg);
								$("#loginname").select();
							}
						});
				}
			},
			login : function() {
				if (this.checkInput()) {
					this.doLogin();
				}
			}
		};
		$(function() {
			$("#loginsubmit").click(function() {
				LOGIN.login();
			});
		});
	
		//防止页面后退
		history.pushState(null, null, document.URL);
		window.addEventListener('popstate', function() {
			history.pushState(null, null, document.URL);
		});
	</script>
</body>
</html>