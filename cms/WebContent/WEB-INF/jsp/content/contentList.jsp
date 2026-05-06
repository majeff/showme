<%@page import="cc.macloud.core.account.utils.AdminHelper"%><%@ 
	taglib uri='http://java.sun.com/jsp/jstl/core'
	prefix="c"%><%@ 
	taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ 
	taglib prefix="spring" uri="http://www.springframework.org/tags"%><%@ 
	taglib prefix="form"
	uri="http://www.springframework.org/tags/form"%><%@ 
	taglib prefix="utils" uri="/WEB-INF/tld/showmoney.tld"%><%@ 
	page language="java"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="contenttable">
	<table>
		<caption class="querytable">
			<form:form method="post" modelAttribute="qryForm">
				<table>
					<tr>
						<th><spring:message code="msg.content.startDate" />
						</td>
						<td>
						<input type="text" name="startDate" class="jqDatePicker" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${qryForm.startDate}" />">
						</td>
						<th><spring:message code="msg.content.endDate" />
						</td>
						<td><input type="text" name="endDate" class="jqDatePicker" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${qryForm.endDate}" />"></td>
					</tr>
					<tr>
						<th><spring:message code="msg.content.status" />
						</td>
						<td><input type="radio" name="isActive" value=""><spring:message code="msg.common.all" /><input type="radio" name="isActive" value="true"><spring:message code="msg.content.status.publish" /><input type="radio" name="isActive" value="false"><spring:message code="msg.content.status.edit" /></td>
						<th><spring:message code="msg.content.title" />
						</td>
						<td><input type="text" name="title" value="${qryForm.title}"></td>
					</tr>
					<tfoot>
						<tr>
							<td colspan="5"><input type="button" value="<spring:message code="msg.common.search"/>" onclick="this.form.action='/cms/content/content/search/${templateCode}.do';this.form.submit();"> <input
								type="button" value="<spring:message code="msg.common.create" />"
								onclick="document.location='/cms/content/content/create/${templateCode}.do'"> 
								<form:errors path="*" cssClass="errorBox" />
						</tr>
						</td>
					</tfoot>
				</table>
			</form:form>
		</caption>
		<thead>
			<tr>
				<th scope="col" width="10%"><spring:message code="msn.content.publishDate" /></th>
				<th scope="col" width="20%"><spring:message code="msg.content.subject" /></th>
				<th scope="col" width="10%"><spring:message code="msg.content.status" /></th>
				<th scope="col" width="10%"><spring:message code="msg.common.modifyUser" /></th>
				<th scope="col" width="10%"><spring:message code="msg.common.modifyDate" /></th>
				<th scope="col" width="10%"><spring:message code="msg.common.createUser" /></th>
				<th scope="col" width="10%"><spring:message code="msg.common.createDate" /></th>
				<th scope="col" width="20%"> </th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="5"><utils:pageutil pagerObj="${pager}" link="/cms/content/content/search/${templateCode}.do" /></td>
			</tr>
		</tfoot>
		<tbody>
			<c:forEach items="${objs}" var="content" varStatus="index">
				<tr <c:if test="${index.count%2==0}"> class="odd"</c:if>>
					<td id="r${index.count}"><fmt:formatDate pattern="yyyy-MM-dd" value="${content.startDate}" /></td>
					<td><a href="/cms/content/content/view/${templateCode}/${content.uuid}.do">${content.title}</a></td>
					<td><c:if test="${!empty content.parentUuid}"><spring:message code="msg.content.status.publish" />/</c:if><c:choose><c:when test="${content.status eq '0'}"><spring:message code="msg.content.status.edit" /></c:when><c:when test="${content.status eq '9'}"><spring:message code="msg.content.status.publish" /></c:when>
					<c:otherwise>${nodes[content.status].description}</c:otherwise> </c:choose></td>
					<td>${content.modifyUser}</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${content.modifyDate}" /></td>
					<td>${content.createUser}</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${content.createDate}" /></td>
					<td> </td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
