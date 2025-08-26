package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.action.CustomGlobalMethod;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.event.ModuleEventEnum;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.RequestPathTypeEnum;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.data.TreeDataBaseBean;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.action.CustomAPICallAction;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.TreeViewComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;
import net.ooder.esd.tool.properties.UrlPathData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomPopTreeComponent<M extends TreeViewComponent> extends CustomTreeComponent<M> {


    public CustomPopTreeComponent() {
        super();
    }

    public CustomPopTreeComponent(EUModule module, MethodConfig methodConfig, Map valueMap) throws ClassNotFoundException {
        super(module, methodConfig, valueMap);
    }


    public void addChildNav(M currComponent) {
        super.addChildLayoutNav(currComponent);
        this.setCurrComponent(currComponent);
        TreeDataBaseBean dataBean = (TreeDataBaseBean) methodAPIBean.getDataBean();
        CustomTreeViewBean popTreeViewBean = (CustomTreeViewBean) methodAPIBean.getView();
        String treeKey = euModule.getName();
        if (treeKey.endsWith(CustomViewFactory.dynBuild)) {
            treeKey = treeKey.substring(0, treeKey.length() - CustomViewFactory.dynBuild.length());
        }
        if (!treeKey.endsWith("Tree")) {
            treeKey = treeKey + "Tree";
        }

        currComponent.setAlias(treeKey);

        try {
            fillViewAction(methodAPIBean);
            this.addChildren(this.genAPIComponent(popTreeViewBean).toArray(new APICallerComponent[]{}));
        } catch (JDSException e) {
            e.printStackTrace();
        }
        fillCallbackAction(dataBean, valueMap);


    }

    protected void fillViewAction(MethodConfig methodConfig) {
        TreeDataBaseBean dataBean = (TreeDataBaseBean) methodConfig.getDataBean();

        if (dataBean != null) {
            if (dataBean.getDataUrl() != null && !dataBean.getDataUrl().equals("")) {
                this.dataUrl = dataBean.getDataUrl();
            }
            if (dataBean.getItemType() != null) {
                this.itemType = dataBean.getItemType();
            }
            if (dataBean.getSaveUrl() != null && !dataBean.getSaveUrl().equals("")) {
                this.saveUrl = dataBean.getSaveUrl();
            }
            if (dataBean.getLoadChildUrl() != null && !dataBean.getLoadChildUrl().equals("")) {
                this.loadChildUrl = dataBean.getLoadChildUrl();
            }

            if (dataUrl == null || dataUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomTreeEvent.TREERELOAD);
                if (methodAPIBean != null) {
                    dataUrl = methodAPIBean.getUrl();
                }

            }

            if (saveUrl == null || saveUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.SAVE);
                if (methodAPIBean != null) {
                    saveUrl = methodAPIBean.getUrl();
                }
            }

            if (loadChildUrl == null || loadChildUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodEvent(CustomTreeEvent.RELOADCHILD);
                if (methodAPIBean != null) {
                    loadChildUrl = methodAPIBean.getUrl();
                }
            }

            if (editorPath == null || editorPath.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.EDITOR);
                if (methodAPIBean != null) {
                    editorPath = methodAPIBean.getUrl();
                }
            }

            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = methodConfig.getUrl();
            }

        }


    }

    void fillCallbackAction(TreeDataBaseBean dataBean, Map<String, Object> valueMap) {
        Map ctx = new HashMap();

        String fieldCaption = dataBean.getFieldCaption();
        if (fieldCaption == null || fieldCaption.equals("")) {
            fieldCaption = (String) valueMap.get("fieldCaption");
        }

        String fieldName = dataBean.getFieldId();
        if (fieldName == null || fieldName.equals("")) {
            fieldName = (String) valueMap.get("fieldName");
        }

        ctx.put("fieldCaption", fieldCaption);
        ctx.put("fieldName", fieldName);
        this.addParams(ctx);
        Condition condition = new Condition();
        condition.setLeft("{page." + this.getCurrComponent().getAlias() + "getCaptionValue()}");
        condition.setRight("{}");
        condition.setSymbol(SymbolType.notequal);
        Action action = new Action(ModuleEventEnum.onDestroy);
        action.setDesc("填充返回值");
        action.addCondition(condition);
        action.setTarget("callback");
        action.setMethod(CustomGlobalMethod.call.getType());
        action.setType(ActionTypeEnum.other);
        List<String> args = new ArrayList<>();
        args.add("{page.getParentModule().setData()}");
        args.add(null);
        args.add(null);
        args.add("{page." + this.getCurrComponent().getAlias() + ".getCallBackValue()}");
        action.setArgs(args);
        action.setEventKey(ModuleEventEnum.onDestroy);
        this.addAction(action);

    }


    protected void fillMenuAction(CustomTreeViewBean customAnnotation, Component currComponent) {
        super.fillMenuAction(customAnnotation, currComponent);
    }


    //数据对象
    @JSONField(serialize = false)
    public List<APICallerComponent> genAPIComponent(CustomTreeViewBean customTreeViewBean) throws JDSException {
        List<APICallerComponent> apiCallerComponents = super.genAPIComponent(customTreeViewBean);
        //装载保存事件
        MethodConfig methodBean = getMethodBeanByItem(CustomMenuItem.TREESAVE);
        if (methodBean != null) {
            APICallerComponent saveAPI = new APICallerComponent(methodBean);
            saveAPI.getProperties().setIsAllform(true);
            saveAPI.setAlias(CustomFormAction.SAVE.getTarget());
            APICallerProperties saveProperties = saveAPI.getProperties();
            saveProperties.setIsAllform(true);
            UrlPathData currPath = new UrlPathData(this.getCurrComponent().getAlias(), RequestPathTypeEnum.FORM, "");
            saveProperties.addRequestData(currPath);
            UrlPathData savepathData = new UrlPathData(this.getCurrComponent().getParent().getAlias(), RequestPathTypeEnum.FORM, "");
            saveProperties.addRequestData(savepathData);
            UrlPathData ctxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.FORM, "");
            saveProperties.addRequestData(ctxData);

            TreeDataBaseBean dataBean = (TreeDataBaseBean) methodAPIBean.getDataBean();
            if (dataBean.getAutoSave() != null && dataBean.getAutoSave()) {
                CustomAPICallAction customAPICallAction = new CustomAPICallAction(saveAPI, ModuleEventEnum.onDestroy);
                this.addAction(customAPICallAction);
            }

            apiCallerComponents.add(saveAPI);
        }


        return apiCallerComponents;
    }


    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }


    /**
     * @return
     */
    @JSONField(serialize = false)
    public ESDClient getEsdClient() throws JDSException {
        ESDClient client = ESDFacrory.getAdminESDClient();
        return client;
    }

}
