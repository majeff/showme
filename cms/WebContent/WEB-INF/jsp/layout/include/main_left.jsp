<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@
	page import="com.showmoney.core.account.utils.AdminHelper"%><%@ 
	taglib uri='http://java.sun.com/jsp/jstl/core'
	prefix="c"%><%@ 
	taglib prefix="utils"
	uri="/WEB-INF/tld/showmoney.tld"%><%@ 
	taglib prefix="spring" uri="http://www.springframework.org/tags"%>

		<div class="left01">
			<div class="left01_right"></div>
			<div class="left01_left"></div>
			<div class="left01_c">
				<spring:message code="msg.account.user.loginId" />：<%=AdminHelper.getUser()!=null?AdminHelper.getUser().getLoginId():""%></div>
		</div><%--
		<div class="left02">
			<div class="left02top">
				<div class="left02top_right"></div>
				<div class="left02top_left"></div>
				<div class="left02top_c">用户信息管理</div>
			</div>
			<div class="left02down">
				<div class="left02down01">
					<a onclick="show_menuB(10)" href="javascript:;"><div id="Bf010" class="left02down01_img"></div>用户信息查询</a>
				</div>
				<div class="left02down01_xia noneBox" id="Bli010">
					<ul>
						<li id="f010"><a href="#">&middot;精确查询</a></li>
						<li id="f011"><a href="#">&middot;组合条件查询</a></li>
					</ul>
				</div>
				<div class="left02down01">
					<a onclick="show_menuB(11)" href="javascript:;">
						<div id="Bf011" class="left02down01_img"></div> 用户密码管理
					</a>
				</div>
				<div class="left02down01_xia noneBox" id="Bli011">
					<ul>
						<li id="f012"><a href="#">&middot;找回密码</a></li>
						<li id="f013"><a href="#">&middot;更改密码</a></li>
					</ul>
				</div>
			</div>
		</div> --%>
<c:forEach items="${sideBars}" var="sidebar" varStatus="index">
		<div class="left02">
			<div class="left02top">
				<div class="left02top_right"></div>
				<div class="left01top_left"></div>
				<div class="left02top_c">${sidebar.name}</div>
			</div>
			<div class="left02down"><c:forEach items="${sidebar.childData}" var="child">
				<div class="left02down01">
					<a href='#' onclick="go_func('${sidebar.name}','${child.name}','<c:url value="${child.url}"/>')"><div class="left0${count+index.index}down01_img"></div>${child.name}</a>
				</div></c:forEach>
			</div>
		</div></c:forEach>
		<div class="left01">
			<div class="left03_right"></div>
			<div class="left01_left"></div>
			<div class="left03_c" onclick="document.location='<spring:url value="/j_spring_security_logout"/>'"><spring:message code="msg.function.system.logout" /></div>
		</div>