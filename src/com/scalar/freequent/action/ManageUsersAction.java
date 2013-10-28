package com.scalar.freequent.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import com.scalar.freequent.service.users.UserService;
import com.scalar.freequent.auth.User;
import com.scalar.freequent.util.StringUtil;
import com.scalar.freequent.util.Constants;
import com.scalar.freequent.l10n.ServiceResource;
import com.scalar.freequent.l10n.ActionResource;
import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.ScalarValidationException;
import com.scalar.core.util.MsgObjectUtil;
import com.scalar.core.service.ServiceFactory;
import com.scalar.core.response.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * User: Sujan Kumar Suppala
 * Date: Oct 6, 2013
 * Time: 7:33:08 PM
 */
public class ManageUsersAction  extends AbstractActionController {
    protected static final Log logger = LogFactory.getLog(ManageUsersAction.class);

    /**
	 * Constant used to identify the total attribute.
	 */
	public static final String TOTAL_ATTRIBUTE = "total";

	/**
	 * Represents the user id value for to create new user.
	 */
	public static final String NEW_USER_ID = "fui-new-user";

	public static final String ATTR_USER_ID = "userId";

	public static final String ATTR_USER = "user";

    public void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException {

        data.put (Response.TEMPLATE_ATTRIBUTE, "user/manageusers");
    }

    public void runsearch(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
        UserService userService = ServiceFactory.getService(UserService.class, request);
        List<User> users = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		try {
			paramMap.put(User.USER_ID, request.getParameter(User.USER_ID));
			paramMap.put(User.FIRST_NAME, request.getParameter(User.FIRST_NAME));
			paramMap.put(User.LAST_NAME, request.getParameter(User.LAST_NAME));
			users = userService.getUsers(paramMap);
        } catch (ScalarServiceException e) {
            throw ScalarActionException.create(e.getMsgObject(), e);
        }
        data.put(Response.ITEMS_ATTRIBUTE, convertToMap(users));
        data.put(TOTAL_ATTRIBUTE, users.size()+"");
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
		String userId = request.getParameter(ATTR_USER_ID);
		if (StringUtil.isEmpty(userId)) {
			throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.USER_ID_REQUIRED), null);
		}
		if (NEW_USER_ID.equals(userId)) {
			data.put(ATTR_USER, new User());
		} else {
			// load user
			UserService userService = ServiceFactory.getService(UserService.class, request);
			try {
				User user = userService.findById(userId);
				if (user == null) {
					throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.UNABLE_TO_FIND_USER, userId), null);
				}
				data.put(ATTR_USER, user);
			} catch (ScalarServiceException e) {
				throw ScalarActionException.create(e.getMsgObject(), e);
			}
		}
		data.put(Response.TEMPLATE_ATTRIBUTE, "user/usertemplate"); 
	}

	/**
	 * Action method to save the user data.
	 *
	 * @param request
	 * @param command
	 * @param data
	 * @throws ScalarActionException
	 */
	public void save(Request request, Object command, Map<String, Object> data) throws ScalarActionException, ScalarValidationException {
		User user = new User();
		bindAndValidate(user, (HttpServletRequest)request.getWrappedObject());
		UserService userService = ServiceFactory.getService(UserService.class, request);

		// check whether the request is from new editor
		String editorId = request.getParameter(Constants.EDITOR_ID_ATTRIBUTE);
		if (editorId.startsWith(Constants.NEW_EDITOR_ID_VALUE)) {
			// creating a new user
			// check whether the new userId already exists
			try {
				if (userService.exists(user.getUserId())) {
					throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.USERID_ALREADY_EXISTS, user.getUserId()), null);
				} else {
					userService.insertOrUpdate(user, true, true);
				}
			} catch (ScalarServiceException e) {
				throw ScalarActionException.create(e.getMsgObject(), e);
			}
		} else {
			// update user
			try {
				if (!userService.exists(user.getUserId())) {
					throw ScalarActionException.create(MsgObjectUtil.getMsgObject(ActionResource.BASE_NAME, ActionResource.USERID_DOES_NOT_EXISTS, user.getUserId()), null);
				} else {
					User oldUser = userService.findById(user.getUserId());
					boolean passwordUpdated = !oldUser.getPassword().equals(user.getPassword());
					userService.insertOrUpdate(user, false, passwordUpdated);
				}
			} catch (ScalarServiceException e) {
				throw ScalarActionException.create(e.getMsgObject(), e);
			}
		}

		try {
			user = userService.findById(user.getUserId());
			data.put(ATTR_USER, user);
		} catch (ScalarServiceException e) {
			throw ScalarActionException.create(e.getMsgObject(), e);
		}
	}

	protected void validate(Object command, BindException errors) throws ScalarValidationException {
		super.validate(command, errors);
		User user = (User)command;

		if (StringUtil.isEmpty(user.getUserId())) {
			throw ScalarValidationException.create(MsgObjectUtil.getMsgObject(ServiceResource.BASE_NAME, ServiceResource.USER_ID_REQUIRED), null);
		}
	}

	private List<HashMap<String,Object>> convertToMap(List<User> users) {
        List<HashMap<String,Object>> items = new ArrayList<HashMap<String,Object>>();
        for (User user: users) {
            HashMap<String, Object> userMap = new HashMap<String,Object>();
            userMap.put(User.USER_ID, user.getUserId());
            userMap.put(User.FIRST_NAME, user.getFirstName());
            userMap.put(User.MIDDLE_NAME, user.getMiddleName());
            userMap.put(User.LAST_NAME, user.getLastName());
			userMap.put(User.DISABLED, user.isDisabled());
			userMap.put(User.EXPIRESON, user.getExpiresOn());
			items.add(userMap);
        }

        return items;
    }
}
