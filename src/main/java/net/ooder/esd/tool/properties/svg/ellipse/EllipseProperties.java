package net.ooder.esd.tool.properties.svg.ellipse;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.svg.SVGEllipseBean;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.esd.tool.properties.svg.comb.rect.RectAttr;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;


public class EllipseProperties<T extends RectAttr> extends SVGProperties {

    EllipseAttr attr;

    public EllipseProperties() {
        svgTag = "FlowChart:Process";
        attr = new EllipseAttr();

    }

//    public EllipseProperties(SVGRectBean rectBean) {
//        svgTag = rectBean.getSvgTag();
//        this.id = rectBean.getSvgKey().getId();
//        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(rectBean.getSvgBean()), Map.class), this, false, false);
//        attr = new RectAttr(rectBean);
//
//
//    }

    public EllipseProperties(SVGEllipseBean rectBean) {
        svgTag = rectBean.getSvgTag();
        this.id = rectBean.getSvgKey().getId();

        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(rectBean.getSvgBean()), Map.class), this, false, false);
        attr = new EllipseAttr(rectBean);

    }


    public String getSvgTag() {
        return svgTag;
    }

    public void setSvgTag(String svgTag) {
        this.svgTag = svgTag;
    }


    public EllipseAttr getAttr() {
        return attr;
    }


    public void setAttr(EllipseAttr attr) {
        this.attr = attr;
    }


}
