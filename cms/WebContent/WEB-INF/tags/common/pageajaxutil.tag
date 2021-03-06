<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix="c"%><%@ 
	taglib uri='http://java.sun.com/jsp/jstl/functions' prefix="fn"%><%@ tag pageEncoding="UTF-8"%><%@ 
	attribute name="link" required="true"%><%@ 
	attribute name="refreshZone" required="true"%><%@ 
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
		<a href="javascript:;" onclick="doAR('${link}${symbol}pager.currentPage=0','couponItemList')">&lt;&lt;</a>
	</c:if>
	<c:if test="${pager.currentPage > 0}">
		<a href="javascript:;" onclick="doAR('${link}${symbol}pager.currentPage=${pager.currentPage-1}','couponItemList')">&lt;</a>
	</c:if>
	<c:forEach begin="${pager.startPage}" end="${pager.endPage}" varStatus="row" var="page">
		<a href="javascript:;" onclick="doAR('${link}${symbol}pager.currentPage=${page-1}','couponItemList')"
			${pager.currentPage == row.index-1 ? "class='pg-now'" : "" }>${page}</a>
	</c:forEach>
	<c:if test="${ pager.currentPage < pager.pageSize -1 }">
		<a href="javascript:;" onclick="doAR('${link}${symbol}pager.currentPage=${pager.currentPage + 1}','couponItemList')">&gt;</a>
	</c:if>
	<c:if test="${ pager.currentPage != pager.pageSize -1 }">
		<a href="javascript:;" onclick="doAR('${link}${symbol}pager.currentPage=${pager.pageSize-1}','couponItemList')">&gt;&gt;</a>
	</c:if>
</div>