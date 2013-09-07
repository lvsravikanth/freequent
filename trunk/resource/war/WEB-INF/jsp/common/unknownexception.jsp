<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.core.request.RequestUtil" %>
<%@ page import="com.scalar.core.ScalarException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title>Freequent - server error</title></head>
  <body>
    <h2>Something bad happened in the server!!</h2>
    <pre>
        <%
            Throwable exception = RequestUtil.getException (request);
        %>
        <h4><%=exception.getMessage()%></h4>

        <h3>Root Message:</h3>
        <h4><%=ScalarException.getLocalizedMessage(exception)%></h4>
    </pre>
  </body>
</html>