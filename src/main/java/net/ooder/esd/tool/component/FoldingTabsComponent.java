package net.ooder.esd.tool.component;

import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.custom.properties.NavFoldingTabsProperties;
import net.ooder.esd.custom.properties.NavTabListItem;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.properties.Action;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RequestParamBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoldingTabsComponent extends TabsComponent<NavFoldingTabsProperties> {

    public void FoldingTabsComponent(String alias, NavFoldingTabsProperties properties) {
        this.alias = alias;
        this.setProperties(properties);
    }

    public FoldingTabsComponent addAction(Action<TabsEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public FoldingTabsComponent() {
        super(ComponentType.FOLDINGTABS);
        this.setProperties(new NavFoldingTabsProperties());
    }

}
