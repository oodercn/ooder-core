package net.ooder.esd.tool.properties.svg.ellipse;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.svg.SVGEllipseBean;
import net.ooder.esd.bean.svg.comb.SVGEllipseCombBean;
import net.ooder.esd.tool.properties.svg.SVGAttr;
import net.ooder.esd.tool.properties.svg.comb.Text;


public class EllipseAttr extends SVGAttr {
    @JSONField(name = "KEY")
    EllipseKey KEY;


    public EllipseAttr(SVGEllipseBean ellipseBean) {
        this.KEY = ellipseBean.getSvgKey();
        this.TEXT = ellipseBean.getSvgText();
        this.BG = ellipseBean.getSvgBG();
    }

    public EllipseAttr(SVGEllipseCombBean ellipseBean) {
        this.KEY = ellipseBean.getSvgKey();
        this.TEXT = ellipseBean.getSvgText();
        this.BG = ellipseBean.getSvgBG();
    }


    public EllipseAttr() {
        KEY = new EllipseKey();
        TEXT = new Text();
    }


    public EllipseKey getKEY() {
        return KEY;
    }

    public void setKEY(EllipseKey KEY) {
        this.KEY = KEY;
    }


}
