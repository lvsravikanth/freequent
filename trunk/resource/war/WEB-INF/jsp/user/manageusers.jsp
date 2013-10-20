<%@ page import="com.scalar.core.ContextUtil" %>
<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.freequent.l10n.WorkspaceResource" %>
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
	<div class="fui-workspace-search-table">
		<div class="fui-workspace-search-table-row">
			<div class="fui-workspace-header ui-widget-header ui-corner-top"><fmt:message key="<%=WorkspaceResource.MANAGEUSERS_TITLE%>"/>
				<div class="fui-workspace-header-search">
					<button type="button" id="newuser"><fmt:message key="<%=WorkspaceResource.NEWUSER%>"/></button>
				</div>
			</div>
		</div>
		<div class="fui-workspace-search-container">
			<form id="manageusers">
				<div class="fui-workspace-search-table-row">
					<span class="fui-workspace-search-table-cell">
						<span for="userid"><fmt:message key="<%=WorkspaceResource.USERID%>"/></span>
					</span>
					<span class="fui-workspace-search-table-cell">
						<input type="text" name="userid" id="userid">
					</span>
					<span class="fui-workspace-search-table-cell">
						<span for="firstname"><fmt:message key="<%=WorkspaceResource.FIRST_NAME%>"/></span>
					</span>
					<span class="fui-workspace-search-table-cell">
						<input type="text" name="firstname" id="firstname">
					</span>
					<span class="fui-workspace-search-table-cell">
						<span for="lastname"><fmt:message key="<%=WorkspaceResource.LAST_NAME%>"/></span>
					</span>
					<span class="fui-workspace-search-table-cell">
						<input type="text" name="lastname" id="lastname">
					</span>
					<div class="fui-workspace-search-actions-container fui-workspace-search-table-cell">
						<button type="button" id="search"><fmt:message key="<%=WorkspaceResource.SEARCH%>"/></button>
						<button type="reset" id="reset"><fmt:message key="<%=WorkspaceResource.RESET%>"/></button>
					</div>
				</div>
			</form>
		</div>
	</div>
    <div class="fui-workspace-grid" id="fui-workspace-grid">
    </div>
</div>
<script type="text/javascript">
    fui.ready(function() {
        fui.query("#search").button()
                .click( function(event) {
            var params = {};
            params.userid = fui.byId("userid").value;
            params.firstname = fui.byId("firstname").value;
            params.lastname = fui.byId("lastname").value;
            fui.ui.manageusers.runSearch(params);          

        });
		fui.query("#newuser").button()
			.click( function(event) {
			fui.ui.grid.manageusers.edit(null);
		});
		fui.query("#reset").button();

        //Register grid
        fui.grid.register(fui.ui.type.MANAGEUSERS, fui.ui.grid.manageusers.get());

        //build grid
        var config = {
            type: fui.ui.type.MANAGEUSERS
        };
        fui.workspace.load(config);
    });
</script>
