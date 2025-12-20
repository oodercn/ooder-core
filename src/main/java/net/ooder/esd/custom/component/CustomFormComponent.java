package net.ooder.esd.custom.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.view.CustomFormViewBean;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.custom.component.form.FormLayoutModule;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.FormLayoutComponent;
import net.ooder.esd.tool.properties.form.FormLayoutProperties;

import java.util.List;
import java.util.Map;

public class CustomFormComponent extends FormLayoutComponent implements DataComponent<Object> {
    @JSONField(serialize = false)
    private final FormLayoutModule layoutModule;


    public CustomFormComponent(EUModule module, List<FieldFormConfig> fieldList, Map dbMap, CustomFormViewBean viewBean) {
        super(module.getName(), new FormLayoutProperties(viewBean));
        this.getProperties().setDock(Dock.fill);
        this.layoutModule = new FormLayoutModule(module, this, fieldList, dbMap, viewBean);

    }

    @Override
    @JSONField(serialize = false)
    public Map<String, Object> getData() {
        return layoutModule.getValueMap();
    }

    @Override
    public void setData(Object data) {
        Map<String, Object> mapData = JSONObject.parseObject(JSON.toJSONString(data), Map.class);
        this.getModuleComponent().fillFormValues(mapData, false);
    }
}
