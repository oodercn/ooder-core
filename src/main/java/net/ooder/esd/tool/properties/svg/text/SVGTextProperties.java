package net.ooder.esd.tool.properties.svg.text;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.svg.SVGTextBean;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;


public class SVGTextProperties<T extends TextAttr> extends SVGProperties {

    TextAttr attr;

    public SVGTextProperties() {
        attr = new TextAttr();

    }

    public SVGTextProperties(SVGTextBean svgTextBean) {
        this.id = svgTextBean.getXpath();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(svgTextBean.getSvgBean()), Map.class), this, false, false);
        attr = new TextAttr(svgTextBean);

    }

    public TextAttr getAttr() {
        return attr;
    }

    public void setAttr(TextAttr attr) {
        this.attr = attr;
    }
}
