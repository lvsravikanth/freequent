<%--
/*######################################################################################
Copyright 2009 Vignette Corporation. All rights reserved. This software
is an unpublished work and trade secret of Vignette, and distributed only
under restriction. This software (or any part of it) may not be used,
modified, reproduced, stored on a retrieval system, distributed, or
transmitted without the express written consent of Vignette. Violation of
the provisions contained herein may result in severe civil and criminal
penalties, and any violators will be prosecuted to the maximum extent
possible under the law. Further, by using this software you acknowledge and
agree that if this software is modified by anyone such as you, a third party
or Vignette, then Vignette will have no obligation to provide support or
maintenance for this software.
#####################################################################################*/
--%>
<%@ page import="com.vignette.as.server.logic.archive.ArchiveUtil" %>
<%@ page import="com.vignette.ui.core.request.Context"%>
<%@ page import="com.vignette.ui.util.ContextUtil" %>
<%@ page import="com.vignette.ui.util.LocaleUtil" %>
<%@ page import="com.vignette.ui.vcm.l10n.WorkspaceResource" %>
<%@ page import="java.util.Locale" %>

<%@ taglib prefix="core" uri="http://ui.vignette.com/core" %>

<%
	Context ctx = (Context) request.getAttribute(Context.CONTEXT_ATTRIBUTE);
	Locale locale = LocaleUtil.getLocale(ctx);
%>

<%-- Set locale and bundle --%>
<core:setLocale value="<%= locale %>"/>
<core:setBundle basename="<%= WorkspaceResource.BASE_NAME %>"/>

<%
	String docType = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
	String context = ContextUtil.getContextPath(request);
	String apiContext = "/contentapi";
	boolean isArchiveConfigured = ArchiveUtil.getArchives().size() > 0;
