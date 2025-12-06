package net.ooder.esd.bean.svg;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.SVGBaseComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.esd.tool.properties.svg.comb.BGText;
import net.ooder.esd.tool.properties.svg.comb.Key;
import net.ooder.esd.tool.properties.svg.comb.Text;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class SVGFieldBaseBean<T extends Key,M extends SVGBaseComponent> implements FieldComponentBean<M> {
    public Text svgText;
    public BGText svgBG;
    public SVGBean svgBean;
    public String xpath;
    public String path;

    public SVGFieldBaseBean() {

    }
    public SVGFieldBaseBean(M svgBaseComponent) {
        this.xpath=svgBaseComponent.getPath();
        this.update((SVGProperties) svgBaseComponent.getProperties());
    }

    protected  void update (SVGProperties properties) {
        svgBean = new SVGBean(properties);
        svgText = new Text(properties);
        svgBG = new BGText(properties);

    }

    public SVGFieldBaseBean(Set<Annotation> annotations) {
        svgBean = new SVGBean(annotations);
        svgText = new Text(annotations);
        svgBG = new BGText(annotations);
    }

    @Override
    public List<JavaSrcBean> getJavaSrcBeans() {
        return new ArrayList<>();
    }

    @Override
    public void update(ModuleComponent moduleComponent, M component) {


    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans  = new ArrayList<>();
        if (svgBean != null && !AnnotationUtil.getAnnotationMap(svgBean).isEmpty()) {
            annotationBeans.add(svgBean);
        }

        if (svgText != null && !AnnotationUtil.getAnnotationMap(svgText).isEmpty()) {
            annotationBeans.add(svgText);
        }
        if (svgBG != null && !AnnotationUtil.getAnnotationMap(svgBG).isEmpty()) {
            annotationBeans.add(svgBG);
        }
        annotationBeans.add(this);
        return annotationBeans;
    }



    public abstract String getSvgTag();

    public abstract T getSvgKey();

    public abstract void setSvgKey(T svgKey);



    public Text getSvgText() {
        return svgText;
    }

    public void setSvgText(Text svgText) {
        this.svgText = svgText;
    }

    public BGText getSvgBG() {
        return svgBG;
    }

    public void setSvgBG(BGText svgBG) {
        this.svgBG = svgBG;
    }

    public SVGBean getSvgBean() {
        return svgBean;
    }

    public void setSvgBean(SVGBean svgBean) {
        this.svgBean = svgBean;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    @Override
    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
