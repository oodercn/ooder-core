package net.ooder.esd.tool.properties.svg.comb.circle;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.svg.SVGCircleBean;
import net.ooder.esd.bean.svg.comb.SVGCircleCombBean;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;


public class CircleProperties extends SVGProperties {

    String svgTag = "FlowChart:OnPageRefrence";

    CircleAttr attr;

    public CircleProperties(SVGCircleBean<CircleKey> rectBean) {
        svgTag = rectBean.getSvgTag();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(rectBean.getSvgBean()), Map.class), this, false, false);
        this.id = rectBean.getSvgKey().getId();
        attr = new CircleAttr(rectBean);

    }

    public CircleProperties(SVGCircleCombBean rectBean) {
        svgTag = rectBean.getSvgTag();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(rectBean.getSvgBean()), Map.class), this, false, false);
        this.id = rectBean.getSvgKey().getId();
        attr = new CircleAttr(rectBean);
    }


    public CircleProperties() {
        attr = new CircleAttr();
    }

    public String getSvgTag() {
        return svgTag;
    }

    public void setSvgTag(String svgTag) {
        this.svgTag = svgTag;
    }

    public CircleAttr getAttr() {
        return attr;
    }

    public void setAttr(CircleAttr attr) {
        this.attr = attr;
    }
}
