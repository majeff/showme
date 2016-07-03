<%@ page import="com.showmoney.core.account.utils.AdminHelper"%><%@ 
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
	<form:form method="post" commandName="content">
		<form:hidden path="uuid"/>
		<table>
			<thead>
				<tr>
					<th width="20%"><spring:message code="msg.contenttemplate.description" /></th>
					<td width="80%">${template.description}</td>
				</tr>
				<tr>
					<td colspan="2"><input type="button" value="<spring:message code="msg.common.save"/>"
						onclick="this.form.action='/cms/content/content/save/${templateCode}/${content.uuid}.do';this.form.submit();"><input
						type="button" value="<spring:message code="msg.common.delete"/>"
						onclick="this.form.action='/cms/content/content/delete/${templateCode}/${content.uuid}.do';if(confirm('<spring:message code="msg.common.confirmDelete"/>')) {this.form.submit();}">
						<input type="button" value="<spring:message code="msg.common.cancel" />"
						onclick="document.location='/cms/content/content/list/${templateCode}.do'"> <form:errors path="*"
							cssClass="errorBox" /></td>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="2"><input type="button" value="<spring:message code="msg.common.save"/>"
						onclick="this.form.action='/cms/content/content/save/${templateCode}/${content.uuid}.do';this.form.submit();"><input
						type="button" value="<spring:message code="msg.common.delete"/>"
						onclick="this.form.action='/cms/content/content/delete/${templateCode}/${content.uuid}.do';if(confirm('<spring:message code="msg.common.confirmDelete"/>')) {this.form.submit();}">
						<input type="button" value="<spring:message code="msg.common.cancel" />"
						onclick="document.location='/cms/content/content/list/${templateCode}.do'"> <form:errors path="*"
							cssClass="errorBox" /></td>
				</tr>
			</tfoot>
			<tbody>
				<tr>
					<th><spring:message code="msg.content.title" /></th>
					<td><input type="text" name="title" size="30" value="${content.title}" /></td>
				</tr>
				<tr>
					<th><spring:message code="msg.content.status" /></th>
					<td>${nodes[content.status].description}</td>
				</tr>
				<tr>
					<th><spring:message code="msg.common.sortOrder" /></th>
					<td><form:radiobutton path="sortOrder" value="500" />一般 <form:radiobutton path="sortOrder" value="300" />排後
						<form:radiobutton path="sortOrder" value="700" />排前</td>
				</tr>
				<tr>
					<th><spring:message code="msn.content.publishDate" /></th>
					<td><input type="text" name="startDate"
						value="<fmt:formatDate pattern="yyyy-MM-dd" value="${content.startDate}"/>" class="jqDatePicker"> ~ <input
						type="text" name="endDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${content.endDate}"/>"
						class="jqDatePicker"></td>
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
						<td><form:hidden path="elements[${element.code}].type"/><c:choose>
								<c:when test="${'TEXTAREA' eq element.type}">
									<form:textarea path="elements[${element.code}].clob" cols="80" rows="5"/>
								</c:when>
								<c:when test="${'RICH' eq element.type}">
									<form:textarea path="elements[${element.code}].clob" cols="80" rows="5" cssClass="jqRichEditor"/>
								</c:when>
								<c:when test="${'DATE' eq element.type}">
									<input type="text" name="elements[${element.code}].text" value="${content.elements[element.code].text}" class="jqDatePicker">
								</c:when>
								<c:when test="${'TIME' eq element.type}">
									<input type="text" name="elements[${element.code}].text" value="${content.elements[element.code].text}" class="jqTimePicker">
								</c:when>
								<c:when test="${'TIMESTAMP' eq element.type}">
									<input type="text" name="elements[${element.code}].text" value="${content.elements[element.code].text}" class="jqDateTimePicker">
								</c:when>
								<c:when test="${'CHECKBOX' eq element.type}">
									<form:checkboxes path="elements[${element.code}].values" items="${element.labelValueMap}"/>
								</c:when>
								<c:when test="${'RADIO' eq element.type}">
									<form:radiobuttons path="elements[${element.code}].text" items="${element.labelValueMap}"/>
								</c:when>
								<c:when test="${'MENU' eq element.type}">
									<form:select path="elements[${element.code}].text" items="${element.labelValueMap}" multiple="false"></form:select>
								</c:when>
								<c:when test="${'MULTIMENU' eq element.type}">
									<form:select path="elements[${element.code}].values" items="${element.labelValueMap}" multiple="true"></form:select>
								</c:when>
								<c:when test="${('FILE' eq element.type) or ('IMAGE' eq element.type)}">
									<input type="file" name="elements[${element.code}].text">
								</c:when>
								<c:when test="${'NUMBER' eq element.type}">
									<input type="text" name="elements[${element.code}].number" value="${content.elements[element.code].text}">
								</c:when>
								<c:otherwise>
									<input type="text" name="elements[${element.code}].text" value="${content.elements[element.code].value}">
								</c:otherwise>
							</c:choose></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form:form>
</div>
