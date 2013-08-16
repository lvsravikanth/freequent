<%--
/*######################################################################################
Copyright 2008 Vignette Corporation. All rights reserved. This software
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
<%@ page import="com.vignette.ui.util.ContextUtil"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="com.vignette.ui.core.request.Context"%>
<%@ page import="com.vignette.ui.util.LocaleUtil" %>
<%@ page import="com.vignette.ui.util.TimeZoneUtil" %>
<%@ page import="com.vignette.ui.service.ServiceFactory"%>
<%@ page import="com.vignette.ui.service.request.RequestService"%>
<%@ page import="com.vignette.ui.core.workspace.WorkspaceFactory"%>
<%@ page import="com.vignette.ui.util.StringUtil" %>

<%
	String docType = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";

	Context ctx = (Context)request.getAttribute(Context.CONTEXT_ATTRIBUTE);
	Locale locale = LocaleUtil.getLocale(ctx);
	TimeZone timeZone = TimeZoneUtil.getTimeZone(ctx);
	
	// TODO remove this once REST authentication is in
	String defaultPath = "";
	boolean loggedIn = (null != request.getUserPrincipal());
	if ( loggedIn ) {
		RequestService requestService = ServiceFactory.getRequest(request);
		defaultPath = WorkspaceFactory.getDefaultPath(requestService);
		defaultPath = StringUtil.jsEscape(defaultPath);
	}
%>
<%= docType %>
<html xmlns="http://www.w3.org/1999/xhtml" class="vui-html">
	<jsp:include page="/common/head.jsp"/>
	<body class="vui-body">
		<%-- loader --%>
		<div id="vui-loader"></div>
		<%-- on ready --%>
		<script type="text/javascript">
			vui.ready(vui.scope(this, function() {
				// timezone should be of the form {id, offset, usesDaylight}
				var tz = {id: "<%=timeZone.getID()%>", offset: "<%=timeZone.getRawOffset()%>", usesDaylight: "<%=timeZone.useDaylightTime()%>"};
				vui.context.setLocalization("<%=locale%>", tz);
				vExt.state.Manager.setProvider(new vExt.state.CookieProvider());
						
				<%-- Clean up --%>
				this.vuiConfig=null;
				this.vcmConfig=null; 
				this.uiConfig=null; 
				
				<%-- Do load --%>
				var path = vui.loader.getHash();
				if ( !path || (path.length === 0) ) {
					path = "<%= defaultPath %>";
				}

<%
	// TODO remove this once REST authentication is in
	if ( loggedIn ) {
%>
				vui.auth.internal.TOKEN = "loggedIn";
<%
	}
%>
				vui.loader.loadBody(path);
	        }));
		</script>
	</body>
</html>	
