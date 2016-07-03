<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@
	page
	import="com.showmoney.core.account.utils.AdminHelper"%><%@ 
	taglib uri="http://tiles.apache.org/tags-tiles"
	prefix="tiles"%><%@ 
	taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ 
	taglib prefix="spring"
	uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<tiles:useAttribute name="validatorName" ignore="true"/><tiles:useAttribute name="moduleJS" ignore="true"/>
<tiles:insertAttribute name="head">
	<tiles:putAttribute name="validatorName" value="${validatorName}"/>
	<tiles:putAttribute name="moduleJS" value="${moduleJS}"/>
</tiles:insertAttribute>
</head>
<body>
	<div class="header">
		<tiles:insertAttribute name="header"/>
	</div>
	<div class="left" id="LeftBox">
		<tiles:insertAttribute name="left"/>
	</div>
	<div class="rrcc" id="RightBox">
		<div class="center" id="Mobile" onclick="show_menuC()"></div>
		<div class="right">
			<div class="right01">
				<img src="/images/04.gif" /> <span id="mainFunc"><c:out value="${mainFunc}"/></span> &gt; <span id="secFunc"><c:out value="${secFunc}"/></span> &gt;
			</div>
			<div class="right02">
				<tiles:insertAttribute name="right"/>
			</div>
		</div>
	</div>
</body>
</html>
