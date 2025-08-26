package net.ooder.esd.dsm.view.context;

import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.view.GridViewAnnotation;
import net.ooder.esd.bean.data.CustomDataBean;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.nav.IButtonLayoutItem;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.dsm.view.ref.ViewEntityRef;
import net.ooder.esd.dsm.view.ref.ViewEntityRefProxy;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.item.ButtonItem;
import net.ooder.esd.tool.properties.item.LayoutListItem;
import net.ooder.esd.tool.properties.item.UIItem;

import net.ooder.annotation.Ref;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.web.util.MethodUtil;
import net.ooder.web.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.*;


public class ViewEntityRoot<T extends CustomViewBean<ESDFieldConfig, UIItem, ? extends Component>, K extends CustomDataBean> implements JavaRoot {


    public String packageName;

    public String className;

    public String rootId = "00000000-0000-0000-0000-000000000000";

    public String basepath;

    public String space;

    public String cnName;

    public DomainInst dsmBean;

    public ESDClass esdClass;

    public String moduleName;

    public ESDClass sourceClass;

    public AggEntityConfig aggEntityConfig;

    public ApiClassConfig apiClassConfig;

    public MethodConfig<T, K> methodConfig;

    private final String basePackage;

    public Set<String> imports = new LinkedHashSet<>();

    public List<ViewEntityRefProxy> refs = new ArrayList<>();

    public List<ViewEntityRefProxy> f2fs = new ArrayList<>();

    public List<ViewEntityRefProxy> o2os = new ArrayList<>();

    public List<ViewEntityRefProxy> o2ms = new ArrayList<>();

    public List<ViewEntityRefProxy> m2os = new ArrayList<>();

    public List<ViewEntityRefProxy> m2ms = new ArrayList<>();

    private List<ESDFieldConfig> allFields = new ArrayList<>();

    private List<CustomMethodInfo> allMethods = new ArrayList<>();

    private Set<ESDFieldConfig> customFields = new LinkedHashSet<ESDFieldConfig>();

    private Set<ESDFieldConfig> moduleFields = new LinkedHashSet<ESDFieldConfig>();


    private Set<ESDFieldConfig> displayFields = new LinkedHashSet<ESDFieldConfig>();

    private Set<ESDFieldConfig> hiddenFields = new LinkedHashSet<>();

    private Set<ESDFieldConfig> captionFields = new LinkedHashSet<ESDFieldConfig>();

    private Set<ESDFieldConfig> otherCaptionFields = new LinkedHashSet<ESDFieldConfig>();

    private Set<ESDFieldConfig> otherFields = new LinkedHashSet<ESDFieldConfig>();

    private Set<ESDFieldConfig> pkFields = new LinkedHashSet<ESDFieldConfig>();

    private Set<ESDFieldConfig> fkFields = new LinkedHashSet<ESDFieldConfig>();

    private static final Class[] customClass = new Class[]{

            PageUtil.class,
            EsbUtil.class,
            ResultModel.class,
            ListResultModel.class,
            TreeListResultModel.class,
            TreeListItem.class,
            ButtonItem.class,
            LayoutListItem.class,
            IButtonLayoutItem.class,
            LayoutAnnotation.class,
            View.class,
            ButtonLayoutAnnotation.class,

            NavTreeAnnotation.class,
            GridAnnotation.class,
            FormAnnotation.class,
            CustomListAnnotation.class,
            TreeAnnotation.class,
            GalleryAnnotation.class,
            TabsAnnotation.class,
            GridViewAnnotation.class,
            MethodChinaName.class,
            Controller.class,
            ModuleAnnotation.class,
            Ref.class,
            MenuBarMenu.class,
            ToolBarMenu.class,
            CustomAnnotation.class,
            RequestMapping.class,
            ResponseBody.class};
    public T view;

    public K data;


