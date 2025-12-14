package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.UIAnnotation;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.UIPositionType;
import net.ooder.esd.annotation.ui.VisibilityType;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = UIAnnotation.class)
public class CustomUIBean implements CustomBean {


    public Dock dock = Dock.none;

    public VisibilityType visibility;

    public Integer zindex;

    public String display;

    public Boolean selectable;

    public String renderer;

    public String imageClass;

    public String left;

    public String right;

    public String top;

    public String bottom;

    public Boolean dynLoad;

    public Boolean shadows;

    public String width;

    public String height;
    @JSONField(serializeUsing = CaseEnumsSerializer.class, deserializeUsing = CaseEnumsSerializer.class)
    public UIPositionType position;

    public CustomUIBean() {

    }

    public void update(Component component) {
        init(component.getProperties());
    }

    public CustomUIBean(Component component) {
        update(component);
    }

    public CustomUIBean(UIAnnotation annotation) {
        fillData(annotation);
    }

    private void init(Properties properties) {
        CustomUIBean obj = JSONObject.parseObject(JSON.toJSONString(properties), CustomUIBean.class);
        Map valueMap = JSON.parseObject(JSON.toJSONString(obj), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public CustomUIBean fillData(UIAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public CustomUIBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(UIAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof UIAnnotation) {
                fillData((UIAnnotation) annotation);
            }
        }

    }

    public Boolean getShadows() {
        return shadows;
    }

    public void setShadows(Boolean shadows) {
        this.shadows = shadows;
    }

    public Integer getZindex() {
        return zindex;
    }

    public void setZindex(Integer zindex) {
        this.zindex = zindex;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (!AnnotationUtil.getAnnotationMap(this).isEmpty()) {
            annotationBeans.add(this);
        }
        return annotationBeans;
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

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public VisibilityType getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityType visibility) {
        this.visibility = visibility;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public Boolean getDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(Boolean dynLoad) {
        this.dynLoad = dynLoad;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }


    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public UIPositionType getPosition() {
        return position;
    }

    public void setPosition(UIPositionType position) {
        this.position = position;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
