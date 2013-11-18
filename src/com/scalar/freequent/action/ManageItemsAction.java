package com.scalar.freequent.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import com.scalar.freequent.auth.User;
import com.scalar.freequent.auth.Capability;
import com.scalar.freequent.common.Item;
import com.scalar.freequent.common.GroupData;
import com.scalar.freequent.common.UnitData;
import com.scalar.freequent.common.CategoryData;
import com.scalar.freequent.service.ItemDataService;
import com.scalar.freequent.service.GroupDataService;
import com.scalar.freequent.service.UnitDataService;
import com.scalar.freequent.service.CategoryDataService;
import com.scalar.freequent.service.users.UserService;
import com.scalar.freequent.util.EditorUtils;
import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.util.Constants;
import com.scalar.freequent.l10n.ServiceResource;
import com.scalar.freequent.dao.CapabilityInfoDAO;
import com.scalar.freequent.dao.CapabilityInfoRow;
import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.jdbc.DAOFactory;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.service.ServiceFactory;
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

	public static final String ATTR_ITEM_DATA = "itemData";
	public static final String ATTR_GROUP_DATA_LIST = "groupDataList";
	public static final String ATTR_UNIT_DATA_LIST = "unitDataList";
	public static final String ATTR_CATEGORY_DATA_LIST = "categoryDataList";

	public void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		data.put(Response.TEMPLATE_ATTRIBUTE, "item/manageitems");
	}

	public void runsearch(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		ItemDataService itemDataService = ServiceFactory.getService(ItemDataService.class, request);
		List<Item> items = itemDataService.findAll();
		data.put(Response.ITEMS_ATTRIBUTE, convertToMap(items));
		data.put(Response.TOTAL_ATTRIBUTE, items.size() + "");
	}

	/**
	 * action method for user edit.
	 *
	 * @param request
	 * @param command
	 * @param data
	 * @throws ScalarActionException
	 */
	public void load(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		String id = request.getParameter(EditorUtils.EDITOR_ID_ATTRIBUTE);
		if (StringUtil.isEmpty(id)) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.ID_REQUIRED), null);
		}
		if (EditorUtils.isNewEditorId(id)) {
			data.put(ATTR_ITEM_DATA, new Item());
		} else {
			// load item
			ItemDataService itemDataService = ServiceFactory.getService(ItemDataService.class, request);
			Item item = itemDataService.findById(id);
			if (item == null) {
				throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_FIND_ITEM, id), null);
			}
			data.put(ATTR_ITEM_DATA, item);
		}

		GroupDataService groupDataService = ServiceFactory.getService(GroupDataService.class, request);
		List<GroupData> groupDataList = groupDataService.findAll();

		UnitDataService unitDataService = ServiceFactory.getService(UnitDataService.class, request);
		List<UnitData> unitDataList = unitDataService.findAll();

		CategoryDataService categoryDataService = ServiceFactory.getService(CategoryDataService.class, request);
		List<CategoryData> categoryDataList = categoryDataService.findAll();

		data.put(ATTR_GROUP_DATA_LIST, groupDataList);
		data.put(ATTR_UNIT_DATA_LIST, unitDataList);
		data.put(ATTR_CATEGORY_DATA_LIST, categoryDataList);

		data.put(Response.TEMPLATE_ATTRIBUTE, "item/itemtemplate");
	}

	private List<Map<String, Object>> convertToMap(List<Item> items) {
		List<Map<String, Object>> itemsList = new ArrayList<Map<String, Object>>();
		for (Item item : items) {
			Map<String, Object> itemMap = item.toMap();
			itemsList.add(itemMap);
		}

		return itemsList;
	}
}
