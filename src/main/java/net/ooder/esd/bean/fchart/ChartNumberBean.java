package net.ooder.esd.bean.fchart;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.fchart.ChartNumberAnnotation;
import net.ooder.esd.tool.properties.fchart.ChartData;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = ChartNumberAnnotation.class)
public class ChartNumberBean implements CustomBean {


    Boolean formatnumber;

    Boolean formatnumberscale;

    String defaultnumberscale;

    String numberscaleunit;

    String numberscalevalue;

    Boolean scalerecursively;

    Integer maxscalerecursion;

    String scaleseparator;

    String numberprefix;

    String numbersuffix;

    String decimalseparator;

    String thousandseparator;

    Integer thousandseparatorposition;

    String indecimalseparator;

    String inthousandseparator;

    Integer decimals;

    Boolean forcedecimals;

    Boolean forceyaxisvaluedecimals;

    Integer yaxisvaluedecimals;

    public ChartNumberBean() {

    }

    public ChartNumberBean( ChartData chartData) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(chartData), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }


    public ChartNumberBean(ChartNumberAnnotation annotation) {

        this.fillData(annotation);
    }

    public ChartNumberBean fillData(ChartNumberAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public Boolean getFormatnumber() {
        return formatnumber;
    }

    public void setFormatnumber(Boolean formatnumber) {
        this.formatnumber = formatnumber;
    }

    public Boolean getFormatnumberscale() {
        return formatnumberscale;
    }

    public void setFormatnumberscale(Boolean formatnumberscale) {
        this.formatnumberscale = formatnumberscale;
    }

    public Boolean getscalerecursively() {
        return scalerecursively;
    }

    public void setscalerecursively(Boolean scalerecursively) {
        this.scalerecursively = scalerecursively;
    }

    public Integer getMaxscalerecursion() {
        return maxscalerecursion;
    }

    public void setMaxscalerecursion(Integer maxscalerecursion) {
        this.maxscalerecursion = maxscalerecursion;
    }


    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }


    public String getDefaultnumberscale() {
        return defaultnumberscale;
    }

    public void setDefaultnumberscale(String defaultnumberscale) {
        this.defaultnumberscale = defaultnumberscale;
    }


    public String getnumberscalevalue() {
        return numberscalevalue;
    }

    public void setnumberscalevalue(String numberscalevalue) {
        this.numberscalevalue = numberscalevalue;
    }


    public String getscaleseparator() {
        return scaleseparator;
    }

    public void setscaleseparator(String scaleseparator) {
        this.scaleseparator = scaleseparator;
    }


    public String getDecimalseparator() {
        return decimalseparator;
    }

    public void setDecimalseparator(String decimalseparator) {
        this.decimalseparator = decimalseparator;
    }

    public String getThousandseparator() {
        return thousandseparator;
    }

    public void setThousandseparator(String thousandseparator) {
        this.thousandseparator = thousandseparator;
    }


    public String getNumberscalevalue() {
        return numberscalevalue;
    }

    public void setNumberscalevalue(String numberscalevalue) {
        this.numberscalevalue = numberscalevalue;
    }

    public Boolean getScalerecursively() {
        return scalerecursively;
    }

    public void setScalerecursively(Boolean scalerecursively) {
        this.scalerecursively = scalerecursively;
    }

    public String getScaleseparator() {
        return scaleseparator;
    }

    public void setScaleseparator(String scaleseparator) {
        this.scaleseparator = scaleseparator;
    }

    public String getNumberprefix() {
        return numberprefix;
    }

    public void setNumberprefix(String numberprefix) {
        this.numberprefix = numberprefix;
    }

    public String getNumbersuffix() {
        return numbersuffix;
    }

    public void setNumbersuffix(String numbersuffix) {
        this.numbersuffix = numbersuffix;
    }

    public String getIndecimalseparator() {
        return indecimalseparator;
    }

    public void setIndecimalseparator(String indecimalseparator) {
        this.indecimalseparator = indecimalseparator;
    }

    public String getInthousandseparator() {
        return inthousandseparator;
    }

    public void setInthousandseparator(String inthousandseparator) {
        this.inthousandseparator = inthousandseparator;
    }

    public Boolean getForcedecimals() {
        return forcedecimals;
    }

    public void setForcedecimals(Boolean forcedecimals) {
        this.forcedecimals = forcedecimals;
    }

    public String getNumberscaleunit() {
        return numberscaleunit;
    }

    public void setNumberscaleunit(String numberscaleunit) {
        this.numberscaleunit = numberscaleunit;
    }

    public Integer getThousandseparatorposition() {
        return thousandseparatorposition;
    }

    public void setThousandseparatorposition(Integer thousandseparatorposition) {
        this.thousandseparatorposition = thousandseparatorposition;
    }

    public Boolean getForceyaxisvaluedecimals() {
        return forceyaxisvaluedecimals;
    }

    public void setForceyaxisvaluedecimals(Boolean forceyaxisvaluedecimals) {
        this.forceyaxisvaluedecimals = forceyaxisvaluedecimals;
    }

    public Integer getYaxisvaluedecimals() {
        return yaxisvaluedecimals;
    }

    public void setYaxisvaluedecimals(Integer yaxisvaluedecimals) {
        this.yaxisvaluedecimals = yaxisvaluedecimals;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
