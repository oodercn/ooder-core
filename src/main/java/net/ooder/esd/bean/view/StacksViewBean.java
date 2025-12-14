package net.ooder.esd.bean.view;

import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.StacksAnnotation;
import net.ooder.esd.annotation.field.StacksFieldAnnotation;
import net.ooder.esd.annotation.TabsAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.properties.StacksListItem;
import net.ooder.esd.custom.properties.StacksProperties;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.tool.component.StacksComponent;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

@AnnotationType(clazz = StacksAnnotation.class)
public class StacksViewBean extends TabsViewBean {
    ModuleViewType moduleViewType = ModuleViewType.NAVSTACKSCONFIG;

    public StacksViewBean() {
        super();
    }


    public StacksViewBean(ModuleComponent<StacksComponent> moduleComponent) {
        AnnotationUtil.fillDefaultValue(TabsAnnotation.class, this);
        AnnotationUtil.fillDefaultValue(StacksAnnotation.class, this);
        StacksComponent component = moduleComponent.getCurrComponent();
        String realPath = component.getPath();
        DSMProperties dsmProperties = moduleComponent.getProperties().getDsmProperties();
        if (dsmProperties != null && dsmProperties.getRealPath() != null) {
            realPath = dsmProperties.getRealPath();
        }
        this.setXpath(realPath);
        StacksProperties tabsProperties = component.getProperties();
        updateModule(moduleComponent);
        this.initProperties(tabsProperties);
    }

    public StacksViewBean(Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof TabsAnnotation) {
                fillData((TabsAnnotation) annotation);
            }
            if (annotation instanceof StacksAnnotation) {
                fillData((StacksAnnotation) annotation);
            }
        }
    }

//
//    @Override
//    public List<StacksListItem> getTabItems() {
//        if (tabItems == null || tabItems.isEmpty()) {
//            Class<? extends Enum> enumClass = getEnumClass();
//            Class<? extends Enum> viewClass = null;
//            String viewClassName = getViewClassName();
//            if (viewClassName != null) {
//                Class clazz = null;
//                try {
//                    clazz = ClassUtility.loadClass(viewClassName);
//                    if (clazz.isEnum()) {
//                        viewClass = clazz;
//                    }
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (viewClass != null) {
//                tabItems = ESDEnumsUtil.getEnumItems(viewClass, StacksListItem.class);
//            } else if (enumClass != null) {
//                tabItems = ESDEnumsUtil.getEnumItems(enumClass, StacksListItem.class);
//            }
//            for (StacksListItem stacksListItem : tabItems) {
//                if (stacksListItem.getBindClass() != null && stacksListItem.getBindClass().length > 0) {
//                    for (Class clazz : stacksListItem.getBindClass()) {
//                        if (clazz != null && clazz.isEnum()) {
//                            MethodConfig editorMethod = null;
//                            try {
//                                ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(clazz.getName());
//                                if (config != null) {
//                                    editorMethod = config.findEditorMethod();
//                                } else {
//                                    AggEntityConfig entityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(clazz.getName(), false);
//                                    editorMethod = entityConfig.getMethodByEvent(CustomTabsEvent.TABEDITOR);
//                                }
//                            } catch (JDSException e) {
//                                e.printStackTrace();
//                            }
//                            if (editorMethod != null) {
//                                stacksListItem.setEuClassName(editorMethod.getEUClassName());
//                            }
//                        }
//
//                    }
//
//                }
//            }
//        }
//        return tabItems;
//    }

    public StacksViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        StacksAnnotation formAnnotation = AnnotationUtil.getClassAnnotation(clazz, StacksAnnotation.class);
        if (formAnnotation == null) {
            AnnotationUtil.fillDefaultValue(StacksAnnotation.class, this);
        } else {
            AnnotationUtil.fillBean(formAnnotation, this);
        }
    }

    public StacksViewBean fillData(StacksAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public StacksViewBean fillData(StacksFieldAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

}