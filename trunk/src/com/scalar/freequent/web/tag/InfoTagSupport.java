package com.scalar.freequent.web.tag;

import java.io.IOException;
import com.scalar.core.util.MsgObject;
import com.scalar.core.request.Request;
import com.scalar.freequent.web.util.ErrorInfoUtil;

import javax.servlet.jsp.JspException;
import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: snakka
 * Date: Sep 15, 2013
 * Time: 7:12:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class InfoTagSupport extends MessageTagSupport {

	// private member variables
	private List<MsgObject> message = null;

	public List<MsgObject> getMessage() {
		return message;
	}

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

			pageContext.getOut().write("<div id='frq-info-msg'><script type=\"text/javascript\"> freequent.util.errorinfoutil.showinfos('frq-info-msg' , "+getMessageJSON() +")</script></div>");

        } catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}

		// Skip the body of this tag
		return SKIP_BODY;
	}

	public List<MsgObject> getMessages() {
		Request  req = (Request)pageContext.getRequest().getAttribute(Request.REQUEST_ATTRIBUTE);
		List<MsgObject> infoList = ErrorInfoUtil.getInfos(req);
		return infoList;
	}

}