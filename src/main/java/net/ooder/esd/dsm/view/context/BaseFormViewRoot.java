package net.ooder.esd.dsm.view.context;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomListAnnotation;
import net.ooder.esd.annotation.FormAnnotation;
import net.ooder.esd.annotation.GridAnnotation;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.event.CustomMFormEvent;
import net.ooder.esd.bean.view.BaseFormViewBean;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.web.util.MethodUtil;

import java.util.*;


public abstract class BaseFormViewRoot<T extends BaseFormViewBean> extends BaseViewRoot<T> {


    private static Class[] formClass = new Class[]{
            CustomListAnnotation.class,
            CustomMFormEvent.class,
            CustomFormEvent.class,
            FormAnnotation.class,
            GridAnnotation.class

    };

    private List<FieldFormConfig> allFields = new ArrayList<>();

    public BaseFormViewRoot() {

    }

    public BaseFormViewRoot(AggViewRoot viewRoot, T viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
        this.allFields = viewBean.getAllFields();
        Set<Class> classes = new HashSet<>();
        classes.addAll(Arrays.asList(formClass));
        this.imports.addAll(getCustomClassImpls(classes));
        imports.addAll(viewRoot.getImports());
        imports.addAll(getFieldImpls(allFields));

    }


    protected Set<String> getFieldImpls(List<FieldFormConfig> allFields) {
        Set<String> imports = new HashSet<>();
        for (ESDFieldConfig esdFieldConfig : allFields) {
            FieldFormConfig fieldFormConfig = (FieldFormConfig) esdFieldConfig;
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

    public static Class[] getFormClass() {
        return formClass;
    }

    public static void setFormClass(Class[] formClass) {
        BaseFormViewRoot.formClass = formClass;
    }


    public List<FieldFormConfig> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<FieldFormConfig> allFields) {
        this.allFields = allFields;
    }


}
