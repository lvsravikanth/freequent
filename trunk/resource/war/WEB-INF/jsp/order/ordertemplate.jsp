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
<%@ page import="com.scalar.freequent.action.ManageOrdersAction" %>
<%@ page import="com.scalar.freequent.common.OrderData" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	Request fRequest = (Request) request.getAttribute(Request.REQUEST_ATTRIBUTE);
	String context = ContextUtil.getContextPath(request);
	String editorId = request.getParameter(EditorUtils.EDITOR_ID_ATTRIBUTE);
	String formId = editorId + "-" + Constants.FORM;
	Context ctx = fRequest.getContext();
	Locale locale = LocaleUtil.getLocale(ctx);
	TimeZone timeZone = TimeZoneUtil.getTimeZone(ctx);
	String datePattern = DateTimeUtil.getDatePattern(ctx);
	OrderData orderData = (OrderData) fRequest.getAttribute(ManageOrdersAction.ATTR_ORDER_DATA);
	DateFormat dateFormatter = DateTimeUtil.getDateFormatter(ctx);
	String orderDateStr = dateFormatter.format(orderData.getOrderDate());
	boolean readOnly = orderData.getStatus() != OrderData.OrderStatus.ACTIVE;
%>
<fmt:setLocale value="<%= locale %>"/>
<fmt:setBundle basename="<%= WorkspaceResource.BASE_NAME %>"/>
<form:form commandName="<%=ManageOrdersAction.ATTR_ORDER_DATA%>" id="<%=formId%>" action="#" class="fui-form">
	<div class="fui-form-container">
		<table class="fui-table" width="100%">
			<tbody>
			<tr>
				<td>
					<table align="left">
						<tbody>
						<tr>
							<td><form:label path="<%=OrderData.ATTR_ORDER_NUMBER%>"
											cssClass="fui-label-text"><fmt:message
									key="<%=WorkspaceResource.ORDER_NUMBER%>"/></form:label></td>
							<td><form:input path="<%=OrderData.ATTR_ORDER_NUMBER%>"
											id="<%=OrderData.ATTR_ORDER_NUMBER%>" cssClass="fui-input fui-input-text"
											disabled="true"/></td>
						</tr>
						<tr>
							<td><form:label path="<%=OrderData.ATTR_CUST_NAME%>" cssClass="fui-label-text"><fmt:message
									key="<%=WorkspaceResource.CUST_NAME%>"/></form:label></td>
							<td><form:input path="<%=OrderData.ATTR_CUST_NAME%>" id="<%=OrderData.ATTR_CUST_NAME%>"
											cssClass="fui-input fui-input-text"/></td>
						</tr>
						</tbody>
					</table>
				</td>
				<td>
					<table align="right">
						<tbody>
						<tr>
							<td><form:label path="<%=OrderData.ATTR_ORDER_DATE%>" cssClass="fui-label-text"><fmt:message
									key="<%=WorkspaceResource.ORDER_DATE%>"/></form:label></td>
							<td><input type="text" name="<%=OrderData.ATTR_ORDER_DATE%>" id="<%=OrderData.ATTR_ORDER_DATE%>" disabled="true" value="<%=orderDateStr%>"/></td>
						</tr>
						<tr>
							<td><form:label path="<%=OrderData.ATTR_STATUS%>" cssClass="fui-label-text"><fmt:message
									key="<%=WorkspaceResource.STATUS%>"/></form:label></td>
							<td><form:input path="<%=OrderData.ATTR_STATUS%>" id="<%=OrderData.ATTR_STATUS%>"
											cssClass="fui-input fui-input-text" disabled="true"/></td>
						</tr>
						<tr>
							<td><form:label path="<%=OrderData.ATTR_REVISION%>" cssClass="fui-label-text"><fmt:message
									key="<%=WorkspaceResource.REVISION%>"/></form:label></td>
							<td><form:input path="<%=OrderData.ATTR_REVISION%>" id="<%=OrderData.ATTR_REVISION%>"
											cssClass="fui-input fui-input-text" disabled="true"/></td>
						</tr>
						</tbody>
					</table>
				</td>
			</tr>
			</tbody>
		</table>
		<table id="orderlineitemsandtotalstable" class="fui-table">
			<tbody>
			<tr>
				<td>
					<table id="orderLineItemsTable" class="fui-table ui-widget-content">
						<tbody>
						<tr class="ui-widget-header">
							<td class="fui-col fui-col-head"><span class="fui-label-text"><fmt:message key="<%=WorkspaceResource.SNO%>"/></span></td>
							<td class="fui-col fui-col-head"><span class="fui-label-text"><fmt:message key="<%=WorkspaceResource.DESCRIPTION%>"/></span></td>
							<td class="fui-col fui-col-head"><span class="fui-label-text"><fmt:message key="<%=WorkspaceResource.QTY%>"/></span></td>
							<td class="fui-col fui-col-head"><span class="fui-label-text"><fmt:message key="<%=WorkspaceResource.PRICE%>"/></span></td>
							<td class="fui-col fui-col-head"><span class="fui-label-text"><fmt:message key="<%=WorkspaceResource.AMOUNT%>"/></span>
							</td>
						</tr>
						<% int lineItemCount = 0; %>
						<c:forEach items="${orderData.lineItems}" var="lineItem" varStatus="row">
							<% lineItemCount++; %>
							<tr>
								<form:hidden path="lineItems[${row.index}].id" id="lineItems[${row.index}]id"/>
								<td class="fui-col fui-col-content"><span>${lineItem.lineNumber}</span>
									<form:hidden path="lineItems[${row.index}].lineNumber" cssClass="fui-input"
												disabled="true"/></td>
								<td class="fui-col fui-col-content">
									<form:input path="lineItems[${row.index}].itemId" id="lineItems${row.index}itemId"
												 cssClass="fui-input fui-input-select"/>
									<script type="text/javascript">
										fui.ready(function() {
											fui.ui.manageorders.makeItemSelect("lineItems${row.index}itemId", fui.workspace.getMessage("select"), <%=readOnly%>);
										});
									</script>
								</td>
								<td class="fui-col fui-col-content"><form:input path="lineItems[${row.index}].qty" id="lineItems${row.index}qty" onchange="fui.ui.manageorders.calculateTotals()" cssClass="fui-input fui-qty-input"/></td>
								<td class="fui-col fui-col-content"><form:input path="lineItems[${row.index}].price" id="lineItems${row.index}price" onchange="fui.ui.manageorders.calculateTotals()" cssClass="fui-input fui-price-input" disabled="true"/></td>
								<td class="fui-col fui-col-content"><form:input path="lineItems[${row.index}].amount" id="lineItems${row.index}amount" cssClass="fui-input fui-price-input" disabled="true"/></td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<button type="button" id="addmore">Add More</button>
					<script type="text/javascript">
						fui.lineItemCount = <%=lineItemCount%>;
						fui.ready(function(){
							fui.query("#addmore").button({disabled: <%=readOnly%>})
								.click( function(event) {
									var lineItem = {};
									lineItem.index = fui.lineItemCount;
									fui.lineItemCount = fui.lineItemCount + 1;
									lineItem.lineNumber = fui.lineItemCount;
									fui.ui.manageorders.addLineItem("orderLineItemsTable", lineItem);
                                    fui.query("#orderLineItemsTable").find("tr:odd").addClass("fui-order-item-odd-row-selection");
									fui.query("#"+"lineItems"+lineItem.index+"itemId").select2("open");
								});;
						});
					</script>

				</td>
			</tr>
			<tr>
				<td align="right">
					<table id="totalsTable" class="fui-table">
						<tbody>
						<tr>
							<td>
								<span class="fui-label-text"><fmt:message key="<%=WorkspaceResource.TOTAL%>"/></span>
							</td>
							<td>
								<form:input path="totalAmount" disabled="true" cssClass="fui-input fui-input-text"/>
							</td>
						</tr>
						<tr>
							<td>
								<span class="fui-label-text"><fmt:message key="<%=WorkspaceResource.TAX%>"><fmt:param
										value="${orderData.taxPercentage}"/></fmt:message></span>
								<form:hidden path="taxPercentage"/>
							</td>
							<td>
								<form:input path="taxAmount" disabled="true" cssClass="fui-input fui-input-text"/>
							</td>
						</tr>
						<tr>
							<td>
								<span class="fui-label-text"><fmt:message
										key="<%=WorkspaceResource.GRAND_TOTAL%>"/></span>
							</td>
							<td>
								<form:input path="grandTotal" disabled="true" cssClass="fui-input fui-input-text"/>
							</td>
						</tr>
						</tbody>
					</table>
				</td>
			</tr>
			</tbody>

		</table>


	</div>
</form:form>
<script type="text/javascript">
	fui.ready(function() {
        fui.query("#orderLineItemsTable").find("tr:odd").addClass("fui-order-item-odd-row-selection");
		var editor = fui.editor.find('<%=editorId%>');
		editor.setFormId('<%=formId%>');
		editor.readOnly = <%=readOnly%>;
		fui.query("#<%=formId%>").validate({
			rules: {
				name: "required",
				price: {
					required: true,
					number: true
				}
			},
			errorPlacement: function(error, element) {
				var errorElementId = element.attr("id") + "-validation-text";
				if (fui.byId(errorElementId)) {
					error.appendTo("#" + errorElementId);
				} else {
					error.insertAfter(element);
				}
			}
		});
	});

	fui.ready(function() {
		var handler = function(event, fuiEvent) {
			if (fui.editor.event.LOAD == fuiEvent.type) {
				// do upon loading
				var editor = fuiEvent.object;
			}
		};
		fui.editor.event.subscribe("<%=editorId%>", handler);
	});
</script>