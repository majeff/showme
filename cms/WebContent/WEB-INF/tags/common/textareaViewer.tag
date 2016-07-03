<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ 
	tag pageEncoding="UTF-8"%><%@ 
	attribute name="value"%>
<c:if test="${not empty value}">
	<c:set var="textareaValue">
		<c:out value="${value}" />
	</c:set>
	<%=((String) jspContext.getAttribute("textareaValue")).replaceAll("\n", "<br />")%>
</c:if>