package net.ooder.esd.dsm.view.context;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.TreeAnnotation;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.field.LayoutFieldAnnotation;
import net.ooder.esd.annotation.view.LayoutViewAnnotation;
import net.ooder.esd.bean.CustomLayoutItemBean;
import net.ooder.esd.bean.nav.LayoutItem;
import net.ooder.esd.bean.view.NavGalleryComboViewBean;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.tool.properties.item.LayoutListItem;
import net.ooder.web.util.MethodUtil;

import java.util.ArrayList;
import java.util.List;


public class NavGalleryViewRoot extends BaseViewRoot<NavGalleryComboViewBean> {


    private List<FieldModuleConfig> allFields = new ArrayList<>();

    private List<LayoutListItem> tabItems = new ArrayList<>();

    private static final Class[] customClass = new Class[]{
            LayoutItem.class,
            LayoutListItem.class,
            TreeAnnotation.class,
            LayoutFieldAnnotation.class,
            LayoutViewAnnotation.class,
    };


    public NavGalleryViewRoot(AggViewRoot viewRoot, NavGalleryComboViewBean viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
        this.allFields = viewBean.getLayoutViewBean().getAllFields();
        this.tabItems = viewBean.getLayoutViewBean().getTabItems();
        if (tabItems.isEmpty()) {
            List<CustomLayoutItemBean> layoutItemBeans = viewBean.getLayoutViewBean().getLayoutItems();
            for (CustomLayoutItemBean layoutItemBean : layoutItemBeans) {
                tabItems.add(new LayoutListItem(layoutItemBean));
            }
        }

        try {
            for (ESDFieldConfig esdFieldConfig : allFields) {
                FieldModuleConfig fieldFormConfig = (FieldModuleConfig) esdFieldConfig;
                if (fieldFormConfig.getMethodConfig() != null) {
                    List<CustomBean> fieldCustomBeans = fieldFormConfig.getMethodConfig().getAnnotationBeans();
                    for (CustomBean customBean : fieldCustomBeans) {
                        imports.add(customBean.getClass().getName());
                        imports = MethodUtil.getAllImports(customBean.getClass(), imports);
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public List<LayoutListItem> getTabItems() {
        return tabItems;
    }

    public void setTabItems(List<LayoutListItem> tabItems) {
        this.tabItems = tabItems;
    }

    public List<FieldModuleConfig> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<FieldModuleConfig> allFields) {
        this.allFields = allFields;
    }

    public Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.NAVTREE;
    }


}
