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
    <div class="fui-workspace-header"><fmt:message key="<%=WorkspaceResource.MANAGEUSERS_TITLE%>"/></div>
    <div class="fui-workspace-search-container">
        <form id="manageusers">
            <label for="userid"><fmt:message key="<%=WorkspaceResource.USERID%>"/></label>
            <input type="text" name="userid" id="userid">
            <label for="firstname"><fmt:message key="<%=WorkspaceResource.FIRST_NAME%>"/></label>
            <input type="text" name="firstname" id="firstname">
            <label for="lastname"><fmt:message key="<%=WorkspaceResource.LAST_NAME%>"/></label>
            <input type="text" name="lastname" id="lastname">

            <div class="fui-workspace-search-actions-container">
                <button type="button" id="search"><fmt:message key="<%=WorkspaceResource.SEARCH%>"/></button>
                <button type="reset" id="reset"><fmt:message key="<%=WorkspaceResource.RESET%>"/></button>
            </div>
        </form>
    </div>
    <div class="fui-workspace-search-results-container" id="fui-workspace-search-results-container">
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
        fui.query("#reset").button();
    });
</script>
