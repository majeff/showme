<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix="c"%><%@ 
	taglib uri='http://java.sun.com/jsp/jstl/functions' prefix="fn"%><%@ 
	tag pageEncoding="UTF-8"%><%@ 
	attribute name="link" required="true"%><%@ 
	attribute name="pagerObj" required="true"
	type="com.showmoney.core.common.entity.SimplePager"%>
<c:choose>
	<c:when test="${fn:indexOf(link,'?') == -1}">
		<c:set var="symbol" value="?" />
	</c:when>
	<c:otherwise>
		<c:set var="symbol" value="&" />
	</c:otherwise>
</c:choose>
<div class="page-change pft">
	<span>目前在第${pager.currentPage + 1 }页,资料共 ${pager.totalSize} 笔</span>
	<c:if test="${pager.currentPage > 0}">
		<a href="${link}${symbol}currentPage=0">&lt;&lt;</a>
	</c:if>
	<c:if test="${pager.currentPage > 0}">
		<a href="${link}${symbol}currentPage=${pager.currentPage-1}">&lt;</a>
	</c:if>
	<c:forEach begin="${pager.startPage}" end="${pager.endPage}" varStatus="row" var="page">
		<a href="${link}${symbol}currentPage=${page-1}" ${pager.currentPage == row.index-1 ? "class='pg-now'" : "" }>${page}</a>
	</c:forEach>
	<c:if test="${ pager.currentPage < pager.pageSize -1 }">
		<a href="${link}${symbol}currentPage=${pager.currentPage + 1}">&gt;</a>
	</c:if>
	<c:if test="${ pager.currentPage != pager.pageSize -1 }">
		<a href="${link}${symbol}currentPage=${pager.pageSize-1}">&gt;&gt;</a>
	</c:if>
</div>