package net.ooder.esd.bean.svg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.svg.SVGAnnotation;
import net.ooder.esd.annotation.ui.UIPositionType;
import net.ooder.esd.annotation.ui.VisibilityType;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = SVGAnnotation.class)
public class SVGBean implements CustomBean {

    Boolean selectable;
    Boolean defaultFocus;
    VisibilityType visibility;
    String renderer;
    @JSONField(serializeUsing = CaseEnumsSerializer.class, deserializeUsing = CaseEnumsSerializer.class)
    UIPositionType position;
    String path;
    // tabindex/zIndex is for compitable only
    Boolean tabindex;

    String zIndex;
    Boolean disableClickEffect;
    Boolean disableHoverEffect;
    Boolean disableTips;
    Boolean disabled;
    String left;
    String top;
    String right;
    String bottom;
    String width;
    String height;


    // Map<String, Object> attr;
    Boolean shadow;
    String animDraw;
    String offSetFlow;


    @JSONField(name = "hAlign")
    String hAlign;

    String vAlign;
    String text;

    public SVGBean() {

    }

    public SVGBean(SVGProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }

    public SVGBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(SVGAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof SVGAnnotation) {
                fillData((SVGAnnotation) annotation);
            }
        }

    }

    public SVGBean(SVGAnnotation annotation) {
        fillData(annotation);
    }

    public SVGBean fillData(SVGAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public VisibilityType getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityType visibility) {
        this.visibility = visibility;
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public Boolean getDefaultFocus() {
        return defaultFocus;
    }

    public void setDefaultFocus(Boolean defaultFocus) {
        this.defaultFocus = defaultFocus;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public UIPositionType getPosition() {
        return position;
    }

    public void setPosition(UIPositionType position) {
        this.position = position;
    }


    public void setTabindex(Boolean tabindex) {
        this.tabindex = tabindex;
    }


    public void setzIndex(String zIndex) {
        this.zIndex = zIndex;
    }

    public Boolean getDisableClickEffect() {
        return disableClickEffect;
    }

    public void setDisableClickEffect(Boolean disableClickEffect) {
        this.disableClickEffect = disableClickEffect;
    }

    public Boolean getDisableHoverEffect() {
        return disableHoverEffect;
    }

    public void setDisableHoverEffect(Boolean disableHoverEffect) {
        this.disableHoverEffect = disableHoverEffect;
    }

    public Boolean getDisableTips() {
        return disableTips;
    }

    public void setDisableTips(Boolean disableTips) {
        this.disableTips = disableTips;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
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

    public Boolean getShadow() {
        return shadow;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setShadow(Boolean shadow) {

        this.shadow = shadow;
    }

    public String getAnimDraw() {
        return animDraw;
    }

    public void setAnimDraw(String animDraw) {
        this.animDraw = animDraw;
    }

    public Boolean getTabindex() {
        return tabindex;
    }

    public String getzIndex() {
        return zIndex;
    }

    public String getOffSetFlow() {
        return offSetFlow;
    }

    public void setOffSetFlow(String offSetFlow) {
        this.offSetFlow = offSetFlow;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String gethAlign() {
        return hAlign;
    }

    public void sethAlign(String hAlign) {
        this.hAlign = hAlign;
    }

    public String getvAlign() {
        return vAlign;
    }

    public void setvAlign(String vAlign) {
        this.vAlign = vAlign;
    }

    public SVGBean clone() {
        SVGBean svgBean = new SVGBean();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(this), Map.class), svgBean, false, false);
        return svgBean;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
