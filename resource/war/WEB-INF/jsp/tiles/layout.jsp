<%@ page import="com.scalar.core.ContextUtil" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String context = ContextUtil.getContextPath(request);
%>
<!DOCTYPE html>
<html style="height:99.9%">
<head class="freequent-html">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Freequent</title>

<style>
.freequent-html {
	height: 100%;
}

.freequent-body {
	min-height: 100%;
	position: relative;
}

.container {
	min-height: 99%;
	min-width: 99%;
	height: 99%;
	background: #ECECEC;

}

.header{
	min-width: 1%;
	min-height: 1%;
	width: 100%;
	height: 20%;
	border: 1px solid red;
}

.content{
	min-width: 1%;
	min-height: 7%;
	width: 100%;
	height: 70%;
	position: relative;
	border: 1px solid blue;
}

.west-menu{
	min-width: 1%;
	min-height: 1%;
	width: 15%;
	height: 90%;
	float: left;
	border: 1px solid black;

}

.west-center{
	min-width: 1%;
	min-height: 1%;
	width: 98%;
	height: 90%;
	border: 1px solid violet;
}

.footer{
	min-width: 1%;
	min-height: 1%;
	width: 100%;
	height: 10%;
	border: 1px solid green;
}

</style>

<c:import url="/common/head.jsp" context="<%=context%>"/>

<body class="freequent-body">

<div class="container" border="1" cellpadding="2" cellspacing="2">
    <div class="header">
        <tiles:insertAttribute name="header" />
		<!-- div style="height:78px;">
			<div class="topLogo">Header</div>
		</div -->
    </div>
    <div class="content">
        <div class="west-menu">
			<tiles:insertAttribute name="menu" />
		</div>

        <div class="west-center">
			<tiles:insertAttribute name="body" />
		</div>

		<div style="clear:both"></div>
    </div>


    <div class="footer">
		<tiles:insertAttribute name="footer" />
    </div>
</div>

</body>

</html>