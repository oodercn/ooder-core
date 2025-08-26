package net.ooder.esd.tool.properties.svg.comb.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.svg.SVGPathBean;
import net.ooder.esd.bean.svg.comb.SVGPathCombBean;
import net.ooder.esd.tool.properties.svg.SVGAttr;
import net.ooder.esd.tool.properties.svg.comb.BGText;
import net.ooder.esd.tool.properties.svg.comb.Text;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;


public class PathAttr extends SVGAttr {
    @JSONField(name = "KEY")
    PathKey KEY;

    public PathAttr() {
        KEY = new PathKey();
        BG = new BGText();
        TEXT = new Text();
    }

    public PathAttr(SVGPathBean pathBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(pathBean.getSvgKey()), Map.class), this, false, false);
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(pathBean.getSvgBean()), Map.class), this, false, false);
        //this.KEY = pathBean.getSvgKey();
    }

    public PathAttr(SVGPathCombBean pathBean) {
        this.KEY = pathBean.getSvgKey();
        this.TEXT = pathBean.getSvgText();
        this.BG = pathBean.getSvgBG();
    }


    public PathKey getKEY() {
        return KEY;
    }

    public void setKEY(PathKey KEY) {
        this.KEY = KEY;
    }

}
