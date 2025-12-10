package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.RequestPathTypeEnum;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.data.CustomDynDataBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.engine.enums.PackagePathType;
import net.ooder.esd.tool.component.*;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.UrlPathData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomDynLoadView<M extends BlockComponent> extends CustomModuleComponent<M> {

    private String saveUrl;

    private String dynReloadUrl;

    CustomDynLoadView() {
        super();
    }


    public CustomDynLoadView(EUModule module, MethodConfig methodConfig, Map valueMap) throws ClassNotFoundException {
        super(module, methodConfig, valueMap);
        M currComponent = (M) new BlockComponent(Dock.fill, module.getName() + DefaultTopBoxfix);
        currComponent.getProperties().setBorderType(BorderType.none);
        this.addChildLayoutNav(currComponent);
        this.setCurrComponent(currComponent);
        this.setModuleViewType(ModuleViewType.DYNCONFIG);
        CustomDynDataBean dataBean = methodConfig.getDynDataBean();
        if (dataBean != null) {
            this.saveUrl = dataBean.getSaveUrl();
            this.dynReloadUrl = dataBean.getDataUrl();

            if (dynReloadUrl == null || dynReloadUrl.equals("")) {
                dynReloadUrl = methodConfig.getUrl();
            }

            String refClassName = dataBean.getRefClassName();
            if (!refClassName.equals("")) {
                PackagePathType pathType = PackagePathType.startPath(refClassName.replace(".", "/"));
                String projectName = dataBean.getProjectName();
                if (projectName == null || projectName.equals("")) {
                    if (pathType != null) {
                        projectName = pathType.getApiType().getDefaultProjectName();
                    }
                }


                try {
                    if (projectName == null || projectName.equals("")) {
                        projectName = DSMFactory.getInstance().getDefaultProjectName();
                    }
                    ProjectVersion projectVersion = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
                    EUModule realModule = projectVersion.getModule(refClassName);
                    if (realModule != null) {
                        ModuleComponent moduleComponent = realModule.getComponent();
                        switch (dataBean.getAppend()) {
                            case ref:
                                moduleComponent = new ModuleComponent();
                                moduleComponent.setClassName(realModule.getClassName());
                                moduleComponent.setAlias(realModule.getComponent().getAlias());
                                currComponent.addChildren(moduleComponent);
                                break;
                            case dyn:
                                this.setCustomFunctions(moduleComponent.getCustomFunctions());
                                this.setFormulas(moduleComponent.getFormulas());
                                this.setDependencies(moduleComponent.getDependencies());
                                this.setFunctions(moduleComponent.getFunctions());
                                this.setModuleVar(moduleComponent.getModuleVar());
                                this.setProperties(moduleComponent.getProperties());
                                this.setCS(moduleComponent.getCS());
                                String customAppend = realModule.getComponent().getCustomAppend();
                                if (customAppend != null && !customAppend.equals("")) {
                                    this.setAfterAppend(customAppend);
                                }
                                Component fristComponent = realModule.getComponent().getLastBoxComponent();
                                if (fristComponent instanceof DialogComponent) {
                                    DialogComponent dialogComponent = (DialogComponent) fristComponent;
                                    if (dialog != null) {
                                        dialog.getEvents().putAll(dialogComponent.getEvents());
                                        dialog.setAlias(dialogComponent.getAlias());
                                    }
                                }

                                this.addChildren(genAPIComponent(currComponent, dataBean));
                                break;
                            case append:
                                this.setProperties(moduleComponent.getProperties());
                                this.setFunctions(moduleComponent.getFunctions());
                                this.setDependencies(moduleComponent.getDependencies());
                                this.setViewConfig(moduleComponent.getViewConfig());
                                this.setRequired(moduleComponent.getRequired());
                                this.setEvents(moduleComponent.getEvents());
                                this.setFormulas(moduleComponent.getFormulas());
                                this.setCustomFunctions(moduleComponent.getCustomFunctions());
                                this.setModuleVar(moduleComponent.getModuleVar());
                                this.setCustomAppend(moduleComponent.getCustomAppend());
                                this.setCS(moduleComponent.getCS());
                                fristComponent = realModule.getComponent().getLastBoxComponent();
                                if (fristComponent instanceof DialogComponent) {
                                    DialogComponent dialogComponent = (DialogComponent) fristComponent;
                                    if (dialog != null) {
                                        dialog.getEvents().putAll(dialogComponent.getEvents());
                                        dialog.setAlias(dialogComponent.getAlias());
                                        ComponentList components = fristComponent.getChildren();
                                        if (components != null) {
                                            for (Component ccomponent : components) {
                                                dialog.addChildren(ccomponent);
                                            }
                                        }
                                    }

                                } else {
                                    currComponent.addChildren(fristComponent);
                                }
                                break;
                        }
                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }


            } else {
                try {
                    this.addChildren(genAPIComponent(currComponent, dataBean));
                } catch (JDSException e) {
                    e.printStackTrace();
                }

            }
        }


        //  fillAction(viewBean, currComponent);

    }


    //数据对象
    @JSONField(serialize = false)
    APICallerComponent[] genAPIComponent(Component boxComponent, CustomDynDataBean dataBean) throws JDSException {
        List<APICallerComponent> apiCallerComponents = new ArrayList<APICallerComponent>();
        if (dynReloadUrl != null && !dynReloadUrl.equals("")) {
            MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.DYNRELOAD);
            APICallerComponent reloadAPI = new APICallerComponent(methodBean);
            reloadAPI.setAlias(CustomFormAction.DYNRELOAD.getTarget());
            //刷新调用
            APICallerProperties reloadProperties = reloadAPI.getProperties();
            UrlPathData treepathData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(treepathData);
            UrlPathData formData = new UrlPathData(boxComponent.getAlias(), ResponsePathTypeEnum.COMPONENT, "data");
            reloadProperties.addResponseData(formData);
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
        }


        if (saveUrl != null && !saveUrl.equals("")) {
            MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.SAVE);
            if (methodBean != null) {
                APICallerComponent saveAPI = new APICallerComponent(methodBean);
                saveAPI.setAlias(CustomFormAction.SAVE.getTarget());
                APICallerProperties saveProperties = saveAPI.getProperties();

                UrlPathData savepathData = new UrlPathData(boxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(savepathData);

                UrlPathData ctxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
                saveProperties.addRequestData(ctxData);
                apiCallerComponents.add(saveAPI);
            }
        }
        return apiCallerComponents.toArray(new APICallerComponent[]{});
    }


    public String getDynReloadUrl() {
        return dynReloadUrl;
    }

    public void setDynReloadUrl(String dynReloadUrl) {
        this.dynReloadUrl = dynReloadUrl;
    }

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }
}
