package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.config.ListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.CustomListAnnotation;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.ComponentBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.gen.view.GenViewDicJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.CS;
import net.ooder.esd.tool.properties.item.UIItem;
import net.ooder.esd.tool.properties.list.AbsListProperties;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.esd.util.OODUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.Callable;

@AnnotationType(clazz = CustomListAnnotation.class)
public class CustomListBean<T extends AbsListProperties> implements ComponentBean {

    Boolean dynLoad;

    String itemsExpression;

    Class bindClass;

    String filter;

    String xpath;

    Set<String> enums;

    List<TreeListItem> items;

    CS cs;

    Class<? extends Enum> enumClass;


    public CustomListBean() {

    }


    public List<GenViewDicJava> update(ModuleComponent moduleComponent, Component<T, ?> component) {
        T listProperties = component.getProperties();
        List<GenViewDicJava> tasks = new ArrayList<>();
        this.update(listProperties);
        try {
            if (listProperties.getItems() != null && !listProperties.getItems().isEmpty()) {
                if (moduleComponent == null) {
                    moduleComponent = component.getModuleComponent();
                }
                DomainInst domainInst = moduleComponent.getDomainInst();
                String simClass = OODUtil.formatJavaName(component.getAlias(), true);
                String packageName = moduleComponent.getClassName().toLowerCase();
                String module = packageName.substring(0, packageName.lastIndexOf("."));
                if (domainInst != null && domainInst.getEuPackage() != null) {
                    if (packageName.startsWith(domainInst.getEuPackage())) {
                        module = packageName.substring(domainInst.getEuPackage().length() + 1);
                    }
                }
                String euClassName = packageName + "." + simClass;
                if (bindClass == null || bindClass.equals(Void.class)) {
                    if (enumClass == null || enumClass.equals(Enum.class)) {
                        GenViewDicJava genViewDicJava = new GenViewDicJava(domainInst.getViewInst(), module.toLowerCase(), listProperties.getItems(), euClassName, null);
                        tasks.add(genViewDicJava);
                        bindClass = genViewDicJava.getDicClass();
                        if (bindClass != null && bindClass.isEnum()) {
                            enumClass = bindClass;
                        }

                    }
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    private void update(T properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
        if (valueMap.get("items") != null) {
            items = JSONArray.parseArray(JSON.toJSONString(valueMap.get("items")), TreeListItem.class);
        }
    }

    public CustomListBean(ESDField esdField) {
        this.init(esdField);
    }


    public CustomListBean(ESDField esdField, Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(CustomListAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof CustomListAnnotation) {
                fillData((CustomListAnnotation) annotation);
            }
        }
        init(esdField);
    }


    void init(ESDField esdField) {
        if (enums == null || enums.size() == 0) {
            enums = esdField.getEnums();
        }

        this.enumClass = esdField.getEnumClass();
        if (bindClass == null) {
            bindClass = enumClass;
        }

        if (bindClass != null && !bindClass.equals(Void.class) && !bindClass.equals(Enum.class)) {
            try {
                ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClass.getName());
                MethodConfig methodConfig = config.getFieldEvent(CustomFieldEvent.LOADCS);
                if (methodConfig != null && methodConfig.getDataBean().getCs() != null) {
                    cs = methodConfig.getDataBean().getCs();
                }
                if (bindClass.isEnum()) {
                    items = ESDEnumsUtil.getItems(bindClass, filter);
                } else if (dynLoad != null && !dynLoad) {
                    MethodConfig loadChildMethod = config.getFieldEvent(CustomFieldEvent.LOADITEMS);
                    if (loadChildMethod != null && UIItem.class.isAssignableFrom(loadChildMethod.getInnerReturnType())) {
                        Object object = loadChildMethod.getRequestMethodBean().invok(JDSActionContext.getActionContext().getOgnlContext(), JDSActionContext.getActionContext().getContext());
                        if (object instanceof ListResultModel) {
                            ListResultModel<List<UIItem>> resultModel = (ListResultModel) object;
                            String json = JSONArray.toJSONString(resultModel.get());
                            items = JSONArray.parseArray(json, TreeListItem.class);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (enums != null && enums.size() > 0) {
            items = ESDEnumsUtil.getItems(enums.toArray(new String[]{}));
        } else if (itemsExpression != null) {
            items = ESDEnumsUtil.parItemExpression(itemsExpression);
        } else {
            Class returnType = esdField.getReturnType();
            if (returnType.isEnum()) {
                if (dynLoad) {
                    items = ESDEnumsUtil.getItems(returnType, null);
                } else {
                    items = ESDEnumsUtil.getItems(returnType, filter);
                }
            }
        }
    }


    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classes = new HashSet<>();
        if (bindClass != null && !bindClass.equals(Void.class)) {
            classes.add(bindClass);
        }
        if (enumClass != null && !enumClass.equals(Enum.class)) {
            classes.add(enumClass);
        }
        return ClassUtility.checkBase(classes);
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (!AnnotationUtil.getAnnotationMap(this).isEmpty()) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }

    public List<TreeListItem> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public CS getCs() {
        return cs;
    }

    public Set<String> getEnums() {
        return enums;
    }

    public void setEnums(Set<String> enums) {
        this.enums = enums;
    }

    public Class getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class bindClass) {
        this.bindClass = bindClass;
    }

    public String getItemsExpression() {
        return itemsExpression;
    }

    public void setItemsExpression(String itemsExpression) {
        this.itemsExpression = itemsExpression;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Boolean getDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(Boolean dynLoad) {
        this.dynLoad = dynLoad;
    }


    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    public void setCs(CS cs) {
        this.cs = cs;
    }

    public void setItems(List<TreeListItem> items) {
        this.items = items;
    }

    public CustomListBean(CustomListAnnotation annotation) {
        fillData(annotation);
    }

    public CustomListBean fillData(CustomListAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.LIST;
    }
}
