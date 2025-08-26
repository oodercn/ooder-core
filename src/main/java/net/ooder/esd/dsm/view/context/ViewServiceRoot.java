package net.ooder.esd.dsm.view.context;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.java.DynamicClassLoader;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.ViewEntityConfig;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.esd.dsm.view.ref.ViewEntityRef;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDField;

import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.web.util.MethodUtil;
import net.ooder.web.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


public class ViewServiceRoot implements JavaRoot {

    public String packageName;

    public String className;

    public String rootId = "00000000-0000-0000-0000-000000000000";

    public String basepath;

    public String space;

    public String cnName;

    public ViewInst dsmBean;

    public String moduleName;

    public ESDClass sourceClass;

    public ESDClass esdClass;

    public Set<String> imports = new LinkedHashSet<>();

    public Set<ESDField> pkFields = new LinkedHashSet<ESDField>();

    private List<ESDField> hiddenFields = new ArrayList<ESDField>();

    private static final Class[] customClass = new Class[]{
            PageUtil.class,
            EsbUtil.class,
            ResultModel.class,
            ListResultModel.class,
            TreeListResultModel.class,
            MethodChinaName.class,
            Controller.class,
            ToolBarMenu.class,
            RequestMapping.class,
            ResponseBody.class};


    private ViewEntityConfig esdClassConfig;

    public ViewServiceRoot(ViewInst dsmBean, ViewEntityConfig esdClassConfig, List<ViewEntityRef> dsmRefs) {
        this.dsmBean = dsmBean;
        this.esdClassConfig = esdClassConfig;
        this.packageName = dsmBean.getPackageName();
        this.className = esdClassConfig.getClassName();
        this.space = dsmBean.getSpace();
        this.sourceClass = esdClassConfig.getSourceClass();
        this.esdClass = esdClassConfig.getCurrClass();
        this.basepath = dsmBean.getPackageName();
        this.hiddenFields = this.sourceClass.getHiddenFieldList();

        try {
            for (Class clazz : customClass) {
                imports.add(clazz.getName());
            }
            imports = MethodUtil.getAllImports(sourceClass.getCtClass(), imports);
            imports = MethodUtil.getAllImports(esdClassConfig.getCurrClass().getCtClass(), imports);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String basePackage = dsmBean.getPackageName() + "." + esdClass.getTopSourceClass().getEntityClass().getName().toLowerCase();
        Map<String, Class> classMap = new HashMap<>();
        classMap.putAll(ClassUtility.getDynClassMap());

        Set<Map.Entry<String, Class>> dynClassSet = classMap.entrySet();
        for (Map.Entry<String, Class> classEntry : dynClassSet) {
            Class dynClass = classEntry.getValue();
            if (dynClass.getPackage().getName().startsWith(basePackage)) {
                this.imports.add(dynClass.getPackage().getName() + ".*");
            }
        }


        for (Package pack : Package.getPackages()) {
            for (String packName : innerPacks) {
                if (pack.getName().startsWith(basePackage + "." + packName)) {
                    this.imports.add(pack.getName() + ".*");
                }
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

    }


    boolean hasCols(ESDField colProxy, Set<ESDField> cols) {
        for (ESDField customCol : cols) {
            if (customCol.getName().toLowerCase().equals(colProxy.getName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public ESDClass getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(ESDClass sourceClass) {
        this.sourceClass = sourceClass;
    }

    public ESDClass getEsdClass() {
        return esdClass;
    }

    public void setEsdClass(ESDClass esdClass) {
        this.esdClass = esdClass;
    }

    public List<ESDField> getHiddenFields() {
        return hiddenFields;
    }

    public void setHiddenFields(List<ESDField> hiddenFields) {
        this.hiddenFields = hiddenFields;
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

    public ViewInst getDsmBean() {
        return dsmBean;
    }

    public void setDsmBean(ViewInst dsmBean) {
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

    public Set<ESDField> getPkFields() {
        return pkFields;
    }

    public void setPkFields(Set<ESDField> pkFields) {
        this.pkFields = pkFields;
    }

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @JSONField(serialize = false)
    public ApiClassConfig getConfig() {
        ApiClassConfig apiConfig = esdClassConfig.getSourceConfig();
        return apiConfig;
    }

}
