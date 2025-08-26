package net.ooder.esd.custom.component.form;

import net.ooder.common.util.CnToSpell;
import net.ooder.esd.annotation.ui.VisibilityType;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.HiddenInputComponent;
import net.ooder.esd.tool.properties.BlockProperties;

import java.util.*;


public class CustomFormHiddenBolck extends BlockComponent {

    public CustomFormHiddenBolck(List<ESDField> fieldList, Map map, String alias) {
        super(CnToSpell.getFullSpell(alias + "Hidden"),  new BlockProperties());
        List<String> names = new ArrayList<>();
        if (map == null) {
            map = new HashMap();
        }
        BlockProperties properties =  this.getProperties();
        properties.setVisibility(VisibilityType.hidden);

        for (ESDField field : fieldList) {
            if (!names.contains(field.getId())) {
                names.add(field.getId());
                this.addChildren(new HiddenInputComponent(field.getId(), field.getName(), map.get(field.getName())));
            }

        }
    }

    public HiddenInputComponent getFieldByName(String name) {
        List<Component> components = this.getChildren();
        if (components != null) {
            for (Component component : components) {
                HiddenInputComponent hiddenInputComponent = (HiddenInputComponent) component;
                if (hiddenInputComponent.getProperties().getName().equals(name)) {
                    return hiddenInputComponent;
                }
            }
        }
        return null;

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
    }

    public CustomFormHiddenBolck(Map<String, Object> params, String alias) {
        super(CnToSpell.getFullSpell(alias + "Hidden"), new BlockProperties());
        if (params == null) {
            params = new HashMap();
        }
        BlockProperties properties =  this.getProperties();
        properties.setVisibility(VisibilityType.hidden);
        Set<String> keyset = params.keySet();
        for (String key : keyset) {

            this.addChildren(new HiddenInputComponent(key, key, params.get(key)));
        }
    }

    public CustomFormHiddenBolck(String[] fields, String alias) {
        super(CnToSpell.getFullSpell(alias + "Hidden"),  new BlockProperties());
        BlockProperties properties = this.getProperties();
        List<String> names = new ArrayList<>();
        properties.setVisibility(VisibilityType.hidden);
        for (String field : fields) {
            if (!names.contains(field)) {
                names.add(field);
                this.addChildren(new HiddenInputComponent(field, field, null));
            }
        }
    }

    public CustomFormHiddenBolck() {

    }

}
