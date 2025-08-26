package net.ooder.esd.tool.properties.svg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.VisibilityType;
import net.ooder.esd.bean.svg.SVGBean;
import net.ooder.esd.util.json.EMSerializer;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class SVGProperties extends Properties {
    public String svgTag;
    Boolean selectable;
    Boolean defaultFocus;
    VisibilityType visibility;
    String renderer;
    String position;
    String path;
    // tabindex/zIndex is for compitable only
    Boolean tabindex;
    Boolean zIndex;
    Boolean disableClickEffect;
    Boolean disableHoverEffect;
    Boolean disableTips;
    Boolean disabled;
    @JSONField(serializeUsing = EMSerializer.class)
    String left;
    @JSONField(serializeUsing = EMSerializer.class)
    String top;
    @JSONField(serializeUsing = EMSerializer.class)
    String right;
    @JSONField(serializeUsing = EMSerializer.class)
    String bottom;
    @JSONField(serializeUsing = EMSerializer.class)
    String width;
    @JSONField(serializeUsing = EMSerializer.class)
    String height;


    // Map<String, Object> attr;
    Boolean shadow;
    String animDraw;
    String offsetFlow;


    @JSONField(name = "hAlign")
    String hAlign;

    String vAlign;
    String text;

    public SVGProperties() {

    }


    public SVGProperties(SVGBean svgBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(svgBean), Map.class), this, false, false);
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    public void setTabindex(Boolean tabindex) {
        this.tabindex = tabindex;
    }


    public void setzIndex(Boolean zIndex) {
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

    public String getSvgTag() {
        return svgTag;
    }

    public void setSvgTag(String svgTag) {
        this.svgTag = svgTag;
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

    public String getOffsetFlow() {
        return offsetFlow;
    }

    public void setOffsetFlow(String offsetFlow) {
        this.offsetFlow = offsetFlow;
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

}
