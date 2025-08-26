package net.ooder.esd.tool.properties;

import net.ooder.esd.annotation.ContainerAnnotation;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.web.util.AnnotationUtil;

public class ContainerDivProperties extends ContainerProperties {


    public ContainerDivProperties(ContainerBean containerBean) {
        if (containerBean == null) {
            containerBean = AnnotationUtil.fillDefaultValue(ContainerAnnotation.class, new ContainerBean());
        }
        super.init(containerBean);
    }

    public ContainerDivProperties() {

    }

    public ContainerDivProperties(Dock dock) {
        super(dock);
    }


}


