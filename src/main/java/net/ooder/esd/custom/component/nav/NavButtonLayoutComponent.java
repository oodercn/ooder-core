package net.ooder.esd.custom.component.nav;

import net.ooder.common.JDSException;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomButtonLayoutFieldBean;
import net.ooder.esd.bean.field.CustomButtonViewsFieldBean;
import net.ooder.esd.bean.view.NavButtonLayoutComboViewBean;
import net.ooder.esd.custom.properties.NavButtonLayoutProperties;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ButtonLayoutComponent;

import java.util.Map;

public class NavButtonLayoutComponent extends ButtonLayoutComponent {


    public NavButtonLayoutComponent(NavButtonLayoutComboViewBean navGalleryViewBean) {
        init(navGalleryViewBean);
    }

    public NavButtonLayoutComponent(EUModule euModule, FieldFormConfig<CustomButtonLayoutFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super();
        try {
            this.init(euModule, field.getMethodConfig(), valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public void init(EUModule module, MethodConfig methodConfig, Map<String, ?> valueMap) throws JDSException {}

    public NavButtonLayoutComponent(MethodConfig methodConfig) {
        super();
        NavButtonLayoutComboViewBean navButtonLayoutViewBean = (NavButtonLayoutComboViewBean) methodConfig.getView();
        init(navButtonLayoutViewBean);
    }

    void init(NavButtonLayoutComboViewBean navButtonLayoutViewBean) {
        this.setProperties(new NavButtonLayoutProperties(navButtonLayoutViewBean));
        this.setAlias(navButtonLayoutViewBean.getName());
    }

}
