package com.scalar.freequent.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import com.scalar.freequent.auth.Capability;
import com.scalar.freequent.common.*;
import com.scalar.freequent.service.*;
import com.scalar.freequent.util.EditorUtils;
import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.util.Constants;
import com.scalar.freequent.l10n.ServiceResource;
import com.scalar.freequent.l10n.ActionResource;
import com.scalar.freequent.dao.CapabilityInfoDAO;
import com.scalar.freequent.dao.CapabilityInfoRow;
import com.scalar.freequent.dao.CategoryAssocDataDAO;
import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.ScalarValidationException;
import com.scalar.core.jdbc.DAOFactory;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.util.GUID;
import com.scalar.core.service.ServiceFactory;
import com.scalar.core.response.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * User: Sujan Kumar Suppala
 * Date: Nov 5, 2013
 * Time: 8:53:01 PM
 */
public class ManageCategoriesAction extends AbstractActionController {
	protected static final Log logger = LogFactory.getLog(ManageCategoriesAction.class);

    public static final String ATTR_CATEGORY_DATA = "categoryData";

	public void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		data.put(Response.TEMPLATE_ATTRIBUTE, "category/managecategories");
	}

	public void runsearch(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		CategoryDataService categoryDataService = ServiceFactory.getService(CategoryDataService.class, request);
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(CategoryData.PARAM_NAME, request.getParameter(CategoryData.PARAM_NAME));

			List<CategoryData> categoryData = categoryDataService.search(params);

			data.put(Response.ITEMS_ATTRIBUTE, categoryData);
			data.put(Response.TOTAL_ATTRIBUTE, categoryData.size() + "");
		} catch (ScalarServiceException e) {
			throw getActionException(e);
		}
	}

	/**
	 * Action method for Category edit.
	 *
	 * @param request
	 * @param command
	 * @param data
	 * @throws com.scalar.core.ScalarActionException
	 */
	public void load(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		String categoryId = request.getParameter(EditorUtils.EDITOR_ID_ATTRIBUTE);
		if (StringUtil.isEmpty(categoryId)) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.CATEGORY_REQUIRED), null);
		}
		if (EditorUtils.isNewEditorId(categoryId)) {
			data.put(ATTR_CATEGORY_DATA, new CategoryData());
		} else {
			// load category
			CategoryDataService categoryDataService = ServiceFactory.getService(CategoryDataService.class, request);
			CategoryData categoryData = categoryDataService.findById(categoryId);
			if (categoryData == null) {
				throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_FIND_CATEGORY, categoryId), null);
			}
			data.put(ATTR_CATEGORY_DATA, categoryData);
		}

		data.put(Response.TEMPLATE_ATTRIBUTE, "category/categorytemplate");
	}

	/**
	 * Action method to save the category data.
	 *
	 * @param request
	 * @param command
	 * @param data
	 * @throws com.scalar.core.ScalarActionException
	 */
	public void save(Request request, Object command, Map<String, Object> data) throws ScalarActionException, ScalarValidationException {
		CategoryData categoryData = new CategoryData();
		bindAndValidate(categoryData, (HttpServletRequest)request.getWrappedObject());
		CategoryDataService categoryDataService = ServiceFactory.getService(CategoryDataService.class, request);

		// check whether the request is from new editor
		String editorId = request.getParameter(EditorUtils.EDITOR_ID_ATTRIBUTE);
		if (logger.isDebugEnabled()) {
			logger.debug("category editor id: " + editorId);
		}
		if (EditorUtils.isNewEditorId(editorId)) {
			try {
				if (categoryDataService.exists(categoryData.getName())) {
					throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.CATEGORY_ALREADY_EXISTS, categoryData.getName()), null);
				}
			} catch (ScalarServiceException e) {
				throw getActionException(e);
			}
			categoryData.setId(null);
		} else {
			// check do we need to set the id?
			categoryData.setId(editorId);
		}
		try {
			categoryDataService.insertOrUpdate(categoryData);
		} catch (Exception e) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.UNABLE_TO_SAVE), e);
		}
		try {
			categoryData = categoryDataService.findByName(categoryData.getName());
		} catch (Exception e) {
			throw getActionException(e);
		}
		data.put(Response.ITEM_ATTRIBUTE, categoryData);
	}

    public void delete(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
        Map<String, String> params = new HashMap<String, String>();
        boolean isDeleteed = false;

		CategoryDataService categoryDataService = ServiceFactory.getService(CategoryDataService.class, request);
        ItemDataService itemDataService = ServiceFactory.getService(ItemDataService.class, request);

        String id = request.getParameter(Constants.ITEM_ID_ATTRIBUTE);
        String categoryName = request.getParameter(Item.PARAM_NAME);    
        params.put(Item.PARAM_CATEGORY, id);

        if (logger.isDebugEnabled()) {
			logger.debug("Deleted category id: " + id);
            logger.debug("Deleted category name: " + categoryName);
		}

        if (StringUtil.isEmpty(id)) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.PARAM_REQUIRED, id), null);
		}

		if (!GUID.isValid(id)) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.INVALID_PARAM_VALUE, categoryName, id), null);
		}

        List<Item> items= itemDataService.search(params);
        if (items.size() > 0) {
            throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.UNABLE_TO_DELETE_ASSOCIATED_GROUP, ObjectType.CATEGORY, categoryName), null);
        } else {
            isDeleteed = categoryDataService.remove(id);
        }
        Map<String, Boolean> succes = new HashMap<String, Boolean>();
        succes.put("success", isDeleteed);
        data.put(Response.ITEMS_ATTRIBUTE, succes);
	}

	protected void validate(Object command, BindException errors) throws ScalarValidationException {
		super.validate(command, errors);
		CategoryData categoryData = (CategoryData)command;

		if (StringUtil.isEmpty(categoryData.getName())) {
			throw ScalarValidationException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.NAME_REQUIRED, ObjectType.CATEGORY), null);
		}
	}

	@Override
	 public Capability[] getRequiredCapabilities(Request request) {
		if ("load".equals(request.getMethod())) {
			return new Capability[]{Capability.CATEGORY_READ};
		} else if ("save".equals(request.getMethod())) {
			return new Capability[]{Capability.CATEGORY_WRITE};
		} else if ("delete".equals(request.getMethod())) {
            return new Capability[]{Capability.CATEGORY_DELETE};
        }

		return new Capability[0];
	}
}