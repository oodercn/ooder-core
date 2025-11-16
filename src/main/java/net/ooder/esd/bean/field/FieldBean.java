package net.ooder.esd.bean.field;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.FieldAnnotation;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.event.CustomHotKeyEvent;
import net.ooder.esd.annotation.event.FieldEvent;
import net.ooder.esd.annotation.event.FieldHotKeyEvent;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.PositionType;
import net.ooder.esd.bean.FieldEventBean;
import net.ooder.esd.bean.HotKeyEventBean;
import net.ooder.esd.custom.service.CustomFieldService;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = FieldAnnotation.class)
public class FieldBean implements CustomBean {

    Boolean required;

    Boolean dirtyMark;

    Boolean dynCheck;

    Integer colSpan;

    Integer rawRow;

    Integer rawCol;

    Integer rawColSpan;

    Integer rawRowSpan;

    Integer manualHeight;

    Integer manualWidth;

    Boolean removed;

    Integer rowSpan;

    Integer tabindex;

    Boolean serialize;

    Boolean haslabel;

    String excelCellId;

    String expression;

    String excelCellFormula;

    ComponentType componentType;

    HAlignType textAlign;

    Set<FieldEvent> event;

    Set<FieldHotKeyEvent> hotKeyEvent;

    Set<CustomFieldEvent> customFieldEvent;

    Set<CustomHotKeyEvent> customHotKeyEvent;

    String caption;

    LinkedHashSet<HotKeyEventBean> extHotKeyEvent = new LinkedHashSet<>();

    LinkedHashSet<FieldEventBean> extFieldEvent = new LinkedHashSet<>();

    List<ComponentType> bindTypes;

    PositionType innerPosition;

    Class customContextMenuService;

    Class serviceClass;

    public FieldBean() {
        AnnotationUtil.fillDefaultValue(FieldAnnotation.class, this);
    }


    private void init(Properties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public FieldBean(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof FieldAnnotation) {
                fillData((FieldAnnotation) annotation);
            }
            if (annotation instanceof FieldEvent) {
                extFieldEvent.add(new FieldEventBean((FieldEvent) annotation));
            }

            if (annotation instanceof FieldHotKeyEvent) {
                extHotKeyEvent.add(new HotKeyEventBean<>((FieldHotKeyEvent) annotation));
            }
        }

        for (FieldEvent fieldEvent : event) {
            extFieldEvent.add(new FieldEventBean(fieldEvent));
        }

        for (FieldHotKeyEvent hotKeyEvent : hotKeyEvent) {
            extHotKeyEvent.add(new HotKeyEventBean(hotKeyEvent));
        }

        if (componentType != null && componentType.equals(ComponentType.RICHEDITOR)) {
            this.setManualHeight(200);
            this.colSpan = -1;
        }
    }

    public void update(Component component) {
        this.componentType = ComponentType.fromType(component.getKey());
        this.init(component.getProperties());
    }

    public FieldBean(Component component) {
        AnnotationUtil.fillDefaultValue(FieldAnnotation.class, this);
        update(component);
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public HAlignType getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(HAlignType textAlign) {
        this.textAlign = textAlign;
    }

    public Integer getManualHeight() {
        return manualHeight;
    }

    public void setManualHeight(Integer manualHeight) {
        this.manualHeight = manualHeight;
    }

    public Integer getManualWidth() {
        return manualWidth;
    }

    public void setManualWidth(Integer manualWidth) {
        this.manualWidth = manualWidth;
    }

    public Integer getRawColSpan() {
        return rawColSpan;
    }

    public void setRawColSpan(Integer rawColSpan) {
        this.rawColSpan = rawColSpan;
    }

    public Integer getRawRowSpan() {
        return rawRowSpan;
    }

    public void setRawRowSpan(Integer rawRowSpan) {
        this.rawRowSpan = rawRowSpan;
    }

    public Integer getRawRow() {
        return rawRow;
    }

    public void setRawRow(Integer rawRow) {
        this.rawRow = rawRow;
    }

    public Integer getRawCol() {
        return rawCol;
    }

    public void setRawCol(Integer rawCol) {
        this.rawCol = rawCol;
    }

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    public Set<FieldHotKeyEvent> getHotKeyEvent() {
        return hotKeyEvent;
    }

    public void setHotKeyEvent(Set<FieldHotKeyEvent> hotKeyEvent) {
        this.hotKeyEvent = hotKeyEvent;
    }


    public String getExcelCellId() {
        return excelCellId;
    }

    public void setExcelCellId(String excelCellId) {
        this.excelCellId = excelCellId;
    }

    public String getExcelCellFormula() {
        return excelCellFormula;
    }

    public void setExcelCellFormula(String excelCellFormula) {
        this.excelCellFormula = excelCellFormula;
    }

    public Set<CustomHotKeyEvent> getCustomHotKeyEvent() {
        return customHotKeyEvent;
    }

    public void setCustomHotKeyEvent(Set<CustomHotKeyEvent> customHotKeyEvent) {
        this.customHotKeyEvent = customHotKeyEvent;
    }

    public LinkedHashSet<HotKeyEventBean> getExtHotKeyEvent() {
        return extHotKeyEvent;
    }

    public void setExtHotKeyEvent(LinkedHashSet<HotKeyEventBean> extHotKeyEvent) {
        this.extHotKeyEvent = extHotKeyEvent;
    }

    public LinkedHashSet<FieldEventBean> getExtFieldEvent() {
        return extFieldEvent;
    }

    public void setExtFieldEvent(LinkedHashSet<FieldEventBean> extFieldEvent) {
        this.extFieldEvent = extFieldEvent;
    }

    public PositionType getInnerPosition() {
        return innerPosition;
    }

    public void setInnerPosition(PositionType innerPosition) {
        this.innerPosition = innerPosition;
    }

    public Integer getTabindex() {
        return tabindex;
    }

    public void setTabindex(Integer tabindex) {
        this.tabindex = tabindex;
    }


    public Boolean getDynCheck() {
        return dynCheck;
    }

    public void setDynCheck(Boolean dynCheck) {
        this.dynCheck = dynCheck;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }


    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getSerialize() {
        return serialize;
    }

    public void setSerialize(Boolean serialize) {
        this.serialize = serialize;
    }

    public boolean isHaslabel() {
        return haslabel;
    }

    public Boolean getHaslabel() {
        return haslabel;
    }

    public void setHaslabel(Boolean haslabel) {
        this.haslabel = haslabel;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public Class getServiceClass() {
        if (serviceClass == null || serviceClass.equals(Void.class)) {
            serviceClass = CustomFieldService.class;
        }
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Boolean getDirtyMark() {
        return dirtyMark;
    }

    public void setDirtyMark(Boolean dirtyMark) {
        this.dirtyMark = dirtyMark;
    }

    public Set<FieldEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<FieldEvent> event) {
        this.event = event;
    }

    public Set<CustomFieldEvent> getCustomFieldEvent() {
        return customFieldEvent;
    }

    public void setCustomFieldEvent(Set<CustomFieldEvent> customFieldEvent) {
        this.customFieldEvent = customFieldEvent;
    }

    public List<ComponentType> getBindTypes() {
        return bindTypes;
    }

    public void setBindTypes(List<ComponentType> bindTypes) {
        this.bindTypes = bindTypes;
    }


    public Class getCustomContextMenuService() {
        return customContextMenuService;
    }

    public void setCustomContextMenuService(Class customContextMenuService) {
        this.customContextMenuService = customContextMenuService;
    }

    public FieldBean fillData(FieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
