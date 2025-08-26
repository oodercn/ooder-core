package net.ooder.esd.custom.component.index;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.CustomLayoutItemBean;
import net.ooder.esd.bean.view.CustomLayoutViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.component.form.field.CustomFieldBlockComponent;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.properties.LayoutProperties;
import net.ooder.esd.tool.properties.item.LayoutListItem;

import java.util.List;
import java.util.Map;

public class IndexMainComponent extends BlockComponent {

    @JSONField(serialize = false)
    LayoutComponent layoutComponent;

    public IndexMainComponent() {
        super();
    }

    public IndexMainComponent(EUModule module, MethodConfig methodConfig, Map valueMap) {
        CustomFieldBlockComponent topBar = new CustomFieldBlockComponent(module, methodConfig, valueMap);
        topBar.setTarget(PosType.before.name());
        this.getProperties().setDock(Dock.fill);
        CustomLayoutViewBean layoutViewBean = new CustomLayoutViewBean(methodConfig);
        layoutComponent = initLayoutComponent(layoutViewBean, module.getName());
        layoutComponent.addChildren(topBar);
        this.addChildren(layoutComponent);

    }

    public LayoutComponent getLayoutComponent() {
        return layoutComponent;
    }

    public void setLayoutComponent(LayoutComponent layoutComponent) {
        this.layoutComponent = layoutComponent;
    }

    @JSONField(serialize = false)
    public LayoutComponent initLayoutComponent(CustomLayoutViewBean layoutViewBean, String moduleName) {

        LayoutComponent layoutComponent = new LayoutComponent(Dock.fill, moduleName + ComponentType.LAYOUT.name());
        LayoutProperties layoutProperties = layoutComponent.getProperties();
        LayoutListItem topItem = new LayoutListItem(PosType.before);
        LayoutListItem mainItem = new LayoutListItem(PosType.main);
        List<CustomLayoutItemBean> itemBeanList = layoutViewBean.getLayoutItems();
        for (CustomLayoutItemBean layoutItemBean : itemBeanList) {
            if (layoutItemBean.getPos().equals(PosType.before)) {
                topItem = new LayoutListItem(layoutItemBean);
            } else if (layoutItemBean.getPos().equals(PosType.main)) {
                mainItem = new LayoutListItem(layoutItemBean);
            }
        }

        if (layoutViewBean != null) {
            layoutProperties = new LayoutProperties(layoutViewBean);
            layoutProperties.getItems().clear();
            layoutProperties.addItem(topItem);
            layoutProperties.addItem(mainItem);
        } else {
            layoutProperties.setBorderType(BorderType.none);
            layoutProperties.setType(LayoutType.vertical);
            layoutProperties.addItem(topItem);
            layoutProperties.addItem(mainItem);
        }
        layoutProperties.setType(LayoutType.vertical);
        layoutComponent.setProperties(layoutProperties);
        return layoutComponent;

    }
}
