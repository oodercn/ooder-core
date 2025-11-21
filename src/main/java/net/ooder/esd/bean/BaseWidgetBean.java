package net.ooder.esd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.ui.AppendType;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CustomWidgetBean;
import net.ooder.esd.tool.properties.ModuleProperties;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.esd.util.OODUtil;
import net.ooder.esd.util.json.ComponentDeserializer;
import net.ooder.web.util.AnnotationUtil;
import ognl.Ognl;
import ognl.OgnlException;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseWidgetBean<T extends CustomViewBean, M extends Component> implements WidgetBean<T, M> {

    public CustomWidgetBean widgetBean;
    public ContainerBean containerBean;

    @JSONField(deserializeUsing = ComponentDeserializer.class)
    public M component;

    @JSONField(serialize = false)
    public T viewBean;
    public Class bindService1;
    public String euClassName;
    public String xpath;
    public List<JavaSrcBean> javaSrcBeans;


    public abstract T createViewBean(ModuleComponent currModuleComponent, M component);


    public List<JavaSrcBean> update(ModuleComponent parentModuleComponent, M component) {
        List<JavaSrcBean> allSrc = new ArrayList<>();
        this.initProperties(component);
        if (parentModuleComponent == null) {
            parentModuleComponent = component.getModuleComponent();
        }
        this.component = component;
        String realPath = this.getFieldRealPath(parentModuleComponent, component);
        this.setXpath(realPath);
        String parentModuleClassName = parentModuleComponent.getClassName();
        String projectName = parentModuleComponent.getProjectName();
        String packageName = parentModuleClassName.substring(0, parentModuleClassName.lastIndexOf(".")).toLowerCase();
        String moduleName = parentModuleClassName.substring(parentModuleClassName.lastIndexOf(".") + 1).toLowerCase();
        String simClass = OODUtil.formatJavaName(component.getAlias(), true);
        Properties properties = component.getProperties();
        if (euClassName == null) {
            try {
                Object className = Ognl.getValue("euClassName", properties);
                if (className != null && !className.equals("")) {
                    euClassName = className.toString();
                }
            } catch (OgnlException e) {
                //   e.printStackTrace();
            }
        }

        if (euClassName == null) {
            String simPack = packageName;
            if (!moduleName.toLowerCase().equals(simClass.toLowerCase())) {
                simPack = simPack + "." + moduleName;
            }
            euClassName = simPack.toLowerCase() + "." + simClass;
        }

        if (!euClassName.equals(parentModuleComponent.getClassName())) {
            ModuleProperties parentModuleProperties = parentModuleComponent.getProperties();
            DSMProperties dsmProperties = new DSMProperties();
            dsmProperties.setSourceClassName(parentModuleComponent.getClassName());
            ModuleProperties moduleProperties = new ModuleProperties();
            moduleProperties.setMethodName(OODUtil.getGetMethodName(component.getAlias()));
            moduleProperties.setDsmProperties(dsmProperties);
            DSMProperties parentDsmProperties = parentModuleProperties.getDsmProperties();
            if (parentDsmProperties != null) {
                dsmProperties.setDomainId(parentDsmProperties.getDomainId());
            }
            dsmProperties.setRealPath(realPath);
            ModuleComponent currModuleComponent = new ModuleComponent(component);
            currModuleComponent.setProperties(moduleProperties);
            currModuleComponent.setClassName(euClassName);
            viewBean = this.createViewBean(currModuleComponent, component);
            viewBean.setComponent(component);
            Class euClass = null;
            try {
                euClass = ClassUtility.loadClass(euClassName);
            } catch (ClassNotFoundException ee) {
            }
            try {
                if (euClass == null || viewBean.getBindService() == null || viewBean.getBindService().equals(Void.class)) {
                    CustomModuleBean customModuleBean = new CustomModuleBean(currModuleComponent);
                    AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(viewBean, euClassName, projectName);
                    List<JavaSrcBean> serviceList = aggRootBuild.build();
                    allSrc = aggRootBuild.getAllSrcBean();
                    if (serviceList.size() > 0) {
                        viewBean = (T) aggRootBuild.getCustomViewBean();
                        customModuleBean.reBindMethod(viewBean.getMethodConfig());
                    }
                }
                this.javaSrcBeans = allSrc;
                DSMFactory.getInstance().saveCustomViewBean(viewBean);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

        return allSrc;
    }

    public List<JavaSrcBean> getJavaSrcBeans() {
        if (javaSrcBeans == null) {
            javaSrcBeans = new ArrayList<>();
        }
        return javaSrcBeans;
    }

    public void setJavaSrcBeans(List<JavaSrcBean> javaSrcBeans) {
        this.javaSrcBeans = javaSrcBeans;
    }

    @Override
    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (containerBean != null) {
            annotationBeans.addAll(containerBean.getAnnotationBeans());
        }
        if (widgetBean != null && !AnnotationUtil.getAnnotationMap(widgetBean).isEmpty()) {
            annotationBeans.add(widgetBean);
        }
        return annotationBeans;
    }


    public void initProperties(M component) {
        if (containerBean == null) {
            containerBean = new ContainerBean(component);
        } else {
            containerBean.update(component);
        }
        widgetBean = new CustomWidgetBean(component.getProperties());
    }


    @Override
    public T getViewBean() {
        return viewBean;
    }


    protected String getFieldRealPath(ModuleComponent moduleComponent, Component component) {
        String realPath = component.getPath();
        if (moduleComponent == null) {
            moduleComponent = component.getModuleComponent();
        }
        if (moduleComponent != null && moduleComponent.getProperties() != null) {
            DSMProperties dsmProperties = moduleComponent.getProperties().getDsmProperties();
            if (dsmProperties != null && dsmProperties.getRealPath() != null) {
                realPath = dsmProperties.getRealPath();
                String fieldName = OODUtil.formatJavaName(component.getAlias(), false).toLowerCase();
                if (!OODUtil.formatJavaName(realPath, false).endsWith("." + fieldName)) {
                    realPath = realPath + "." + component.getAlias();
                }
            }
        }
        return realPath;
    }


    @Override
    @JSONField(serialize = false)
    public List<CustomBean> getFieldAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (this.getViewBean() != null && this.getViewBean().getModuleViewType().getAppendType().equals(AppendType.append)) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }

    public M getComponent() {
        return component;
    }

    public void setComponent(M component) {
        this.component = component;
    }

    @Override
    public void setViewBean(T viewBean) {
        this.viewBean = viewBean;
    }


    public String getEuClassName() {
        return euClassName;
    }

    public void setEuClassName(String euClassName) {
        this.euClassName = euClassName;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    public ContainerBean getContainerBean() {
        return containerBean;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }

    public CustomWidgetBean getWidgetBean() {
        return widgetBean;
    }

    public void setWidgetBean(CustomWidgetBean widgetBean) {
        this.widgetBean = widgetBean;
    }
}
