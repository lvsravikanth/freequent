package com.scalar.freequent.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;
import com.scalar.core.response.Response;

import java.util.Map;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 5, 2013
 * Time: 8:53:01 PM
 */
public class ManageItemsAction extends AbstractActionController{
	protected static final Log logger = LogFactory.getLog(ManageItemsAction.class);

	public void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		 data.put (Response.TEMPLATE_ATTRIBUTE, "item/manageitems");
	}
}
