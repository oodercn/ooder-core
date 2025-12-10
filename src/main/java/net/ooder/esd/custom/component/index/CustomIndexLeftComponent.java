package net.ooder.esd.custom.component.index;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.LayoutAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.CustomLayoutItemBean;
import net.ooder.esd.bean.view.CustomLayoutViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.properties.LayoutProperties;
import net.ooder.esd.tool.properties.item.LayoutListItem;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.List;
import java.util.Map;

public class CustomIndexLeftComponent extends BlockComponent {

    @JSONField(serialize = false)
    LayoutComponent layoutComponent;

    @JSONField(serialize = false)
    LayoutComponent mainLayoutComponent;


    public CustomIndexLeftComponent() {
        super();
    }

    public CustomIndexLeftComponent(EUModule module, MethodConfig methodConfig, LayoutComponent mainLayoutComponent, Map valueMap) {
        Class clazz = JSONGenUtil.getInnerReturnType(methodConfig.getMethod());
        LayoutAnnotation annotation = AnnotationUtil.getClassAnnotation(clazz, LayoutAnnotation.class);
        CustomLayoutViewBean layoutViewBean = new CustomLayoutViewBean(methodConfig);
        this.mainLayoutComponent = mainLayoutComponent;
        layoutComponent = this.getLayoutComponent(layoutViewBean, module.getName());

        IndexLeftTopComponent topBar = new IndexLeftTopComponent();
        topBar.setTarget(PosType.before.name());
        layoutComponent.addChildren(topBar);

        IndexLeftTreeComponent leftTreeComponent = new IndexLeftTreeComponent(module, methodConfig, mainLayoutComponent, valueMap);
        leftTreeComponent.setTarget(PosType.main.name());
        layoutComponent.addChildren(leftTreeComponent);


        this.getModuleComponent().addChildren(layoutComponent);

    }

    @JSONField(serialize = false)
    public LayoutComponent getLayoutComponent(CustomLayoutViewBean layoutViewBean, String moduleName) {
        LayoutComponent layoutComponent = new LayoutComponent(Dock.fill, moduleName + ComponentType.LAYOUT.name());
        LayoutProperties layoutProperties = layoutComponent.getProperties();
        LayoutListItem topItem = new LayoutListItem(PosType.before);
        LayoutListItem mainItem = new LayoutListItem(PosType.main);

        List<CustomLayoutItemBean> itemBeanList=layoutViewBean.getLayoutItems();
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
            layoutProperties.setLayoutType(LayoutType.vertical);
            layoutProperties.addItem(topItem);
            layoutProperties.addItem(mainItem);
        }
        layoutProperties.setLayoutType(LayoutType.vertical);
        layoutComponent.setProperties(layoutProperties);
        return layoutComponent;

    }


}
