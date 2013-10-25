<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.core.ContextUtil" %>
<%@ page import="com.scalar.freequent.auth.User" %>
<%@ page import="com.scalar.freequent.l10n.WorkspaceResource" %>
<%@ page import="com.scalar.freequent.util.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	Request fRequest = (Request)request.getAttribute(Request.REQUEST_ATTRIBUTE);
	String context = ContextUtil.getContextPath(request);
	User user = (User)fRequest.getAttribute("user");
	String editorId = request.getParameter(Constants.EDITOR_ID_ATTRIBUTE);
	String formId = request.getParameter(Constants.EDITOR_ID_ATTRIBUTE) + Constants.FORM;
%>
<fmt:setLocale value="<%= fRequest.getLocale() %>"/>
<fmt:setBundle basename="<%= WorkspaceResource.BASE_NAME %>"/>
<form id="<%=formId%>" action="#" class="fui-form">
<div class="fui-form-container">
	<div class="fui-form-block-container">
		<label for="userId" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.USERID%>"/></label><br>
		<div class="fui-form-inline-input"><input type="text" class="fui-input fui-input-text" name="userId" id="userId"/></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="password" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.PASSWORD%>"/></label><br>
		<div class="fui-form-inline-input"><input type="password" class="fui-input fui-input-text" name="password" id="password"/></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="confirmPassword" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.CONFIRM_PASSWORD%>"/></label><br>
		<div class="fui-form-inline-input"><input type="password" class="fui-input fui-input-text" name="confirmPassword" id="confirmPassword"/></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="firstName" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.FIRST_NAME%>"/></label><br>
		<div class="fui-form-inline-input"><input type="text" class="fui-input fui-input-text" name="firstName" id="firstName"/></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="middleName" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.MIDDLE_NAME%>"/></label><br>
		<div class="fui-form-inline-input"><input type="text" class="fui-input fui-input-text" name="middleName" id="middleName"/></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="lastName" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.LAST_NAME%>"/></label><br>
		<div class="fui-form-inline-input"><input type="text" class="fui-input fui-input-text" name="lastName" id="lastName"/></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="expiresOn" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.EXPIRES_ON%>"/></label><br>
		<div class="fui-form-inline-input"><input type="text" class="fui-input fui-input-text" name="expiresOn" id="expiresOn"/></div>
		<div class="fui-layout-end"></div>
	</div>
</div>
</form>
<script type="text/javascript">
	fui.ready(function() {
		fui.editor.find('<%=editorId%>').setFormId('<%=formId%>');
		fui.query( "#expiresOn" ).datepicker({
			dateFormat: "dd-mm-yy",
			showOn: "button",
      		buttonImage: "images/calendar.gif",
      		buttonImageOnly: true
		});

		fui.query("#<%=formId%>").validate({
		  rules: {
			userId: "required",
			password: {
				required: true,
				minlength: 5
			},
			confirm_password: {
				required: true,
				minlength: 5,
				equalTo: "#password"
			},
			firstName: "required",
			lastName: "required",
			expiresOn: "required"
		  }
		});
	});

	fui.ready(function(){
		var handler = function(event, fuiEvent) {
			if (fui.editor.event.LOAD == fuiEvent.type) {
				// do upon loading
				var editor = fuiEvent.object;
			}
		};
		fui.editor.event.subscribe("<%=editorId%>", handler);
	});
</script>
