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
	<form:form>
		<input type="hidden" name="publishCommand">
		<input type="hidden" name="actionCode">
		<table>
			<thead>
				<tr>
					<th width="20%"><spring:message code="msg.contenttemplate.description" /></th>
					<td width="80%">${template.description}</td>
				</tr>
				<tr>
					<td colspan="2"><input type="button" value="<spring:message code="msg.common.edit"/>"
						onclick="document.location='/cms/content/content/edit/${templateCode}/${content.uuid}.do'"> <c:forEach
							items="${nodes}" var="node">
							<c:if test="${node.value.status eq content.status}">
								<c:forEach items="${node.value.actions}" var="action">
									<input type="button" name="doFlow" value="${action.description}"
										onclick="this.form.actionCode.value='${action.code}';this.form.action='/cms/content/content/publish/${templateCode}/${content.uuid}.do';this.form.submit();">
								</c:forEach>
							</c:if>
						</c:forEach> <input type="button" value="<spring:message code="msg.common.delete"/>"
						onclick="this.form.action='/cms/content/content/delete/${templateCode}/${content.uuid}.do';if(confirm('<spring:message code="msg.common.confirmDelete"/>')) {this.form.submit();}"><input
						type="button" value="<spring:message code="msg.common.home" />"
						onclick="document.location='/cms/content/content/list/${templateCode}.do'"> <form:errors path="*"
							cssClass="errorBox" /></td>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="2"><input type="button" value="<spring:message code="msg.common.edit"/>"
						onclick="document.location='/cms/content/content/edit/${templateCode}/${content.uuid}.do'"> <c:forEach
							items="${nodes}" var="node">
							<c:if test="${node.value.status eq content.status}">
								<c:forEach items="${node.value.actions}" var="action">
									<input type="button" name="doFlow" value="${action.description}"
										onclick="this.form.actionCode.value='${action.code}';this.form.action='/cms/content/content/publish/${templateCode}/${content.uuid}.do';this.form.submit();">
								</c:forEach>
							</c:if>
						</c:forEach> <input type="button" value="<spring:message code="msg.common.delete"/>"
						onclick="this.form.action='/cms/content/content/delete/${templateCode}/${content.uuid}.do';if(confirm('<spring:message code="msg.common.confirmDelete"/>')) {this.form.submit();}"><input
						type="button" value="<spring:message code="msg.common.home" />"
						onclick="document.location='/cms/content/content/list/${templateCode}.do'"> <form:errors path="*"
							cssClass="errorBox" /></td>
				</tr>
			</tfoot>
			<tbody>
				<tr>
					<th><spring:message code="msg.content.title" /></th>
					<td>${content.title}</td>
				</tr>
				<tr>
					<th><spring:message code="msg.content.status" /></th>
					<td><c:if test="${!empty content.parentUuid}">
							<spring:message code="msg.content.status.publish" />/</c:if> <c:choose>
							<c:when test="${content.status eq '0'}">
								<spring:message code="msg.content.status.edit" />
							</c:when>
							<c:when test="${content.status eq '9'}">
								<spring:message code="msg.content.status.publish" />
							</c:when>
							<c:otherwise>${nodes[content.status].description}</c:otherwise>
						</c:choose></td>
				</tr>
				<tr>
					<th><spring:message code="msn.content.publishDate" /></th>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${content.startDate}" /> ~ <fmt:formatDate
							pattern="yyyy-MM-dd" value="${content.endDate}" /></td>
				</tr>
				<tr>
					<th><spring:message code="msg.common.createDate" /> / <spring:message code="msg.common.createUser" /></th>
					<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${content.createDate}" /> / ${content.createUser}</td>
				</tr>
				<tr>
					<th><spring:message code="msg.common.modifyDate" /> / <spring:message code="msg.common.modifyUser" /></th>
					<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${content.modifyDate}" /> / ${content.modifyUser}</td>
				</tr>
				<c:forEach items="${template.elements}" var="element">
					<tr>
						<th>${element.name}</th>
						<td><c:out value="${content.elements[element.code].value}" escapeXml="true" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form:form>
</div>
