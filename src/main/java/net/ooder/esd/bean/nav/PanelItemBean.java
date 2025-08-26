package net.ooder.esd.bean.nav;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.CustomPanelBean;

public interface PanelItemBean extends CustomBean {

    public String getId();

    public String getSourceMethodName();

    public BtnBean getBtnBean();

    public String getEntityClassName();

    public String getSourceClassName();

    public String getMethodName();

    public String getDomainId();

    public CustomPanelBean getPanelBean();

    public ContainerBean getContainerBean();

}
