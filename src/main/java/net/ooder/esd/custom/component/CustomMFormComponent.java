package net.ooder.esd.custom.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.view.CustomFormViewBean;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.custom.component.form.MFormLayoutModule;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.MFormLayoutComponent;
import net.ooder.esd.tool.properties.form.FormLayoutProperties;

import java.util.List;
import java.util.Map;

public class CustomMFormComponent extends MFormLayoutComponent implements DataComponent<Map<String, Object>> {
    @JSONField(serialize = false)
    private final MFormLayoutModule layoutModule;


    public CustomMFormComponent(EUModule module, List<FieldFormConfig> fieldList, Map dbMap, CustomFormViewBean viewBean) {
        super(module.getName() + "Form",  new FormLayoutProperties(viewBean));
        this.getProperties().setDock(Dock.fill);
        this.layoutModule = new MFormLayoutModule(module, (MFormLayoutComponent) this, fieldList, dbMap, viewBean);

    }

    @Override
    @JSONField(serialize = false)
    public Map<String, Object> getData() {
        return layoutModule.getValueMap();
    }

    @Override
    public void setData(Map<String, Object> data) {
        this.getModuleComponent().fillFormValues(data, false);
    }
}
