package net.ooder.esd.dsm.view.context;

import net.ooder.esd.annotation.GridAnnotation;
import net.ooder.esd.bean.view.BaseGalleryViewBean;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.view.field.FieldGalleryConfig;
import net.ooder.esd.tool.properties.item.OpinionItem;
import net.ooder.esd.tool.properties.item.TabListItem;

import java.util.*;

public abstract class BaseGalleryRoot<T extends BaseGalleryViewBean> extends BaseViewRoot<T> {

    private static Class[] galleryClass = new Class[]{
            OpinionItem.class,
            GridAnnotation.class

    };

    private List<FieldGalleryConfig> allFields = new ArrayList<>();

    private List<TabListItem> tabListItems = new ArrayList<>();

    public BaseGalleryRoot(){

    }

    public BaseGalleryRoot(AggViewRoot viewRoot, T viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
        this.allFields = viewBean.getAllFields();
        this.tabListItems = viewBean.getTabItems();
        for (FieldGalleryConfig esdFieldConfig : allFields) {
            imports.add(esdFieldConfig.getClassName());
        }
        Set<Class> classes = new HashSet<>();
        classes.addAll(Arrays.asList(galleryClass));
        this.imports.addAll(getCustomClassImpls(classes));

    }



    public List<FieldGalleryConfig> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<FieldGalleryConfig> allFields) {
        this.allFields = allFields;
    }

    public List<TabListItem> getTabListItems() {
        return tabListItems;
    }

    public void setTabListItems(List<TabListItem> tabListItems) {
        this.tabListItems = tabListItems;
    }
}
