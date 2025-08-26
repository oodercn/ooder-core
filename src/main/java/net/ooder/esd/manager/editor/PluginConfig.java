package net.ooder.esd.manager.editor;

import net.ooder.esd.annotation.ui.ComponentBaseType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.engine.enums.PluginsContextType;
import net.ooder.esd.engine.enums.PluginsPostationType;
import java.util.ArrayList;
import java.util.List;

public class PluginConfig {


    TreeListItem menus = new TreeListItem();

    List<PluginsPostationType> postations = new ArrayList<PluginsPostationType>();

    List<PluginsContextType> contexts = new ArrayList<PluginsContextType>();

    List<ComponentBaseType> componenTypes = new ArrayList<>();

    public PluginConfig() {

    }

    public TreeListItem getMenus() {
        return menus;
    }

    public void setMenus(TreeListItem menus) {
        this.menus = menus;
    }

    public List<PluginsPostationType> getPostations() {
        return postations;
    }

    public void setPostations(List<PluginsPostationType> postations) {
        this.postations = postations;
    }

    public List<PluginsContextType> getContexts() {
        return contexts;
    }

    public void setContexts(List<PluginsContextType> contexts) {
        this.contexts = contexts;
    }

    public List<ComponentBaseType> getComponenTypes() {
        return componenTypes;
    }

    public void setComponenTypes(List<ComponentBaseType> componenTypes) {
        this.componenTypes = componenTypes;
    }
}
