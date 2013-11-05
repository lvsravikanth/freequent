<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.core.ContextUtil" %>
<%@ page import="com.scalar.freequent.auth.User" %>
<%@ page import="com.scalar.freequent.l10n.WorkspaceResource" %>
<%@ page import="com.scalar.freequent.util.Constants" %>
<%@ page import="com.scalar.core.request.Context" %>
<%@ page import="com.scalar.core.util.LocaleUtil" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="com.scalar.core.util.TimeZoneUtil" %>
<%@ page import="com.scalar.freequent.util.DateTimeUtil" %>
<%@ page import="com.scalar.freequent.action.ManageUsersAction" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="com.scalar.freequent.auth.UserCapability" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.scalar.freequent.util.EditorUtils" %>
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
	User user = (User)fRequest.getAttribute(ManageUsersAction.ATTR_USER);
	DateFormat dateFormatter = DateTimeUtil.getDateFormatter(ctx);
%>
<fmt:setLocale value="<%= locale %>"/>
<fmt:setBundle basename="<%= WorkspaceResource.BASE_NAME %>"/>
<form id="<%=formId%>" action="#" class="fui-form">
<div class="fui-form-container">
	<div class="fui-form-block-container">
		<label for="userId" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.USERID%>"/>&nbsp;*</label><br>
		<div class="fui-form-inline-input"><input type="text" class="fui-input fui-input-text" name="userId" id="userId" value="<c:out value="${user.userId}"/>"/></div>
		<div class="fui-form-item-validation"><div id="userId-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="password" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.PASSWORD%>"/>&nbsp;*</label><br>
		<div class="fui-form-inline-input"><input type="password" class="fui-input fui-input-text" name="password" id="password" value="<c:out value="${user.password}"/>"/></div>
		<div class="fui-form-item-validation"><div id="password-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="confirmPassword" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.CONFIRM_PASSWORD%>"/>&nbsp;*</label><br>
		<div class="fui-form-inline-input"><input type="password" class="fui-input fui-input-text" name="confirmPassword" id="confirmPassword" value="<c:out value="${user.password}"/>"/></div>
		<div class="fui-form-item-validation"><div id="confirmPassword-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="firstName" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.FIRST_NAME%>"/>&nbsp;*</label><br>
		<div class="fui-form-inline-input"><input type="text" class="fui-input fui-input-text" name="firstName" id="firstName" value="<c:out value="${user.firstName}"/>"/></div>
		<div class="fui-form-item-validation"><div id="firstName-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="middleName" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.MIDDLE_NAME%>"/></label><br>
		<div class="fui-form-inline-input"><input type="text" class="fui-input fui-input-text" name="middleName" id="middleName" value="<c:out value="${user.middleName}"/>"/></div>
		<div class="fui-form-item-validation"><div id="middleName-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="lastName" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.LAST_NAME%>"/>&nbsp;*</label><br>
		<div class="fui-form-inline-input"><input type="text" class="fui-input fui-input-text" name="lastName" id="lastName" value="<c:out value="${user.lastName}"/>"/></div>
		<div class="fui-form-item-validation"><div id="lastName-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>

	<div class="fui-form-block-container">
		<label for="expiresOn" class="fui-label-text"><fmt:message key="<%=WorkspaceResource.EXPIRES_ON%>"/>&nbsp;(<%=datePattern%>)&nbsp;*</label><br>
		<div class="fui-form-inline-input"><input type="text" class="fui-input fui-input-text" name="expiresOn" id="expiresOn" value="<%=user.getExpiresOn() == null ? "" : dateFormatter.format(user.getExpiresOn())%>"/></div>
		<div class="fui-form-item-validation"><div id="expiresOn-validation-text" class="fui-form-item-validation-text"></div></div>
		<div class="fui-layout-end"></div>
	</div>
	<div id="capabilities-accordian">
	  <h3><fmt:message key="<%=WorkspaceResource.USER_CAPABILITIES%>"/></h3>
	  <div id="capabilities-content">
			<table>
				<thead>
					<tr>
						<th><fmt:message key="<%=WorkspaceResource.CAPABILITY%>"/></th>
						<th><fmt:message key="<%=WorkspaceResource.READ%>"/></th>
						<th><fmt:message key="<%=WorkspaceResource.WRITE%>"/></th>
						<th><fmt:message key="<%=WorkspaceResource.DELETE%>"/></th>
					</tr>
				</thead>
				<tbody>
				<%
					Map<String, UserCapability> userCapabilityMap = user.getUserCapabilitiesMap();
				%>
					<c:forEach items="${capabilities}" var="capability" varStatus="loop">
						<c:set var="capabilityName">${capability.name}</c:set>
						<%
							UserCapability userCapability = userCapabilityMap.get(pageContext.getAttribute("capabilityName"));
							String hasRead = "";
							String hasWrite = "";
							String hasDelete = "";
							if (userCapability != null) {
								hasRead = userCapability.isHasRead() ? "checked" : "";
								hasWrite = userCapability.isHasWrite() ? "checked" : "";
								hasDelete = userCapability.isHasDelete() ? "checked" : "";
							}
						%>
						<tr>
							<td>
								${capability.name}
								<input type="hidden" value="${capability.name}" name="userCapabilities[${loop.index}].capabilityName" />
							</td>
							<td>
								<c:if test="${capability.supportsRead}">
									<input type="checkbox" name="userCapabilities[${loop.index}].hasRead" value="true" <%=hasRead%> />
								</c:if>
							</td>
							<td>
								<c:if test="${capability.supportsWrite}">
									<input type="checkbox" name="userCapabilities[${loop.index}].hasWrite" value="true" <%=hasWrite%>/>
								</c:if>
							</td>
							<td>
								<c:if test="${capability.supportsDelete}">
									<input type="checkbox" name="userCapabilities[${loop.index}].hasDelete" value="true" <%=hasDelete%>/>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	  </div>
	</div>
</div>
</form>
<script type="text/javascript">
	fui.ready(function() {
		fui.editor.find('<%=editorId%>').setFormId('<%=formId%>');

		<%
		if (!EditorUtils.isNewEditorId(editorId)) {
		%>
			fui.query('#userId').prop("readonly",true);
		<%}%>

		fui.query( "#expiresOn" ).datepicker({
			dateFormat: fui.context.getContext()[fui.context.FORMAT_KEY].date,
			showOn: "button",
      		buttonImage: "<%=context%>/theme/corporate/icon/calendar.gif",
      		buttonImageOnly: true
		});

		 fui.query( "#capabilities-accordian" ).accordion({ collapsible: true });

		fui.query("#<%=formId%>").validate({
			rules: {
				userId: "required",
				password: {
					required: true,
					minlength: 5
				},
				confirmPassword: {
					required: true,
					minlength: 5,
					equalTo: "#password"
				},
				firstName: "required",
				lastName: "required",
				expiresOn: "required"
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
