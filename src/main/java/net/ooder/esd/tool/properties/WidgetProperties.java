package net.ooder.esd.tool.properties;


import net.ooder.esd.annotation.ui.Dock;

public class WidgetProperties extends ContainerUIProperties {
    Boolean shadow;

    public WidgetProperties() {

    }

    public WidgetProperties(Dock dock) {
        this.dock = dock;
    }


    public WidgetProperties(CustomWidgetBean widgetBean) {
        initWidget(widgetBean);

    }

    public void initWidget(CustomWidgetBean widgetBean) {
        if (widgetBean != null) {
            this.width = widgetBean.getWidth();
            this.height = widgetBean.getHeight();
            this.shadow = widgetBean.getShadow();
        }
    }

    public Boolean getShadow() {
        return shadow;
    }

    public void setShadow(Boolean shadow) {
        this.shadow = shadow;
    }
}
