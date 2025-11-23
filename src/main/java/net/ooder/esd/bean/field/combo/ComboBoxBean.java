package net.ooder.esd.bean.field.combo;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;

import java.util.List;
import java.util.Set;

public interface ComboBoxBean<M extends Component> {

    List<JavaSrcBean> getJavaSrcBeans();

    ComboInputType[] getComboInputType();

    ComboInputType getInputType();

    List<CustomBean> getAnnotationBeans();

    Set<Class> getOtherClass();


}