%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<%-- Base CSS --%>
		<link rel="stylesheet" type="text/css" href="<%= context %>/theme/base.css"/>
		<link rel="stylesheet" type="text/css" href="<%= context %>/script/thirdparty/vext/resources/css/vext-all.css"/>
		<%-- vExt CSS

		<link rel="stylesheet" type="text/css" href="<%= context %>/script/thirdparty/vext/vext-ux/GridFilters/resources/style.css"/>
		<link rel="stylesheet" type="text/css" href="<%= context %>/script/thirdparty/vext/vext-ux/multiselect/multiselect.css"/>
		--%>
		<link rel="stylesheet" type="text/css" href="<%= context %>/script/thirdparty/vext/vext-ux/carousel/carousel.css"/>
		<link rel="stylesheet" type="text/css" href="<%= context %>/script/thirdparty/vext/vext-ux/grid/css/GridFilters.css"/>
		<link rel="stylesheet" type="text/css" href="<%= context %>/script/thirdparty/vext/vext-ux/grid/css/RangeMenu.css"/>

		<%-- Global CSS --%>
		<core:global-css/>

		<%-- Theme CSS --%>
		<core:css/>

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
		<%-- vquery --%>
		<script type="text/javascript" src="<%= apiContext %>/script/thirdparty/vquery/vquery.min.js"></script>
		<script type="text/javascript">
			(function(){ vQuery.noConflict(); })();
		</script>
		<script type="text/javascript" src="<%= apiContext %>/script/thirdparty/vquery/vquery.form.js"></script>

		<%-- vext --%>
		<script type="text/javascript" src="<%= context %>/script/thirdparty/vext/vext-debug.js"></script>
		<script type="text/javascript" src="<%= context %>/script/thirdparty/vext/vext-all-debug.js"></script>
		<script type="text/javascript" src="<%= context %>/script/thirdparty/vext/vext-ux/vext-ux.js"></script>
		
		<%-- rangy --%>
		<script type="text/javascript" src="<%= context %>/script/thirdparty/rangy/rangy-core.js"></script>
		<script type="text/javascript" src="<%= context %>/script/thirdparty/rangy/rangy-cssclassapplier.js"></script>
		<script type="text/javascript" src="<%= context %>/script/thirdparty/rangy/rangy-selectionsaverestore.js"></script>

		<%-- tinyMCE --%>
		<script type="text/javascript" src="<%= context %>/script/thirdparty/tiny_mce/tiny_mce.js"></script>

		<%-- swfObject --%>
		<script type="text/javascript" src="<%= context %>/script/thirdparty/swf/swfobject.js"></script>


		<script type="text/javascript">
			<%-- Configure VUI --%>
			var vui;
			var vuiConfig = {
				<%-- Libraries --%>
				query: vQuery,
				ext: vExt,
				extRootName: 'vExt',

				<%-- Set the api path --%>
				apiPath: "<%= apiContext %>",
		
		 		<%-- Set the request URL --%>
				requestURL: "<%= context %>/request",

				<%-- Set the context path --%>
				appContext: "<%= context %>"
			};
		
			<%-- Configure VCM --%>
			var vcmConfig = {
				archiveConfigured: <%= isArchiveConfigured%>
			};

			<%-- Configure UI --%>
			var uiConfig = {
				<%-- Set the classic console path. --%>
				appConsole: "/AppConsole"
			};

			<%-- Call back handler for classic --%>
			function doPostSubmit(JSName, JSCall) {
				if (eval(JSName)) eval(JSCall);
			}

			<%-- Configure VUI libraries --%>
		</script>

		<%-- Application javascript --%>
		<%-- vui --%>
		<script type="text/javascript" src="<%= apiContext %>/script/impl/vquery/logging.js"></script>
		<script type="text/javascript" src="<%= apiContext %>/script/impl/vquery/vui-impl.js"></script>
		<script type="text/javascript" src="<%= apiContext %>/script/vui/vui.js"></script>

		<%-- vui ui & vext --%>
		<script type="text/javascript" src="<%= context %>/script/vui/ui/vui-ui.js"></script>
		<script type="text/javascript" src="<%= context %>/script/vui/ui/vext.js"></script>

		<%-- vcm --%>
		<script type="text/javascript" src="<%= apiContext %>/script/vcm/vcm.js"></script>

		<%-- vcm vext & vcm ui --%>
		<script type="text/javascript" src="<%= context %>/script/vcm/ui/vcm-vext.js"></script>
		<script type="text/javascript" src="<%= context %>/script/vcm/ui/vcm-ui.js"></script>

		<%-- patch--%>
		<script type="text/javascript">
			(function(){
				try{
					var patchCSSBoxModel = function(){
						//set CSS box model to "content-box"
						vQuery("html").removeClass("x-border-box");
						vExt.isBorderBox = false; //default ExtJS box model calculations to be based on "content-box"
					}

					vExt.onReady(patchCSSBoxModel);
				} catch (ex){
					if (vui.log.isError()){
						vui.log.error(e);
					}
				}
			})();
		</script>

		<%-- messages --%>
		<script type="text/javascript" src="<%= context %>/script/messages.jsp"></script>
		
		<%-- editlive --%>
		<script type="text/javascript" src="/editLiveJava/editlivejava.js"></script>

		<%-- Global javascript --%>
		<core:global-javascript/>

		<%-- Theme javascript --%>
		<core:javascript/>
	
		<%-- 
			vuit : block vuit from clobbering console and this needs to be loaded at the end of all other JS due to the JS threading/loading in FF3.5.x 
		--%>
		<script type="text/javascript">
			if ( console && !console["firebug"] ) {
				console.firebug = "vuit";
			}
		</script>		
		<script type="text/javascript" src="<%= context %>/script/thirdparty/vuit/vuit.js"></script>
		<script type="text/javascript">
			vExt.Loader.setConfig({enabled:true});
		</script>
		
		<title><core:message key="<%= WorkspaceResource.OPENTEXT_TITLE %>"/></title>
	</head>
