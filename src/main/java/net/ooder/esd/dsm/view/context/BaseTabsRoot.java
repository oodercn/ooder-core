package net.ooder.esd.dsm.view.context;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.TabsAnnotation;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.bean.view.BaseTabsViewBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.aggregation.context.MethodRoot;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.web.util.MethodUtil;

import java.util.*;

public abstract class BaseTabsRoot<T extends BaseTabsViewBean, M extends TabListItem> extends BaseViewRoot<T> {

    private List<FieldFormConfig> allFields = new ArrayList<>();

    private List<FieldModuleConfig> items = new ArrayList<>();

    private List<M> tabItems = new ArrayList<>();

    private List<MethodRoot> methodRoots = new ArrayList<>();

    @JSONField(serialize = false)
    List<CustomModuleBean> moduleBeans = new ArrayList<>();

    private final Class[] baseClass = new Class[]{
            TabItem.class,
            TabListItem.class,
            TabsAnnotation.class

    };

    public BaseTabsRoot() {
        super();
    }


    public BaseTabsRoot(AggViewRoot viewRoot, T viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
        this.tabItems = viewBean.getTabItems();
        this.allFields = viewBean.getAllFields();
        this.items = viewBean.getNavItems();
        Set<Class> classes = new HashSet<>();
        classes.addAll(Arrays.asList(baseClass));
        this.moduleBeans = viewBean.getModuleBeans();
        this.methodRoots = initMethodRoots(moduleBeans);
        this.imports.addAll(getMethodsImpls(methodRoots));
        imports.addAll(viewRoot.getImports());

        try {
            this.imports.addAll(getDomainImpls(moduleName));
        } catch (JDSException e) {
            e.printStackTrace();
        }
        imports.remove(null);

    }


    protected List<MethodRoot> initMethodRoots(List<CustomModuleBean> moduleBeans) {
        List<MethodRoot> methodRoots = new ArrayList<>();
        for (CustomModuleBean customModuleBean : moduleBeans) {
            MethodRoot methodRoot = new MethodRoot(customModuleBean);
            methodRoots.add(methodRoot);
        }
        return methodRoots;
    }

    protected Set<String> getMethodsImpls(List<MethodRoot> methodRoots) {
        Set<String> imports = new HashSet<>();
        for (MethodRoot methodRoot : methodRoots) {
            List<CustomBean> customBeans = methodRoot.getAnnotationBeans();
            for (CustomBean customBean : customBeans) {
                imports.add(customBean.getClass().getName());
                try {
                    imports = MethodUtil.getAllImports(customBean.getClass(), imports);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (methodRoot.getModuleBean().getEuClassName() != null) {
                imports.add(methodRoot.getModuleBean().getEuClassName());
            }
        }
        return imports;
    }

    protected Set<String> getDomainImpls(String moduleName) throws JDSException {
        Set<String> imports = new HashSet<>();
        DomainInst domainInst = null;
        String domainPackageName = null;
        if (dsmBean instanceof DomainInst) {
            domainInst = (DomainInst) dsmBean;
            domainPackageName = domainInst.getPackageName();
        }
        if (!moduleName.equals(simClassName.toLowerCase()) && !moduleName.endsWith("." + simClassName.toLowerCase())) {
            if (domainInst != null) {
                domainPackageName = domainPackageName + "." + moduleName.toLowerCase() + "." + simClassName.toLowerCase();
            }
        } else {
            if (domainInst != null) {
                domainPackageName = domainPackageName + "." + moduleName.toLowerCase();
            }
        }
        List<CustomModuleBean> moduleBeans = viewBean.getModuleBeans();
        for (CustomModuleBean customModuleBean : moduleBeans) {
            MethodRoot methodRoot = new MethodRoot(customModuleBean);
            methodRoots.add(methodRoot);
            List<CustomBean> customBeans = methodRoot.getAnnotationBeans();
            for (CustomBean customBean : customBeans) {
                imports.add(customBean.getClass().getName());
                try {
                    imports = MethodUtil.getAllImports(customBean.getClass(), imports);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            //imports.add(customModuleBean.getModuleComponent().getClassName().toLowerCase());
        }


        if (domainInst != null) {
            if (this.getRepositoryImpls().size() > 0) {
                Set<String> domainJavaTempIds = domainInst.getJavaTempIds();
                for (String javaTempId : domainJavaTempIds) {
                    JavaTemp javaTemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
                    if (javaTemp != null && !javaTemp.getRangeType().equals(RangeType.MODULEVIEW) && javaTemp.getAggregationType().equals(AggregationType.ENTITY)) {
                        if (javaTemp.getPackagePostfix() == null || javaTemp.getPackagePostfix().equals("..")) {
                            this.imports.add(domainPackageName + ".*");
                        } else {
                            this.imports.add(domainPackageName + "." + javaTemp.getPackagePostfix() + ".*");
                        }
                    }

                }
            }
        }

        return imports;
    }


    public List<FieldFormConfig> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<FieldFormConfig> allFields) {
        this.allFields = allFields;
    }

    public List<FieldModuleConfig> getItems() {
        return items;
    }

    public void setItems(List<FieldModuleConfig> items) {
        this.items = items;
    }

    public List<M> getTabItems() {
        return tabItems;
    }

    public void setTabItems(List<M> tabItems) {
        this.tabItems = tabItems;
    }

    public List<MethodRoot> getMethodRoots() {
        return methodRoots;
    }

    public void setMethodRoots(List<MethodRoot> methodRoots) {
        this.methodRoots = methodRoots;
    }

    public List<CustomModuleBean> getModuleBeans() {
        return moduleBeans;
    }

    public void setModuleBeans(List<CustomModuleBean> moduleBeans) {
        this.moduleBeans = moduleBeans;
    }


}
