<%@ page import="com.scalar.core.request.Request" %>
<%@ page import="com.scalar.core.request.RequestUtil" %>
<%@ page import="com.scalar.core.ScalarException" %>
<%@ page import="com.scalar.core.response.Response" %>
<%@ page import="com.scalar.core.response.ErrorResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <%
        Request req = RequestUtil.getRequest(request);
        Response resp = (Response)request.getAttribute(Response.RESPONSE_ATTRIBUTE);
    %>
    <h2>Unable to process the requested action method: <%=req.getMethod()%></h2>
    <pre>
        <%
            Throwable exception = null;
            if ( (null != resp) && ErrorResponse.class.isInstance(resp) ) {
                exception = ((ErrorResponse)resp).getThrowable();
            }
            if (exception == null) {
                exception = RequestUtil.getException (request);
            }
            if (exception instanceof ScalarException) {
                out.println (((ScalarException)exception).getMsgObject());
            } else {
                out.println (exception.getMessage());
            }
        %>
    </pre>
