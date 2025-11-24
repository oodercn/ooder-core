package net.ooder.esd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSException;
import net.ooder.esd.bean.field.FieldComponentBean;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.tool.component.Component;

import java.util.List;

public interface WidgetBean<T extends CustomViewBean, M extends Component> extends FieldComponentBean<M> {
    @JSONField(serialize = false)
    List<CustomBean> getFieldAnnotationBeans();

    @JSONField(serialize = false)
    public AggRootBuild getFieldRootBuild() throws JDSException;

    @JSONField(serialize = false)
    T getViewBean();

    void setViewBean(T viewBean);
}
