<%@page import="com.showmoney.core.account.utils.AdminHelper"%><%@ 
	taglib uri='http://java.sun.com/jsp/jstl/core'
	prefix="c"%><%@ 
	taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%><%@ 
	taglib prefix="spring" uri="http://www.springframework.org/tags"%><%@ 
	taglib prefix="form"
	uri="http://www.springframework.org/tags/form"%><%@ 
	taglib prefix="utils" uri="/WEB-INF/tld/showmoney.tld"%><%@ 
	page language="java"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="contenttable">
	<form:form method="post" commandName="user">
		<table>
			<thead>
				<tr style="height: 1px">
					<td width="20%"></td>
					<td width="80%"></td>
				</tr>
				<tr>
					<td colspan="2"><input type="button" value="<spring:message code="msg.common.edit"/>"
						onclick="document.location='/cms/account/user/edit/${user.loginId}.do';"><input
						type="button" value="<spring:message code="msg.common.resetPwd"/>"
						onclick="document.location='/cms/account/user/resetPwd/${user.loginId}.do';"> <input
						type="button" value="<spring:message code="msg.common.home" />"
						onclick="document.location='/cms/account/user/list.do'"> <form:errors path="*" cssClass="errorBox" /></td>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="2"><input type="button" value="<spring:message code="msg.common.edit"/>"
						onclick="document.location='/cms/account/user/edit/${user.loginId}.do';"><input
						type="button" value="<spring:message code="msg.common.resetPwd"/>"
						onclick="document.location='/cms/account/user/resetPwd/${user.loginId}.do';"> <input
						type="button" value="<spring:message code="msg.common.home" />"
						onclick="document.location='/cms/account/user/list.do'"></td>
				</tr>
			</tfoot>
			<tbody>
				<tr>
					<td><spring:message code="msg.account.user.loginId" /></td>
					<td>${user.loginId}</td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.nameNative" /></td>
					<td>${user.nameNative}</td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.group" /></td>
					<td>${user.group.codeDesc}</td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.email" /></td>
					<td>${user.email}</td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.mobile" /></td>
					<td>${user.mobile}</td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.phone" /></td>
					<td>${user.phone}</td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.lang" /></td>
					<td>${user.lang}</td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.accountExpireDate" /> / <spring:message code="msg.account.user.accountNonLocked" /></td>
					<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${user.accountExpireDate}" /> / ${user.accountNonLocked}</td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.credentialsExpireDate" /> / <spring:message code="msg.account.user.needChangePassword" /></td>
					<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${user.credentialsExpireDate}" /> / ${user.needChangePassword}</td>
				</tr>
			</tbody>
		</table>
	</form:form>
</div>
