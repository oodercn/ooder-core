package net.ooder.esd.custom.service;

import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.common.JDSException;
import net.ooder.config.ListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esb.util.EsbFactory;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComboType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.annotation.ui.ResponsePathEnum;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.FieldProperties;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.esd.util.ESDEnumsUtil;

import net.ooder.jds.core.esb.EsbUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping(path = "/form/custom/field/")

public class CustomFieldService {


    @RequestMapping(value = "onChange.dyn")
    @APIEventAnnotation(customResponseData = ResponsePathEnum.EXPRESSION, customRequestData = RequestPathEnum.CURRFORM)
    @ResponseBody
    public ListResultModel<List<Component>> onChange(String fieldName, ModuleComponent moduleComponent) {
        ListResultModel<List<Component>> listResultModel = new ListResultModel();
        List<Component> components = new ArrayList<>();
        Set<String> fieldNames = new HashSet<>();
        List<String> dynAlains = new ArrayList<>();
        List<Component> comboInputComponents = new ArrayList<>();
        Map<String, Object> objectMap = JDSActionContext.getActionContext().getContext();
        objectMap.putAll(moduleComponent.getValueMap());
        try {
            moduleComponent = moduleComponent.getRealModuleComponent();
            if (fieldName!=null){
                Component currComponent = moduleComponent.findComponentByAlias(fieldName);
                Object fieldValue = JDSActionContext.getActionContext().getContext().get(currComponent.getAlias());
                FieldProperties currProperties = (FieldProperties) currComponent.getProperties();
                if (currProperties instanceof ComboInputProperties) {
                    ComboInputType inputType = ((ComboInputProperties) currProperties).getType();
                    ComboType comboType = inputType.getComboType();
                    if (comboType.equals(ComboType.number)) {
                        currProperties.setValue(TypeUtils.cast(fieldValue, Float.class, null));
                    }
                } else {
                    currProperties.setValue(fieldValue);
                }
                objectMap.put(fieldName, fieldValue);
                objectMap.put(currComponent.getTarget(), fieldValue);
                dynAlains.add(fieldName);
                dynAlains.add(currComponent.getTarget());
            }


            List<Component> childComponents = moduleComponent.findByPropertiesType(FieldProperties.class, "");
            List<Component> dynComs = new ArrayList<>();
            for (Component childComponent : childComponents) {
                fieldNames.add(childComponent.getAlias());
                fieldNames.add(childComponent.getTarget());
                FieldProperties itemProperties = (FieldProperties) childComponent.getProperties();
                String expression = itemProperties.getExpression();
                if (expression != null && !expression.equals("")) {
                    dynComs.add(childComponent);
                }
                if (itemProperties instanceof ComboInputProperties) {
                    ComboInputProperties comboInputProperties = (ComboInputProperties) itemProperties;
                    Class enumClassName = comboInputProperties.getEnumClass();
                    String filter = comboInputProperties.getFilter();
                    if (enumClassName != null && filter != null && !filter.equals("")) {
                        dynComs.add(childComponent);
                    }
                }

            }
            //支持级联运算
            boolean loop = true;
            while (loop) {
                loop = false;
                for (Component childComponent : dynComs) {
                    FieldProperties itemProperties = (FieldProperties) childComponent.getProperties();
                    boolean hasAlias = false;
                    if (!comboInputComponents.contains(childComponent)) {
                        String expression = itemProperties.getExpression();
                        if (expression != null) {
                            for (String alia : dynAlains) {
                                String macrosExpression = EsbFactory.macrosExpression(expression, fieldNames);
                                if (macrosExpression.contains("$Custom['" + alia + "']")) {
                                    hasAlias = true;
                                }
                            }
                        }
                        if (!hasAlias) {
                            if (itemProperties instanceof ComboInputProperties) {
                                ComboInputProperties comboInputProperties = (ComboInputProperties) itemProperties;
                                String itemPropertiesExpression = comboInputProperties.getItemsExpression();
                                if (itemPropertiesExpression != null) {
                                    for (String alia : dynAlains) {
                                        String macrosExpression = EsbFactory.macrosExpression(itemPropertiesExpression, fieldNames);
                                        if (macrosExpression.contains("$Custom['" + alia + "']")) {
                                            hasAlias = true;
                                        }
                                    }
                                }

                                String filter = comboInputProperties.getFilter();
                                if (filter != null && !hasAlias) {
                                    for (String alia : dynAlains) {
                                        String macrosExpression = EsbFactory.macrosExpression(filter, fieldNames);
                                        if (macrosExpression.contains("$Custom['" + alia + "']")) {
                                            hasAlias = true;
                                        }
                                    }
                                }
                            }
                        }
                        if (hasAlias) {
                            loop = true;
                            comboInputComponents.add(childComponent);
                            dynAlains.add(childComponent.getAlias());
                            dynAlains.add(childComponent.getTarget());
                        }
                    }
                }
            }


            for (Component childComponent : childComponents) {
                FieldProperties fieldProperties = (FieldProperties) childComponent.getProperties();
                Object value = fieldProperties.getValue();
                if (fieldProperties instanceof ComboInputProperties) {
                    ComboInputType inputType = ((ComboInputProperties) fieldProperties).getType();
                    ComboType comboType = inputType.getComboType();
                    if (comboType.equals(ComboType.number)) {
                        if (value==null||value.equals("")){
                            value=0;
                        }else{
                            value =TypeUtils.cast(value, Float.class, null);
                        }
                    }
                }
                objectMap.put(childComponent.getAlias(), value);
                objectMap.put(childComponent.getTarget(), value);
            }



            for (Component childComponent : comboInputComponents) {
                FieldProperties itemProperties = (FieldProperties) childComponent.getProperties();

                if (itemProperties instanceof ComboInputProperties) {
                    ComboInputProperties comboInputProperties = (ComboInputProperties) itemProperties;
                    Class<? extends Enum> enumClass = comboInputProperties.getEnumClass();
                    String filter = comboInputProperties.getFilter();
                    String itemPropertiesExpression = comboInputProperties.getItemsExpression();
                    String expression = comboInputProperties.getExpression();
                    if (filter != null && !filter.equals("") && !enumClass.equals(Enum.class)) {
                        List<TreeListItem> items = ESDEnumsUtil.getItems(enumClass, comboInputProperties.getFilter());
                        comboInputProperties.setItems(items);
                        itemProperties.setValue(JDSActionContext.getActionContext().getContext().get(fieldName));
                    } else if (itemPropertiesExpression != null && !itemPropertiesExpression.equals("")) {
                        List<TreeListItem> items = ESDEnumsUtil.parItemExpression(itemPropertiesExpression);
                        comboInputProperties.setItems(items);
                        itemProperties.setValue(JDSActionContext.getActionContext().getContext().get(fieldName));
                    } else if (expression != null && !expression.equals("")) {
                        if (itemProperties instanceof ComboInputProperties) {
                            ComboInputType inputType = ((ComboInputProperties) itemProperties).getType();
                            ComboType comboType = inputType.getComboType();
                            if (comboType.equals(ComboType.number)) {
                                Object object = EsbUtil.parExpression(expression, Float.class);
                                itemProperties.setValue(object);
                            }
                        } else {
                            itemProperties.setValue(EsbUtil.parExpression(expression));
                        }

                    }
                }
            }
            components.addAll(comboInputComponents);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        listResultModel.setData(components);
        return listResultModel;
    }

    @RequestMapping(value = "fieldReload.dyn")
    @APIEventAnnotation(customResponseData = ResponsePathEnum.EXPRESSION, customRequestData = RequestPathEnum.CURRFORM, bindFieldEvent = CustomFieldEvent.DYNRELOAD)
    @ResponseBody
    public ListResultModel<List<Component>> fieldReload(String fieldName, ModuleComponent moduleComponent) {
        ListResultModel<List<Component>> listResultModel = new ListResultModel();
        List<Component> components = new ArrayList<>();
        List<Component> comboInputComponents = new ArrayList<>();
        try {
            moduleComponent = moduleComponent.getRealModuleComponent();
            Component component = moduleComponent.findComponentByAlias(fieldName);
            JDSActionContext.getActionContext().getContext().put(component.getTarget(), JDSActionContext.getActionContext().getContext().get(fieldName));
            FieldProperties itemProperties = (FieldProperties) component.getProperties();
            if (itemProperties instanceof ComboInputProperties) {
                ComboInputProperties comboInputProperties = (ComboInputProperties) itemProperties;
                Class<? extends Enum>  enumClass = comboInputProperties.getEnumClass();
                String filter = comboInputProperties.getFilter();
                if (filter != null && !filter.equals("") && !enumClass.equals(Enum.class)) {
                    List<TreeListItem> items = ESDEnumsUtil.getItems(enumClass, comboInputProperties.getFilter());
                    comboInputProperties.setItems(items);
                }
            }

            itemProperties.setValue(JDSActionContext.getActionContext().getContext().get(fieldName));
            comboInputComponents.add(component);
            components.addAll(comboInputComponents);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        listResultModel.setData(components);
        return listResultModel;
    }
}
