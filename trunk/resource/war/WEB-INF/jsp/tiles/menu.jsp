<script type="text/javascript" src="<%=request.getContextPath()%>/script/jquery/js/jquery-1.9.1.js" ></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/script/jquery/js/jquery-ui-1.10.3.custom.js" ></script>
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
	<li>Admin</li>
	<ul>
		<li><a href="#">Manage Users</a></li>
		<li><a href="#">Manage Items</a></li>
		<li><a href="#">System Setup</a></li>
	</ul>
	<li>Entry</li>
	<ul>
		<li><a href="#">New Bill</a></li>
		<li><a href="#">Manage Bills</a></li>
	</ul>
	<li>Reports</li>
	<ul>
		<li><a href="#">User Report</a></li>
		<li><a href="#">Item Report</a></li>
		<li><a href="#">Bills Report</a></li>
	</ul>
</ul>
<SCRIPT>
$("#accordion > li").click(function(){

	if(false == $(this).next().is(':visible')) {
		$('#accordion > ul').slideUp(300);
	}
	$(this).next().slideToggle(300);
});

$('#accordion > ul:eq(1)').show();

</SCRIPT>