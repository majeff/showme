<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ 
	taglib prefix="spring"
	uri="http://www.springframework.org/tags"%><%@ 
	taglib uri='http://java.sun.com/jsp/jstl/core'
	prefix="c"%><%@ 
	taglib uri="http://tiles.apache.org/tags-tiles"
	prefix="tiles"%><%@ taglib uri="http://www.springmodules.org/tags/commons-validator" prefix="validator" %><%@
	taglib prefix="showmoney" uri="/WEB-INF/tld/showmoney.tld" %>
<title><spring:message code="msg.system.common.title" text="管理介面" /></title>
<link href="/css/style.css" rel="stylesheet" type="text/css" />
<link href="/css/table.css" rel="stylesheet" type="text/css" />
<link href="/css/jquery-ui-1.8.20.custom.css" rel="stylesheet" type="text/css" />
<link href="/css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui-1.8.20.custom.min.js"></script>
<script type="text/javascript" src="/js/jquery.datetimepicker.js"></script>
<script type="text/javascript" src="/js/jquery.cookie.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/tiny_mce/jquery.tinymce.js"></script>
<tiles:useAttribute name="validatorName" ignore="true"/> <c:if test="${!empty validatorName}">
<script type="text/javascript" src="/js/validator.common.js"></script>
<script type="text/javascript" src="/js/validator.function.js"></script>
<validator:javascript formName="${validatorName}" dynamicJavascript="true" staticJavascript="false" xhtml="true" cdata="false" /></c:if>
<tiles:useAttribute name="moduleJS" ignore="true"/> <c:if test="${!empty moduleJS}">
<script type="text/javascript" src="/js/${moduleJS}"></script></c:if>
<script type="text/javascript">
	$(document).ready(function() {
	  if ($.cookie('menu.hidden') == 'true' && $("#LeftBox").css('display') == 'block') {
		  $("#LeftBox").hide();
		  $("#RightBox").css('marginLeft', '0');
		  $("#Mobile").css('background', 'url(/images/center.gif)');
	  }
	  if ($("#mainFunc").html() == '') {
		  $("#mainFunc").html($.cookie('menu.mainFunc'));
	  }
	  if ($("#secFunc").html() == '') {
		  $("#secFunc").html($.cookie('menu.secFunc'));
	  }
	  $('.jqDatePicker').datepicker({
	    showOn : "button",
	    buttonImage : "/images/smoothness/calendar.gif",
	    numberOfMonths : 2,
	    dateFormat : 'yy-mm-dd'
	  });
	  $('.jqTimePicker').timepicker({
	    showOn : "button",
	    buttonImage : "/images/smoothness/calendar.gif",
	    timeFormat : 'hh:mm',
	    stepMinute : 10
	  });
	  $('.jqDateTimePicker').datetimepicker({
	    showOn : "button",
	    buttonImage : "/images/smoothness/calendar.gif",
	    numberOfMonths : 2,
	    dateFormat : 'yy-mm-dd',
	    timeFormat : 'hh:mm',
	    stepMinute : 10
	  });
	  $(".jqRichEditor").tinymce({
	  	script_url : '/js/tiny_mce/tiny_mce.js',
			content_css : "/css/tiny_mce/content.css"
	  });
  });
</script>