<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix="c"%><%@ 
	page language="java"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ 
	taglib prefix="spring"
	uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><spring:message code="msg.system.common.title" text="管理介面" /></title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	overflow: hidden;
}

.STYLE3 {
	color: #528311;
	font-size: 12px;
}

.STYLE4 {
	color: #42870a;
	font-size: 12px;
}
-->
</style>
<script type="text/javascript" src="/js/common.js" ></script>
</head>
<body>
	<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td bgcolor="#e5f6cf">&nbsp;</td>
		</tr>
		<tr>
			<td height="608" background="/images/login_03.gif"><table width="862" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td height="266" background="/images/login_04.gif">&nbsp;</td>
					</tr>
					<tr>
						<td height="95">
							<form action="/cms/j_spring_security_check" name="login" method="post">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="424" height="95" background="/images/login_06.gif">&nbsp;</td>
										<td width="183" background="/images/login_07.gif"><table width="100%" border="0" cellspacing="0"
												cellpadding="0">
												<tr>
													<td width="21%" height="30"><div align="center">
															<span class="STYLE3"><spring:message code="msg.account.user.loginId" /></span>
														</div></td>
													<td width="79%" height="30"><input type="text" name="j_username"
														style="height: 18px; width: 130px; border: solid 1px #cadcb2; font-size: 12px; color: #81b432;"></td>
												</tr>
												<tr>
													<td height="30"><div align="center">
															<span class="STYLE3"><spring:message code="msg.account.user.password" /></span>
														</div></td>
													<td height="30"><input type="password" name="j_password"
														style="height: 18px; width: 130px; border: solid 1px #cadcb2; font-size: 12px; color: #81b432;"
														onKeyPress="submitEnter(this.form,event);"></td>
												</tr>
												<tr>
													<td height="30">&nbsp;</td>
													<td height="30"><img src="/images/dl.gif" width="81" height="22" border="0" usemap="#Map"></td>
												</tr>
											</table>
											<form:errors path="*" cssClass="errorBox" />
											</td>
										<td width="255" background="/images/login_08.gif">&nbsp;</td>
									</tr>
								</table>
							</form>
						</td>
					</tr>
					<tr>
						<td height="247" valign="top" background="/images/login_09.gif"><table width="100%" border="0"
								cellspacing="0" cellpadding="0">
								<tr>
									<td width="22%" height="30">&nbsp;</td>
									<td width="56%">&nbsp;</td>
									<td width="22%">&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td height="30"><table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="44%" height="20">&nbsp;</td>
												<td width="56%" class="STYLE4"><spring:message code="msg.common.version" text="版本 2012 v0.1" /></td>
											</tr>
										</table></td>
									<td>&nbsp;</td>
								</tr>
							</table></td>
					</tr>
				</table></td>
		</tr>
		<tr>
			<td bgcolor="#a2d962">&nbsp;</td>
		</tr>
	</table>
	<map name="Map">
		<area shape="rect" coords="3,3,36,19" href="#" onclick="document.login.submit();">
		<area shape="rect" coords="40,3,78,18" href="#" onclick="document.login.reset();">
	</map>
</body>
</html>
