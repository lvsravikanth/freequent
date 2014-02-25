<%@ page import="com.scalar.core.ContextUtil" %>
<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.freequent.l10n.WorkspaceResource" %>
<%@ page import="com.scalar.freequent.action.ManageUsersAction" %>
<%@ page import="com.scalar.freequent.common.Item" %>
<%@ page import="com.scalar.freequent.common.OrderData" %>
<%@ page import="com.scalar.freequent.util.DateTimeUtil" %>
<%@ page import="com.scalar.core.request.Context" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    String context = ContextUtil.getContextPath(request);
    Request fRequest = (Request) request.getAttribute(Request.REQUEST_ATTRIBUTE);
	Context ctx = fRequest.getContext();
	String datePattern = DateTimeUtil.getDatePattern(ctx);
%>
<fmt:setLocale value="<%= fRequest.getLocale() %>"/>
<fmt:setBundle basename="<%= WorkspaceResource.BASE_NAME %>"/>

<% %>
<div class="fui-workspace-container">
    <div class="fui-workspace-search-panel">
        <div>
            <div class="fui-workspace-search-header ui-widget-header ui-corner-top"><fmt:message key="<%=WorkspaceResource.MANAGEORDERS_TITLE%>"/></div>
			<form id="manageorders" class="fui-workspace-search-manageorders">
                <div class="fui-workspace-search-container">
                    <div >
                        <span class="fui-workspace-search-wrap-row">
                            <div class="fui-workspace-search-container-column">
							    <label for="<%=OrderData.PARAM_ORDER_NUMBER%>"><fmt:message key="<%=WorkspaceResource.ORDER_NUMBER%>"/></label>
                            </div>
						    <div class="fui-workspace-search-container-column fui-workspace-search-container-wrap-column">
							    <input type="text" name="<%=OrderData.PARAM_ORDER_NUMBER%>" id="<%=OrderData.PARAM_ORDER_NUMBER%>">
                            </div>
						</span>
                        <span class="fui-workspace-search-wrap-row">
                            <div class="fui-workspace-search-container-column">
                                <label for="<%=OrderData.PARAM_ITEM_ID%>"><fmt:message key="<%=WorkspaceResource.ITEM_NAME%>"/></label>
                            </div>
                            <div class="fui-workspace-search-container-column fui-workspace-search-container-wrap-column">
                                <input type="hidden" name="<%=OrderData.PARAM_ITEM_ID%>" id="<%=OrderData.PARAM_ITEM_ID%>" class="fui-input fui-input-select"/>
                                <script type="text/javascript">
                                    fui.ready(function(){
									    fui.ui.manageorders.makeItemSelect("<%=OrderData.PARAM_ITEM_ID%>", fui.workspace.getMessage("all"));
								    });
                                </script>
                            </div>
                        </span>
                        <span class="fui-workspace-search-wrap-row">
                            <div class="fui-workspace-search-container-column">
                                <label for="<%=OrderData.PARAM_FROM_DATE%>"><fmt:message key="<%=WorkspaceResource.FROM_DATE%>"/></label>
                            </div>
                            <div class="fui-workspace-search-container-column fui-workspace-search-container-wrap-column">
                                <input type="text" class="fui-input fui-input-text" placeholder="<%=datePattern%>" name="<%=OrderData.PARAM_FROM_DATE%>" id="<%=OrderData.PARAM_FROM_DATE%>"/>
                            </div>
                        </span>
                        <span class="fui-workspace-search-wrap-row fui-workspace-search-order-datepicker">
                            <div class="fui-workspace-search-container-column">
                                <label for="<%=OrderData.PARAM_TO_DATE%>"><fmt:message key="<%=WorkspaceResource.TO_DATE%>"/></label>
                            </div>
                            <div class="fui-workspace-search-container-column fui-workspace-search-container-wrap-column">
                                <input type="text" class="fui-input fui-input-text" placeholder="<%=datePattern%>" name="<%=OrderData.PARAM_TO_DATE%>" id="<%=OrderData.PARAM_TO_DATE%>"/>
                            </div>
                        </span>
						<span class="fui-workspace-search-wrap-row">
                            <div class="fui-workspace-search-container-column">
                                <label for="<%=OrderData.PARAM_STATUS%>"><fmt:message key="<%=WorkspaceResource.STATUS%>"/></label>
                            </div>
                            <div class="fui-workspace-search-container-column fui-workspace-search-container-wrap-column">
								<select name="<%=OrderData.PARAM_STATUS%>" id="<%=OrderData.PARAM_STATUS%>" class="fui-worspace-search-order-status">
									<option value=""><fmt:message key="<%=WorkspaceResource.ALL%>"/></option>
								<%
									OrderData.OrderStatus[] statuses = OrderData.OrderStatus.values();
									for (OrderData.OrderStatus status: statuses) {
								%>
									<option value="<%=status%>" <%=status.isActive() ? "selected" : ""%>><%=status%></option>
								<% } %>
								</select>
                                <script type="text/javascript">
                                    fui.ready(function(){
									    fui.query("#<%=OrderData.PARAM_STATUS%>").select2();
								    });
                                </script>
                            </div>
                        </span>
                        <span class="fui-workspace-search-wrap-row fui-workspace-search-button-wrap-row">
                            <div class="fui-workspace-search-container-column  fui-workspace-search-container-wrap-column fui-workspace-search-container-button-wrap-column">
                                <button type="button" id="search"><fmt:message key="<%=WorkspaceResource.SEARCH%>"/></button>
                            </div>
                            <div class="fui-workspace-search-container-column fui-workspace-search-container-wrap-column fui-workspace-search-container-button-wrap-column">
                                <button type="reset" id="reset"><fmt:message key="<%=WorkspaceResource.RESET%>"/></button>
                            </div>
                        </span>
                    </div>
			    </div>
            </form>
            </div>
            <div class="fui-workspace-search-footer">
                <div class="fui-workspace-footer-search-newuser">
                    <button type="button" id="neworder"><fmt:message key="<%=WorkspaceResource.NEW_ORDER%>"/></button>
                </div>
            </div>
        </div>
        <div class="fui-workspace-grid" id="fui-workspace-grid"></div>
