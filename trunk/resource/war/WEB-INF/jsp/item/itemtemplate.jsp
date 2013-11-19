<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="com.scalar.core.ContextUtil" %>
<%@ page import="com.scalar.core.request.Context" %>
<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.core.util.LocaleUtil" %>
<%@ page import="com.scalar.core.util.TimeZoneUtil" %>
<%@ page import="com.scalar.freequent.action.ManageItemsAction" %>
<%@ page import="com.scalar.freequent.common.Item" %>
<%@ page import="com.scalar.freequent.l10n.WorkspaceResource" %>
<%@ page import="com.scalar.freequent.util.Constants" %>
<%@ page import="com.scalar.freequent.util.DateTimeUtil" %>
<%@ page import="com.scalar.freequent.util.EditorUtils" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.TimeZone" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	Request fRequest = (Request)request.getAttribute(Request.REQUEST_ATTRIBUTE);
	String context = ContextUtil.getContextPath(request);
	String editorId = request.getParameter(EditorUtils.EDITOR_ID_ATTRIBUTE);
	String formId = editorId + "-" + Constants.FORM;
	Context ctx = fRequest.getContext();
	Locale locale = LocaleUtil.getLocale(ctx);
	TimeZone timeZone = TimeZoneUtil.getTimeZone(ctx);
	String datePattern = DateTimeUtil.getDatePattern(ctx);
	Item item = (Item)fRequest.getAttribute(ManageItemsAction.ATTR_ITEM_DATA);
	DateFormat dateFormatter = DateTimeUtil.getDateFormatter(ctx);
%>
<fmt:setLocale value="<%= locale %>"/>
<fmt:setBundle basename="<%= WorkspaceResource.BASE_NAME %>"/>
<form:form commandName="itemData" id="<%=formId%>" action="#" class="fui-form">
<div class="fui-form-container">
	<div class="fui-form-block-container">
		<label for="name" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.ITEM_NAME%>"/><span class="fui-widget-required-flag">&nbsp;*</span></label><br>
		<div class="fui-form-inline-input"><input type="text" class="fui-input fui-input-text" name="name" id="name" value="<c:out value="${itemData.name}"/>"/></div>
		<div class="fui-form-item-validation"><div id="name-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="price" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.PRICE%>"/><span class="fui-widget-required-flag">*</span></label><br>
		<div class="fui-form-inline-input"><input type="text" class="fui-input fui-input-text" name="price" id="price" value="<c:out value="${itemData.price}"/>"/></div>
		<div class="fui-form-item-validation"><div id="price-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="priceQty" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.PRICE_QTY%>"/><span class="fui-widget-required-flag">*</span></label><br>
		<div class="fui-form-inline-input"><input type="text" class="fui-input fui-input-text" name="priceQty" id="priceQty" value="<c:out value="${itemData.priceQty}"/>"/></div>
		<div class="fui-form-item-validation"><div id="priceQty-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="unitData" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.UNIT%>"/><span class="fui-widget-required-flag">*</span></label><br>
		<div class="fui-form-inline-input">
			<form:select path="unitData.name" id="unitData" cssClass="fui-input-select">
				<form:options items="${unitDataList}" itemLabel="name" itemValue="name"/>
			</form:select>
		</div>
		<div class="fui-form-item-validation"><div id="unitData-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="groupData" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.GROUP%>"/></label><br>
		<div class="fui-form-inline-input">
			<form:select path="groupData.name" id="groupData" cssClass="fui-input-select">
				<form:options items="${groupDataList}" itemLabel="name" itemValue="name"/>
			</form:select>
		</div>
		<div class="fui-form-item-validation"><div id="groupData-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="categoryAssocData" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.CATEGORY%>"/><span class="fui-widget-required-flag">*</span></label><br>
		<div class="fui-form-inline-input">
			<form:select path="categoryId" id="categoryAssocData" cssClass="fui-input-select">
				<form:options items="${categoryDataList}" itemLabel="name" itemValue="id"/>
			</form:select>
		</div>
		<div class="fui-form-item-validation"><div id="categoryAssocData-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>
</div>
</form:form>
<script type="text/javascript">
	fui.ready(function() {
		fui.editor.find('<%=editorId%>').setFormId('<%=formId%>');
		fui.query("#unitData").select2({dropdownCssClass:"ui-dialog"/*fix for to focus search field. https://github.com/ivaynberg/select2/issues/1246*/,
									minimumResultsForSearch: 10
									});
		fui.query("#groupData").select2({dropdownCssClass:"ui-dialog", minimumResultsForSearch: 10});
		fui.query("#categoryAssocData").select2({dropdownCssClass:"ui-dialog", minimumResultsForSearch: 10});

		fui.query("#<%=formId%>").validate({
			rules: {
				name: "required",
				price: {
					required: true,
					number: true
				},
				priceQty: {
					required: true,
					digits: true
				}
			},
			errorPlacement: function(error, element) {
				var errorElementId = element.attr("id")+"-validation-text";
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




