package net.ooder.esd.tool.properties.svg.comb.rect;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.svg.SVGRectBean;
import net.ooder.esd.bean.svg.comb.SVGRectCombBean;
import net.ooder.esd.tool.properties.svg.SVGAttr;
import net.ooder.esd.tool.properties.svg.comb.Text;


public class RectAttr extends SVGAttr {
    @JSONField(name = "KEY")
    RectKey KEY;


    public RectAttr(SVGRectBean rectBean) {
        this.KEY = rectBean.getSvgKey();
        this.TEXT = rectBean.getSvgText();
        this.BG = rectBean.getSvgBG();
    }
    public RectAttr(SVGRectCombBean rectBean) {
        this.KEY = rectBean.getSvgKey();
        this.TEXT = rectBean.getSvgText();
        this.BG = rectBean.getSvgBG();
    }



    public RectAttr() {
        KEY = new RectKey();
        TEXT = new Text();
    }


    public RectKey getKEY() {
        return KEY;
    }

    public void setKEY(RectKey KEY) {
        this.KEY = KEY;
    }


}
