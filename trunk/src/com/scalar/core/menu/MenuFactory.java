package com.scalar.core.menu;

import com.scalar.core.factory.AbstractFactory;
import com.scalar.core.request.Request;
import com.scalar.freequent.auth.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.JAXBException;
import java.util.*;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 4, 2013
 * Time: 7:11:19 PM
 */
public class MenuFactory extends AbstractFactory {
    protected static final Log logger = LogFactory.getLog(MenuFactory.class);
    /**
     * A <code>Map</code> of <code>Map</code> objects which contain <code>Logic</code> objects stored by prefix.
     * This map uses {@link Request} objects as the key.
     */
    private static Map<String, List<Menu>> cacheMap = new Hashtable<String, List<Menu>>();

    public static List<Menu> getMenu(Request request) throws JAXBException {
        if (logger.isDebugEnabled()) {
            logger.debug("Finding Menu for request: " + request);
        }
        User user = request.getActiveUser();
        List<Menu> userMenu = null;
        if (user != null) {
            userMenu = cacheMap.get(user.getUserId());
            if (userMenu == null) {
                userMenu = getMenuBuilder().getMenuListForUser(user);
                cacheMap.put(user.getUserId(), userMenu);
            }
        }
        return userMenu==null ? new LinkedList<Menu>() : userMenu;
    }

    protected static MenuBuilder getMenuBuilder() {
        MenuBuilder bean = getFactory().getBean("menuBuilder", MenuBuilder.class);
        if (null == bean) {
            if (logger.isDebugEnabled()) {
                logger.debug("Unable to find bean: " + "menuBuilder");
            }

            return null;
        }
        return bean;
    }
}
