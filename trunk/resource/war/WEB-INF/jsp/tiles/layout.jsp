<%@ page import="com.scalar.core.ContextUtil" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String context = ContextUtil.getContextPath(request);
%>
<!DOCTYPE html>
<html class="fui-html">
<c:import url="/common/head.jsp" context="<%=context%>"/>
<body class="fui-body">
<div id="fui-loader">
    <div id="fui-header"><tiles:insertAttribute name="header"/></div>
    <div id="fui-container">
        <div id="fui-center" class="fui-column">
            <tiles:insertAttribute name="body"/>
        </div>
        <div id="fui-left" class="fui-column">
            <tiles:insertAttribute name="menu"/>
        </div>
        <div id="fui-right" class="fui-column"></div>
    </div>
    <div id="fui-footer-wrapper">
        <div id="fui-footer"><tiles:insertAttribute name="footer"/></div>
    </div>
</div>
</body>

</html>
