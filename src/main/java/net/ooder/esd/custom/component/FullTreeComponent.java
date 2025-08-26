package net.ooder.esd.custom.component;

import net.ooder.common.JDSException;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.data.TreeDataBaseBean;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.TreeViewComponent;

import java.util.Map;

public class FullTreeComponent<M extends TreeViewComponent> extends CustomTreeComponent<M> {
    public FullTreeComponent() {
        super();
    }


    public FullTreeComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws JDSException, ClassNotFoundException {
        super(module, methodConfig, valueMap);
        M currComponent = (M) new TreeViewComponent((CustomTreeViewBean) methodConfig.getView());
        currComponent.getProperties().setDock(Dock.fill);
        CustomTreeViewBean customTreeViewBean = (CustomTreeViewBean) methodConfig.getView();
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
