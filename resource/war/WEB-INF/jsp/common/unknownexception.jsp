<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.core.request.RequestUtil" %>
<%@ page import="com.scalar.core.ScalarException" %>
<%@ page import="com.scalar.core.response.Response" %>
<%@ page import="com.scalar.core.response.ErrorResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title>Freequent - server error</title></head>
  <body>
    <h2>Something bad happened in the server!!</h2>
    <%
        Request req = RequestUtil.getRequest(request);
        Response resp = (Response)request.getAttribute(Response.RESPONSE_ATTRIBUTE);
    %>
    <pre>
        <%
            Throwable exception = null;
            if ( (null != resp) && ErrorResponse.class.isInstance(resp) ) {
                exception = ((ErrorResponse)resp).getThrowable();
            }
            if (exception == null) {
                exception = RequestUtil.getException (request);
            }
			if (exception == null) {
				exception = (Exception) request.getAttribute("javax.servlet.error.exception");
			}
            if (exception instanceof ScalarException) {
                out.println (((ScalarException)exception).getMsgObject());
            } else {
				if (exception == null) {
					out.println ("Unable to find the exception. check the logs.");
				} else {
                	out.println (exception.getMessage());
				}
            }
        %>

        <h3>Root Message:</h3>
        <%
            out.println (ScalarException.getRootLocalizedMessage(exception));
        %>
    </pre>
  </body>
</html>