package net.ooder.esd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.component.Component;

import java.util.List;
import java.util.Set;

public interface ComponentBean<M extends Component> extends CustomBean {

    ComponentType getComponentType();

    String getXpath();


    @JSONField(serialize = false)
    List<CustomBean> getAnnotationBeans();

    Set<Class> getOtherClass();

}
