package net.ooder.esd.bean;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ui.ModuleViewType;

import java.util.Set;

public interface CustomData extends CustomBean {

    String getDataUrl();

    Boolean getCache();

    Set<String> getCustomServiceClass();

    ModuleViewType getModuleViewType();
}
