<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="com.scalar.core.ContextUtil" %>
<%@ page import="com.scalar.core.request.Context" %>
<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.core.util.LocaleUtil" %>
<%@ page import="com.scalar.freequent.action.ManageCategoriesAction" %>
<%@ page import="com.scalar.freequent.common.CategoryData" %>
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
	CategoryData categoryData = (CategoryData)fRequest.getAttribute(ManageCategoriesAction.ATTR_CATEGORY_DATA);
%>
<fmt:setLocale value="<%= locale %>"/>
<fmt:setBundle basename="<%= WorkspaceResource.BASE_NAME %>"/>
<form:form commandName="categoryData" id="<%=formId%>" action="#" class="fui-form">
<div class="fui-form-container">

	<div class="fui-form-block-container">
		<label for="<%=CategoryData.ATTR_NAME%>" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.CATEGORY_NAME%>"/><span class="fui-widget-required-flag">&nbsp;*</span></label><br>
		<div class="fui-form-inline-input"><form:input path="<%=CategoryData.ATTR_NAME%>" cssClass="fui-input fui-input-text"/></div>
		<div class="fui-form-item-validation"><div id="name-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>

    <div class="fui-form-block-container">
		<label for="<%=CategoryData.ATTR_DESCRIPTION%>" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.CATEGORY_DESCRIPTION%>"/></label><br>
		<div class="fui-form-inline-input"><form:textarea path="<%=CategoryData.ATTR_DESCRIPTION%>" cssClass="fui-input fui-input-text" cols="80" rows="5"/></div>
		<div class="fui-form-item-validation"><div id="description-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>
    
</div>
</form:form>
<script type="text/javascript">
	fui.ready(function() {
		fui.editor.find('<%=editorId%>').setFormId('<%=formId%>');

		fui.query("#<%=formId%>").validate({
			submitHandler : function(form) {/*do nothing. hack, to prevent text field enter key submitting.*/},
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




