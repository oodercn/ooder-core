package net.ooder.esd.dsm.view.context;

import net.ooder.esd.annotation.TreeItem;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.bean.view.PopTreeViewBean;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.view.field.FieldTreeConfig;

import java.util.ArrayList;
import java.util.List;


public class PopTreeViewRoot extends BaseViewRoot<PopTreeViewBean> {


    public CustomTreeViewBean treeViewBean;

    private List<FieldTreeConfig> allFields = new ArrayList<>();

    private static final Class[] customClass = new Class[]{
            TreeItem.class,
    };

    public PopTreeViewRoot(AggViewRoot viewRoot, PopTreeViewBean viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
        this.treeViewBean = viewBean;
        this.allFields = treeViewBean.getAllFields();
        for (FieldTreeConfig esdFieldConfig : allFields) {
            imports.add(esdFieldConfig.getClassName());
        }

    }

    public CustomTreeViewBean getTreeViewBean() {
        return treeViewBean;
    }

    public void setTreeViewBean(CustomTreeViewBean treeViewBean) {
        this.treeViewBean = treeViewBean;
    }

    public List<FieldTreeConfig> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<FieldTreeConfig> allFields) {
        this.allFields = allFields;
    }

    @Override
    public Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.TREE;
    }
}
