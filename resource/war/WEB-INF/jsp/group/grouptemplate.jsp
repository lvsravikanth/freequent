<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="com.scalar.core.ContextUtil" %>
<%@ page import="com.scalar.core.request.Context" %>
<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.core.util.LocaleUtil" %>
<%@ page import="com.scalar.freequent.action.ManageGroupsAction" %>
<%@ page import="com.scalar.freequent.common.GroupData" %>
<%@ page import="com.scalar.freequent.l10n.WorkspaceResource" %>
<%@ page import="com.scalar.freequent.util.Constants" %>
<%@ page import="com.scalar.freequent.util.EditorUtils" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	Request fRequest = (Request)request.getAttribute(Request.REQUEST_ATTRIBUTE);
	String context = ContextUtil.getContextPath(request);
	String editorId = request.getParameter(EditorUtils.EDITOR_ID_ATTRIBUTE);
	String formId = editorId + "-" + Constants.FORM;
	Context ctx = fRequest.getContext();
	Locale locale = LocaleUtil.getLocale(ctx);
	GroupData groupData = (GroupData)fRequest.getAttribute(ManageGroupsAction.ATTR_GROUP_DATA);
%>
<fmt:setLocale value="<%= locale %>"/>
<fmt:setBundle basename="<%= WorkspaceResource.BASE_NAME %>"/>
<form:form commandName="groupData" id="<%=formId%>" action="#" class="fui-form">
<div class="fui-form-container">

	<div class="fui-form-block-container">
		<label for="name" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.GROUP_NAME%>"/><span class="fui-widget-required-flag">&nbsp;*</span></label><br>
		<div class="fui-form-inline-input"><input type="text" class="fui-input fui-input-text" name="name" id="name" value="<c:out value="${groupData.name}"/>"/></div>
		<div class="fui-form-item-validation"><div id="name-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>

    <div class="fui-form-block-container">
		<label for="name" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.GROUP_DESCRIPTION%>"/></label><br>
		<div class="fui-form-inline-input"><textarea class="fui-input fui-input-text" name="description" id="description" width="300" height="150" value="<c:out value="${groupData.description}"/>"/></div>
		<div class="fui-form-item-validation"><div id="description-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>
    
</div>
</form:form>
<script type="text/javascript">
	fui.ready(function() {
		fui.editor.find('<%=editorId%>').setFormId('<%=formId%>');

		fui.query("#<%=formId%>").validate({
			rules: {
				name: "required"			
			},
			errorPlacement: function(error, element) {
				var errorElementId = element.attr("name")+"-validation-text";
				if (fui.byId(errorElementId)) {
					error.appendTo("#"+errorElementId);
				} else {
					error.insertAfter(element);
				}
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