</div>
<script type="text/javascript">
    fui.ready(function() {

		var params = {};
		params.<%=OrderData.PARAM_ORDER_NUMBER%> = fui.byId("<%=OrderData.PARAM_ORDER_NUMBER%>").value;
		params.<%=OrderData.PARAM_ITEM_ID%> = fui.byId("<%=OrderData.PARAM_ITEM_ID%>").value;
		params.<%=OrderData.PARAM_FROM_DATE%> = fui.byId("<%=OrderData.PARAM_FROM_DATE%>").value;
		params.<%=OrderData.PARAM_TO_DATE%> = fui.byId("<%=OrderData.PARAM_TO_DATE%>").value;
		params.<%=OrderData.PARAM_STATUS%> = fui.byId("<%=OrderData.PARAM_STATUS%>").value;
		fui.ui.manageorders.internal.setSearchParams(params);

        fui.query("#search").button()
                .click( function(event) {
            var params = {};
            params.<%=OrderData.PARAM_ORDER_NUMBER%> = fui.byId("<%=OrderData.PARAM_ORDER_NUMBER%>").value;
            params.<%=OrderData.PARAM_ITEM_ID%> = fui.byId("<%=OrderData.PARAM_ITEM_ID%>").value;
            params.<%=OrderData.PARAM_FROM_DATE%> = fui.byId("<%=OrderData.PARAM_FROM_DATE%>").value;
            params.<%=OrderData.PARAM_TO_DATE%> = fui.byId("<%=OrderData.PARAM_TO_DATE%>").value;
            params.<%=OrderData.PARAM_STATUS%> = fui.byId("<%=OrderData.PARAM_STATUS%>").value;
            fui.ui.manageorders.internal.setSearchParams(params);
			fui.grid.find(fui.ui.type.ORDER).refresh();
        });
		fui.query("#neworder").button()
			.click( function(event) {
			fui.ui.manageorders.edit();
		});
		fui.query("#reset").button()
				.click( function(event) {
			fui.query("#<%=OrderData.PARAM_ITEM_ID%>").select2("val", "");
			fui.query("#<%=OrderData.PARAM_STATUS%>").select2("val", fui.query('#<%=OrderData.PARAM_STATUS%>').find(':selected').val());
		});

		fui.query( "#<%=OrderData.PARAM_FROM_DATE%>" ).datepicker({
			dateFormat: fui.context.getContext()[fui.context.FORMAT_KEY].date,
			//showOn: "button",
      		buttonImage: "<%=context%>/theme/corporate/icon/calendar.gif",
      		buttonImageOnly: true
		});

		fui.query( "#<%=OrderData.PARAM_TO_DATE%>" ).datepicker({
            //showAnim: fui.query(this).val(),
			dateFormat: fui.context.getContext()[fui.context.FORMAT_KEY].date,
			//showOn: "button",
      		buttonImage: "<%=context%>/theme/corporate/icon/calendar.gif",
      		buttonImageOnly: true
		});

        //Register grid
        fui.grid.register(fui.ui.type.ORDER, fui.ui.grid.manageorders.get());

        //build grid
        var config = {
            type: fui.ui.type.ORDER
        };
        fui.workspace.load(config);
    });

	fui.ready(function(){
		// by default load the data into the grid when page loads
		//fui.query("#search").button().click();
	});
</script>
