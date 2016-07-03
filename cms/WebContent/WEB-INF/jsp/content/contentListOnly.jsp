<%@page import="com.showmoney.core.account.utils.AdminHelper"%><%@ 
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
		<thead>
			<tr>
				<th scope="col" width="10%"><spring:message code="msn.content.publishDate" /></th>
				<th scope="col" width="20%"><spring:message code="msg.content.subject" /></th>
				<th scope="col" width="70%"><spring:message code="msg.content.body" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${contents}" var="content" varStatus="index">
				<tr <c:if test="${index.count%2==0}"> class="odd"</c:if>>
					<td id="r${index.count}"><fmt:formatDate pattern="yyyy-MM-dd" value="${content.startDate}" /></td>
					<td>${content.title}</td>
					<td>${content.elements['body'].clob}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
