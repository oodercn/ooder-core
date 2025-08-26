package net.ooder.esd.dsm.view.context;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.GridAnnotation;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.bean.view.CustomGridViewBean;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.dsm.view.field.FieldGridConfig;
import net.ooder.web.util.MethodUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GridViewRoot extends BaseViewRoot<CustomGridViewBean> {

    private List<FieldGridConfig> allFields = new ArrayList<>();

    private static final Class[] customClass = new Class[]{
            GridAnnotation.class
    };

    public GridViewRoot(AggViewRoot viewRoot, CustomGridViewBean viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
        this.allFields = viewBean.getAllFields();
        imports.addAll(getFieldImpls(allFields));
    }

    protected Set<String> getFieldImpls(List<FieldGridConfig> allFields) {
        Set<String> imports = new HashSet<>();
        for (ESDFieldConfig esdFieldConfig : allFields) {
            FieldGridConfig fieldFormConfig = (FieldGridConfig) esdFieldConfig;
            Set<Class> fieldClassSet = fieldFormConfig.getOtherClass();
            for (Class clazz : fieldClassSet) {
                if (clazz != null) {
                    imports.add(clazz.getName());
                    if (clazz.isAnnotation()) {
                        try {
                            imports = MethodUtil.getAllImports(clazz, imports);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            List<CustomBean> fieldCustomBeans = fieldFormConfig.getAnnotationBeans();
            for (CustomBean customBean : fieldCustomBeans) {
                imports.add(customBean.getClass().getName());
                try {
                    imports = MethodUtil.getAllImports(customBean.getClass(), imports);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
        return imports;
    }

    @Override
    public Class[] getCustomClass() {
        return customClass;
    }

    public List<FieldGridConfig> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<FieldGridConfig> allFields) {
        this.allFields = allFields;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.GRID;
    }


}
