package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.UIPositionType;
import net.ooder.esd.annotation.ui.VisibilityType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.CustomUIBean;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.esd.util.json.EMSerializer;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.List;
import java.util.Map;


public class AbsUIProperties extends Properties {
    public Dock dock;
    public VisibilityType visibility;
    public String display;
    public Boolean selectable;
    public String renderer;
    public String imageClass;
    public String caption;
    @JSONField(serializeUsing = EMSerializer.class)
    public String left;
    @JSONField(serializeUsing = EMSerializer.class)
    public String right;
    @JSONField(serializeUsing = EMSerializer.class)
    public String top;
    @JSONField(serializeUsing = EMSerializer.class)
    public String bottom;
    public Boolean dynLoad;
    @JSONField(serializeUsing = EMSerializer.class)
    public String width;
    @JSONField(serializeUsing = EMSerializer.class)
    public String height;
    @JSONField(serializeUsing = CaseEnumsSerializer.class, deserializeUsing = CaseEnumsSerializer.class)
    public UIPositionType position;


    public AbsUIProperties() {

    }

    public void init(ContainerBean uiBean) {
        if (uiBean != null && uiBean.getUiBean() != null) {
            this.init(uiBean.getUiBean());
        }
    }

    public void init(CustomUIBean uiBean) {
        if (uiBean != null && uiBean.getAnnotationBeans() != null) {
            List<CustomBean> items = uiBean.getAnnotationBeans();
            for (CustomBean customBean : items) {
                AbsUIProperties obj = JSONObject.parseObject(JSON.toJSONString(customBean), AbsUIProperties.class);
                Map valueMap = JSON.parseObject(JSON.toJSONString(obj), Map.class);
                OgnlUtil.setProperties(valueMap, this, false, false);
            }

        }
    }

    public Boolean getDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(Boolean dynLoad) {
        this.dynLoad = dynLoad;
    }

    public AbsUIProperties(Dock dock) {
        this.dock = dock;
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

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
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

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
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

    public UIPositionType getPosition() {
        return position;
    }

    public void setPosition(UIPositionType position) {
        this.position = position;
    }
}
