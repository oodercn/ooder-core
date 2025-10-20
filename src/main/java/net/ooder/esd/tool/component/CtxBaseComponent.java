package net.ooder.esd.tool.component;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.VisibilityType;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.tool.properties.BlockProperties;
import net.ooder.esd.tool.properties.form.FormField;
import net.ooder.esd.tool.properties.form.HiddenInputProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CtxBaseComponent extends BlockComponent {

    @JSONField(serialize = false)
    Map<String, HiddenInputComponent> ctxMap = new HashMap();


    public CtxBaseComponent(Map<String, Object> params, String ctxName) {
        super(ctxName, new BlockProperties());
        BlockProperties properties = this.getProperties();
        properties.setVisibility(VisibilityType.hidden);
        Set<String> nameSet = params.keySet();
        for (String name : nameSet) {
            if (name != null && !name.equals("")) {
                addParams(name, params.get(name), false);
            }
        }
    }

    public Map<String, String> getParams() {
        Map<String, String> paramMap = new HashMap<>();
        Map<String, HiddenInputComponent> params = getCtxMap();
        Set<String> keyset = params.keySet();
        for (String key : keyset) {
            HiddenInputComponent hiddenInputComponent = getFieldByName(key);
            if (hiddenInputComponent != null) {
                Object obj = hiddenInputComponent.getProperties().getValue();
                if (obj != null && !obj.equals("")) {
                    paramMap.put(key, obj.toString());
                }
            }
        }
        return paramMap;

    }

    ;


    public CtxBaseComponent(List<Component> componentList) {
        super(ModuleComponent.PAGECTXNAME, new BlockProperties());
        BlockProperties properties = this.getProperties();
        properties.setVisibility(VisibilityType.hidden);
        if (componentList != null) {
            for (Component component : componentList) {
                if (component.getProperties() instanceof FormField) {
                    FormField field = (FormField) component.getProperties();
                    if (field instanceof HiddenInputProperties) {
                        HiddenInputProperties hiddenInputProperties = (HiddenInputProperties) field;
                        addParams(field.getName(), field.getValue(), hiddenInputProperties.getPid());
                    } else {
                        addParams(field.getName(), field.getValue(), false);
                    }

                }
            }
        }
    }

    public HiddenInputComponent getFieldByName(String name) {
        List<Component> components = this.getChildren();
        if (components != null) {
            for (Component component : components) {
                HiddenInputComponent hiddenInputComponent = (HiddenInputComponent) component;
                String fieldName = hiddenInputComponent.getProperties().getName() == null ? hiddenInputComponent.getAlias() : hiddenInputComponent.getProperties().getName();
                if (fieldName.equals(name)) {
                    return hiddenInputComponent;
                }
            }
        }
        return null;
    }


    public void addESDFields(List<ESDField> fieldList, Map<String, Object> map) {
        for (ESDField field : fieldList) {
            HiddenInputComponent hiddenInputComponent = getFieldByName(field.getId());

            if (hiddenInputComponent == null) {
                this.addChildren(new HiddenInputComponent(field.getId(), field.getName(), map.get(field.getName()), (field.isPid() || field.isUid())));
            } else {
                Object obj = map.get(field.getId());
                if (obj != null && !obj.equals("")) {
                    hiddenInputComponent.getProperties().setValue(map.get(field.getId()));
                }
                hiddenInputComponent.getProperties().setPid(field.isPid() || field.isUid());
            }
        }

    }


    public void addFields(List<ESDFieldConfig> fieldList, Map<String, Object> map) {
        for (ESDFieldConfig field : fieldList) {
            boolean isPid = false;
            if (field.getPid() != null && field.getPid()) {
                isPid = true;
            }
            boolean isUid = false;
            if (field.getUid() != null && field.getUid()) {
                isUid = true;
            }
            HiddenInputComponent hiddenInputComponent = getFieldByName(field.getId());
            if (hiddenInputComponent == null) {

                this.addChildren(new HiddenInputComponent(field.getId(), field.getFieldname(), map.get(field.getFieldname()), (isPid || isUid)));
            } else {
                Object obj = map.get(field.getId());
                if (obj != null && !obj.equals("")) {
                    hiddenInputComponent.getProperties().setValue(map.get(field.getId()));
                }

                hiddenInputComponent.getProperties().setPid(isPid || isUid);
            }
        }

    }

    public void addParams(Map<String, Object> params) {
        Set<String> keyset = params.keySet();
        for (String key : keyset) {
            HiddenInputComponent hiddenInputComponent = getFieldByName(key);
            if (hiddenInputComponent == null) {
                this.addChildren(new HiddenInputComponent(key, key, params.get(key)));
            } else {
                Object obj = params.get(key);
                if (obj != null && !obj.equals("")) {
                    hiddenInputComponent.getProperties().setValue(params.get(key));
                }
            }
        }
        this.getModuleComponent().setChildren(ModuleComponent.PAGECTXNAME, this);
    }

    public void removeParams(String fileName) {
        HiddenInputComponent component = getFieldByName(fileName);
        if (component != null) {
            this.getChildren().remove(component);
            this.ctxMap.remove(fileName);
        }
    }

    public void clearParams() {
        Map<String, HiddenInputComponent> params = getCtxMap();
        Set<String> keyset = params.keySet();
        for (String key : keyset) {
            HiddenInputComponent hiddenInputComponent = getFieldByName(key);
            if (hiddenInputComponent != null) {
                hiddenInputComponent.getProperties().setValue(null);
            }
        }
    }

    public void addParams(String name, Object obj, boolean isPid) {
        HiddenInputComponent component = ctxMap.get(name);
        if (component == null) {
            component = new HiddenInputComponent(name, name, obj, isPid);
            this.addChildren(component);
            ctxMap.put(name, component);
        } else {
            component.getProperties().setValue(obj);
            component.getProperties().setPid(isPid);
        }
    }

    public Map<String, HiddenInputComponent> getCtxMap() {
        return ctxMap;
    }

    public void setCtxMap(Map<String, HiddenInputComponent> ctxMap) {
        this.ctxMap = ctxMap;
    }

}
