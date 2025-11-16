package net.ooder.esd.bean.field;

import net.ooder.esd.bean.ComponentBean;
import net.ooder.esd.bean.FieldEventBean;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;

import java.util.LinkedHashSet;
import java.util.List;

public interface FieldComponentBean<M extends Component> extends ComponentBean<M> {

    public List<JavaSrcBean> getJavaSrcBeans();

    public List<JavaSrcBean> update(ModuleComponent moduleComponent, M component);

}
