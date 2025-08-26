package net.ooder.esd.tool.properties.svg.comb.rect;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.svg.SVGRectBean;
import net.ooder.esd.bean.svg.comb.SVGRectCombBean;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;


public class RectProperties<T extends RectAttr> extends SVGProperties {

    RectAttr attr;

    public RectProperties() {
        svgTag = "FlowChart:Process";
        attr = new RectAttr();

    }

    public RectProperties(SVGRectBean rectBean) {
        svgTag = rectBean.getSvgTag();
        this.id = rectBean.getSvgKey().getId();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(rectBean.getSvgBean()), Map.class), this, false, false);
        attr = new RectAttr(rectBean);


    }

    public RectProperties(SVGRectCombBean rectBean) {
        svgTag = rectBean.getSvgTag();
        this.id = rectBean.getSvgKey().getId();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(rectBean.getSvgBean()), Map.class), this, false, false);
        attr = new RectAttr(rectBean);

    }


    public String getSvgTag() {
        return svgTag;
    }

    public void setSvgTag(String svgTag) {
        this.svgTag = svgTag;
    }


    public RectAttr getAttr() {
        return attr;
    }


    public void setAttr(RectAttr attr) {
        this.attr = attr;
    }


}
