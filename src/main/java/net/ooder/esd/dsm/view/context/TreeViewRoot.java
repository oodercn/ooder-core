package net.ooder.esd.dsm.view.context;

import net.ooder.config.TreeListResultModel;
import net.ooder.esd.annotation.GridAnnotation;
import net.ooder.esd.annotation.TreeItem;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.bean.view.ChildTreeViewBean;
import net.ooder.esd.bean.view.CustomTreeViewBean;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.view.field.FieldTreeConfig;
import net.ooder.esd.bean.TreeListItem;

import java.util.*;


public class TreeViewRoot extends BaseViewRoot<CustomTreeViewBean> {


    private List<FieldTreeConfig> allFields = new ArrayList<>();

    private static final Class[] customClass = new Class[]{
            TreeListResultModel.class,
            TreeItem.class,
            TreeListItem.class,
            GridAnnotation.class,
    };


    public TreeViewRoot(AggViewRoot viewRoot, CustomTreeViewBean viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
        this.dsmBean = viewRoot.getDsmBean();
        this.viewBean = viewBean;
        this.allFields = viewBean.getAllFields();

        Set<ChildTreeViewBean>   childTreeViewBeans = viewBean.getChildTreeViewBeans();

        for (FieldTreeConfig esdFieldConfig : allFields) {
            imports.add(esdFieldConfig.getClassName());
        }
    }


    public CustomTreeViewBean getViewBean() {
        return viewBean;
    }

    public void setViewBean(CustomTreeViewBean viewBean) {
        this.viewBean = viewBean;
    }

    public List<FieldTreeConfig> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<FieldTreeConfig> allFields) {
        this.allFields = allFields;
    }

    public Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.TREE;
    }


}
