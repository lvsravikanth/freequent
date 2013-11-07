package com.scalar.freequent.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import com.scalar.freequent.auth.User;
import com.scalar.freequent.common.Item;
import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;
import com.scalar.core.response.Response;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 5, 2013
 * Time: 8:53:01 PM
 */
public class ManageItemsAction extends AbstractActionController {
	protected static final Log logger = LogFactory.getLog(ManageItemsAction.class);

	public void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		data.put(Response.TEMPLATE_ATTRIBUTE, "item/manageitems");
	}

	public void runsearch(Request request, Object command, Map<String, Object> data) throws ScalarActionException {

		data.put(Response.ITEMS_ATTRIBUTE, convertToMap((List<Item>)null));
		data.put(Response.TOTAL_ATTRIBUTE, "");
	}

	private List<HashMap<String, Object>> convertToMap(List<Item> items) {
		List<HashMap<String, Object>> itemsList = new ArrayList<HashMap<String, Object>>();
		for (Item item : items) {
			HashMap<String, Object> itemMap = convertToMap(item);
			itemsList.add(itemMap);
		}

		return itemsList;
	}

	private HashMap<String, Object> convertToMap(Item item) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		return resultMap;
	}
}
