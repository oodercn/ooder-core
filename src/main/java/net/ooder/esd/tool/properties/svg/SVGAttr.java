package net.ooder.esd.tool.properties.svg;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.tool.properties.svg.comb.BGText;
import net.ooder.esd.tool.properties.svg.comb.Text;

import java.util.List;

public class SVGAttr {

    Integer rx;

    Integer ry;

    Integer x;

    Integer y;

    Integer r;

    Integer cx;

    Integer cy;

    String stroke;

    String fill;

    String path;

    String text;

    String src;

    List<String> transform;

    @JSONField(name = "stroke-width")
    Integer strokewidth;

    @JSONField(name = "stroke-dasharray")
    String strokedasharray;

    @JSONField(name = "stroke-linecap")
    String strokelinecap;

    @JSONField(name = "stroke-opacity")
    Long strokeopacity;

    @JSONField(name = "stroke-linejoin")
    String strokelinejoin;

    @JSONField(name = "stroke-miterlimit")
    Integer strokemiterlimit;

    @JSONField(name = "arrow-end")
    String arrowend;

    @JSONField(name = "arrow-start")
    String arrowstart;

    String title;


    @JSONField(name = "TEXT")
    public Text TEXT;
    @JSONField(name = "BG")
    public BGText BG;

    public SVGAttr() {

    }




    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getR() {
        return r;
    }

    public void setR(Integer r) {
        this.r = r;
    }


    public Long getStrokeopacity() {
        return strokeopacity;
    }

    public void setStrokeopacity(Long strokeopacity) {
        this.strokeopacity = strokeopacity;
    }

    public String getStrokelinejoin() {
        return strokelinejoin;
    }

    public void setStrokelinejoin(String strokelinejoin) {
        this.strokelinejoin = strokelinejoin;
    }

    public Integer getRx() {
        return rx;
    }

    public void setRx(Integer rx) {
        this.rx = rx;
    }

    public Integer getRy() {
        return ry;
    }

    public void setRy(Integer ry) {
        this.ry = ry;
    }

    public Integer getCx() {
        return cx;
    }

    public void setCx(Integer cx) {
        this.cx = cx;
    }

    public Integer getCy() {
        return cy;
    }

    public void setCy(Integer cy) {
        this.cy = cy;
    }

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public List<String> getTransform() {
        return transform;
    }

    public void setTransform(List<String> transform) {
        this.transform = transform;
    }

    public Integer getStrokewidth() {
        return strokewidth;
    }

    public void setStrokewidth(Integer strokewidth) {
        this.strokewidth = strokewidth;
    }

    public String getStrokedasharray() {
        return strokedasharray;
    }

    public void setStrokedasharray(String strokedasharray) {
        this.strokedasharray = strokedasharray;
    }

    public String getStrokelinecap() {
        return strokelinecap;
    }

    public void setStrokelinecap(String strokelinecap) {
        this.strokelinecap = strokelinecap;
    }

    public Integer getStrokemiterlimit() {
        return strokemiterlimit;
    }

    public void setStrokemiterlimit(Integer strokemiterlimit) {
        this.strokemiterlimit = strokemiterlimit;
    }

    public String getArrowend() {
        return arrowend;
    }

    public void setArrowend(String arrowend) {
        this.arrowend = arrowend;
    }

    public String getArrowstart() {
        return arrowstart;
    }

    public void setArrowstart(String arrowstart) {
        this.arrowstart = arrowstart;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BGText getBG() {
        return BG;
    }

    public void setBG(BGText BG) {
        this.BG = BG;
    }


    public Text getTEXT() {
        return TEXT;
    }

    public void setTEXT(Text TEXT) {
        this.TEXT = TEXT;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
