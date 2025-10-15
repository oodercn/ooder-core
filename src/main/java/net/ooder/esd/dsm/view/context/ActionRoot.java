package net.ooder.esd.dsm.view.context;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.SimpleCustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.event.CustomEvent;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.CustomAPICallBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.util.OODUtil;
import net.ooder.web.ConstructorBean;
import net.ooder.web.RequestMappingBean;
import net.ooder.web.RequestMethodBean;
import net.ooder.web.RequestParamBean;
import net.ooder.web.util.MethodUtil;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

public class ActionRoot {

    CustomModuleBean moduleBean;

    RequestMethodBean requestMethodBean;

    RequestMappingBean requestMapping;

    CustomAPICallBean apiCallBean;

    CustomFieldBean fieldBean;

    MenuBarBean subMenuBarBean;

    TreeListItem listItem;

    Integer index = 0;

    public ActionRoot(TreeListItem listItem, Integer index, List<RequestParamBean> paramBeans, List<CustomEvent> events) {
        if (paramBeans == null) {
            paramBeans = new ArrayList<>();
        }
        if (events == null) {
            events = new ArrayList<>();
        }

        ModuleComponent moduleComponent = null;
        if (listItem.getEuClassName() != null) {
            try {
                EUModule module = ESDFacrory.getAdminESDClient().getModule(listItem.getEuClassName(), null);
                if (module == null) {
                    module = CustomViewFactory.getInstance().getView(listItem.getEuClassName(), null);
                }
                if (module != null) {
                    moduleComponent = module.getComponent();
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

        if (moduleComponent != null) {
            CustomViewBean customViewBean = null;
            if (moduleComponent.getMethodAPIBean() != null) {
                customViewBean = moduleComponent.getMethodAPIBean().getView();
                moduleBean = customViewBean.getModuleBean();
                if (moduleBean == null) {
                    moduleBean = new CustomModuleBean(moduleComponent);
                } else {
                    moduleBean.update(moduleComponent);
                }
                moduleBean.reBindMethod(moduleComponent.getMethodAPIBean());
            }

            String moduleName = moduleBean.getAlias() != null ? moduleBean.getAlias() : OODUtil.formatJavaName(moduleBean.getMethodName(), false);
            moduleName = OODUtil.formatJavaName(moduleName, false);
            requestMethodBean = new RequestMethodBean(moduleName, moduleComponent.getClassName(), paramBeans);
            requestMapping = new RequestMappingBean(moduleName, moduleName);
        } else {
            String moduleName = listItem.getMethodName();
            if (moduleName == null) {
                if (listItem.getId() != null) {
                    moduleName = listItem.getId();
                } else {
                    moduleName = listItem.getType().name();
                }
            }
            moduleName = OODUtil.formatJavaName(moduleName, false);

            Class[] bindClassList = listItem.getBindClass();
            if (bindClassList!=null && bindClassList.length>0){
                String bindClass = bindClassList[0].getName();
                requestMethodBean = new RequestMethodBean(moduleName, bindClass, paramBeans);

            }else{
                requestMethodBean = new RequestMethodBean(moduleName, moduleName, paramBeans);
            }

            requestMapping = new RequestMappingBean(moduleName, moduleName);
        }


        if (listItem.getSub() != null && listItem.getSub().size() > 0) {
            subMenuBarBean = new MenuBarBean();
            subMenuBarBean.setMenuType(CustomMenuType.SUB);
        }

        this.index = index;
        fieldBean = new CustomFieldBean(listItem);
        apiCallBean = new CustomAPICallBean();
        for (CustomEvent customEvent : events) {
            apiCallBean.addBindEvent(customEvent);
        }

    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();

        if (moduleBean != null) {
            annotationBeans.addAll(moduleBean.getAnnotationBeans());
            ModuleViewType moduleViewType = moduleBean.getModuleViewType();
            try {

                if (moduleViewType != null) {
                    Class beanClass = ClassUtility.loadClass(moduleViewType.getDataClassName());
                    if (!beanClass.equals(Void.class) && !beanClass.equals(Enum.class)) {
                            annotationBeans.add((CustomBean) beanClass.newInstance());
                        }
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (subMenuBarBean != null) {
            annotationBeans.add(subMenuBarBean);
        }
        annotationBeans.add(listItem);
        annotationBeans.add(fieldBean);
        annotationBeans.add(requestMapping);
        annotationBeans.add(apiCallBean);
        annotationBeans.add(new SimpleCustomBean(ResponseBody.class));
        return annotationBeans;
    }


    public String toMethodStr() {
        StringBuffer stringBuffer = new StringBuffer();
        if (moduleBean != null) {
            stringBuffer = MethodUtil.genJavaMethodStr(requestMethodBean, moduleBean.getModuleViewType().getDefaultView(), false);
        } else {
            stringBuffer = MethodUtil.genJavaMethodStr(requestMethodBean, ViewType.BLOCK, false);
        }
        return stringBuffer.toString();
    }

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

    public TreeListItem getListItem() {
        return listItem;
    }

    public void setListItem(TreeListItem listItem) {
        this.listItem = listItem;
    }

    public CustomFieldBean getFieldBean() {
        return fieldBean;
    }

    public void setFieldBean(CustomFieldBean fieldBean) {
        this.fieldBean = fieldBean;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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
