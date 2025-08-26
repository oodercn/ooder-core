package net.ooder.esd.tool.properties.svg.comb.circle;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.svg.SVGCircleBean;
import net.ooder.esd.bean.svg.comb.SVGCircleCombBean;
import net.ooder.esd.tool.properties.svg.SVGAttr;
import net.ooder.esd.tool.properties.svg.comb.BGText;


public class CircleAttr extends SVGAttr {
    @JSONField(name = "KEY")
    CircleKey KEY;


    public CircleAttr(SVGCircleCombBean rectBean) {
        this.KEY = rectBean.getSvgKey();
        this.TEXT = rectBean.getSvgText();
        this.BG = rectBean.getSvgBG();
    }

    public CircleAttr(SVGCircleBean rectBean) {
        this.KEY = rectBean.getSvgKey();
        this.TEXT = rectBean.getSvgText();
        this.BG = rectBean.getSvgBG();
    }


    public CircleAttr() {

        KEY = new CircleKey();
        BG = new BGText();
    }

    public CircleKey getKEY() {
        return KEY;
    }

    public void setKEY(CircleKey KEY) {
        this.KEY = KEY;
    }

}