    public ViewEntityRoot(DomainInst dsmBean, MethodConfig<T, K> methodConfig, List<ViewEntityRef> dsmRefs) {
        this.dsmBean = dsmBean;
        this.methodConfig = methodConfig;
        this.view = methodConfig.getView();
        this.data = methodConfig.getDataBean();

        try {
            esdClass = methodConfig.getViewClass().getEntityClass();
            apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(methodConfig.getViewClassName());
            sourceClass = methodConfig.getSourceClass();
            for (Class clazz : customClass) {
                imports.add(clazz.getName());
                if (clazz.isAnnotation()) {
                    imports = MethodUtil.getAllImports(clazz, imports);
                }
            }

            Method method = methodConfig.getMethod();
            Set<String> customServiceClass = data.getCustomServiceClass();
            if (method != null) {
                String serviceClass = method.getDeclaringClass().getName();
                if (methodConfig.getEsdClass().isProxy() && !customServiceClass.contains(serviceClass)) {
                    customServiceClass.add(methodConfig.getSourceClass().getClass().getName());
                }
            }

            if (data.getCustomService() != null) {
                for (Class clazz : data.getCustomService()) {
                    imports.add(clazz.getName());
                }
            }

            if (view.getCustomService() != null) {
                for (Class clazz : view.getCustomService()) {
                    imports.add(clazz.getName());
                }
            }
            imports.addAll(customServiceClass);
            view.getCustomServiceClass().addAll(customServiceClass);


            if (getView().getOtherClass() != null) {
                for (Class clazz : getView().getOtherClass()) {
                    imports.add(clazz.getName());
                }
            }

            if (getView().getBindService() != null) {
                imports.add(getView().getBindService().getName());
            }


            imports = MethodUtil.getAllImports(sourceClass.getCtClass(), imports);
            imports = MethodUtil.getAllImports(esdClass.getCtClass(), imports);

            if (methodConfig.getMethod() != null) {
                imports = MethodUtil.getAllImports(methodConfig.getMethod().getDeclaringClass(), imports);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JDSException e) {
            e.printStackTrace();
        }

        this.basePackage = dsmBean.getPackageName() + "." + methodConfig.getSourceClass().getTopSourceClass().getEntityClass().getName().toLowerCase();


        for (String packName : innerPacks) {
            String innerPackageName = basePackage + "." + packName;
            if (Package.getPackage(innerPackageName) != null) {
                this.imports.add(innerPackageName + ".*");
            }

            for (Package pack : ClassUtility.getAllDynPacks()) {
                if (pack.getName().startsWith(basePackage + "." + packName)) {
                    this.imports.add(pack.getName() + ".*");
                }
            }

        }

        Map<String, Class> classMap = new HashMap<>();
        classMap.putAll(ClassUtility.getDynClassMap());

        Set<Map.Entry<String, Class>> dynClassSet = classMap.entrySet();
        for (Map.Entry<String, Class> classEntry : dynClassSet) {
            Class dynClass = classEntry.getValue();
            if (dynClass.getPackage().getName().startsWith(basePackage)) {
                this.imports.add(dynClass.getPackage().getName() + ".*");
            }
        }


        try {
            aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(esdClass.getClassName(), false);
        } catch (JDSException e) {
            e.printStackTrace();
        }

        try {
            Set<String> javaTempIds = dsmBean.getJavaTempIds();
            javaTempIds.addAll(dsmBean.getJavaTempIds());
            for (String javaTempId : javaTempIds) {
                JavaTemp javaTemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
                if (javaTemp != null && (javaTemp.getDsmType().equals(DSMType.VIEW) || javaTemp.getDsmType().equals(DSMType.REPOSITORY))) {
                    if (javaTemp.getPackagePostfix() == null || javaTemp.getPackagePostfix().equals("..") || javaTemp.getPackagePostfix().equals("")) {
                        this.imports.add(basePackage + ".*");
                    } else {
                        this.imports.add(basePackage + "." + javaTemp.getPackagePostfix() + ".*");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        this.packageName = dsmBean.getPackageName();
        this.className = esdClass.getCtClass().getName();
        this.space = dsmBean.getSpace();
        this.allMethods = esdClass.getMethodsList();
        this.refs = new ArrayList<>();
        this.allFields = view.getAllFields();
        customFields.addAll(view.getCustomFields());
        for (String fieldname : view.getHiddenFieldNames()) {
            ESDFieldConfig esdFieldConfig = view.getFieldByName(fieldname);
            this.hiddenFields.add(esdFieldConfig);
        }
        this.basepath = dsmBean.getPackageName();
        this.imports.remove(packageName + ".*");
        if (imports.contains(Component.class.getPackage().getName() + ".*")
                && imports.contains("org.springframework.stereotype.*")) {
            imports.add(Component.class.getName());
        }


        for (ViewEntityRef dsmRef : dsmRefs) {
            try {
                ViewEntityRefProxy dsmRefProxy = new ViewEntityRefProxy(dsmRef);
                if (dsmRef.getPkField() != null && dsmRef.getFkField() != null) {
                    refs.add(dsmRefProxy);
                    ESDFieldConfig captionField = view.getFieldByName(dsmRefProxy.getPk().getFieldName());
                    ESDFieldConfig otherCaptionField = view.getFieldByName(dsmRefProxy.getFk().getFieldName());
                    if (dsmRefProxy.getCaption() != null) {
                        captionField = view.getFieldByName(dsmRefProxy.getCaption().getName());
                    }
                    if (dsmRefProxy.getOtherCaption() != null) {
                        otherCaptionField = view.getFieldByName(dsmRefProxy.getOtherCaption().getName());
                    }
                    ESDFieldConfig fieldProxy = view.getFieldByName(dsmRefProxy.getFk().getName());
                    switch (dsmRef.getRefBean().getRef()) {
                        case F2F:
                            this.f2fs.add(dsmRefProxy);
                            otherFields.add(captionField);
                            otherCaptionFields.add(otherCaptionField);
                            addFkField(fieldProxy);
                            break;
                        case M2M:
                            this.m2ms.add(dsmRefProxy);
                            break;
                        case O2M:
                            o2ms.add(dsmRefProxy);
                            otherFields.add(otherCaptionField);
                            captionFields.add(captionField);
                            break;
                        case O2O:
                            this.o2os.add(dsmRefProxy);
                            break;
                        case M2O:
                            otherFields.add(captionField);
                            otherCaptionFields.add(otherCaptionField);
                            addFkField(fieldProxy);
                            this.m2os.add(dsmRefProxy);
                            break;
                    }
                }
            } catch (JDSException e) {

            }


        }


        for (ESDFieldConfig field : view.getAllFields()) {
            if (esdClass.getUid() != null) {
                if (field.getFieldname().equals(esdClass.getUid())) {
                    this.pkFields.add(field);
                }
            }
        }

        for (ESDFieldConfig colProxy : view.getAllFields()) {
            if (!hasCols(colProxy, fkFields)
                    && !hasCols(colProxy, hiddenFields)
                    && !hasCols(colProxy, pkFields)
                    && !hasCols(colProxy, otherFields)
                    && !hasCols(colProxy, captionFields)
                    && !hasCols(colProxy, otherCaptionFields)
                    ) {
                this.displayFields.add(colProxy);
            }
        }

        for (ESDFieldConfig colProxy : view.getAllFields()) {
            if (hasCols(colProxy, pkFields) || hasCols(colProxy, otherFields)) {
                this.hiddenFields.add(colProxy);
            }

        }

        for (ESDFieldConfig colProxy : view.getAllFields()) {
            if (colProxy instanceof FieldFormConfig) {
                FieldFormConfig fieldFormConfig = (FieldFormConfig) colProxy;
                if (fieldFormConfig.getComponentType().equals(ComponentType.LIST)
                        || fieldFormConfig.getComponentType().equals(ComponentType.MODULE)
                        ) {

                    this.moduleFields.add(colProxy);
                }
            }

        }


        //this.customFields.addAll(displayFields);
        this.customFields.addAll(hiddenFields);
    }

    public DomainInst getDsmBean() {
        return dsmBean;
    }

    public void setDsmBean(DomainInst dsmBean) {
        this.dsmBean = dsmBean;
    }

    void addFkField(ESDFieldConfig colProxy) {
        if (colProxy != null) {
            fkFields.add(colProxy);
        }

    }

    public ApiClassConfig getApiClassConfig() {
        return apiClassConfig;
    }

    public void setApiClassConfig(ApiClassConfig apiClassConfig) {
        this.apiClassConfig = apiClassConfig;
    }

    boolean hasCols(ESDFieldConfig colProxy, Set<ESDFieldConfig> cols) {
        for (ESDFieldConfig customCol : cols) {
            if (customCol != null && colProxy != null && customCol.getFieldname().toLowerCase().equals(colProxy.getFieldname().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Set<ESDFieldConfig> getDisplayFields() {
        return displayFields;
    }

    public void setDisplayFields(Set<ESDFieldConfig> displayFields) {
        this.displayFields = displayFields;
    }

    public ESDClass getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(ESDClass sourceClass) {
        this.sourceClass = sourceClass;
    }

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
    }


    public List<CustomMethodInfo> getAllMethods() {
        return allMethods;
    }

    public void setAllMethods(List<CustomMethodInfo> allMethods) {
        this.allMethods = allMethods;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public List<ViewEntityRefProxy> getRefs() {
        return refs;
    }

    public void setRefs(List<ViewEntityRefProxy> refs) {
        this.refs = refs;
    }

    public List<ViewEntityRefProxy> getF2fs() {
        return f2fs;
    }

    public void setF2fs(List<ViewEntityRefProxy> f2fs) {
        this.f2fs = f2fs;
    }

    public List<ViewEntityRefProxy> getO2os() {
        return o2os;
    }

    public void setO2os(List<ViewEntityRefProxy> o2os) {
        this.o2os = o2os;
    }

    public List<ViewEntityRefProxy> getO2ms() {
        return o2ms;
    }

    public void setO2ms(List<ViewEntityRefProxy> o2ms) {
        this.o2ms = o2ms;
    }

    public List<ViewEntityRefProxy> getM2os() {
        return m2os;
    }

    public void setM2os(List<ViewEntityRefProxy> m2os) {
        this.m2os = m2os;
    }

    public List<ViewEntityRefProxy> getM2ms() {
        return m2ms;
    }

    public void setM2ms(List<ViewEntityRefProxy> m2ms) {
        this.m2ms = m2ms;
    }

    public ESDClass getEsdClass() {
        return esdClass;
    }

    public void setEsdClass(ESDClass esdClass) {
        this.esdClass = esdClass;
    }

    public List<ESDFieldConfig> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<ESDFieldConfig> allFields) {
        this.allFields = allFields;
    }

    public Set<ESDFieldConfig> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Set<ESDFieldConfig> customFields) {
        this.customFields = customFields;
    }

    public Set<ESDFieldConfig> getHiddenFields() {
        return hiddenFields;
    }

    public void setHiddenFields(Set<ESDFieldConfig> hiddenFields) {
        this.hiddenFields = hiddenFields;
    }

    public Set<ESDFieldConfig> getCaptionFields() {
        return captionFields;
    }

    public void setCaptionFields(Set<ESDFieldConfig> captionFields) {
        this.captionFields = captionFields;
    }

    public Set<ESDFieldConfig> getOtherCaptionFields() {
        return otherCaptionFields;
    }

    public void setOtherCaptionFields(Set<ESDFieldConfig> otherCaptionFields) {
        this.otherCaptionFields = otherCaptionFields;
    }

    public Set<ESDFieldConfig> getOtherFields() {
        return otherFields;
    }

    public void setOtherFields(Set<ESDFieldConfig> otherFields) {
        this.otherFields = otherFields;
    }

    public Set<ESDFieldConfig> getPkFields() {
        return pkFields;
    }

    public void setPkFields(Set<ESDFieldConfig> pkFields) {
        this.pkFields = pkFields;
    }

    public Set<ESDFieldConfig> getFkFields() {
        return fkFields;
    }

    public void setFkFields(Set<ESDFieldConfig> fkFields) {
        this.fkFields = fkFields;
    }

    public T getView() {
        return view;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setView(T view) {
        this.view = view;
    }

    public K getData() {
        return data;
    }

    public void setData(K data) {
        this.data = data;
    }

    public MethodConfig<T, K> getMethodConfig() {
        return methodConfig;
    }

    public void setMethodConfig(MethodConfig<T, K> methodConfig) {
        this.methodConfig = methodConfig;
    }

    public AggEntityConfig getAggEntityConfig() {
        return aggEntityConfig;
    }

    public void setAggEntityConfig(AggEntityConfig aggEntityConfig) {
        this.aggEntityConfig = aggEntityConfig;
    }


}
