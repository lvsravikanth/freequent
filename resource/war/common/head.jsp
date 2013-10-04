<%@ taglib prefix="core" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.core.ContextUtil" %>
<%@ page import="com.scalar.freequent.l10n.MessageResource" %>

<%--<%@ taglib prefix="core" uri="http://ui.vignette.com/core" %>--%>

<%
	//Context ctx = (Context) request.getAttribute(Context.CONTEXT_ATTRIBUTE);
	//Locale locale = LocaleUtil.getLocale(ctx);
    Request fRequest = (Request)request.getAttribute(Request.REQUEST_ATTRIBUTE);
%>

<%-- Set locale and bundle --%>
<fmt:setLocale value="<%= fRequest.getLocale() %>"/>
<fmt:setBundle basename="<%= MessageResource.BASE_NAME %>"/>


<%
	String docType = "<!doctype html>";
	String context = ContextUtil.getContextPath(request);
	String apiContext = context;
%>
	<head class="fui-head">
		<meta http-equiv="Content-Type" content="text/html; utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<%-- Base CSS --%>
        <link rel="stylesheet" type="text/css" href="<%=context%>/theme/base.css"/>
        <link rel="stylesheet" type="text/css" href="<%=context%>/theme/corporate/style/content.css"/>
        <link rel="stylesheet" type="text/css" href="<%=context%>/script/thirdparty/jquery/css/start/jquery-ui-1.10.3.custom.css"/>

		<%-- vExt CSS

		<link rel="stylesheet" type="text/css" href="<%= context %>/script/thirdparty/vext/vext-ux/GridFilters/resources/style.css"/>
		<link rel="stylesheet" type="text/css" href="<%= context %>/script/thirdparty/vext/vext-ux/multiselect/multiselect.css"/>
		--%>

		<%-- Global CSS --%>
		<%--<core:global-css/>--%>

		<%-- Theme CSS --%>
		<%--<core:css/>--%>

		<!--[if lte IE 7]>
		  <script type="text/javascript">
		  (function() {
			var html = document.documentElement;
			var vstr = navigator.appVersion;
			var match = vstr.match(/MSIE\s+(\d)/);
			if(match) {
				var majorVersion = match[1];
				var browserId = "vui-ie vui-ie" + majorVersion;
				if(html.className) {
					if(html.className.indexOf(browserId) < 0) {
						html.className += " " + browserId;
					}
				} else {
					html.className = browserId;
				}
			}
		  })();
		  </script>
		<![endif]-->

		<%-- Thirdparty javascript --%>
		<%-- jquery --%>
        <script type="text/javascript" src="<%=context%>/script/thirdparty/jquery/js/jquery-1.9.1.min.js" ></script>
		<script type="text/javascript" src="<%=context%>/script/thirdparty/jquery/js/jquery-ui-1.10.3.custom.min.js" ></script>


		<script type="text/javascript">
			(function(){ jQuery.noConflict(); })();
		</script>
		<script type="text/javascript" src="<%= context %>/script/thirdparty/jquery/plugins/form/jquery.form.min.js"></script>

		<script type="text/javascript">
			<%-- Configure VUI --%>
			var fui;
			var fuiConfig = {
				<%-- Libraries --%>
				query: jQuery,

				<%-- Set the api path --%>
				apiPath: "<%= apiContext %>",
		
		 		<%-- Set the request URL --%>
				requestURL: "<%= context %>/request",

				<%-- Set the context path --%>
				appContext: "<%= context %>"
			};
		
			<%-- Configure VUI libraries --%>
		</script>

		<%-- Application javascript --%>
		<%-- fui --%>
		<%--<script type="text/javascript" src="<%= apiContext %>/script/impl/vquery/logging.js"></script>--%>
		<script type="text/javascript" src="<%= apiContext %>/script/fui/fui.js"></script>

		<%-- fui ui --%>
		<script type="text/javascript" src="<%= context %>/script/fui/ui/fui-ui.js"></script>



		<%-- patch--%>
		<script type="text/javascript">
			(function(){
				try{
					/*var patchCSSBoxModel = function(){
						//set CSS box model to "content-box"
						jQuery("html").removeClass("x-border-box");
						vExt.isBorderBox = false; //default ExtJS box model calculations to be based on "content-box"
					}

					vExt.onReady(patchCSSBoxModel);*/
				} catch (ex){
					if (fui.log.isError()){
						fui.log.error(e);
					}
				}
			})();
		</script>

		<%-- messages --%>
		<%--<script type="text/javascript" src="<%= context %>/script/messages.jsp"></script>--%>
		
		<title><fmt:message key="<%= MessageResource.SAMSKRITI_TITLE %>"/></title>
	</head>
