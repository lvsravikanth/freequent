<%@ page import="com.scalar.core.ContextUtil" %>
<%@ page isELIgnored="true" %>
<%@ taglib prefix="freequent" uri="http://www.freequent.com/freequent" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String context = ContextUtil.getContextPath(request);
%>
<!doctype html>
<html class="fui-html">
	<c:import url="/common/head.jsp" context="<%=context%>"/>
	<body class="fui-body">
        <script type="text/javascript">
				fui.ready(function() {
					fui.query("#site_signin_button" ).button();
				});
		</script>
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
							<freequent:errors />
						</div>
					</div>

				</div>
			<div id="login_footer"></div>
		</div>
	</body>
</html>