package com.scalar.core.request;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.scalar.freequent.auth.User;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 6, 2013
 * Time: 9:10:08 PM
 */
public abstract class AbstractRequest implements Request {
    protected static final Log logger = LogFactory.getLog(AbstractRequest.class);

    public User getActiveUser() {
        return User.getActiveUser();
    }

    public void setActiveUser(User user) {
        User.setActiveUser(user);
    }
}
