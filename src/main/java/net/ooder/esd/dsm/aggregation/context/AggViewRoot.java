package net.ooder.esd.dsm.aggregation.context;

import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.event.CustomCallBack;
import net.ooder.esd.annotation.FormAnnotation;
import net.ooder.esd.annotation.GridAnnotation;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.UIType;
import net.ooder.esd.annotation.view.*;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMInst;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.ModuleProperties;
import net.ooder.esd.tool.properties.fchart.Categories;
import net.ooder.esd.tool.properties.fchart.LineData;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.web.util.MethodUtil;
import net.ooder.web.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


public class AggViewRoot implements JavaRoot {

    public String packageName;

    public String className;

    public String simClassName;

    public String basepath;

    public String baseUrl;

    public String space;

    public String cnName;

    public DSMInst dsmBean;

    public String moduleName;

    public Set<String> imports = new LinkedHashSet<>();

    private List<ESDField> hiddenFields = new ArrayList<ESDField>();

    MethodRoot methodRoot;

    CustomModuleBean moduleBean;


    private static final Class[] customClass = new Class[]{
            PageUtil.class,
            EsbUtil.class,
            ResultModel.class,
            ListResultModel.class,
            TreeListResultModel.class,
            CustomMenuItem.class,
            CustomCallBack.class,
            UIType.class,
            View.class,
            LineData.class,
            Categories.class,
            ModuleViewType.class,
            Aggregation.class,
            LayoutViewAnnotation.class,
            ButtonViewsViewAnnotation.class,

            NavTreeViewAnnotation.class,
            APIEventAnnotation.class,
            FormAnnotation.class,
            GridAnnotation.class,
            TreeAnnotation.class,
            TabsViewAnnotation.class,
            FormViewAnnotation.class,
            TreeViewAnnotation.class,
            GalleryViewAnnotation.class,
            TabsAnnotation.class,
            GridViewAnnotation.class,
            ModuleAnnotation.class,
            MethodChinaName.class,
            Controller.class,
            SVGPagerViewAnnotation.class,
            ToolBarMenu.class,
            RequestMapping.class,
            ResponseBody.class
    };

    public AggViewRoot(DSMInst dsmBean) {
        this.dsmBean = dsmBean;

    }


    public AggViewRoot(DSMInst dsmBean, String className, CustomModuleBean moduleBean) {


        this.dsmBean = dsmBean;
        this.className = className;
        this.packageName = className.substring(0, className.lastIndexOf("."));
        this.simClassName = className.substring(className.lastIndexOf(".") + 1);
        this.moduleName = "";
        String baseUrl = dsmBean.getProjectVersionName() + "." + dsmBean.getSpace();
        if (packageName.startsWith(baseUrl + ".")) {
            moduleName = packageName.substring((baseUrl + ".").length()).toLowerCase();
        }
        this.baseUrl = StringUtility.replace(packageName, ".", "/");
        this.space = dsmBean.getSpace();
        this.basepath = dsmBean.getPackageName();

        if (moduleBean != null) {
            if (moduleBean.getModuleComponent() == null) {
                ModuleComponent moduleComponent = new ModuleComponent();
                ModuleProperties moduleProperties = new ModuleProperties();
                moduleComponent.setProperties(moduleProperties);
                moduleComponent.setClassName(className);
                moduleBean.setModuleComponent(moduleComponent);
            }
            moduleBean.getModuleComponent().setClassName(className);
            moduleBean.setEuClassName(className);
        }


        this.moduleBean = moduleBean;
        this.methodRoot = new MethodRoot(moduleBean);

        List<CustomBean> customBeans = methodRoot.getAnnotationBeans();
        for (CustomBean customBean : customBeans) {
            imports.add(customBean.getClass().getName());
            try {
                imports = MethodUtil.getAllImports(customBean.getClass(), imports);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        for (Class clazz : customClass) {
            imports.add(clazz.getName());
        }


        Set<String> basePackages = new HashSet();
        Map<String, Class> classMap = new HashMap<>();
        classMap.putAll(ClassUtility.getDynClassMap());

        Set<Map.Entry<String, Class>> dynClassSet = classMap.entrySet();
        for (Map.Entry<String, Class> classEntry : dynClassSet) {
            Class dynClass = classEntry.getValue();
            for (String basePackage : basePackages) {
                if (dynClass.getPackage().getName().startsWith(basePackage)) {
                    this.imports.add(dynClass.getPackage().getName() + ".*");
                }
            }

        }

        if (moduleBean.getViewBean() != null && moduleBean.getViewBean().getViewClassName() != null) {
            Set<Class> otherClass = moduleBean.getViewBean().getOtherClass();
            if (otherClass != null) {
                for (Class other : otherClass) {
                    if (other != null) {
                        imports.add(other.getPackage().getName() + ".*");
                    }
                }
            }
        }

//        if (moduleBean.getModuleComponent() != null) {
//            String viewClassName = moduleBean.getModuleComponent().getClassName();
//            try {
//                if (viewClassName != null && ClassUtility.loadClass(viewClassName) != null) {
//                    imports.add(viewClassName);
//                }
//            } catch (Throwable e) {
//                //e.printStackTrace();
//            }
//        }

        for (Package pack : Package.getPackages()) {
            for (String packName : innerPacks) {
                for (String basePackage : basePackages) {
                    if (pack.getName().startsWith(basePackage + "." + packName)) {
                        this.imports.add(pack.getName() + ".*");
                    }
                }
            }
        }


        for (Package pack : ClassUtility.getAllDynPacks()) {
            for (String basePackage : basePackages) {
                if (pack.getName().startsWith(basePackage)) {
                    this.imports.add(pack.getName() + ".*");
                }
            }
        }


        try {
            Set<String> javaTempIds = dsmBean.getJavaTempIds();
            for (String javaTempId : javaTempIds) {
                JavaTemp javaTemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
                if (javaTemp != null && javaTemp.getAggregationType() != null && javaTemp.getAggregationType().equals(AggregationType.ROOT)) {
                    for (String basePackage : basePackages) {
                        if (javaTemp.getPackagePostfix() == null || javaTemp.getPackagePostfix().equals("..")) {
                            this.imports.add(basePackage + ".*");
                        } else {
                            this.imports.add(basePackage + "." + javaTemp.getPackagePostfix() + ".*");
                        }
                    }
                }
            }
            this.imports.remove(packageName + ".*");
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getSimClassName() {
        return simClassName;
    }

    public void setSimClassName(String simClassName) {
        this.simClassName = simClassName;
    }

    public CustomModuleBean getModuleBean() {
        return moduleBean;
    }

    public void setModuleBean(CustomModuleBean moduleBean) {
        this.moduleBean = moduleBean;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public MethodRoot getMethodRoot() {
        return methodRoot;
    }

    public void setMethodRoot(MethodRoot methodRoot) {
        this.methodRoot = methodRoot;
    }

    public List<ESDField> getHiddenFields() {
        return hiddenFields;
    }

    public void setHiddenFields(List<ESDField> hiddenFields) {
        this.hiddenFields = hiddenFields;
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

    public DSMInst getDsmBean() {
        return dsmBean;
    }

    public void setDsmBean(DSMInst dsmBean) {
        this.dsmBean = dsmBean;
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


    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }


}
