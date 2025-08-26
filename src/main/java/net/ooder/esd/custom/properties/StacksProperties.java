package net.ooder.esd.custom.properties;

import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomStacksFieldBean;
import net.ooder.esd.bean.view.StacksViewBean;

import java.util.Map;


public class StacksProperties extends NavTabsProperties<StacksListItem> {

    public StacksProperties() {

    }

    public StacksProperties(StacksViewBean stacksViewBean, Map<String, ?> valueMap) {
        init(stacksViewBean);
    }

    public StacksProperties(CustomStacksFieldBean blockBean) {
        super.init(blockBean.getContainerBean());
        this.init(blockBean.getViewBean());

    }


    public StacksProperties(MethodConfig methodConfig, Map<String, ?> valueMap) {
        this.name = methodConfig.getName();
        StacksViewBean tabsViewBean = (StacksViewBean) methodConfig.getView();
        init(tabsViewBean);
    }


    void init(StacksViewBean stacksViewBean) {
        this.setBorderType(BorderType.none);

        if (this.getItems() == null || this.getItems().isEmpty()) {
            this.setItems(stacksViewBean.getTabItems());
        }
        if (this.getItems() != null && this.getItems().size() > 0) {
            this.setValue(this.getItems().get(0).getId());
        }
        if (stacksViewBean.getCaption() != null && !stacksViewBean.getCaption().equals("")) {
            this.caption = stacksViewBean.getCaption();
        }
        if (stacksViewBean.getImageClass() != null && !stacksViewBean.getImageClass().equals("")) {
            this.imageClass = stacksViewBean.getImageClass();
        }
    }


}
