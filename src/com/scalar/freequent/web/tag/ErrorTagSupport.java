package com.scalar.freequent.web.tag;

import java.io.IOException;
import com.scalar.core.util.MsgObject;
import com.scalar.core.request.Request;
import com.scalar.freequent.web.util.ErrorInfoUtil;

import javax.servlet.jsp.JspException;
import java.util.*;

import org.json.JSONException;


public class ErrorTagSupport extends MessageTagSupport {

	/**
	 * Performs localization on the message <code>String</code> from the
	 * message catalog.
	 *
	 * @return <code>SKIP_BODY</code> - <code>int</code> indicating to skip the body of the tag.
	 *
	 * @throws javax.servlet.jsp.JspException thrown if errors occurs. It wraps any thrown exceptions into this one.
	 *
	 */
	public int doStartTag() throws JspException {

		// Finally, localize the MsgObject and return the localized string.
		try {

			pageContext.getOut().write("<div id='frq-error-msg'><script type=\"text/javascript\"> fui.util.errorinfoutil.showerrors("+getMessageJSON() +")</script></div>");

        } catch (JSONException jsonException) {
			throw new JspException(jsonException.getMessage());
		} catch (IOException ioException) {
			throw new JspException(ioException.getMessage());
		}

		// Skip the body of this tag
		return SKIP_BODY;
	}

	public List<MsgObject> getMessages() {
		Request  req = (Request)pageContext.getRequest().getAttribute(Request.REQUEST_ATTRIBUTE);
		List<MsgObject> errorsList = ErrorInfoUtil.getErrors(req);
		return errorsList;
	}


}

