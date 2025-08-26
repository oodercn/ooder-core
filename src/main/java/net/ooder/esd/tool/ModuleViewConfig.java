package net.ooder.esd.tool;

import net.ooder.esd.bean.DesignViewBean;
import net.ooder.esd.bean.ModuleStyleBean;
import net.ooder.esd.bean.ModuleViewBean;

public class ModuleViewConfig {
    public DesignViewConf designViewConf;
    public ViewStyles viewStyles;

    public ModuleViewConfig() {
        this.designViewConf = new DesignViewConf(new DesignViewBean());
        this.viewStyles = new ViewStyles(new ModuleStyleBean());
    }

    public ModuleViewConfig(ModuleViewBean moduleViewBean) {
        if (moduleViewBean.getDesignViewBean() != null) {
            this.designViewConf = new DesignViewConf(moduleViewBean.getDesignViewBean());
        } else {
            this.designViewConf = new DesignViewConf(new DesignViewBean());
        }

        if (moduleViewBean.getViewStyles() != null) {
            this.viewStyles = new ViewStyles(moduleViewBean.getViewStyles());

        } else {
            this.viewStyles = new ViewStyles(new ModuleStyleBean());
        }
    }


    public DesignViewConf getDesignViewConf() {
        return designViewConf;
    }

    public void setDesignViewConf(DesignViewConf designViewConf) {
        this.designViewConf = designViewConf;
    }

    public ViewStyles getViewStyles() {
        return viewStyles;
    }

    public void setViewStyles(ViewStyles viewStyles) {
        this.viewStyles = viewStyles;
    }
}
