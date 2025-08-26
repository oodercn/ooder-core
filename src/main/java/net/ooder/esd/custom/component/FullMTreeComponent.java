package net.ooder.esd.custom.component;

import net.ooder.common.JDSException;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.bean.view.CustomMTreeViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.data.TreeDataBaseBean;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.MTreeViewComponent;

import java.util.Map;

public class FullMTreeComponent<M extends MTreeViewComponent> extends CustomMTreeComponent<M> {
    public FullMTreeComponent() {
        super();
    }


    public FullMTreeComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws JDSException {
        super(module, methodConfig, valueMap);
        M currComponent = (M) new MTreeViewComponent((CustomMTreeViewBean) methodConfig.getView());
        CustomMTreeViewBean customTreeViewBean = (CustomMTreeViewBean) methodConfig.getView();
        this.addChildLayoutNav(currComponent);
        this.setCurrComponent(currComponent);
        this.fillViewAction(methodConfig);
        this.fillAction(customTreeViewBean);
        this.fillTreeAction(customTreeViewBean, currComponent);

        this.fillMenuAction(customTreeViewBean, currComponent);
        this.addChildren(genAPIComponent(customTreeViewBean).toArray(new APICallerComponent[]{}));
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

            if (dataUrl == null || dataUrl.equals("")) {
                MethodConfig methodAPIBean = methodConfig.getDataBean().getMethodByItem(CustomMenuItem.RELOAD);
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


}
