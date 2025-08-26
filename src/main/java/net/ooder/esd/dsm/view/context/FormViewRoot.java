package net.ooder.esd.dsm.view.context;

import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.config.TreeListResultModel;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.event.CustomGridEvent;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.bean.DBTableBean;
import net.ooder.esd.bean.view.CustomFormViewBean;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.field.LabelFieldBean;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.web.EntityBean;
import net.ooder.web.util.MethodUtil;

import java.util.*;


public class FormViewRoot extends BaseViewRoot {

    public final static String formTitleName = "formTitle";

    private List<FieldFormConfig> allFields = new ArrayList<>();

    private List<FieldFormConfig> displayFields = new ArrayList<>();

    private List<CustomBean> dbAnnotations = new ArrayList<>();

    private static final Class[] customClass = new Class[]{
            TreeListResultModel.class,
            CustomGridEvent.class

    };


    public FormViewRoot(AggViewRoot viewRoot, CustomFormViewBean viewBean, ModuleComponent moduleComponent, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
        dbAnnotations = new ArrayList<>();
        try {
            if (viewRoot.getDsmBean() != null) {
                RepositoryInst repositoryInst = null;

                repositoryInst = DSMFactory.getInstance().getRepositoryManager().getProjectRepository(viewRoot.getDsmBean().getProjectVersionName());
                DBTableBean dbTableBean = new DBTableBean();
                dbTableBean.setTableName(viewBean.getName());
                FieldFormConfig fieldFormConfig = (FieldFormConfig) viewBean.getUidField();
                if (fieldFormConfig == null) {
                    fieldFormConfig = (FieldFormConfig) viewBean.findFieldByName("id");
                }
                if (fieldFormConfig == null) {
                    fieldFormConfig = viewBean.createUidField("id");
                }
                dbTableBean.setPrimaryKey(fieldFormConfig.getFieldname());
                dbTableBean.setCname(viewBean.getCaption());
                dbTableBean.setConfigKey(repositoryInst.getSchema());
                dbAnnotations.add(dbTableBean);
                EntityBean entityBean = new EntityBean();
                dbAnnotations.add(entityBean);

            }


            DomainInst domainInst = DSMFactory.getInstance().getDomainInstById(viewBean.getDomainId());
            if (domainInst != null && domainInst.getUserSpace().equals(UserSpace.FORM)) {

                if (viewBean.getMethodConfig() != null && viewBean.getMethodConfig().getMethod() != null && viewBean.getViewClassName() != null) {
                    viewBean.setViewClassName(className);
                    FieldFormConfig fieldInfo = viewBean.getFieldByName(formTitleName);
                    if (fieldInfo == null) {
                        try {
                            fieldInfo = viewBean.createField(formTitleName, ComponentType.LABEL, moduleComponent, null, String.class, "A1");
                        } catch (JDSException e) {
                            e.printStackTrace();
                        }
                    }

                    if (fieldInfo != null) {
                        FieldBean fieldBean = fieldInfo.createFieldBean();
                        fieldBean.setColSpan(-1);
                        fieldBean.setManualHeight(60);
                        CustomFieldBean customFieldBean = fieldInfo.createCustomBean();
                        customFieldBean.setIndex(1);
                        customFieldBean.setTarget("A1");
                        customFieldBean.setCaption(viewBean.getCaption());
                        fieldInfo.setComponentType(ComponentType.LABEL);
                        LabelFieldBean labelFieldBean = (LabelFieldBean) fieldInfo.getWidgetConfig();
                        if (labelFieldBean != null) {
                            labelFieldBean.setCaption(viewBean.getCaption());
                            labelFieldBean.sethAlign(HAlignType.center);
                            labelFieldBean.setFontSize("3em");
                        }
                        //  viewBean.moveTop(formTitleName);
                        try {
                            DSMFactory.getInstance().saveCustomViewBean(viewBean);
                        } catch (JDSException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }

        this.space = dsmBean.getSpace();
        this.viewBean = viewBean;
        this.allFields = viewBean.getAllFields();
        Set<Class> classes = new HashSet<>();
        classes.addAll(Arrays.asList(customClass));
        imports.addAll(getCustomClassImpls(classes));
        imports.addAll(viewRoot.getImports());
        imports.addAll(getFieldImpls(allFields));

        for (FieldFormConfig fieldFormConfig : allFields) {
            if (fieldFormConfig.getCustomBean() != null && fieldFormConfig.getCustomBean().getTarget() != null && fieldFormConfig.getComponentType().equals(ComponentType.LABEL)) {
                String target = fieldFormConfig.getCustomBean().getTarget();
                if (!fieldFormConfig.getFieldname().toUpperCase().equals(target.toUpperCase())) {
                    displayFields.add(fieldFormConfig);
                }
            } else {
                displayFields.add(fieldFormConfig);
            }
        }


    }

    protected Set<String> getFieldImpls(List<FieldFormConfig> allFields) {
        Set<String> imports = new HashSet<>();
        for (ESDFieldConfig esdFieldConfig : allFields) {
            FieldFormConfig fieldFormConfig = (FieldFormConfig) esdFieldConfig;
            Set<Class> fieldClassSet = fieldFormConfig.getOtherClass();
            for (Class clazz : fieldClassSet) {
                if (clazz != null) {
                    imports.add(clazz.getPackage().getName() + ".*");
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
                imports.add(customBean.getClass().getPackage().getName() + ".*");
                try {
                    imports = MethodUtil.getAllImports(customBean.getClass(), imports);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
        return imports;
    }


    public List<CustomBean> getDbAnnotations() {
        return dbAnnotations;
    }

    public void setDbAnnotations(List<CustomBean> dbAnnotations) {
        this.dbAnnotations = dbAnnotations;
    }

    public List<FieldFormConfig> getDisplayFields() {
        return displayFields;
    }

    public void setDisplayFields(List<FieldFormConfig> displayFields) {
        this.displayFields = displayFields;
    }

    public static String getFormTitleName() {
        return formTitleName;
    }

    public List<FieldFormConfig> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<FieldFormConfig> allFields) {
        this.allFields = allFields;
    }

    @Override
    public Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.FORM;
    }


}
