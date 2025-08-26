package net.ooder.esd.tool.properties.svg;


import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.SVGPaperBean;
import net.ooder.esd.bean.field.CustomSVGPaperFieldBean;
import net.ooder.esd.tool.properties.ContainerDivProperties;

public class SVGPaperProperties extends ContainerDivProperties {

    Boolean scaleChildren;
    Integer graphicZIndex;

    public SVGPaperProperties() {

    }



    public SVGPaperProperties(CustomSVGPaperFieldBean fieldBean , ContainerBean containerBean) {

        if (containerBean != null) {
            this.init(containerBean);
        }

        this.overflow=fieldBean.getOverflow();
        if (fieldBean.getWidth()!=null){
            this.width=fieldBean.getWidth();
        }
        if (fieldBean.getHeight()!=null){
            this.height=fieldBean.getHeight();
        }

        this.scaleChildren = fieldBean.getScaleChildren();
        this.graphicZIndex = fieldBean.getGraphicZIndex();
    }

    public SVGPaperProperties(SVGPaperBean fieldBean, ContainerBean containerBean) {
        if (containerBean != null) {
            this.init(containerBean);

        }

        this.overflow=fieldBean.getOverflow();
        if (fieldBean.getWidth()!=null){
            this.width=fieldBean.getWidth();
        }
        if (fieldBean.getHeight()!=null){
            this.height=fieldBean.getHeight();
        }

        this.scaleChildren = fieldBean.getScaleChildren();
        this.graphicZIndex = fieldBean.getGraphicZIndex();
    }

    public Boolean getScaleChildren() {
        return scaleChildren;
    }

    public void setScaleChildren(Boolean scaleChildren) {
        this.scaleChildren = scaleChildren;
    }


    public Integer getGraphicZIndex() {
        return graphicZIndex;
    }

    public void setGraphicZIndex(Integer graphicZIndex) {
        this.graphicZIndex = graphicZIndex;
    }


}
