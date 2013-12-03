<%@ page import="com.scalar.core.menu.Menu" %>
<%@ page import="java.util.List" %>
<%@ page import="com.scalar.core.menu.MenuFactory" %>
<%@ page import="com.scalar.core.request.RequestUtil" %>
<%@ page import="com.scalar.freequent.util.StringUtil" %>
<%@ page import="com.scalar.core.ContextUtil" %>
<STYLE>
#accordion {
	list-style: none;
	padding: 0 0 0 0;
	width: 140px;
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
        String context = ContextUtil.getContextPath(request);
    List<Menu> menuList = MenuFactory.getMenu(RequestUtil.getRequest(request));
    for (Menu menu: menuList) {
        out.println(prepareMenu(menu,context));
    }
%>
</ul>

<%!

    private String prepareMenu(Menu menu, String context) {
        String menuHtml = "<li>";
        if (menu.isEnabled() && !StringUtil.isEmpty(menu.getLink())) {
            menuHtml += "<a href='" + context + "/" + menu.getLink()+"'>" + menu.getName()+"</a>";
        } else {
            menuHtml += menu.getName();
        }
        menuHtml += "</li>";
        List<Menu> menuItems = menu.getMenuItems();
        if (menuItems != null) {
            menuHtml  += "<ul>";
            for (Menu menuItem: menuItems) {
                menuHtml += prepareMenu(menuItem, context);
            }
            menuHtml += "</ul>";
        }
        return menuHtml;
    }
%>

<SCRIPT>
fui.query("#accordion > li").click(function(){

	if(false == fui.query(this).next().is(':visible')) {
		fui.query('#accordion > ul').slideUp(300);
	}
	fui.query(this).next().slideToggle(300);
});

//fui.query('#accordion > ul:eq(1)').show();

</SCRIPT>