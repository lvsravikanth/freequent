<%@ page import="com.scalar.core.ContextUtil" %>
<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.freequent.l10n.WorkspaceResource" %>
<%@ page import="com.scalar.freequent.common.Item" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    String context = ContextUtil.getContextPath(request);
    Request fRequest = (Request) request.getAttribute(Request.REQUEST_ATTRIBUTE);
%>
<fmt:setLocale value="<%= fRequest.getLocale() %>"/>
<fmt:setBundle basename="<%= WorkspaceResource.BASE_NAME %>"/>

<% %>
<div class="fui-workspace-container">
    <div class="fui-workspace-search-panel">
        <div>
            <div class="fui-workspace-search-header ui-widget-header ui-corner-top"><fmt:message key="<%=WorkspaceResource.MANAGEITEMS_TITLE%>"/></div>
            <form id="manageitems">
                    <div class="fui-workspace-search-container">
                        <div >
                            <span class="fui-workspace-search-row">
                                <div class="fui-workspace-search-container-column">
                                    <label for="<%=Item.PARAM_NAME%>"><fmt:message key="<%=WorkspaceResource.ITEM_NAME%>"/></label>
                                </div>
                                <div class="fui-workspace-search-container-column">
                                    <input type="text" name="<%=Item.PARAM_NAME%>" id="<%=Item.PARAM_NAME%>">
                                </div>
                            </span>
                            <span class="fui-workspace-search-row">
                                <div class="fui-workspace-search-container-column">
                                    <label for="<%=Item.PARAM_GROUP%>"><fmt:message key="<%=WorkspaceResource.GROUP%>"/></label>
                                </div>
                                <div class="fui-workspace-search-container-column">
                                    <select name="<%=Item.PARAM_GROUP%>" id="<%=Item.PARAM_GROUP%>" class="fui-inpu fui-input-select">
                                        <option value=""><fmt:message key="<%=WorkspaceResource.ALL%>"/></option>
                                        <c:forEach items="${groupDataList}" var="groupData" varStatus="loop">
                                            <option value="${groupData.name}">${groupData.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </span>
                            <span class="fui-workspace-search-row">
                                <div class="fui-workspace-search-container-column">
                                    <label for="<%=Item.PARAM_CATEGORY%>"><fmt:message key="<%=WorkspaceResource.CATEGORY%>"/></label>
                                </div>
                                <div class="fui-workspace-search-container-column">
                                    <select name="<%=Item.PARAM_CATEGORY%>" id="<%=Item.PARAM_CATEGORY%>" class="fui-inpu fui-input-select">
                                        <option value=""><fmt:message key="<%=WorkspaceResource.ALL%>"/></option>
                                        <c:forEach items="${categoryDataList}" var="categoryData" varStatus="loop">
                                            <option value="${categoryData.id}">${categoryData.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </span>
                            <span class="fui-workspace-search-row">
                                <div class="fui-workspace-search-container-column">
                                    <button type="button" id="search"><fmt:message key="<%=WorkspaceResource.SEARCH%>"/></button>
                                </div>
                                <div class="fui-workspace-search-container-column">
                                    <button type="reset" id="reset"><fmt:message key="<%=WorkspaceResource.RESET%>"/></button>
                                </div>
                            </span>
                    </div>
                </div>
            </form>
            </div>
            <div class="fui-workspace-search-footer">
                <div class="fui-workspace-footer-search-newuser">
                    <button type="button" id="newitem"><fmt:message key="<%=WorkspaceResource.NEW_ITEM%>"/></button>
                </div>
            </div>
        </div>
        <div class="fui-workspace-grid" id="fui-workspace-grid"></div>
</div>
<script type="text/javascript">
    fui.ready(function() {
		fui.query("#<%=Item.PARAM_GROUP%>").select2({dropdownCssClass:"ui-dialog", minimumResultsForSearch: 10});
		fui.query("#<%=Item.PARAM_CATEGORY%>").select2({dropdownCssClass:"ui-dialog", minimumResultsForSearch: 10});
        fui.query("#search").button()
                .click( function(event) {
            var params = {};
            params.name = fui.byId("name").value;
            params.group = fui.byId("group").value;
            params.category = fui.byId("category").value;
            fui.ui.manageitems.runSearch(params);

        });
		fui.query("#newitem").button()
			.click( function(event) {
			fui.ui.manageitems.edit();
		});
		fui.query("#reset").button()
				.click( function(event) {
			fui.query("#<%=Item.PARAM_GROUP%>").select2('data', fui.query("#<%=Item.PARAM_GROUP%>").find('option')[0]);
			fui.query("#<%=Item.PARAM_CATEGORY%>").select2('data', fui.query("#<%=Item.PARAM_CATEGORY%>").find('option')[0]);
		});

        //Register grid
        fui.grid.register(fui.ui.type.ITEM, fui.ui.grid.manageitems.get());

        //build grid
        var config = {
            type: fui.ui.type.ITEM
        };
        fui.workspace.load(config);
    });

	fui.ready(function(){
		// by default load the data into the grid when page loads
		fui.query("#search").button().click();
	});
</script>
