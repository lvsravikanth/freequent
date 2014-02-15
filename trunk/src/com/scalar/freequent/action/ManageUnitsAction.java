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


public class ManageUnitsAction extends AbstractActionController {
	protected static final Log logger = LogFactory.getLog(ManageUnitsAction.class);

    public static final String ATTR_UNIT_DATA = "unitData";

	public void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		data.put(Response.TEMPLATE_ATTRIBUTE, "unit/manageunits");
	}

	public void runsearch(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		UnitDataService unitDataService = ServiceFactory.getService(UnitDataService.class, request);
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(UnitData.PARAM_NAME, request.getParameter(UnitData.PARAM_NAME));

			List<UnitData> unitdata = unitDataService.search(params);

			data.put(Response.ITEMS_ATTRIBUTE, unitdata);
			data.put(Response.TOTAL_ATTRIBUTE, unitdata.size() + "");
		} catch (ScalarServiceException e) {
			throw getActionException(e);
		}
	}

	/**
	 * Action method for Unit edit.
	 *
	 * @param request
	 * @param command
	 * @param data
	 * @throws com.scalar.core.ScalarActionException
	 */
	public void load(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		String unitId = request.getParameter(EditorUtils.EDITOR_ID_ATTRIBUTE);
		if (StringUtil.isEmpty(unitId)) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNIT_REQUIRED), null);
		}
		if (EditorUtils.isNewEditorId(unitId)) {
			data.put(ATTR_UNIT_DATA, new UnitData());
		} else {
			// load unit
			UnitDataService unitDataService = ServiceFactory.getService(UnitDataService.class, request);
			UnitData unitData = unitDataService.findById(unitId);
			if (unitData == null) {
				throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_FIND_UNIT, unitId), null);
			}
			data.put(ATTR_UNIT_DATA, unitData);
		}

		data.put(Response.TEMPLATE_ATTRIBUTE, "unit/unittemplate");
	}

	/**
	 * Action method to save the unit data.
	 *
	 * @param request
	 * @param command
	 * @param data
	 * @throws com.scalar.core.ScalarActionException
	 */
	public void save(Request request, Object command, Map<String, Object> data) throws ScalarActionException, ScalarValidationException {
		UnitData unitData = new UnitData();
		bindAndValidate(unitData, (HttpServletRequest)request.getWrappedObject());
		UnitDataService unitDataService = ServiceFactory.getService(UnitDataService.class, request);

		// check whether the request is from new editor
		String editorId = request.getParameter(EditorUtils.EDITOR_ID_ATTRIBUTE);
		if (logger.isDebugEnabled()) {
			logger.debug("Unit editor id: " + editorId);
		}
		if (EditorUtils.isNewEditorId(editorId)) {
			try {
				if (unitDataService.exists(unitData.getName())) {
					throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.UNIT_ALREADY_EXISTS, unitData.getName()), null);
				}
			} catch (ScalarServiceException e) {
				throw getActionException(e);
			}
			unitData.setId(null);
		} else {
			// check do we need to set the id?
			unitData.setId(editorId);
		}
		try {
			unitDataService.insertOrUpdate(unitData);
		} catch (Exception e) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.UNABLE_TO_SAVE), e);
		}
		try {
			unitData = unitDataService.findByName(unitData.getName());
		} catch (Exception e) {
			throw getActionException(e);
		}
		data.put(Response.ITEM_ATTRIBUTE, unitData);
	}

    public void delete(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
        Map<String, String> params = new HashMap<String, String>();
        boolean isDeleteed = false;

		UnitDataService unitDataService = ServiceFactory.getService(UnitDataService.class, request);
        ItemDataService itemDataService = ServiceFactory.getService(ItemDataService.class, request);

        String id = request.getParameter(Constants.ITEM_ID_ATTRIBUTE);
        String unitName = request.getParameter(Item.PARAM_NAME);
        params.put(Item.PARAM_UNIT, id);

        if (logger.isDebugEnabled()) {
			logger.debug("Deleted unit id: " + id);
            logger.debug("Deleted unit name: " + unitName);
		}

        if (StringUtil.isEmpty(id)) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.PARAM_REQUIRED, id), null);
		}

		if (!GUID.isValid(id)) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.INVALID_PARAM_VALUE, unitName, id), null);
		}

        List<Item> items= itemDataService.search(params);
        if (items.size() > 0) {
            throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.UNABLE_TO_DELETE_ASSOCIATED_GROUP, ObjectType.UNIT, unitName), null);
        } else {
            isDeleteed = unitDataService.remove(id);
        }
        Map<String, Boolean> succes = new HashMap<String, Boolean>();
        succes.put("success", isDeleteed);
        data.put(Response.ITEMS_ATTRIBUTE, succes);
	}

	protected void validate(Object command, BindException errors) throws ScalarValidationException {
		super.validate(command, errors);
		UnitData unitData = (UnitData)command;

		if (StringUtil.isEmpty(unitData.getName())) {
			throw ScalarValidationException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.NAME_REQUIRED, ObjectType.UNIT), null);
		}
	}

	@Override
	 public Capability[] getRequiredCapabilities(Request request) {
		if ("load".equals(request.getMethod())) {
			return new Capability[]{Capability.UNIT_READ};
		} else if ("save".equals(request.getMethod())) {
			return new Capability[]{Capability.UNIT_WRITE};
		} else if ("delete".equals(request.getMethod())) {
            return new Capability[]{Capability.UNIT_DELETE};
        }

		return new Capability[0];
	}
}