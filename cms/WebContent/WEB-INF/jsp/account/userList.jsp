<%@page import="cc.macloud.core.account.utils.AdminHelper"%><%@ 
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
	<table>
		<caption class="querytable">
			<form:form action="/cms/account/user/search.do" method="post" modelAttribute="qryForm">
				<table>
					<tr>
						<th><spring:message code="msg.account.user.loginId" />
						</th>
						<td><input type="text" name="loginId" value="${loginId}"></td>
						<th><spring:message code="msg.account.user.group" />
						</th>
						<td><input type="text" name="groupCode" value="${groupCode}"></td>
					</tr>
					<tr>
						<th><spring:message code="msg.account.user.nameNative" />
						</th>
						<td><input type="text" name="nameNative" value="${nameNative}"></td>
						<th><spring:message code="msg.account.user.isActive" />
						</th>
						<td><input type="radio" name="isActive" value="" checked="checked">不限<input type="radio" name="isActive" value="true">啟用
							<input type="radio" name="isActive" value="false">停用</td>
					</tr>
					<tfoot>
						<tr>
							<td colspan="5"><input type="submit" value="<spring:message code="msg.common.search" />"> <input
								type="button" value="<spring:message code="msg.common.create" />"
								onclick="document.location='/cms/account/user/create.do'"> <spring:hasBindErrors name="" /> <form:errors
									path="*" cssClass="errorBox" />
						</tr>
						</td>
					</tfoot>
				</table>
			</form:form>
		</caption>
		<thead>
			<tr>
				<th scope="col"><spring:message code="msg.account.user.loginId" /></th>
				<th scope="col"><spring:message code="msg.account.user.nameNative" /></th>
				<th scope="col"><spring:message code="msg.account.user.group" /></th>
				<th scope="col"><spring:message code="msg.account.user.accountNonLocked" /></th>
				<th scope="col"><spring:message code="msg.account.user.credentialsExpireDate" /></th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="5"><utils:pageutil pagerObj="${pager}" link="/cms/account/user/search.do" /></td>
			</tr>
		</tfoot>
		<tbody>
			<c:forEach items="${objs}" var="user" varStatus="index">
				<tr <c:if test="${index.count%2==0}"> class="odd"</c:if>>
					<th scope="row" id="r${index.count}"><a href="/cms/account/user/view/${user.loginId}.do">${user.loginId}</a></th>
					<td>${user.nameNative}</td>
					<td>${user.group.description} / ${user.groupCode}</td>
					<td>${user.accountNonLocked}</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${user.credentialsExpireDate}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
