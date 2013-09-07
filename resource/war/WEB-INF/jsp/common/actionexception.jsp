<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.core.request.RequestUtil" %>
<%@ page import="com.scalar.core.ScalarException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title>Simple jsp page</title></head>
  <body>
    <%
        Request req = RequestUtil.getRequest(request);
    %>
    <h2>Unable to process the requested action method: <%=req.getMethod()%></h2>
    <pre>
        <%
            Throwable exception = RequestUtil.getException (request);
            if (exception instanceof ScalarException) {
                out.println (((ScalarException)exception).getMsgObject());
            } else {
                out.println (exception.getMessage());
            }
        %>
    </pre>
  </body>
</html>