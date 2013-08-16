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
<%@ page import="com.vignette.ui.vcm.l10n.WorkspaceResource"%>
<%@ page import="java.util.Locale"%>
<%@ page import="com.vignette.ui.util.LocaleUtil"%>
<%@ page import="com.vignette.ui.util.TimeZoneUtil"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="com.vignette.ui.core.request.Context"%>
<%@ page import="com.vignette.ui.core.action.system.AuthenticationAction"%>
<%@ page import="com.vignette.ui.util.StringUtil"%>

<%@ taglib prefix="core" uri="http://ui.vignette.com/core" %>

<%
	Context ctx = (Context)request.getAttribute(Context.CONTEXT_ATTRIBUTE);
	Locale locale = LocaleUtil.getLocale(ctx);
	TimeZone timeZone = TimeZoneUtil.getTimeZone(ctx);

	String message = StringUtil.nonNull(request.getAttribute(AuthenticationAction.MESSAGE_ATTRIBUTE));
	
	String path = StringUtil.nonNull(request.getAttribute(AuthenticationAction.LOADER_PATH_ATTRIBUTE));
	path = StringUtil.jsEscape(path);
%>

<%-- Set locale and timezone --%>
<core:setLocale value="<%= locale %>"/>
<core:setBundle basename="<%= WorkspaceResource.BASE_NAME %>"/>

<div class="vui-login-html">
	<div class="vui-login-body">
		<div class="vui-login-container-grey">
			<div class="vui-login-lines-top">
				<div class="vui-login-lines-bottom">
					<%-- footer begin --%>
					<div class="vui-footer vui-workspace-footer vui-login-footer">
						<p><core:message key="<%= WorkspaceResource.COPYRIGHT %>"/></p>
						<ul>
							<li class="last"><a href="http://www.opentext.com" target="_blank"><core:message key="<%= WorkspaceResource.OPENTEXT_WEB_SITE %>"/></a></li>
						</ul>
					</div>
					<%-- footer end --%>
					<div class="vui-layout-end"></div>
				</div>
				<div class="vui-layout-end"></div>
			</div>
			<div class="vui-layout-end"></div>
		</div>
		<div class="vui-layout-end"></div>
	</div>
</div>

<%-- run ready functions --%>
<script type="text/javascript">
vExt.onReady(function() {
	vExt.getBody().addCls('vui-login');

	var config = config || {};
	config.path = '<%=path%>';
	config.message = '<%=message%>';
	config.showCancel = false;
	vui.vcm.ui.login.showLogin(config);
});
</script>