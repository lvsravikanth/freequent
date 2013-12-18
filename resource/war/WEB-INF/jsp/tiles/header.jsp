<%@ page import="com.scalar.core.ContextUtil" %>
<%@ page import="com.scalar.core.request.Request" %>
<%
    String context = ContextUtil.getContextPath(request);
    Request fRequest = (Request) request.getAttribute(Request.REQUEST_ATTRIBUTE);
%>
<div class="fui-header-container">
    <div class="fui-header-user-menu">
        <div class="fui-header-user-menu-label">Welcome <%=fRequest.getActiveUser().getFirstName()%>  <%=fRequest.getActiveUser().getLastName()%></div>
        <div class="fui-header-signoff-button">
            <a href="<%=ContextUtil.getContextPath(request)%>/auth/logout"><span class="ui-icon ui-icon-power fui-header-singoff-icon">Sign Out</span></a>
        </div>
    </div>
</div>
