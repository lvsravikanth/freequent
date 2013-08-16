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
<%@ page import="com.vignette.ui.core.editor.Editor"%>
<%@ page import="com.vignette.ui.core.request.Context"%>
<%@ page import="com.vignette.ui.core.request.Request"%>
<%@ page import="com.vignette.ui.util.LocaleUtil"%>
<%@ page import="com.vignette.ui.util.StringUtil" %>
<%@ page import="com.vignette.ui.util.TimeZoneUtil" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.TimeZone" %>

<%
	String docType = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";

	Context ctx = (Context)request.getAttribute(Context.CONTEXT_ATTRIBUTE);
	Locale locale = LocaleUtil.getLocale(ctx);
	TimeZone timeZone = TimeZoneUtil.getTimeZone(ctx);
	
	String type = StringUtil.htmlUnescape((String)request.getAttribute(Editor.TYPE_XML_NAME_ATTRIBUTE));
	String id = (String)request.getAttribute(Editor.ID_ATTRIBUTE);

	// fetch the other request content if any
	String otherRequestContent = (String)request.getAttribute(Request.REQUEST_CONTENT);
	otherRequestContent = StringUtil.isEmpty(otherRequestContent) ? "{}" : otherRequestContent;
	
	// TODO remove this once REST authentication is in
	boolean loggedIn = (null != request.getUserPrincipal());
%>
<%= docType %>

<html xmlns="http://www.w3.org/1999/xhtml" class="vui-html">
	<jsp:include page="/common/head.jsp"/>
	<body class="vui-body">
		<%-- on ready --%>
		<script type="text/javascript">
			vui.ready(vui.scope(this, function() {
				<%-- set the pop out's context to match the opener --%>
				var ctx = null;
				if(opener){
					ctx = vui.exists("vui.context", opener) && !opener.closed ? opener.vui.context.getContext() : null;
					if (ctx) {
						vui.context.set(ctx);
					}
				}

				<%-- timezone should be of the form {id, offset, usesDaylight} --%>
				var tz = {id: "<%=timeZone.getID()%>", offset: "<%=timeZone.getRawOffset()%>", usesDaylight: "<%=timeZone.useDaylightTime()%>"};
				vui.context.setLocalization("<%=locale%>", tz);
				vExt.state.Manager.setProvider(new vExt.state.CookieProvider());
						
				<%-- Clean up --%>
				this.vuiConfig=null;
				this.vcmConfig=null; 
				this.uiConfig=null; 

				<%-- Get the request data with which the editor was first loaded and use the same for the pop out mode --%>
				var requestData = {
					content : vui.json.parse(decodeURIComponent('<%= otherRequestContent %>'))
				};

				var typeId = requestData.content[vui.ui.editor.TYPE_ID_ATTRIBUTE] || undefined;
				var typeXmlName = requestData.content[vui.ui.editor.TYPE_XML_NAME_ATTRIBUTE] || undefined;
				
				var saveCallback = function(data, noAssociations) {
					if (opener && vui.exists("vui.vcm.ui.workspace", opener) && !opener.closed) {
						opener.vui.vcm.ui.workspace.editorSaveCallback(data, noAssociations);
					}
					vui.vcm.ui.workspace.editorSaveCallback(data, noAssociations);
				}
				
				<%-- Do editor --%>
				var editConfig = {
					asObjectType: '<%= type %>',
					id: '<%= id %>',
					objectTypeId: typeId,
					objectTypeXmlName: typeXmlName,
					fullscreen: true,
					saveCallback: saveCallback
				};
<%
	// TODO remove this once REST authentication is in
	if ( loggedIn ) {
%>
				vui.auth.internal.TOKEN = "loggedIn";
<%
	}
%>
				if ( vui.auth.isValid() ) {
					vui.vcm.ui.editor.edit(editConfig, requestData);
				} else {
					var loginConfig = {
						postLoginHandler: function() {
							vui.vcm.ui.editor.edit(editConfig, requestData);
						}
					};

					vui.vcm.ui.login.showLogin(loginConfig);
				}
	        }));
		</script>
	</body>
</html>	
