package net.ooder.esd.dsm.aggregation.context;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.SimpleCustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.ui.AppendType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.EmbedType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.CustomAPICallBean;
import net.ooder.esd.bean.data.CustomDataBean;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.combo.CustomModuleRefFieldBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.util.OODUtil;
import net.ooder.web.ConstructorBean;
import net.ooder.web.RequestMappingBean;
import net.ooder.web.RequestMethodBean;
import net.ooder.web.RequestParamBean;
import net.ooder.web.util.MethodUtil;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodRoot {

    CustomModuleBean moduleBean;

    CustomDataBean dataBean;

    RequestMethodBean requestMethodBean;

    RequestMappingBean requestMapping;

    CustomAPICallBean apiCallBean;


    public MethodRoot(CustomModuleBean moduleBean) {
        this.update(moduleBean, null);
    }

    public MethodRoot(CustomModuleBean moduleBean, Component component) {
        this.update(moduleBean, component);
    }

    public void update(CustomModuleBean moduleBean, Component component) {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        ModuleComponent moduleComponent = moduleBean.getModuleComponent();
        List<RequestParamBean> paramBeans = new ArrayList<>();
        ModuleComponent parentModuleComponent = null;
        if (component != null) {
            parentModuleComponent = component.getModuleComponent();
        }
        String euClassName = moduleBean.getEuClassName();
        if (moduleComponent != null && moduleComponent.getClassName() != null) {
            euClassName = moduleComponent.getClassName();
            List<Component> components = moduleComponent.findComponents(ComponentType.HIDDENINPUT, null);
            for (Component childcomponent : components) {
                if (!Arrays.asList(DSMFactory.SkipParams).contains(childcomponent.getAlias())) {
                    RequestParamBean requestParamBean = new RequestParamBean(childcomponent.getAlias(), String.class, null);
                    paramBeans.add(requestParamBean);
                }
            }
        }

        if (parentModuleComponent != null) {
            String parentModuleClassName = parentModuleComponent.getClassName();
            String simClass = OODUtil.formatJavaName(component.getAlias(), true);
            if (euClassName == null) {
                euClassName = parentModuleClassName.toLowerCase() + "." + simClass;
            } else if (!parentModuleClassName.equals(euClassName) && moduleBean.getSourceClassName() == null) {
                euClassName = parentModuleClassName.toLowerCase() + "." + simClass;
            }
        }

        if (euClassName == null) {
            if (moduleBean.getMethodConfig() != null) {
                euClassName = moduleBean.getMethodConfig().getEUClassName();
            } else {
                String moduleName = moduleBean.getAlias() != null ? moduleBean.getAlias() : OODUtil.formatJavaName(moduleBean.getMethodName(), false);
                euClassName = OODUtil.formatJavaName(moduleName, true);
            }
        }

        String packageName = "";
        String simClass = euClassName;
        if (euClassName.indexOf(".") > -1) {
            packageName = euClassName.substring(0, euClassName.lastIndexOf("."));
            simClass = euClassName.substring(euClassName.lastIndexOf(".") + 1);//.toLowerCase();
        }
        simClass = OODUtil.formatJavaName(simClass, true);
        euClassName = packageName + "." + simClass;
        if (dataBean == null) {
            if (moduleComponent != null && moduleComponent.getMethodAPIBean() != null) {
                dataBean = moduleComponent.getMethodAPIBean().getDataBean();
            } else {
                ModuleViewType moduleViewType = moduleBean.getModuleViewType();
                if (moduleViewType != null) {
                    if (moduleViewType.equals(ModuleViewType.LAYOUTCONFIG) && moduleBean.getModuleComponent() != null) {
                        moduleViewType = moduleBean.getModuleComponent().guessComponentType();
                    }
                    try {
                        dataBean = (CustomDataBean) ClassUtility.loadClass(moduleViewType.getDataClassName()).newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (requestMapping == null) {
            requestMapping = new RequestMappingBean(simClass, packageName.replace(".", "/"));
        }
        if (requestMethodBean == null) {
            requestMethodBean = new RequestMethodBean(simClass, euClassName, paramBeans);
        } else {
            requestMethodBean.setReturnClassName(euClassName);
        }
        if (apiCallBean == null) {
            apiCallBean = new CustomAPICallBean();
            apiCallBean.getBindTabsEvent().add(CustomTabsEvent.TABEDITOR);
        }
        this.moduleBean = moduleBean;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAllAnnotation() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (dataBean != null) {
            annotationBeans.add(dataBean);
        }
        annotationBeans.add(apiCallBean);
        annotationBeans.add(requestMapping);
        annotationBeans.addAll(moduleBean.getAnnotationBeans());
        if (!annotationBeans.contains(new SimpleCustomBean(ResponseBody.class))) {
            annotationBeans.add(new SimpleCustomBean(ResponseBody.class));
        }
        return annotationBeans;
    }


    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();

        if (moduleBean.getMethodConfig() != null) {
            CustomFieldBean fieldBean = moduleBean.getMethodConfig().getFieldBean();
            if (fieldBean == null) {
                if (moduleBean.getIndex() > 0) {
                    fieldBean = new CustomFieldBean();
                    fieldBean.setIndex(moduleBean.getIndex());
                    annotationBeans.add(fieldBean);
                }
            } else {
                annotationBeans.add(fieldBean);
            }

        }
        ModuleViewType moduleViewType = moduleBean.getModuleViewType();
        if (moduleViewType != null) {
            if (moduleViewType.getAppendType().equals(AppendType.append)) {
                annotationBeans.addAll(moduleBean.getUIAnnotationBeans());
            } else if (moduleBean.getBindService() != null && !moduleBean.getBindService().equals(Void.class)) {
                CustomModuleRefFieldBean refFieldBean = new CustomModuleRefFieldBean(moduleBean);
                refFieldBean.setEmbed(EmbedType.component);
                annotationBeans.add(refFieldBean);
                annotationBeans.addAll(moduleBean.getAnnotationBeans());
            } else {
                if (dataBean != null) {
                    annotationBeans.add(dataBean);
                }
                annotationBeans.add(apiCallBean);
                annotationBeans.add(requestMapping);
            }
        } else {
            annotationBeans.addAll(moduleBean.getUIAnnotationBeans());
        }

        if (!annotationBeans.contains(new SimpleCustomBean(ResponseBody.class))) {
            annotationBeans.add(new SimpleCustomBean(ResponseBody.class));
        }
        return annotationBeans;
    }


    public CustomDataBean getDataBean() {
        return dataBean;
    }

    public void setDataBean(CustomDataBean dataBean) {
        this.dataBean = dataBean;
    }

    public RequestMappingBean getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(RequestMappingBean requestMapping) {
        this.requestMapping = requestMapping;
    }

    public CustomAPICallBean getApiCallBean() {
        return apiCallBean;
    }

    public void setApiCallBean(CustomAPICallBean apiCallBean) {
        this.apiCallBean = apiCallBean;
    }

    public String toMethodStr() {
        ModuleViewType viewType = moduleBean.getModuleViewType();
        if (viewType == null && moduleBean.getMethodConfig() != null) {
            viewType = this.getModuleBean().getMethodConfig().getModuleViewType();
        }
        StringBuffer stringBuffer = MethodUtil.genJavaMethodStr(requestMethodBean, viewType.getDefaultView(), true);
        return stringBuffer.toString();
    }

    @JSONField(serialize = false)
    public String toConstructorStr() {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            ConstructorBean constructorBean = new ConstructorBean(moduleBean.getName(), requestMethodBean.getParamSet().toArray(new RequestParamBean[]{}));
            stringBuffer = MethodUtil.toConstructorStr(constructorBean);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    public CustomModuleBean getModuleBean() {
        return moduleBean;
    }

    public void setModuleBean(CustomModuleBean moduleBean) {
        this.moduleBean = moduleBean;
    }

    public RequestMethodBean getRequestMethodBean() {
        return requestMethodBean;
    }

    public void setRequestMethodBean(RequestMethodBean requestMethodBean) {
        this.requestMethodBean = requestMethodBean;
    }
}
