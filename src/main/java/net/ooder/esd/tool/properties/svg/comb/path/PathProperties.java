package net.ooder.esd.tool.properties.svg.comb.path;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.svg.SVGPathBean;
import net.ooder.esd.bean.svg.comb.SVGPathCombBean;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class PathProperties extends SVGProperties {
    public PathAttr attr;

    public PathProperties(SVGPathBean svgPathBean) {
        svgTag = svgPathBean.getSvgTag();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(svgPathBean.getSvgKey()), Map.class), this, false, false);
        this.id = svgPathBean.getSvgKey().getId();
        attr = new PathAttr(svgPathBean);
    }

    public PathProperties(SVGPathCombBean svgPathBean) {
        svgTag = svgPathBean.getSvgTag();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(svgPathBean.getSvgBean()), Map.class), this, false, false);

        this.id = svgPathBean.getSvgKey().getId();
        attr = new PathAttr(svgPathBean);
    }

    public PathProperties() {
        attr = new PathAttr();
    }

    public PathAttr getAttr() {
        return attr;
    }

    public void setAttr(PathAttr attr) {
        this.attr = attr;
    }

}
