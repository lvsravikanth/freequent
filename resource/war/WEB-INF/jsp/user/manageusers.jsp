<%@ page import="com.scalar.core.ContextUtil" %>
<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.freequent.l10n.WorkspaceResource" %>
<%@ page import="com.scalar.freequent.action.ManageUsersAction" %>
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
	<div class="fui-workspace-search-header ui-widget-header ui-corner-top"><fmt:message key="<%=WorkspaceResource.MANAGEUSERS_TITLE%>"/></div>
	<form id="manageusers">
			<div class="fui-workspace-search-container">
				<div >
					<span class="fui-workspace-search-row">
						<div class="fui-workspace-search-container-column" style="padding-right:24px">
							<label for="userid"><fmt:message key="<%=WorkspaceResource.USERID%>"/></label>
						</div>
						<div class="fui-workspace-search-container-column">
							<input type="text" name="userid" id="userid" />
						</div>
					</span>
					<span class="fui-workspace-search-row">

						<div class="fui-workspace-search-container-column">
							<label for="firstname"><fmt:message key="<%=WorkspaceResource.FIRST_NAME%>"/></label>
						</div>
						<div class="fui-workspace-search-container-column">
							<input type="text" name="firstname" id="firstname" />
						</div>
					</span>
					<span class="fui-workspace-search-row">
						<div class="fui-workspace-search-container-column">
							<label for="lastname"><fmt:message key="<%=WorkspaceResource.LAST_NAME%>"/></label>
						</div>
						<div class="fui-workspace-search-container-column">
							<input type="text" name="lastname" id="lastname">
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
			<button type="button" id="newuser"><fmt:message key="<%=WorkspaceResource.NEWUSER%>"/></button>
		</div>
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
			fui.ui.manageusers.edit();
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
	fui.ready(function(){
		// by default load the data into the grid when page loads
		fui.query("#search").button().click();
	});
</script>
