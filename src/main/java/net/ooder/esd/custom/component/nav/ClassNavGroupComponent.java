package net.ooder.esd.custom.component.nav;


import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.RequestPathAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.nav.GroupItemBean;
import net.ooder.esd.custom.properties.ClassNavGroupProperties;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.UrlPathData;
import net.ooder.esd.util.OODUtil;
import net.ooder.web.RequestParamBean;

import java.util.*;

public class ClassNavGroupComponent extends GroupComponent {

    public ClassNavGroupComponent(EUModule parentModule, GroupItemBean itemBean, Map valueMap) throws JDSException {
        this.setAlias(OODUtil.formatJavaName(itemBean.getMethodName(), true));
        init(itemBean.getMethodConfig(), parentModule, valueMap);
        this.setProperties(new ClassNavGroupProperties(itemBean, itemBean.getContainerBean()));

    }

    void init(MethodConfig methodConfig, EUModule parentModule, Map valueMap) throws JDSException {
        EUModule childModule = methodConfig.getModule(valueMap, parentModule.getProjectVersion().getProjectName());
        ModuleComponent childmoduleComponent = childModule.getComponent();
        ModuleComponent mainModuleComponent = this.getModuleComponent();
        if (mainModuleComponent == null) {
            mainModuleComponent = parentModule.getComponent();
        }


        if (methodConfig != null && methodConfig.getModuleBean().getDynLoad() != null && methodConfig.getModuleBean().getDynLoad() && childmoduleComponent.getTopComponentBox() != null) {
            Component topComponent = childmoduleComponent.getTopComponentBox();
            List<APICallerComponent> components = childmoduleComponent.findComponents(ComponentType.APICALLER.name(), null);
            for (APICallerComponent apicomponent : components) {
                String innerAlias = childmoduleComponent.getAlias() + "_" + apicomponent.getAlias();
                List<Action> actions = apicomponent.getAllAction();
                for (Action action : actions) {
                    if (action.getMethod().equals("invoke")) {
                        action.getArgs().set(0, "{page." + innerAlias + ".invoke()}");
                    }
                    action.setTarget(innerAlias);
                }
                apicomponent.setAlias(innerAlias);
                parentModule.getComponent().addChildren(apicomponent);
                UrlPath urlPath = new UrlPathData();
                urlPath.setName(topComponent.getAlias());
                urlPath.setPath("");
                urlPath.setType(RequestPathTypeEnum.FORM);
                apicomponent.getProperties().addRequestData(urlPath);
                apicomponent.getProperties().addRequestData(new UrlPathData((RequestPathAnnotation) RequestPathEnum.CTX));
            }

            List<Action> actions = childmoduleComponent.findAction(null);
            for (Action action : actions) {
                if (action.getTarget() != null && action.getTarget().equals(childmoduleComponent.getClassName())) {
                    action.setTarget(parentModule.getClassName());
                }
                if (action.getRedirection() != null && action.getRedirection().indexOf(childmoduleComponent.getClassName()) > -1) {
                    action.setRedirection(StringUtility.replace(action.getRedirection(), childmoduleComponent.getClassName(), parentModule.getClassName()));
                }
            }

            if (topComponent == null) {
                topComponent = childmoduleComponent.getTopComponentBox();
            }
            this.addChildren(topComponent);
            CtxBaseComponent ctxBaseComponent = parentModule.getComponent().getCtxBaseComponent();
            Set<String> keySet = ctxBaseComponent.getCtxMap().keySet();
            for (String key : keySet) {
                HiddenInputComponent hiddenInputComponent = childmoduleComponent.getCtxBaseComponent().getFieldByName(key);
                if (hiddenInputComponent != null) {
                    childmoduleComponent.getCtxBaseComponent().getChildren().remove(hiddenInputComponent);
                }

            }
            Map params = new HashMap();
            params.put("projectName", parentModule.getProjectVersion().getVersionName());
            parentModule.addParams(params);
        } else {
            if (childmoduleComponent.getModuleViewType() != null && childmoduleComponent.getModuleViewType().equals(ModuleViewType.DYNCONFIG)) {
                Component component = childmoduleComponent.getTopComponentBox();
                if (mainModuleComponent.findComponentByAlias(component.getAlias()) == null) {
                    this.addChildren(component);
                }
                List<Component> apiComponents = childmoduleComponent.findComponents(ComponentType.APICALLER, null);
                for (Component apiComponent : apiComponents) {
                    if (mainModuleComponent.findComponentByAlias(component.getAlias()) == null) {
                        parentModule.getComponent().addChildren(apiComponent);
                    }
                }
            } else {
                ModuleComponent moduleComponent = new ModuleComponent(childModule.getClassName());
                moduleComponent.setClassName(childModule.getClassName());
                moduleComponent.setAlias(childModule.getComponent().getAlias() + "Module");
                Map<String, Object> tagMap = new HashMap<>();
                Set<RequestParamBean> paramBeans = methodConfig.getParamSet();
                for (RequestParamBean paramBean : paramBeans) {
                    Object obj = valueMap.get(paramBean.getParamName());
                    if (!Arrays.asList(DSMFactory.SkipParams).contains(paramBean.getParamName())) {
                        if (obj != null && !obj.equals("")) {
                            tagMap.put(paramBean.getParamName(), obj);
                            moduleComponent.addParams(tagMap);
                        }
                    }
                }
                this.addChildren(moduleComponent);
            }
            this.setAlias(methodConfig.getId());

        }
    }

}
