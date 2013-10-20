package com.scalar.freequent.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.freequent.web.spring.controller.AbstractActionController;
import com.scalar.freequent.service.users.UserService;
import com.scalar.freequent.auth.User;
import com.scalar.core.request.Request;
import com.scalar.core.ScalarActionException;
import com.scalar.core.ScalarServiceException;
import com.scalar.core.service.ServiceFactory;
import com.scalar.core.response.Response;

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

    public void defaultProcess(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
        data.put (Response.TEMPLATE_ATTRIBUTE, "user/manageusers");
    }

    public void runsearch(Request request, Object command, Map<String, Object> data) throws ScalarActionException {
        UserService userService = ServiceFactory.getService(UserService.class, request);
        List<User> users = null;
        try {
			users = userService.manageUserSearch();
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
		data.put(Response.TEMPLATE_ATTRIBUTE, "user/usertemplate"); 
	}

    private List<HashMap<String,Object>> convertToMap(List<User> users) {
        List<HashMap<String,Object>> items = new ArrayList<HashMap<String,Object>>();
        for (User user: users) {
            HashMap<String, Object> usermap = new HashMap<String,Object>();
            usermap.put("userid", user.getUserId());
            usermap.put("firstname", user.getFirstName());
            usermap.put("middlename", user.getMiddleName());
            usermap.put("lastname", user.getLastName());

            items.add(usermap);
        }

        return items;
    }
}
