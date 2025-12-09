package net.ooder.esd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.ui.AppendType;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaGenSource;
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

    public Class bindService;

    public String euClassName;

    ModuleProperties moduleProperties;

    @JSONField(serialize = false)
    AggRootBuild fieldRootBuild;

    public String xpath;

    public abstract T createViewBean(ModuleComponent currModuleComponent, M component);


    public List<JavaGenSource> build() throws JDSException {
        List<JavaGenSource> serviceList = new ArrayList<>();
        AggRootBuild fieldRootBuild = getFieldRootBuild();
        if (fieldRootBuild.getAggServiceRootBean().isEmpty() || fieldRootBuild.getRepositorySrcList().isEmpty() || fieldRootBuild.getViewSrcList().isEmpty()) {
            serviceList = fieldRootBuild.build();
            if (serviceList.size() > 0) {
                viewBean = (T) fieldRootBuild.getCustomViewBean();
                viewBean.getModuleBean().reBindMethod(viewBean.getMethodConfig());
                DSMFactory.getInstance().saveCustomViewBean(viewBean);
            }
        } else {
            serviceList = fieldRootBuild.getAllGenBean();
        }
        return serviceList;
    }

    public AggRootBuild getFieldRootBuild() throws JDSException {
        if (fieldRootBuild == null) {
            if (viewBean == null) {
                viewBean = genViewBean();
            }
            fieldRootBuild = BuildFactory.getInstance().getAggRootBuild(viewBean, euClassName, null);
        }
        return fieldRootBuild;
    }

    public void initWidget(ModuleComponent parentModuleComponent, M component) {
        this.component = component;
        this.initProperties(component);
        if (parentModuleComponent == null) {
            parentModuleComponent = component.getModuleComponent();
        }
        String realPath = this.getFieldRealPath(parentModuleComponent, component);
        this.setXpath(realPath);
        String parentModuleClassName = parentModuleComponent.getClassName();
        String packageName = parentModuleClassName.substring(0, parentModuleClassName.lastIndexOf(".")).toLowerCase();
        String moduleName = parentModuleClassName.substring(parentModuleClassName.lastIndexOf(".") + 1).toLowerCase();
        String simClass = OODUtil.formatJavaName(component.getAlias(), true);
        ModuleProperties parentModuleProperties = parentModuleComponent.getProperties();
        DSMProperties dsmProperties = new DSMProperties();
        dsmProperties.setSourceClassName(parentModuleComponent.getClassName());
        DSMProperties parentDsmProperties = parentModuleProperties.getDsmProperties();
        if (parentDsmProperties != null) {
            dsmProperties.setDomainId(parentDsmProperties.getDomainId());
        }
        dsmProperties.setRealPath(this.getXpath());
        if (moduleProperties == null) {
            moduleProperties = new ModuleProperties();
        }
        moduleProperties.setMethodName(OODUtil.getGetMethodName(component.getAlias()));
        moduleProperties.setDsmProperties(dsmProperties);

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

    }


    public AggRootBuild initAggRootBuild(ModuleComponent parentModuleComponent, M component) throws JDSException {
        if (viewBean == null) {
            viewBean = genViewBean();
        }
        fieldRootBuild = BuildFactory.getInstance().getAggRootBuild(viewBean, euClassName, null);
        return fieldRootBuild;
    }


    public void update(ModuleComponent parentModuleComponent, M component) {
        if (moduleProperties == null) {
            initWidget(parentModuleComponent, component);
        }
        try {
            initAggRootBuild(parentModuleComponent, component);
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }

    @JSONField(serialize = false)
    public List<JavaSrcBean> getJavaSrcBeans() {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        if (viewBean == null) {
            viewBean = genViewBean();
        }
        if (viewBean != null) {
            javaSrcBeans.addAll(viewBean.getAllJavaSrc());
        }
        return javaSrcBeans;
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


    public T genViewBean() {
        if (viewBean == null && component != null) {
            ModuleComponent currModuleComponent = new ModuleComponent(component);
            CustomModuleBean customModuleBean = new CustomModuleBean(currModuleComponent);
            currModuleComponent.setProperties(moduleProperties);
            currModuleComponent.setClassName(euClassName);

            viewBean = this.createViewBean(currModuleComponent, component);
            viewBean.setModuleBean(customModuleBean);
            viewBean.setComponent(component);
        }
        return viewBean;
    }

    public T getViewBean() {
        return viewBean;
    }

    public Class getBindService() {
        return bindService;
    }

    public void setBindService(Class bindService) {
        this.bindService = bindService;
    }

    public ModuleProperties getModuleProperties() {

        return moduleProperties;
    }

    public void setModuleProperties(ModuleProperties moduleProperties) {
        this.moduleProperties = moduleProperties;
    }

    public void setFieldRootBuild(AggRootBuild fieldRootBuild) {
        this.fieldRootBuild = fieldRootBuild;
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
        if (viewBean == null) {
            viewBean = genViewBean();
        }
        if (viewBean != null && viewBean.getModuleViewType().getAppendType().equals(AppendType.append)) {
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
