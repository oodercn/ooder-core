package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.CustomBlockBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.CustomModuleEmbedFieldBean;
import net.ooder.esd.bean.field.combo.CustomModuleRefFieldBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.properties.*;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomFieldModuleComponent extends BlockComponent {


    public CustomFieldModuleComponent(EUModule module, FieldFormConfig<?, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super(Dock.fill, field.getFieldname());
        CustomModuleRefFieldBean fieldBean = null;
        if (field.getWidgetConfig() instanceof CustomModuleEmbedFieldBean) {
            fieldBean = new CustomModuleRefFieldBean((CustomModuleEmbedFieldBean) field.getWidgetConfig());
        }


        EUModule euModule = module;
        MethodConfig sourceMethodConfig = module.getComponent().getMethodAPIBean();
        try {
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(module.getProjectVersion().getVersionName());
            ApiClassConfig classConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceMethodConfig.getSourceClassName());
            MethodConfig fieldMethodConfig = null;
            if (fieldBean.getSrc() != null && !fieldBean.getSrc().equals("")) {
                fieldMethodConfig = classConfig.getMethodByName(fieldBean.getSrc());
            } else if (fieldBean.getBindClass() != null && !fieldBean.getBindClass().equals(Void.class)) {
                ApiClassConfig bindConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(fieldBean.getBindClass().getName());
                fieldMethodConfig = bindConfig.getMethodByItem(CustomMenuItem.INDEX);
            } else {
                ApiClassConfig bindConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(field.getViewClassName());
                if (bindConfig != null) {
                    fieldMethodConfig = bindConfig.getMethodByName(field.getFieldname());
                }

            }

            if (fieldMethodConfig != null) {
                CustomBlockBean blockBean = fieldMethodConfig.getModuleBean().getBlockBean();
                blockBean.setContainerBean(field.getContainerBean());
                CustomBlockBean methodBlock = new CustomBlockBean(AnnotationUtil.getAllAnnotations(fieldMethodConfig.getMethod(),true));
                //合并方法注解
                OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(methodBlock), Map.class), blockBean, false, false);
                BlockProperties blockProperties = new BlockProperties(blockBean);
                if (blockProperties.getDock() == null) {
                    blockProperties.setDock(Dock.fill);
                }
                blockProperties.setId(field.getId());
                blockProperties.setName(field.getFieldname());
                EUModule newmodule = ESDFacrory.getAdminESDClient().getCustomModule(fieldMethodConfig, version.getVersionName(), valueMap);
                if (newmodule != null) {
                    switch (fieldBean.getAppend()) {
                        case ref:
                            ModuleComponent moduleComponent = new ModuleComponent<>();
                            moduleComponent.setClassName(newmodule.getClassName());
                            moduleComponent.setAlias(newmodule.getComponent().getAlias());
                            this.addChildren(moduleComponent);
                            break;
                        case append:
                            CustomFieldBean customFieldBean = field.getCustomBean();
                            if (customFieldBean != null) {
                                blockProperties.setDesc(field.getCustomBean().getCaption());
                            } else {
                                blockProperties.setCaption(field.getAggConfig().getCaption());
                            }
                            if (newmodule != null) {
                                ComponentList childlist = newmodule.getComponent().getChildren();
                                if (childlist != null) {
                                    List<Component> uiComponents = new ArrayList<Component>();
                                    for (Component childcomponent : childlist) {
                                        ComponentType componentType = ComponentType.fromType(childcomponent.getKey());
                                        if (childcomponent instanceof SVGPaperComponent || componentType.isModuleObj()) {
                                            euModule.getComponent().addChildren(childcomponent);
                                        } else {
                                            uiComponents.add(childcomponent);
                                        }

                                        if (childcomponent instanceof APICallerComponent) {
                                            APICallerComponent apiCallerComponent = (APICallerComponent) childcomponent;
                                            String apiCallName = newmodule.getComponent().getAlias() + "_" + apiCallerComponent.getAlias();
                                            List<Action> actions = newmodule.getComponent().getAllAction();
                                            for (Action action : actions) {
                                                if (action.getTarget().equals(apiCallerComponent.getAlias())) {
                                                    action.setTarget(apiCallName);
                                                }
                                            }
                                            apiCallerComponent.setAlias(apiCallName);
                                            if (euModule.getComponent().getTopComponentBox() != null) {
                                                UrlPath urlPath = new UrlPathData(euModule.getComponent().getTopComponentBox().getAlias(), RequestPathTypeEnum.FORM, "");
                                                apiCallerComponent.getProperties().addRequestData(urlPath);
                                            }
                                        }
                                    }

                                    if (fieldBean.getEmbed() == null || fieldBean.getEmbed().equals(EmbedType.module)) {
                                        while (uiComponents.size() == 1 && uiComponents.get(0).getChildren().size() > 1) {
                                            uiComponents = uiComponents.get(0).getChildren();
                                        }
                                    } else {
                                        uiComponents = new ArrayList<>();
                                        uiComponents.add(newmodule.getComponent().getCurrComponent());
                                    }
                                    for (Component childcomponent : uiComponents) {
                                        this.addChildren(childcomponent);
                                    }
                                }
                            }
                            break;
                        case runtime:
                            moduleComponent = new ModuleComponent<>();
                            BlockComponent currComponent = new BlockComponent(Dock.fill, module.getName() + ModuleComponent.DefaultTopBoxfix);
                            currComponent.getProperties().setBorderType(BorderType.none);
                            currComponent.setTarget(target);
                            this.addChildren(currComponent);
                            module.getComponent().addChildren(genAPIComponent(currComponent, moduleComponent, fieldMethodConfig));
                            break;

                    }
                }


                this.setProperties(blockProperties);
            } else {
                this.getProperties().setHtml("module " + fieldBean.getSrc() + " not fround!");
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }


        this.setTarget(target);
    }


    //数据对象
    @JSONField(serialize = false)
    APICallerComponent[] genAPIComponent(Component boxComponent, ModuleComponent moduleComponent, MethodConfig methodBean) throws JDSException {
        List<APICallerComponent> apiCallerComponents = new ArrayList<APICallerComponent>();
        APICallerComponent reloadAPI = new APICallerComponent(methodBean);
        reloadAPI.setAlias(CustomFormAction.DYNRELOAD.getTarget());
        //刷新调用
        APICallerProperties reloadProperties = reloadAPI.getProperties();
        UrlPathData treepathData = new UrlPathData(moduleComponent.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
        reloadProperties.addRequestData(treepathData);

        UrlPathData formData = new UrlPathData(boxComponent.getAlias(), ResponsePathTypeEnum.COMPONENT, "data");
        reloadProperties.addResponseData(formData);

//        UrlPathData formCtxData = new UrlPathData(moduleComponent.getCtxBaseComponent().getAlias(), ResponsePathTypeEnum.form, "data");
//        reloadProperties.addResponseData(formCtxData);
        reloadProperties.setAutoRun(true);

        String url = reloadProperties.getQueryURL();

        if (url.indexOf("?") > -1) {
            String httpUrl = url.split("\\?")[0];
            String queryStr = url.split("\\?")[1];
            if (httpUrl.endsWith(".dyn")) {
                httpUrl = httpUrl + ".dyn";
            }
            url = httpUrl + "?" + queryStr;
        } else {
            if (!url.endsWith(".dyn")) {
                url = url + ".dyn";
            }
        }
        reloadProperties.setQueryURL(url);
        apiCallerComponents.add(reloadAPI);


        return apiCallerComponents.toArray(new APICallerComponent[]{});
    }


}
