package net.ooder.esd.bean.field.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ListMenu;
import net.ooder.esd.annotation.field.ListAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.ListMenuBean;
import net.ooder.esd.bean.field.CustomListBean;
import net.ooder.esd.bean.field.FieldBaseBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.field.CustomListComponent;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.FieldComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.list.ListFieldProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;
import org.checkerframework.checker.units.qual.C;

import java.lang.annotation.Annotation;
import java.util.*;

@CustomClass(clazz = CustomListComponent.class,
        viewType = CustomViewType.COMPONENT,
        componentType = ComponentType.LIST
)
@AnnotationType(clazz = ListAnnotation.class)
public class ListFieldBean<T extends ListFieldProperties, M extends FieldComponent> extends FieldBaseBean<M> {

    String id;

    String xpath;

    SelModeType selMode;

    BorderType borderType;

    Boolean noCtrlKey;

    Dock dock;

    String width;

    String height;

    Integer maxHeight;

    ItemRow itemRow;

    String optBtn;

    String tagCmds;

    TagCmdsAlign tagCmdsAlign;

    String labelCaption;

    CustomListBean<T> customListBean;

    ListMenuBean listMenuBean;

    public ListFieldBean() {

    }

    public ListFieldBean(ModuleComponent moduleComponent, M component) {
        update(moduleComponent, component);
    }

    @Override
    public void update(ModuleComponent moduleComponent, M component) {
        this.update((T) component.getProperties());
//        customListBean = new CustomListBean();
//        javaSrcBeans = customListBean.update(moduleComponent, (Component<T, ?>) component);
    }


    public void update(T properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public ListFieldBean(T properties) {
        update(properties);
    }


    public ListFieldBean(ESDField esdField, Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(ListAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ListAnnotation) {
                fillData((ListAnnotation) annotation);
            }
            if (annotation instanceof ListMenu) {
                listMenuBean = new ListMenuBean((ListMenu) annotation);
            }
        }
        customListBean = new CustomListBean(esdField, annotations);
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (customListBean != null && !AnnotationUtil.getAnnotationMap(customListBean).isEmpty()) {
            annotationBeans.add(customListBean);
        }
        if (listMenuBean != null && !AnnotationUtil.getAnnotationMap(listMenuBean).isEmpty()) {
            annotationBeans.add(listMenuBean);
        }
        annotationBeans.add(this);
        return annotationBeans;
    }

    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classes = new HashSet<>();
        if (listMenuBean != null) {
            classes.addAll(listMenuBean.getOtherClass());
        }
        if (customListBean != null) {
            classes.addAll(customListBean.getOtherClass());
        }
        return ClassUtility.checkBase(classes);
    }

    @Override
    public String getXpath() {
        return xpath;
    }


    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public ListMenuBean getListMenuBean() {
        return listMenuBean;
    }

    public void setListMenuBean(ListMenuBean listMenuBean) {
        this.listMenuBean = listMenuBean;
    }

    public CustomListBean getCustomListBean() {
        return customListBean;
    }

    public void setCustomListBean(CustomListBean customListBean) {
        this.customListBean = customListBean;
    }


    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SelModeType getSelMode() {
        return selMode;
    }

    public void setSelMode(SelModeType selMode) {
        this.selMode = selMode;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public Boolean getNoCtrlKey() {
        return noCtrlKey;
    }

    public void setNoCtrlKey(Boolean noCtrlKey) {
        this.noCtrlKey = noCtrlKey;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    public ItemRow getItemRow() {
        return itemRow;
    }

    public void setItemRow(ItemRow itemRow) {
        this.itemRow = itemRow;
    }

    public String getOptBtn() {
        return optBtn;
    }

    public void setOptBtn(String optBtn) {
        this.optBtn = optBtn;
    }

    public String getTagCmds() {
        return tagCmds;
    }

    public void setTagCmds(String tagCmds) {
        this.tagCmds = tagCmds;
    }

    public TagCmdsAlign getTagCmdsAlign() {
        return tagCmdsAlign;
    }

    public void setTagCmdsAlign(TagCmdsAlign tagCmdsAlign) {
        this.tagCmdsAlign = tagCmdsAlign;
    }

    public String getLabelCaption() {
        return labelCaption;
    }

    public void setLabelCaption(String labelCaption) {
        this.labelCaption = labelCaption;
    }


    public ListFieldBean(ListAnnotation annotation) {
        fillData(annotation);
    }

    public ListFieldBean fillData(ListAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.LIST;
    }
}
