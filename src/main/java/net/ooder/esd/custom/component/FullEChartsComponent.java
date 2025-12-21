package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.RequestPathTypeEnum;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.bean.bar.DynBar;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.ToolBarMenuBean;
import net.ooder.esd.bean.data.CustomFChartDataBean;
import net.ooder.esd.bean.view.CustomFChartViewBean;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.custom.component.form.field.CustomFieldEChartComponent;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.manager.editor.PluginsFactory;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.UrlPathData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FullEChartsComponent extends CustomModuleComponent<CustomFieldEChartComponent> {

    String JSONUrl;

    String XMLUrl;

    String categoriesUrl;

    String datasetUrl;

    String dataUrl;

    String trendlinesUrl;


    @JSONField(serialize = false)
    CustomToolsBar customToolsBar;

    public FullEChartsComponent() {
        super();
    }


    public FullEChartsComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);
        try {
            this.init(methodConfig);
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }

    void init(MethodConfig methodConfig) throws JDSException {
        CustomFChartViewBean viewBean = (CustomFChartViewBean) methodConfig.getView();
        CustomFChartDataBean dataBean = (CustomFChartDataBean) methodConfig.getDataBean();
        if (dataBean != null) {
            this.dataUrl = dataBean.getDataUrl();
            this.categoriesUrl = dataBean.getCategoriesUrl();
            this.datasetUrl = dataBean.getDatasetUrl();
            this.trendlinesUrl = dataBean.getTrendlinesUrl();
            if (dataUrl == null || dataUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.DATAURL);
                if (methodAPIBean != null) {
                    dataUrl = methodAPIBean.getUrl();
                }
            }

            if (categoriesUrl == null || categoriesUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.CATEGORIESURL);
                if (methodAPIBean != null) {
                    categoriesUrl = methodAPIBean.getUrl();
                }
            }


            if (datasetUrl == null || datasetUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.DATASETURL);
                if (methodAPIBean != null) {
                    datasetUrl = methodAPIBean.getUrl();
                }
            }

            if (trendlinesUrl == null || trendlinesUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.TRENDLINESURL);
                if (methodAPIBean != null) {
                    trendlinesUrl = methodAPIBean.getUrl();
                }
            }

            if (dataUrl == null || dataUrl.equals("")) {
                this.dataUrl = methodConfig.getUrl();
            }
        }


        CustomFieldEChartComponent currComponent = new CustomFieldEChartComponent(euModule, methodConfig, valueMap);
        this.addChildLayoutNav(currComponent);
        this.setCurrComponent(currComponent);

        if (viewBean != null) {
            fillFromAction(viewBean, currComponent);
        }

        this.fillToolBar(viewBean, currComponent);
        APICallerComponent[] apiCallerComponents = this.genAPIComponent(getCtxBaseComponent(), getMainComponent());
        this.addChildren(apiCallerComponents);
    }


    protected void fillFromAction(CustomFChartViewBean view, Component
            currComponent) {
        Set<CustomFieldEvent> customFormEvents = view.getEvent();
        for (CustomFieldEvent eventType : customFormEvents) {
            for (CustomAction actionType : eventType.getActions(false)) {
                Action action = new Action(actionType,eventType.getEventEnum());
                MethodConfig methodConfig = methodAPIBean.getDataBean().getMethodEvent(eventType);
                if (methodConfig != null) {
                    if (!methodConfig.isModule()) {
                        if (this.findComponentByAlias(actionType.target()) == null) {
                            APICallerComponent apiCallerComponent = new APICallerComponent(methodConfig);
                            apiCallerComponent.setAlias(actionType.target());
                            this.addChildren(apiCallerComponent);
                        }
                    } else {
                        action = new ShowPageAction(methodConfig,eventType.getEventEnum());
                        action.updateArgs("{args[1]}", 6);
                        action.updateArgs("{args[2]}", 5);
                    }
                }
                currComponent.addAction(action);
            }
        }
        List<CustomFormMenu> customFormMenus = view.getCustomMenu();
        if (customFormMenus != null && customFormMenus.size() > 0) {
            this.getMenuBar(view.getMenuBar()).addMenu(customFormMenus.toArray(new CustomMenu[]{}));
        }

        List<CustomFormMenu> customBottombar = view.getBottombarMenu();
        if (customBottombar != null && customBottombar.size() > 0) {
            this.getBottomBar(view.getBottomBar()).addMenu(customBottombar.toArray(new CustomFormMenu[]{}));
        }

        super.fillAction(view);

    }

    void fillToolBar(CustomFChartViewBean view, Component currComponent) throws JDSException {
        ComponentType[] bindTypes = (ComponentType[]) view.getBindTypes().toArray(new ComponentType[]{});
        ToolBarMenuBean toolBarBean = view.getToolBar();
        if (toolBarBean != null) {
            Class<DynBar>[] serviceObjs = toolBarBean.getMenuClasses();
            String groupId = currComponent.getAlias() + "Bar";
            if (toolBarBean.getGroupId() != null && !toolBarBean.getGroupId().equals("")) {
                groupId = toolBarBean.getGroupId();
            }
            customToolsBar = new CustomToolsBar(groupId);
            if (serviceObjs != null) {
                for (Class obj : serviceObjs) {
                    if (!obj.equals(Void.class)) {
                        CustomToolsBar bar = PluginsFactory.getInstance().initMenuClass(obj, CustomToolsBar.class);
                        if (customToolsBar == null) {
                            this.customToolsBar = bar;
                            List<APICallerComponent> components = customToolsBar.getApis();
                            this.addApi(components);
                        } else {
                            this.addBindService(obj, customToolsBar, bindTypes);
                        }
                    }
                }
            }

            if (customToolsBar != null && customToolsBar.getProperties().getGroup() != null && customToolsBar.getProperties().getGroup().getSub() != null && customToolsBar.getProperties().getGroup().getSub().size() > 0) {
                customToolsBar.getProperties().setDock(Dock.top);
                currComponent.getParent().addChildren(customToolsBar);
            }

        }
    }

    //数据对象
    @JSONField(serialize = false)
    APICallerComponent[] genAPIComponent(Component ctxComponent, Component mainComponent) throws JDSException {
        List<APICallerComponent> apiCallerComponents = new ArrayList<APICallerComponent>();

        MethodConfig trendlinesMethod = methodAPIBean.getDataBean().getMethodByItem(CustomMenuItem.TRENDLINESURL);
        if (trendlinesMethod != null) {
            APICallerComponent api = new APICallerComponent(trendlinesMethod);
            //刷新调用
            APICallerProperties reloadProperties = api.getProperties();
            UrlPathData treepathData = new UrlPathData(mainComponent.getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(treepathData);
            UrlPathData formData = new UrlPathData(mainComponent.getAlias(), ResponsePathTypeEnum.FORM, "data");
            reloadProperties.addResponseData(formData);
            UrlPathData formCtxData = new UrlPathData(getCurrComponent().getAlias(), ResponsePathTypeEnum.FCHARTTRENDLINES, "data");
            reloadProperties.addResponseData(formCtxData);
            UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(ctxData);
            apiCallerComponents.add(api);
        }


        MethodConfig datasetMethod = methodAPIBean.getDataBean().getMethodByItem(CustomMenuItem.DATASETURL);
        if (datasetMethod != null) {
            APICallerComponent api = new APICallerComponent(datasetMethod);
            APICallerProperties reloadProperties = api.getProperties();
            UrlPathData treepathData = new UrlPathData(mainComponent.getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(treepathData);
            UrlPathData formData = new UrlPathData(mainComponent.getAlias(), ResponsePathTypeEnum.FORM, "data");
            reloadProperties.addResponseData(formData);
            UrlPathData formCtxData = new UrlPathData(getCurrComponent().getAlias(), ResponsePathTypeEnum.FCHARTDATASET, "data");
            reloadProperties.addResponseData(formCtxData);
            UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(ctxData);
            apiCallerComponents.add(api);
        }


        MethodConfig categoriesMethod = methodAPIBean.getDataBean().getMethodByItem(CustomMenuItem.CATEGORIESURL);
        if (categoriesMethod != null) {
            APICallerComponent api = new APICallerComponent(categoriesMethod);
            APICallerProperties reloadProperties = api.getProperties();
            UrlPathData treepathData = new UrlPathData(mainComponent.getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(treepathData);
            UrlPathData formData = new UrlPathData(mainComponent.getAlias(), ResponsePathTypeEnum.FORM, "data");
            reloadProperties.addResponseData(formData);
            UrlPathData formCtxData = new UrlPathData(getCurrComponent().getAlias(), ResponsePathTypeEnum.FCHARTCATEGORIES, "data");
            reloadProperties.addResponseData(formCtxData);
            UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(ctxData);
            apiCallerComponents.add(api);
        }


        MethodConfig methodBean = methodAPIBean.getDataBean().getMethodByItem(CustomMenuItem.DATAURL);
        if (methodBean == null) {
            methodBean = methodAPIBean;
        }
        APICallerComponent reloadAPI = new APICallerComponent(methodBean);
        if (reloadAPI != null) {
            reloadAPI.setAlias(CustomFormAction.RELOAD.getTarget());
            //刷新调用
            APICallerProperties reloadProperties = reloadAPI.getProperties();
            UrlPathData treepathData = new UrlPathData(mainComponent.getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(treepathData);
            UrlPathData formData = new UrlPathData(mainComponent.getAlias(), ResponsePathTypeEnum.FORM, "data");
            reloadProperties.addResponseData(formData);
            UrlPathData formCtxData = new UrlPathData(getCurrComponent().getAlias(), ResponsePathTypeEnum.FCHART, "data");
            reloadProperties.addResponseData(formCtxData);
            UrlPathData ctxData = new UrlPathData(ctxComponent.getAlias(), RequestPathTypeEnum.FORM, "");
            reloadProperties.addRequestData(ctxData);
            apiCallerComponents.add(reloadAPI);
        }
        return apiCallerComponents.toArray(new APICallerComponent[]{});
    }

    public String getJSONUrl() {
        return JSONUrl;
    }

    public void setJSONUrl(String JSONUrl) {
        this.JSONUrl = JSONUrl;
    }

    public String getXMLUrl() {
        return XMLUrl;
    }

    public void setXMLUrl(String XMLUrl) {
        this.XMLUrl = XMLUrl;
    }

    public String getCategoriesUrl() {
        return categoriesUrl;
    }

    public void setCategoriesUrl(String categoriesUrl) {
        this.categoriesUrl = categoriesUrl;
    }

    public String getDatasetUrl() {
        return datasetUrl;
    }

    public void setDatasetUrl(String datasetUrl) {
        this.datasetUrl = datasetUrl;
    }

    @Override
    public String getDataUrl() {
        return dataUrl;
    }

    @Override
    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getTrendlinesUrl() {
        return trendlinesUrl;
    }

    public void setTrendlinesUrl(String trendlinesUrl) {
        this.trendlinesUrl = trendlinesUrl;
    }

    @Override
    public CustomToolsBar getCustomToolsBar() {
        return customToolsBar;
    }

    @Override
    public void setCustomToolsBar(CustomToolsBar customToolsBar) {
        this.customToolsBar = customToolsBar;
    }
}
