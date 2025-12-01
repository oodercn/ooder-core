package net.ooder.esd.tool.properties.svg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.svg.SVGBean;
import net.ooder.esd.tool.properties.ContainerProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class SVGProperties extends ContainerProperties {


    public String svgTag;

    public String path;

    Boolean disabled;

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

    public String getSvgTag() {
        return svgTag;
    }

    public void setSvgTag(String svgTag) {
        this.svgTag = svgTag;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public Boolean getDisabled() {
        return disabled;
    }

    @Override
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getShadow() {
        return shadow;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
