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
 * Date: Nov 5, 2013
 * Time: 8:53:01 PM
 */
public class ManageGroupsAction extends AbstractActionController {
	protected static final Log logger = LogFactory.getLog(ManageGroupsAction.class);

    public static final String ATTR_GROUP_DATA = "groupData";

	public void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		data.put(Response.TEMPLATE_ATTRIBUTE, "group/managegroups");
	}

	public void runsearch(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		GroupDataService groupDataService = ServiceFactory.getService(GroupDataService.class, request);
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(GroupData.PARAM_NAME, request.getParameter(GroupData.PARAM_NAME));

			List<GroupData> groupData = groupDataService.search(params);

			data.put(Response.ITEMS_ATTRIBUTE, groupData);
			data.put(Response.TOTAL_ATTRIBUTE, groupData.size() + "");
		} catch (ScalarServiceException e) {
			throw getActionException(e);
		}
	}

	/**
	 * Action method for Group edit.
	 *
	 * @param request
	 * @param command
	 * @param data
	 * @throws ScalarActionException
	 */
	public void load(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
		String groupId = request.getParameter(EditorUtils.EDITOR_ID_ATTRIBUTE);
		if (StringUtil.isEmpty(groupId)) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.GROUP_REQUIRED), null);
		}
		if (EditorUtils.isNewEditorId(groupId)) {
			data.put(ATTR_GROUP_DATA, new GroupData());
		} else {
			// load group
			GroupDataService groupDataService = ServiceFactory.getService(GroupDataService.class, request);
			GroupData groupData = groupDataService.findById(groupId);
			if (groupData == null) {
				throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_FIND_GROUP, groupId), null);
			}
			data.put(ATTR_GROUP_DATA, groupData);
		}

		data.put(Response.TEMPLATE_ATTRIBUTE, "group/grouptemplate");
	}

	/**
	 * Action method to save the group data.
	 *
	 * @param request
	 * @param command
	 * @param data
	 * @throws com.scalar.core.ScalarActionException
	 */
	public void save(Request request, Object command, Map<String, Object> data) throws ScalarActionException, ScalarValidationException {
		GroupData groupData = new GroupData();
		bindAndValidate(groupData, (HttpServletRequest)request.getWrappedObject());
		GroupDataService groupDataService = ServiceFactory.getService(GroupDataService.class, request);

		// check whether the request is from new editor
		String editorId = request.getParameter(EditorUtils.EDITOR_ID_ATTRIBUTE);
		if (logger.isDebugEnabled()) {
			logger.debug("group editor id: " + editorId);
		}
		if (EditorUtils.isNewEditorId(editorId)) {
			try {
				if (groupDataService.exists(groupData.getName())) {
					throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.GROUP_ALREADY_EXISTS, groupData.getName()), null);
				}
			} catch (ScalarServiceException e) {
				throw getActionException(e);
			}
			groupData.setId(null);
		} else {
			// check do we need to set the id?
			groupData.setId(editorId);
		}
		try {
			groupDataService.insertOrUpdate(groupData);
		} catch (Exception e) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.UNABLE_TO_SAVE), e);
		}
		try {
			groupData = groupDataService.findByName(groupData.getName());
		} catch (Exception e) {
			throw getActionException(e);
		}
		data.put(Response.ITEM_ATTRIBUTE, groupData);
	}


	protected void validate(Object command, BindException errors) throws ScalarValidationException {
		super.validate(command, errors);
		GroupData groupData = (GroupData)command;

		if (StringUtil.isEmpty(groupData.getName())) {
			throw ScalarValidationException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.NAME_REQUIRED, ObjectType.GROUP), null);
		}
	}

	@Override
	 public Capability[] getRequiredCapabilities(Request request) {
		if ("load".equals(request.getMethod())) {
			return new Capability[]{Capability.GROUP_READ};
		} else if ("save".equals(request.getMethod())) {
			return new Capability[]{Capability.GROUP_WRITE};
		}

		return new Capability[0];
	}
}