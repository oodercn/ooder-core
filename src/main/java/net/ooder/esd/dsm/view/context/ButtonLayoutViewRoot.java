package net.ooder.esd.dsm.view.context;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.annotation.ButtonLayoutAnnotation;
import net.ooder.esd.annotation.GridAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.bean.view.CustomButtonLayoutViewBean;
import net.ooder.esd.bean.nav.IButtonLayoutItem;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.dsm.view.field.FieldGalleryConfig;
import net.ooder.esd.tool.properties.item.ButtonLayoutItem;
import net.ooder.web.util.MethodUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


public class ButtonLayoutViewRoot extends BaseViewRoot<CustomButtonLayoutViewBean> {

    private List<FieldGalleryConfig> allFields = new ArrayList<>();

    private List<ButtonLayoutItem> tabItems = new ArrayList<>();

    private static final Class[] customClass = new Class[]{
            ResultModel.class,
            ListResultModel.class,
            ButtonLayoutAnnotation.class,
            IButtonLayoutItem.class,
            ButtonLayoutItem.class,
            TreeListResultModel.class,
            GridAnnotation.class,
            MethodChinaName.class,
            Controller.class,
            ModuleAnnotation.class,
            ToolBarMenu.class,
            ResponseBody.class};

    public ButtonLayoutViewRoot(AggViewRoot viewRoot, CustomButtonLayoutViewBean viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
        this.allFields = viewBean.getAllFields();
        this.tabItems = viewBean.getTabItems();

        try {
            for (ESDFieldConfig esdFieldConfig : allFields) {
                FieldGalleryConfig fieldFormConfig = (FieldGalleryConfig) esdFieldConfig;
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

    public List<ButtonLayoutItem> getTabItems() {
        return tabItems;
    }

    public void setTabItems(List<ButtonLayoutItem> tabItems) {
        this.tabItems = tabItems;
    }

    public List<FieldGalleryConfig> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<FieldGalleryConfig> allFields) {
        this.allFields = allFields;
    }

    public Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.LAYOUT;
    }


}
