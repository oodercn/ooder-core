package net.ooder.esd.tool.properties;


import net.ooder.esd.annotation.ui.VisibilityType;
import net.ooder.esd.bean.ContainerBean;

public class ContainerUIProperties extends ContainerProperties {

    public VisibilityType visibility;
    public String display;
    public Boolean selectable;
    public String renderer;

    public ContainerUIProperties() {

    }

    public void init(ContainerBean containerBean) {
        super.init(containerBean);

        if (containerBean.getUiBean() != null) {
            super.init(containerBean.getUiBean());
        }

    }

    public ContainerUIProperties(ContainerBean containerBean) {
        if (containerBean != null) {
            init(containerBean);
        }

    }

    public VisibilityType getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityType visibility) {
        this.visibility = visibility;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    @Override
    public Boolean getSelectable() {
        return selectable;
    }

    @Override
    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }
}
