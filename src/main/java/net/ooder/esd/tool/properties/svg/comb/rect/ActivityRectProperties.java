package net.ooder.esd.tool.properties.svg.comb.rect;

public class ActivityRectProperties extends RectProperties {


    public ActivityRectProperties() {
        super();
        this.getAttr().getKEY().setFill("90-#CEF8FF:0-#7FE0F8:50-#9BF1FF:100");
        this.getAttr().getKEY().setStroke("#004A7F");
    }

    String animDraw = "2s ease-in-out";
    String offSetFlow = "2x";

    public String getAnimDraw() {
        return animDraw;
    }

    public void setAnimDraw(String animDraw) {
        this.animDraw = animDraw;
    }

    public String getOffSetFlow() {
        return offSetFlow;
    }

    public void setOffSetFlow(String offSetFlow) {
        this.offSetFlow = offSetFlow;
    }
}
