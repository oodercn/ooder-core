package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.JoinOperator;
import net.ooder.annotation.Operator;
import net.ooder.esd.annotation.SearchAnnotation;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = SearchAnnotation.class)
public class SearchFieldBean implements CustomBean {


    Operator operator;

    JoinOperator joinOperator;

    Boolean orderAsc;

    Boolean orderDesc;

    Class bindClass;

    public SearchFieldBean(Properties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public SearchFieldBean(SearchAnnotation searchAnnotation) {
        AnnotationUtil.fillBean(searchAnnotation, this);
    }

    public SearchFieldBean() {
        AnnotationUtil.fillDefaultValue(SearchAnnotation.class, this);
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public JoinOperator getJoinOperator() {
        return joinOperator;
    }

    public void setJoinOperator(JoinOperator joinOperator) {
        this.joinOperator = joinOperator;
    }

    public boolean isOrderAsc() {
        return orderAsc;
    }

    public void setOrderAsc(boolean orderAsc) {
        this.orderAsc = orderAsc;
    }

    public boolean isOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(boolean orderDesc) {
        this.orderDesc = orderDesc;
    }

    public Class getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class bindClass) {
        this.bindClass = bindClass;
    }

    public SearchFieldBean fillData(SearchAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
