<%@ page import="com.scalar.core.menu.Menu" %>
<%@ page import="java.util.List" %>
<%@ page import="com.scalar.core.menu.MenuFactory" %>
<%@ page import="com.scalar.core.request.RequestUtil" %>
<%@ page import="com.scalar.freequent.util.StringUtil" %>
<STYLE>
body, input{
	font-family: Calibri, Arial;
}
#accordion {
	list-style: none;
	padding: 0 0 0 0;
	width: 170px;
}
#accordion li{
	display: block;
	background-color: #FF9927;
	font-weight: bold;
	margin: 1px;
	cursor: pointer;
	padding: 5 5 5 7px;
	list-style: circle;
	-moz-border-radius: 10px;
	-webkit-border-radius: 10px;
	border-radius: 10px;
}
#accordion ul {
	list-style: none;
	padding: 0 0 0 0;
	display: none;
}
#accordion ul li{
	font-weight: normal;
	cursor: auto;
	background-color: #fff;
	padding: 0 0 0 7px;
}
#accordion a {
	text-decoration: none;
}
#accordion a:hover {
	text-decoration: underline;
}
</STYLE>
<ul id="accordion">
    <%
    List<Menu> menuList = MenuFactory.getMenu(RequestUtil.getRequest(request));
    for (Menu menu: menuList) {
%>
	<li>
        <%
            String menuHtml = "";
            if (StringUtil.isEmpty(menu.getLink())) {
                menuHtml = menu.getName();
            } else {
                menuHtml = "<a href='" + request.getContextPath() + "/" + menu.getLink()+"'>" + menu.getName()+"</a>";
            }
        %>
            <%=menuHtml%>
    </li>
    <ul>
    <%
        List<Menu> menuItems = menu.getMenuItems();
        if (menuItems != null) {
        for (Menu menuItem: menuItems) {
    %>

		<li><a href="#"><%=menuItem.getName()%></a></li>
     <%}
     }%>

	</ul>
    <%
        }
    %>
</ul>

<SCRIPT>
fui.query("#accordion > li").click(function(){

	if(false == fui.query(this).next().is(':visible')) {
		fui.query('#accordion > ul').slideUp(300);
	}
	fui.query(this).next().slideToggle(300);
});

fui.query('#accordion > ul:eq(1)').show();

</SCRIPT>