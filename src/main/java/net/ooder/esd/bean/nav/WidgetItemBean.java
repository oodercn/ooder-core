package net.ooder.esd.bean.nav;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.tool.properties.CustomWidgetBean;


public interface WidgetItemBean extends CustomBean {

    public String getId();

    public String getSourceMethodName();

    public String getEntityClassName();

    public String getSourceClassName();

    public String getMethodName();

    public String getDomainId();

    public String getViewInstId();

    public CustomWidgetBean getWidgetBean();

    public ContainerBean getContainerBean();


}
