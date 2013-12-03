<%@ page import="com.scalar.core.ContextUtil" %>
<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.core.request.Context" %>
<%@ page import="com.scalar.core.util.TimeZoneUtil" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="com.scalar.core.util.LocaleUtil" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.scalar.freequent.util.DateTimeUtil" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	Request fRequest = (Request)request.getAttribute(Request.REQUEST_ATTRIBUTE);
	Context ctx = (Context)request.getAttribute(Context.CONTEXT_ATTRIBUTE);
	String context = ContextUtil.getContextPath(request);
%>
<!DOCTYPE html>
<html class="fui-html">
<c:import url="/common/head.jsp" context="<%=context%>"/>
<body class="fui-body">
<div id="fui-loader" class="fui-loader">
	<script type="text/javascript">
		fui.query(document).ready(function() {
			/* Maintain minimum width and height of the window */
			var MIN_WIDTH = 1024;
			var MIN_HEIGHT = 768;
			var resizeWindow = function() {
				var height = window.innerHeight || document.documentElement.clientHeight,
					width = (fui.query(window).width() > MIN_WIDTH) ? fui.query(window).width() : MIN_WIDTH,
					calculateWidth = width - 280;

				height = MIN_HEIGHT > height ? MIN_HEIGHT : height;
				calculatedHeight = height - (fui.query(".fui-header").height() + fui.query(".fui-footer").height());

				fui.query(".fui-loader").width(width);
				fui.query(".fui-center").width(parseInt(calculateWidth) + 1);
				fui.query(".fui-center").height(parseInt(calculatedHeight) + 1);
			};
			resizeWindow();
			fui.query(window).resize(function(e) {
				resizeWindow();
				fui.publish(fui.loader.event.TOPIC_RESIZE, new fui.loader.Event({type: fui.loader.event.TOPIC_RESIZE}));
			});
		});
	</script>
	<div class="fui-header"><tiles:insertAttribute name="header"/></div>
    <div class="fui-container">
		<div id="fui-left" class="fui-left fui-column">
			<tiles:insertAttribute name="menu"/>
		</div>
        <div id="fui-center" class="fui-center fui-column">
            <tiles:insertAttribute name="body"/>
        </div>
        <div id="fui-right" class="fui-right fui-column"></div>
		<div style="clear:both"></div>
	</div>
    <div class="fui-footer-wrapper">
        <div class="fui-footer"><tiles:insertAttribute name="footer"/></div>
    </div>
</div>
</body>

</html>

