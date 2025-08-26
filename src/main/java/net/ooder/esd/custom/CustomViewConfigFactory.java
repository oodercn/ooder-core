package net.ooder.esd.custom;


import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esb.config.manager.EsbBeanFactory;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.ComponentBean;
import net.ooder.esd.bean.WidgetBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.util.OODUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomViewConfigFactory {


    static CustomViewConfigFactory instance;

    public static final String THREAD_LOCK = "Thread Lock";

    Map<ComponentType, Class<Component>> widgetComponentMap = new HashMap<>();

    Map<ModuleViewType, Class<Component>> moduleViewTypeComponentMap = new HashMap<>();

    Map<ComponentType, Class<WidgetBean>> widgetBeanMap = new HashMap<>();

    Map<ComponentType, Class<Annotation>> widgetAnnMap = new HashMap<>();

    Map<Class<Annotation>, CustomClass> viewTypeAnnMap = new HashMap<>();

    Map<ComboInputType, Class<Component>> comboBoxComponentMap = new HashMap<>();

    Map<ComboInputType, Class<ComboBoxBean>> comboBoxBeanMap = new HashMap<>();

    Map<ComboInputType, Class<Annotation>> comboBoxAnnMap = new HashMap<>();

    Set<Class<Annotation>> widgetClass = new HashSet<>();


    public static CustomViewConfigFactory getInstance() {
        if (instance == null) {
            synchronized (THREAD_LOCK) {
                if (instance == null) {
                    instance = new CustomViewConfigFactory();
                }
            }
        }
        return instance;
    }

    public CustomViewConfigFactory() {
        Map<String, Class<?>> allClassMap = new HashMap<>();
        allClassMap.putAll(EsbBeanFactory.getInstance().getAllClass());
        Set<Map.Entry<String, Class<?>>> allClass = allClassMap.entrySet();
        for (Map.Entry<String, Class<?>> clazzEntry : allClass) {
            Class clazz = clazzEntry.getValue();
            try {
                initClass(clazz);
            } catch (Throwable e) {

            }

        }
    }

    void initClass(Class beanClazz) {
        CustomClass customClass = (CustomClass) beanClazz.getAnnotation(CustomClass.class);
        if (CustomBean.class.isAssignableFrom(beanClazz) && customClass != null) {
            AnnotationType ann = (AnnotationType) beanClazz.getAnnotation(AnnotationType.class);
            viewTypeAnnMap.put(ann.clazz(), customClass);
            if (Component.class.isAssignableFrom(customClass.clazz())) {
                switch (customClass.viewType()) {
                    case COMPONENT:
                        widgetBeanMap.put(customClass.componentType(), beanClazz);
                        widgetComponentMap.put(customClass.componentType(), customClass.clazz());
                        if (ann != null) {
                            widgetClass.add(ann.clazz());
                            widgetAnnMap.put(customClass.componentType(), ann.clazz());
                        }
                        break;
                    case COMBOBOX:
                        if (ann != null) {
                            for (ComboInputType inputType : customClass.inputType()) {
                                comboBoxAnnMap.put(inputType, ann.clazz());
                                comboBoxComponentMap.put(inputType, customClass.clazz());
                            }
                            widgetClass.add(ann.clazz());
                            widgetAnnMap.put(customClass.componentType(), ann.clazz());
                        }


                        for (ComboInputType inputType : customClass.inputType()) {
                            comboBoxBeanMap.put(inputType, beanClazz);
                        }

                        break;
                }
            }

        }
    }

    public Class<Component> getComboBoxComponent(ComboInputType inputType) {
        Class<Component> componentClass = comboBoxComponentMap.get(inputType);
        return componentClass;
    }

    public Class<Component> getComponent(ComponentType componentType) {
        Class<Component> componentClass = widgetComponentMap.get(componentType);
        return componentClass;
    }

    public Class<Component> getComponent(ModuleViewType moduleViewType) {
        Class<Component> componentClass = moduleViewTypeComponentMap.get(moduleViewType);
        return componentClass;
    }


    public CustomClass getRealCustomAnnotation(Method method) {
        Set<Annotation> annotationSet = AnnotationUtil.getAllAnnotations(method, true);

        for (Annotation annotation : annotationSet) {
            CustomClass customClass = viewTypeAnnMap.get(annotation.annotationType());

            if (customClass != null && !customClass.moduleType().equals(ModuleViewType.DYNCONFIG) && !customClass.moduleType().equals(ModuleViewType.NONE)) {
                return customClass;
            }
        }
        return null;
    }


    public Set<Annotation> getViewAnnotation(Field field) {
        Set<Annotation> annotationSet = new HashSet<>();
        for (Annotation annotation : field.getAnnotations()) {
            CustomClass customClass = viewTypeAnnMap.get(annotation.annotationType());
            if (customClass != null && !customClass.moduleType().equals(Void.class)) {
                annotationSet.add(customClass);
            }
        }
        return annotationSet;
    }


    public Annotation getViewAnnotation(Method method) {
        Set<Annotation> annotationSet = AnnotationUtil.getAllAnnotations(method, true);
        if (method.getName().startsWith("get")) {
            Field field = null;
            String fieldName = OODUtil.formatJavaName(method.getName(), false);
            try {
                field = method.getDeclaringClass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                try {
                    field = method.getDeclaringClass().getField(fieldName);
                } catch (NoSuchFieldException e1) {
                    //  e1.printStackTrace();
                }
            }
            if (field != null) {
                annotationSet.addAll(getViewAnnotation(field));
            }
        }


        for (Annotation annotation : annotationSet) {
            CustomClass customClass = viewTypeAnnMap.get(annotation.annotationType());
            if (customClass != null && !customClass.moduleType().equals(Void.class)) {
                return annotation;
            }
        }
        return null;
    }


    public ModuleViewType getModuleViewType(Method method) {
        CustomClass customClass = getRealCustomAnnotation(method);
        if (customClass != null && !customClass.moduleType().equals(ModuleViewType.NONE)) {
            return customClass.moduleType();
        }
        return null;
    }


    public Annotation getWidgetAnnotation(Method method) {
        Set<Annotation> allAnnotation = AnnotationUtil.getAllAnnotations(method, true);
        return getWidgetAnnotation(allAnnotation.toArray(new Annotation[]{}));
    }

    public Annotation getWidgetAnnotation(Field field) {
        Annotation[] allAnnotation = field.getAnnotations();
        return getWidgetAnnotation(allAnnotation);
    }


    public Annotation getWidgetAnnotation(Annotation[] allAnnotation) {
        for (Annotation annotation : allAnnotation) {
            CustomClass customClass = viewTypeAnnMap.get(annotation.annotationType());
            if (customClass != null && Component.class.isAssignableFrom(customClass.clazz())) {
                if (customClass.viewType().equals(CustomViewType.COMBOBOX)) {
                    return annotation;
                }
            }
        }

        for (Annotation annotation : allAnnotation) {
            CustomClass customClass = viewTypeAnnMap.get(annotation.annotationType());
            if (customClass != null && Component.class.isAssignableFrom(customClass.clazz())) {
                if (customClass.viewType().equals(CustomViewType.COMPONENT)) {
                    return annotation;
                }
            }
        }


        return null;
    }


    public CustomClass getWidgetCustomAnnotation(Annotation[] allAnnotation) {
        for (Annotation annotation : allAnnotation) {
            CustomClass customClass = viewTypeAnnMap.get(annotation.annotationType());
            if (customClass != null && Component.class.isAssignableFrom(customClass.clazz())) {
                return  customClass;
            }
        }
        return null;
    }

    public Class getWidgetComponentByType(ComponentType componentType) {
        return widgetComponentMap.get(componentType);
    }


    public Class<? extends ComponentBean> getDefaultWidgetClass(ComponentType componentType) {
        Class<? extends ComponentBean> customBeanClass = widgetBeanMap.get(componentType);
        if (customBeanClass == null) {
            //customBeanClass = InputFieldBean.class;
            customBeanClass = ComboInputFieldBean.class;
        }
        return customBeanClass;
    }


    public Class<? extends ComboBoxBean> getDefaultComboBoxClass(ComboInputType inputType) {
        Class<? extends ComboBoxBean> customBeanClass = comboBoxBeanMap.get(inputType);
        if (customBeanClass == null) {
            customBeanClass = ComboInputFieldBean.class;
        }
        return customBeanClass;
    }

    public Map<ComponentType, Class<Component>> getWidgetComponentMap() {
        return widgetComponentMap;
    }

    public void setWidgetComponentMap(Map<ComponentType, Class<Component>> widgetComponentMap) {
        this.widgetComponentMap = widgetComponentMap;
    }

    public Map<ComponentType, Class<WidgetBean>> getWidgetBeanMap() {
        return widgetBeanMap;
    }

    public void setWidgetBeanMap(Map<ComponentType, Class<WidgetBean>> widgetBeanMap) {
        this.widgetBeanMap = widgetBeanMap;
    }

    public Map<ComponentType, Class<Annotation>> getWidgetAnnMap() {
        return widgetAnnMap;
    }

    public void setWidgetAnnMap(Map<ComponentType, Class<Annotation>> widgetAnnMap) {
        this.widgetAnnMap = widgetAnnMap;
    }

    public Map<ComboInputType, Class<Component>> getComboBoxComponentMap() {
        return comboBoxComponentMap;
    }

    public void setComboBoxComponentMap(Map<ComboInputType, Class<Component>> comboBoxComponentMap) {
        this.comboBoxComponentMap = comboBoxComponentMap;
    }

    public Map<ComboInputType, Class<ComboBoxBean>> getComboBoxBeanMap() {
        return comboBoxBeanMap;
    }

    public void setComboBoxBeanMap(Map<ComboInputType, Class<ComboBoxBean>> comboBoxBeanMap) {
        this.comboBoxBeanMap = comboBoxBeanMap;
    }

    public Map<ComboInputType, Class<Annotation>> getComboBoxAnnMap() {
        return comboBoxAnnMap;
    }

    public void setComboBoxAnnMap(Map<ComboInputType, Class<Annotation>> comboBoxAnnMap) {
        this.comboBoxAnnMap = comboBoxAnnMap;
    }

    public Set<Class<Annotation>> getWidgetClass() {
        return widgetClass;
    }

    public void setWidgetClass(Set<Class<Annotation>> widgetClass) {
        this.widgetClass = widgetClass;
    }
}
