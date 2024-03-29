package com.scalar.core.menu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;

import com.scalar.freequent.util.Global;
import com.scalar.freequent.auth.User;
import com.scalar.freequent.auth.Capability;
import com.scalar.freequent.auth.UserCapability;
import com.scalar.core.ScalarAuthException;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 5, 2013
 * Time: 8:55:55 PM
 */
public class MenuBuilder {
    protected static final Log logger = LogFactory.getLog(MenuBuilder.class);
    private static List<MenuType> rawMenu = null;

    public MenuBuilder() {
    }

    protected List<MenuType> build() throws JAXBException {
        logger.debug("Menus Unmarshalling started...");
        List<MenuType> menuTypes = null;
        JAXBContext jaxbContext = JAXBContext.newInstance("com.scalar.core.menu");
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        /*JAXBElement<CourseBooking> bookingElement
                    = (JAXBElement<CourseBooking>) unmarshaller.unmarshal ( new File( ConfigManager.getMenuXmlFilePath() ) );
                            //new File("src/test/resources/xml/booking.xml"));
        CourseBooking booking = bookingElement.getValue();
        System.out.println ( "===" + booking.getCompany().getName() );*/
        InputStream menuxmlStream = MenuBuilder.class.getResourceAsStream(Global.getString(Global.MENU_XML));
        try {
            JAXBElement<MenusType> menusElement
                    = (JAXBElement<MenusType>) unmarshaller.unmarshal(menuxmlStream);
            //new File("src/test/resources/xml/booking.xml"));
            logger.debug("Menus unmarshalling success");
            MenusType menusType = menusElement.getValue();
            menuTypes = menusType.getMenu();
            //need to remove menus whose display property is False
            if (menuTypes != null) {
                int listSize = menuTypes.size();
                for (int i = listSize - 1; i >= 0; i--) //main menus iterator
                {
                    MenuType mainMenu = menuTypes.get(i);
                    if (mainMenu.isDisplay()) {
                        MenuItemsType menuItemsType = mainMenu.getMenuItems();
                        if (menuItemsType != null) {
                            List<MenuType> menuItemsList = menuItemsType.getMenu();
                            int size = (menuItemsList != null) ? menuItemsList.size() : 0;
                            for (int j = size - 1; j >= 0; j--) //main menu items iterator
                            {
                                MenuType menuItem = menuItemsList.get(j);
                                if (menuItem.isDisplay()) {
                                    MenuItemsType subMenuItemstype = menuItem.getMenuItems();
                                    if (subMenuItemstype != null) {
                                        List<MenuType> subMenuItems = subMenuItemstype.getMenu();
                                        int subMenuItemsSize = (subMenuItems != null) ? subMenuItems.size() : 0;
                                        for (int k = subMenuItemsSize - 1; k >= 0; k--) {
                                            MenuType subMenuItem = subMenuItems.get(k);
                                            if (subMenuItem.isDisplay()) {
                                                //keep in the list
                                            } else {
                                                subMenuItems.remove(k);
                                            }
                                        }
                                    }
                                } else {
                                    menuItemsList.remove(j);
                                }

                            }
                        }
                    } else //remove from the menu
                    {
                        menuTypes.remove(i);
                    }
                }
            }
        } finally {
            if (menuxmlStream != null) {
                try {
                    menuxmlStream.close();
                } catch (IOException ignore) {
                }
            }
        }

        return menuTypes;
    }

    protected List<MenuType> getRawMenu() throws JAXBException {
        if (rawMenu == null) {
            rawMenu = build();
        }
        return rawMenu;
    }

