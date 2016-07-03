<%@page import="com.showmoney.core.account.utils.AdminHelper"%><%@ 
	taglib uri='http://java.sun.com/jsp/jstl/core'
	prefix="c"%><%@ 
	taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%><%@ 
	taglib prefix="spring"
	uri="http://www.springframework.org/tags"%><%@ 
	taglib prefix="form" uri="http://www.springframework.org/tags/form"%><%@ 
	taglib prefix="utils"
	uri="/WEB-INF/tld/showmoney.tld"%><%@ 
	page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="contenttable">
	<form:form method="post" commandName="user">
		<table>
			<thead>
				<tr style="height: 1px">
					<td width="20%"></td>
					<td width="80%"></td>
				</tr>
				<tr>
					<td colspan="2"><input type="button" value="<spring:message code="msg.common.save"/>"
						onclick="if(validateUser(this.form)){this.form.action='/cms/account/user/save/'+this.form.loginId.value+'.do';this.form.submit();}"><input
						type="button" value="<spring:message code="msg.common.home" />"
						onclick="document.location='/cms/account/user/list.do'"> <form:errors path="*" cssClass="errorBox" /></td>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="2"><input type="button" value="<spring:message code="msg.common.save"/>"
						onclick="if(validateUser(this.form)){this.form.action='/cms/account/user/save/'+this.form.loginId.value+'.do';this.form.submit();}"><input
						type="button" value="<spring:message code="msg.common.home" />"
						onclick="document.location='/cms/account/user/list.do'"> <form:errors path="*" cssClass="errorBox" /></td>
				</tr>
			</tfoot>
			<tbody>
				<tr>
					<td><spring:message code="msg.account.user.loginId" /></td>
					<td><c:if test="${empty user.loginId}">
							<form:input path="loginId" />
						</c:if> <c:if test="${!empty user.loginId}">${user.loginId}<form:hidden path="loginId" />
						</c:if></td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.nameNative" /></td>
					<td><form:input path="nameNative" /></td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.group" /></td>
					<td><form:select path="groupCode" multiple="false" items="${groups}" itemValue="code" itemLabel="codeDesc" /></td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.email" /></td>
					<td><form:input path="email" /></td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.mobile" /></td>
					<td><form:input path="mobile" /></td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.phone" /></td>
					<td><form:input path="phone" /></td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.lang" /></td>
					<td><form:input path="lang" /></td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.needChangePassword" /></td>
					<td><form:radiobutton path="needChangePassword" value="true" /> <spring:message code="msg.common.yes" /> <form:radiobutton
							path="needChangePassword" value="false" /> <spring:message code="msg.common.no" /></td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.accountNonLocked" /></td>
					<td><form:radiobutton path="accountNonLocked" value="true" /> <spring:message code="msg.common.yes" /> <form:radiobutton
							path="accountNonLocked" value="false" /> <spring:message code="msg.common.no" /></td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.credentialsExpireDate" /></td>
					<td><input type="text" name="credentialsExpireDate" class="jqDateTimePicker"
						value="<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${user.credentialsExpireDate}"/>"></td>
				</tr>
				<tr>
					<td><spring:message code="msg.account.user.accountExpireDate" /></td>
					<td><input type="text" name="accountExpireDate" class="jqDateTimePicker"
						value="<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${user.accountExpireDate}"/>"></td>
				</tr>
			</tbody>
		</table>
	</form:form>
</div>

