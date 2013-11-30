package com.scalar.freequent.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import com.scalar.freequent.auth.User;
import com.scalar.freequent.auth.Capability;
import com.scalar.freequent.common.*;
import com.scalar.freequent.service.*;
import com.scalar.freequent.service.users.UserService;
import com.scalar.freequent.util.EditorUtils;
import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.util.Constants;
import com.scalar.freequent.l10n.ServiceResource;
import com.scalar.freequent.l10n.ActionResource;
import com.scalar.freequent.dao.CapabilityInfoDAO;
import com.scalar.freequent.dao.CapabilityInfoRow;
import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.ScalarValidationException;
import com.scalar.core.jdbc.DAOFactory;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.service.ServiceFactory;
import com.scalar.core.response.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 30, 2013
 * Time: 8:53:01 PM
 */
public class ManageOrdersAction extends AbstractActionController {
	protected static final Log logger = LogFactory.getLog(ManageOrdersAction.class);

	public static final String ATTR_ITEM_DATA = "itemData";
	public static final String ATTR_GROUP_DATA_LIST = "groupDataList";
	public static final String ATTR_UNIT_DATA_LIST = "unitDataList";
	public static final String ATTR_CATEGORY_DATA_LIST = "categoryDataList";

	public void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		GroupDataService groupDataService = ServiceFactory.getService(GroupDataService.class, request);
		List<GroupData> groupDataList = groupDataService.findAll();

		CategoryDataService categoryDataService = ServiceFactory.getService(CategoryDataService.class, request);
		List<CategoryData> categoryDataList = categoryDataService.findAll();

		data.put(ATTR_GROUP_DATA_LIST, groupDataList);
		data.put(ATTR_CATEGORY_DATA_LIST, categoryDataList);

		data.put(Response.TEMPLATE_ATTRIBUTE, "item/manageorders");
	}

	public void runsearch(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		ItemDataService itemDataService = ServiceFactory.getService(ItemDataService.class, request);
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put(Item.PARAM_NAME, request.getParameter(Item.PARAM_NAME));
			params.put(Item.PARAM_GROUP, request.getParameter(Item.PARAM_GROUP));
			params.put(Item.PARAM_CATEGORY, request.getParameter(Item.PARAM_CATEGORY));

			List<Item> items = itemDataService.search(params);

			data.put(Response.ITEMS_ATTRIBUTE, convertToMap(items));
			data.put(Response.TOTAL_ATTRIBUTE, items.size() + "");
		} catch (ScalarServiceException e) {
			throw getActionException(e);
		}
	}

	/**
	 * action method for user edit.
	 *
	 * @param request
	 * @param command
	 * @param data
	 * @throws com.scalar.core.ScalarActionException
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
			try {
				Item item = itemDataService.findById(id);
				if (item == null) {
					throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_FIND_ITEM, id), null);
				}
				data.put(ATTR_ITEM_DATA, item);
			} catch (ScalarServiceException e) {
				throw getActionException(e);
			}
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

	/**
	 * Action method to save the item data.
	 *
	 * @param request
	 * @param command
	 * @param data
	 * @throws com.scalar.core.ScalarActionException
	 */
	public void save(Request request, Object command, Map<String, Object> data) throws ScalarActionException, ScalarValidationException {
		Item item = new Item();
		bindAndValidate(item, (HttpServletRequest)request.getWrappedObject());
		ItemDataService itemDataService = ServiceFactory.getService(ItemDataService.class, request);

		// check whether the request is from new editor
		String editorId = request.getParameter(EditorUtils.EDITOR_ID_ATTRIBUTE);
		if (logger.isDebugEnabled()) {
			logger.debug("item editor id: " + editorId);
		}
		if (EditorUtils.isNewEditorId(editorId)) {
			try {
				if (itemDataService.exists(item.getName())) {
					throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.ITEM_ALREADY_EXISTS, item.getName()), null);
				}
			} catch (ScalarServiceException e) {
				throw getActionException(e);
			}
			item.setId(null);
		} else {
			// check do we need to set the id?
			item.setId(editorId);
		}
		try {
			itemDataService.insertOrUpdate(item);
		} catch (ScalarServiceException e) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.UNABLE_TO_SAVE), e);
		}
		try {
			item = itemDataService.findById(item.getId());
		} catch (ScalarServiceException e) {
			throw getActionException(e);
		}
		data.put(Response.ITEM_ATTRIBUTE, item.toMap());
	}

	protected void validate(Object command, BindException errors) throws ScalarValidationException {
		super.validate(command, errors);
		Item item = (Item)command;

		if (StringUtil.isEmpty(item.getName())) {
			throw ScalarValidationException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.NAME_REQUIRED, ObjectType.ITEM), null);
		}
		if (StringUtil.isEmpty(item.getCode())) {
			throw ScalarValidationException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.ITEM_CODE_REQUIRED), null);
		}
	}

	private List<Map<String, Object>> convertToMap(List<Item> items) {
		List<Map<String, Object>> itemsList = new ArrayList<Map<String, Object>>();
		for (Item item : items) {
			Map<String, Object> itemMap = item.toMap();
			itemMap.put ("groupName", item.getGroupData()==null ? null : item.getGroupData().getName()); //hack for the pqgrid not supporting nested properties
			itemsList.add(itemMap);
		}

		return itemsList;
	}

	@Override
	 public Capability[] getRequiredCapabilities(Request request) {
		if ("load".equals(request.getMethod())) {
			return new Capability[]{Capability.ITEM_READ};
		} else if ("save".equals(request.getMethod())) {
			return new Capability[]{Capability.ITEM_WRITE};
		}

		return new Capability[0];
	}
}