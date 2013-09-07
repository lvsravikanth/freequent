package com.scalar.core.menu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.List;

import com.scalar.freequent.auth.Capability;

/**
 * User: Sujan Kumar Suppala
 * Date: Sep 4, 2013
 * Time: 7:20:33 PM
 */
public class Menu implements Serializable {
    protected static final Log logger = LogFactory.getLog(Menu.class);

    private String link;
    private boolean enabled;
    private boolean display;
    private Capability capability;
    private String id;
    private String name;
    private List<Menu> menuItems;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public Capability getCapability() {
        return capability;
    }

    public void setCapability(Capability capability) {
        this.capability = capability;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Menu> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<Menu> menuItems) {
        this.menuItems = menuItems;
    }
}
    