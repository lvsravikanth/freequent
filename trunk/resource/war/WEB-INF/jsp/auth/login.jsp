<%@ page import="com.scalar.core.util.MsgObject" %>
<%@ page import="java.util.List" %>
<%@ page isELIgnored="true" %>
<%@ taglib prefix="freequent" uri="http://ui.vignette.com/vcm" %>


<!doctype html>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/corporate/style/content.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/jquery/css/start/jquery-ui-1.10.3.custom.css"/>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/jquery/js/jquery-1.9.1.js" ></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/jquery/js/jquery-ui-1.10.3.custom.js" ></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/util/notifications.js"> </script>
		<script type="text/javascript">
				$(function() {
					$("#site_signin_button" ).button();
					/*$('<div class="ui-widget"><div class="ui-state-highlight ui-corner-all" style="margin-top: 20px; padding: 0 .7em;"><p id="warning"></p></div></div>').appendTo('#error');
					$('#warning').text("Sample ui-state-highlight style.");
					$('#warning').text("Sample ui-state-highlight style2.");
					$('#warning').append('<span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>');  */
			})


		</script>
	</head>
	<body >
		<div id="login_globalContainer">

		</div>
			<div id="login_container" class="frq_main_container frq_main_content">
				<div id="site_signin_wrapper" class="frq_signin_container">
					<div id="site_signin_container" class="ui-overlay">
						<div class="ui-widget-overlay">
							<div id="site_signin_box" class="frq_signin_container_box ui-widget-shadow ui-corner-all">
								<h2 align="center" style="">
									Login
									<img src=""/>
								</h2>
								<div class="site_signin_table" >
								<form id="site_signin_form" action="<%=request.getContextPath()%>/auth/authenticate" method="post">
									<div class="site_signin_row">
										<span class="site_signin_cell">
											<label for="site_signin_username_input site_signin_label_cell">
												<strong>Username </strong>
											</label>
										</span>
										<span class="site_signin_cell">
											<input id="site_signin_username_input" size="24" style="padding-bottom: 10px;" type="text" name="username"/>
										</span>
									</div>
									<div class="site_signin_row">
											<span class="site_signin_cell">
												<label for="site_signin_password_input">
													<strong>Password </strong>
												</label>
											</span>
											<span class="site_signin_cell">
												<input id="site_signin_password_input" type="password" size="24" style="padding-bottom: 10px;" name="password"/>
											</span>
									</div>
									<div class="site_signin_row site_signin_align">
									<span class="site_signin_cell">
									</span>
									<span class="site_signin_cell site_signin_cell_padding site_signin_button_padding">
										<button id="site_signin_button" class="site_signin_button">Signin</button>
									</span>
									</div>
								</form>
							</div>
							<freequent:error />
						</div>
					</div>

				</div>
			<div id="login_footer"></div>
		</div>
	</body>
</html>