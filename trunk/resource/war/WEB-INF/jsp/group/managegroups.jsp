<%@ page import="com.scalar.core.ContextUtil" %>
<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.freequent.l10n.WorkspaceResource" %>
<%@ page import="com.scalar.freequent.common.GroupData" %>
<%@ page import="com.scalar.freequent.common.GroupData" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
            <div class="fui-workspace-search-header ui-widget-header ui-corner-top"><fmt:message key="<%=WorkspaceResource.MANAGEGROUPS_TITLE%>"/></div>
            <form id="managegroups">
                    <div class="fui-workspace-search-container">
                        <div >
                            <span class="fui-workspace-search-row">
                                <div class="fui-workspace-search-container-column">
                                    <label for="<%=GroupData.PARAM_NAME%>"><fmt:message key="<%=WorkspaceResource.GROUP_NAME%>"/></label>
                                </div>
                                <div class="fui-workspace-search-container-column">
                                    <input type="text" name="<%=GroupData.PARAM_NAME%>" id="<%=GroupData.PARAM_NAME%>">
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
                    <button type="button" id="newgroup"><fmt:message key="<%=WorkspaceResource.NEW_GROUP%>"/></button>
                </div>
            </div>
        </div>
        <div class="fui-workspace-grid" id="fui-workspace-grid"></div>
</div>
<script type="text/javascript">
    fui.ready(function() {
		var params = {};
		params.name = fui.byId("name").value;
		fui.ui.managegroups.internal.setSearchParams(params);

        fui.query("#search").button()
            .click( function(event) {
                var params = {};
                params.name = fui.byId("name").value;
                fui.ui.managegroups.internal.setSearchParams(params);
				fui.grid.find(fui.ui.type.GROUP).refresh();
            }
        );
		fui.query("#newgroup").button()
			.click( function(event) {
		    	fui.ui.managegroups.edit();
		});
		fui.query("#reset").button();

        //Register grid
        fui.grid.register(fui.ui.type.GROUP, fui.ui.grid.managegroups.get());

        //build grid
        var config = {
            type: fui.ui.type.GROUP
        };
        fui.workspace.load(config);
    });

	fui.ready(function(){
		// by default load the data into the grid when page loads
		// fui.query("#search").button().click();
	});
</script>