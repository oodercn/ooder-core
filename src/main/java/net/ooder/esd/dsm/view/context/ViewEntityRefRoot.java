package net.ooder.esd.dsm.view.context;

import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.java.DynamicClassLoader;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.GridViewAnnotation;
import net.ooder.esd.bean.data.CustomDataBean;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.dsm.view.ref.ViewEntityRefProxy;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.item.UIItem;
import net.ooder.esd.tool.properties.list.ListFieldProperties;

import net.ooder.annotation.Ref;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.web.util.MethodUtil;
import net.ooder.web.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


public class ViewEntityRefRoot<T extends CustomViewBean<ESDFieldConfig,UIItem,? extends Component>, K extends CustomDataBean> implements JavaRoot {

    public String packageName;

    public String className;

    public String basepath;

    public String space;

    public String cnName;

    public DomainInst dsmBean;

    public ESDClass esdClass;

    public String moduleName;

    public ViewEntityRefProxy ref;

    MethodConfig<T, K> methodConfig;

    ViewEntityRoot entityRoot;

    private Set<ESDFieldConfig> pkFields = new LinkedHashSet<ESDFieldConfig>();

    public ESDClass sourceClass;

    private final String basePackage;

    public Set<String> imports = new LinkedHashSet<>();

    public T view;

    public K data;


    private static final Class[] customClass = new Class[]{

            PageUtil.class,
            EsbUtil.class,
            ResultModel.class,
            ListResultModel.class,
            TreeListResultModel.class,
            ListFieldProperties.class,
            LayoutAnnotation.class,
            View.class,
            NavTreeAnnotation.class,
            GridAnnotation.class,
            FormAnnotation.class,
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
            ResponseBody.class
    };


    public ViewEntityRefRoot(DomainInst viewInst, MethodConfig<T, K> methodConfig, ViewEntityRoot entityRoot, JavaTemp refTemp, ViewEntityRefProxy ref) {
        this.dsmBean = viewInst;
        this.entityRoot = entityRoot;
        this.methodConfig = methodConfig;
        if (methodConfig.getModuleViewType() == null) {
            ModuleViewType moduleViewType = ModuleViewType.getModuleViewByViewType(refTemp.getViewType());
            methodConfig.getModuleBean().setModuleViewType(moduleViewType);
        }


        this.view = methodConfig.getView();
        this.data = methodConfig.getDataBean();
        this.packageName = dsmBean.getPackageName();
        this.className = methodConfig.getJavaClassName();
        this.cnName = ref.getMain().getCurrClass().getDesc();
        this.basepath = dsmBean.getPackageName();
        this.esdClass = ref.getMain().getCurrClass();
        this.ref = ref;
        this.methodConfig = methodConfig;
        this.space = dsmBean.getSpace();

        try {

            esdClass = methodConfig.getViewClass().getEntityClass();
            sourceClass = methodConfig.getSourceClass();
            for (Class clazz : customClass) {
                imports.add(clazz.getName());
                if (clazz.isAnnotation()) {
                    imports = MethodUtil.getAllImports(clazz, imports);
                }
            }


            Method method = methodConfig.getMethod();
            if (method != null) {
                String serviceClass = method.getDeclaringClass().getName();
                Set<String> customServiceClass = data.getCustomServiceClass();

                if (customServiceClass.isEmpty() || !customServiceClass.contains(serviceClass)) {
                    customServiceClass.add(serviceClass);
                }
                if (!sourceClass.getSourceClass().getClassName().equals(sourceClass.getClassName())) {
                    customServiceClass.remove(sourceClass.getSourceClass().getClassName());
                }
            }


            imports = MethodUtil.getAllImports(sourceClass.getCtClass(), imports);
            imports = MethodUtil.getAllImports(esdClass.getCtClass(), imports);
            if (methodConfig.getMethod() != null) {
                imports = MethodUtil.getAllImports(methodConfig.getMethod().getDeclaringClass(), imports);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //   String basePackage = dsmBean.getPackageName() + "." + methodConfig.getSourceClass().getTopSourceClass().getEntityClass().getName().toLowerCase();
        this.basePackage = dsmBean.getPackageName() + "." + methodConfig.getSourceClass().getTopSourceClass().getEntityClass().getName().toLowerCase();
        for (Package pack : Package.getPackages()) {
            for (String packName : innerPacks) {
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


        if (esdClass.getCtClass().getClassLoader() instanceof DynamicClassLoader) {
            DynamicClassLoader dynamicClassLoader = (DynamicClassLoader) esdClass.getCtClass().getClassLoader();
            for (Package pack : dynamicClassLoader.getPackages()) {
                if (pack.getName().startsWith(basePackage)) {
                    this.imports.add(pack.getName() + ".*");
                }
            }
        } else {
            for (Package pack : Package.getPackages()) {
                if (pack.getName().startsWith(basePackage)) {
                    this.imports.add(pack.getName() + ".*");
                }
            }
        }


        try {
            Set<String> javaTempIds = dsmBean.getJavaTempIds();
            for (String javaTempId : javaTempIds) {
                JavaTemp javaTemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
                if (javaTemp != null) {
                    if (javaTemp.getPackagePostfix() == null || javaTemp.getPackagePostfix().equals("..")) {
                        this.imports.add(basePackage + ".*");
                    } else {
                        this.imports.add(basePackage + "." + javaTemp.getPackagePostfix() + ".*");
                    }
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }


        this.packageName = dsmBean.getPackageName();
        this.className = esdClass.getCtClass().getName();
        this.space = dsmBean.getSpace();

        this.basepath = dsmBean.getPackageName();
        this.imports.remove(packageName + ".*");


        for (ESDFieldConfig field : view.getAllFields()) {
            if (esdClass.getUid() != null) {
                if (field.getFieldname().equals(esdClass.getUid())) {
                    this.pkFields.add(field);
                }
            }
        }


    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public ViewEntityRoot getEntityRoot() {
        return entityRoot;
    }

    public void setEntityRoot(ViewEntityRoot entityRoot) {
        this.entityRoot = entityRoot;
    }

    public Set<ESDFieldConfig> getPkFields() {
        return pkFields;
    }

    public void setPkFields(Set<ESDFieldConfig> pkFields) {
        this.pkFields = pkFields;
    }

    public ESDClass getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(ESDClass sourceClass) {
        this.sourceClass = sourceClass;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }

    public T getView() {
        return view;
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

    public DomainInst getDsmBean() {
        return dsmBean;
    }

    public void setDsmBean(DomainInst dsmBean) {
        this.dsmBean = dsmBean;
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

    public ESDClass getEsdClass() {
        return esdClass;
    }

    public void setEsdClass(ESDClass esdClass) {
        this.esdClass = esdClass;
    }

    public ViewEntityRefProxy getRef() {
        return ref;
    }

    public void setRef(ViewEntityRefProxy ref) {
        this.ref = ref;
    }
}
