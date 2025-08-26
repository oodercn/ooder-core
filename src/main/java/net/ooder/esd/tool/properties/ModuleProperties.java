package net.ooder.esd.tool.properties;


import net.ooder.esd.annotation.ui.AppendType;
import net.ooder.esd.annotation.ui.EmbedType;
import net.ooder.esd.annotation.ui.PanelType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.properties.item.TabListItem;

public class ModuleProperties extends TabListItem {

    DSMProperties dsmProperties;

    Boolean autoDestroy = true;

    String currComponentAlias;

    String viewClass;


    Integer rotate;

    PanelType panelType;

    Class bindService;

    String target;

    String childname;

    String src;

    AppendType append;

    EmbedType embed;

    public ModuleProperties() {
        this.dock = Dock.fill;
    }

    public ModuleProperties(Enum enumType) {
        super(enumType);
    }


    public ModuleProperties(Class viewClass, MethodConfig methodConfig, String projectName) {
        this.viewClass = viewClass.getName();
        this.src = methodConfig.getUrl();
        this.dynLoad = methodConfig.getModuleBean().getDynLoad();
        this.caption = methodConfig.getModuleBean().getCaption();
        this.rotate = methodConfig.getModuleBean().getRotate();
        this.panelType = methodConfig.getModuleBean().getPanelType();
        this.imageClass = methodConfig.getModuleBean().getImageClass();
        this.target = methodConfig.getModuleBean().getTarget();
        this.name = methodConfig.getModuleBean().getName();
        this.childname = methodConfig.getModuleBean().getChildname();
        if (methodConfig.getModuleBean().getBindService() != null && !methodConfig.getModuleBean().getBindService().equals(Void.class) && !methodConfig.getModuleBean().getBindService().equals(Enum.class)) {
            this.bindService = methodConfig.getModuleBean().getBindService();
            this.bindClass = new Class[]{bindService};
        }
        dsmProperties = new DSMProperties(methodConfig, projectName);
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public AppendType getAppend() {
        return append;
    }

    public void setAppend(AppendType append) {
        this.append = append;
    }

    public EmbedType getEmbed() {
        return embed;
    }

    public void setEmbed(EmbedType embed) {
        this.embed = embed;
    }


    public String getChildname() {
        return childname;
    }

    public void setChildname(String childname) {
        this.childname = childname;
    }

    public DSMProperties getDsmProperties() {
        return dsmProperties;
    }

    public void setDsmProperties(DSMProperties dsmProperties) {
        this.dsmProperties = dsmProperties;
    }

    public Integer getRotate() {
        return rotate;
    }

    public void setRotate(Integer rotate) {
        this.rotate = rotate;
    }

    public PanelType getPanelType() {
        return panelType;
    }

    public void setPanelType(PanelType panelType) {
        this.panelType = panelType;
    }

    public Class getBindService() {
        return bindService;
    }

    public void setBindService(Class bindService) {
        this.bindService = bindService;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getViewClass() {
        return viewClass;
    }

    public void setViewClass(String viewClass) {
        this.viewClass = viewClass;
    }

    public Boolean getAutoDestroy() {
        return autoDestroy;
    }

    public void setAutoDestroy(Boolean autoDestroy) {
        this.autoDestroy = autoDestroy;
    }

    public String getCurrComponentAlias() {
        return currComponentAlias;
    }

    public void setCurrComponentAlias(String currComponentAlias) {
        this.currComponentAlias = currComponentAlias;
    }
}