    /**
     * returns the menu list which contains all displayable menus, menus are set either enabled or disabled based on the given user functionality.
     *
     * @param user
     * @return
     */
    public List<Menu> getMenuListForUser(User user) throws JAXBException {
        List<Menu> userMainMenuList = new LinkedList<Menu>();
        if (user == null) {
            return userMainMenuList;
        }
        List<MenuType> mainMenuList = getRawMenu();
        if (mainMenuList != null) {
            int listSize = mainMenuList.size();
            for (int i = 0; i < listSize; i++) //main menus iterator
            {
                MenuType mainMenu = mainMenuList.get(i);
                Menu userMainMenu = new Menu();
                buildUserMenuFromXmlMenu(mainMenu, userMainMenu, user);
                userMainMenuList.add(userMainMenu);
            }
        }
        return userMainMenuList;
    }

    private void buildUserMenuFromXmlMenu(MenuType xmlMenu, Menu userMenu, User user) {
        userMenu.setDisplay(xmlMenu.isDisplay());
        userMenu.setEnabled(xmlMenu.isEnabled());
        CapabilityType capabilityType = xmlMenu.getCapability();
        if (capabilityType != null) {
            Capability capability = new Capability(capabilityType.getName());
            if (Capability.READ_TYPE.equalsIgnoreCase(capabilityType.getType())) {
                capability.setSupportsRead(true);
            } else if (Capability.WRITE_TYPE.equalsIgnoreCase(capabilityType.getType())) {
                capability.setSupportsWrite(true);
            } else if (Capability.DELETE_TYPE.equalsIgnoreCase(capabilityType.getType())) {
                capability.setSupportsDelete(true);
            }
            userMenu.setCapability(capability);
        }
        userMenu.setId(xmlMenu.getId());
        userMenu.setLink(xmlMenu.getLink());
        userMenu.setName(xmlMenu.getName());
		if (user != null) {
			boolean hasCapability;
			try {
				user.checkCapability(userMenu.getCapability());
				hasCapability = true;
			} catch (ScalarAuthException e) {
				hasCapability = false;
			}
			userMenu.setEnabled(hasCapability);
		}
		if (userMenu.isEnabled()) {
			MenuItemsType menuItemsType = xmlMenu.getMenuItems();
			if (menuItemsType != null) {
				List<MenuType> xmlMenuItemsList = menuItemsType.getMenu();
				if (xmlMenuItemsList != null) {
					int size = xmlMenuItemsList.size();
					List<Menu> userMenuItemsList = new ArrayList<Menu>(size);
					for (int i = 0; i < size; i++) {
						MenuType xmlMenuItem = xmlMenuItemsList.get(i);
						Menu userMenuItem = new Menu();
						buildUserMenuFromXmlMenu(xmlMenuItem, userMenuItem, user);
						userMenuItemsList.add(userMenuItem);
					}
					userMenu.setMenuItems(userMenuItemsList);
				}
			}
		}
    }

    public String getMenuId(String menuUri) throws JAXBException {
        if (menuUri == null) {
            return null;
        }
        List<MenuType> mainMenuList = getRawMenu();
        if (mainMenuList != null) {
            int listSize = mainMenuList.size();
            for (int i = 0; i < listSize; i++) //main menus iterator
            {
                MenuType mainMenu = mainMenuList.get(i);
                Menu userMainMenu = new Menu();
                buildUserMenuFromXmlMenu(mainMenu, userMainMenu, null);
                if (menuUri.equalsIgnoreCase(userMainMenu.getLink())) {
                    return userMainMenu.getId();
                } else {
                    List<Menu> menuItems = userMainMenu.getMenuItems();
                    if (menuItems != null) {
                        for (int k = 0; k < menuItems.size(); k++) {
                            Menu menuItem = menuItems.get(k);
                            if (menuUri.equalsIgnoreCase(menuItem.getLink())) {
                                return menuItem.getId();
                            } else {
                                List<Menu> subMenuItems = menuItem.getMenuItems();
                                if (subMenuItems != null) {
                                    for (int j = 0; j < subMenuItems.size(); j++) {
                                        Menu subMenuItem = subMenuItems.get(j);
                                        if (menuUri.equalsIgnoreCase(subMenuItem.getLink())) {
                                            return subMenuItem.getId();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
