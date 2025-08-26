package net.ooder.esd.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.annotation.BeanClass;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.event.BindMenuItem;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.CustomView;
import net.ooder.esd.custom.component.CustomDynLoadView;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RequestMethodBean;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class DSMAnnotationUtil {

    //自定义扩展
    public static <T> T getMethodCustomAnnotation(Method method, Class<T> targetClass) {
        T instance = null;
        Set<Annotation> allAnnotation = AnnotationUtil.getAllAnnotations(method, true);
        for (Annotation annotation : allAnnotation) {
            CustomClass customClass = annotation.annotationType().getAnnotation(CustomClass.class);
            if (customClass != null && targetClass.isAssignableFrom(customClass.clazz())) {
                Constructor[] constructors = customClass.clazz().getConstructors();
                for (Constructor constructor : constructors) {
                    if (constructor.getParameterTypes().length == 1) {
                        Class annClass = constructor.getParameterTypes()[0];
                        if (annClass.isAssignableFrom(annotation.annotationType())) {
                            try {
                                instance = (T) constructor.newInstance(new Object[]{annotation});
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return instance;
    }


    public static CustomClass getMethodCustomClass(Method method, Class targetClass) {
        CustomClass customClass = null;
        List<CustomClass> customClasses = DSMAnnotationUtil.getCustomClassList(method, CustomDynLoadView.class, true);
        if (customClasses.isEmpty()) {
            customClasses = DSMAnnotationUtil.getCustomClassList(method, ModuleComponent.class, true);
        }
        if (customClasses.size() > 0) {
            customClass = customClasses.get(0);
        }
        return customClass;
    }

    public static boolean isDYNView(Method method) {
        List<CustomClass> customClasses = DSMAnnotationUtil.getCustomClassList(method, ModuleComponent.class, true);
        for (CustomClass custom : customClasses) {
            if (CustomDynLoadView.class.equals(custom.clazz())) {
                return true;
            }
        }
        return false;
    }


//    public static CustomClass getRealCustomClass(Method method) {
//        CustomClass customClass = null;
//        List<CustomClass> customClasses = DSMAnnotationUtil.getCustomClassList(method, ModuleComponent.class, true);
//
//        for (CustomClass custom : customClasses) {
//            if (!CustomDynLoadView.class.equals(custom.clazz()) && Arrays.asList(CustomViewType.getCustomModule()).contains(custom.viewType())) {
//                customClass = custom;
//            }
//        }
//        return customClass;
//    }
//
//    public static CustomClass getRealCustomClass(Field field) {
//        CustomClass customClass = null;
//        List<CustomClass> customClasses = DSMAnnotationUtil.getCustomClassList(field, ModuleComponent.class);
//        for (CustomClass custom : customClasses) {
//            if (!CustomDynLoadView.class.equals(custom.clazz())) {
//                customClass = custom;
//            }
//        }
//        return customClass;
//    }
//
//    public static CustomClass getRealWidgetClass(Method method) {
//        CustomClass customClass = null;
//        List<CustomClass> customClasses = DSMAnnotationUtil.getCustomClassList(method, Component.class, true);
//        for (CustomClass custom : customClasses) {
//            if (!CustomDynLoadView.class.equals(custom.clazz())) {
//                customClass = custom;
//            }
//        }
//        return customClass;
//    }
//

    //自定义扩展
    public static List<CustomClass> getCustomClassList(Method method, Class targetClass, boolean hasField) {
        Set<Annotation> allAnnotation = AnnotationUtil.getAllAnnotations(method, true);
        List<CustomClass> customClasses = new ArrayList<CustomClass>();
        Field field = null;
        if (method.getName().startsWith("get") && hasField) {
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
        }

        if (field != null) {
            customClasses.addAll(getCustomClassList(field, targetClass));
        }

        for (Annotation annotation : allAnnotation) {
            CustomClass customClass = annotation.annotationType().getAnnotation(CustomClass.class);
            if (customClass != null && (targetClass.equals(customClass.clazz()) || customClass.clazz().equals(Void.class) || targetClass.isAssignableFrom(customClass.clazz()))) {
                customClasses.add(customClass);
            }
        }
        return customClasses;
    }

    //自定义扩展
    public static List<CustomClass> getCustomClassList(Field field, Class targetClass) {
        Annotation[] allAnnotation = field.getAnnotations();
        List<CustomClass> customClasses = new ArrayList<CustomClass>();
        for (Annotation annotation : allAnnotation) {
            CustomClass customClass = annotation.annotationType().getAnnotation(CustomClass.class);
            if (customClass != null && (targetClass.equals(customClass.clazz()) || targetClass.isAssignableFrom(customClass.clazz()))) {
                customClasses.add(customClass);
            }
        }
        return customClasses;
    }

    public static <T extends Annotation> T getClassCustomAnnotation(Class clazz, Class<T> targetClass) {
        T instance = null;
        Annotation[] allAnnotation = clazz.getAnnotations();
        for (Annotation annotation : allAnnotation) {
            CustomClass customClass = annotation.getClass().getAnnotation(CustomClass.class);
            if (customClass != null && targetClass.isAssignableFrom(customClass.clazz())) {
                Constructor[] constructors = customClass.clazz().getConstructors();
                for (Constructor constructor : constructors) {
                    if (constructor.getParameterTypes().length == 1) {
                        Class annClass = constructor.getParameterTypes()[0];
                        if (annClass.isAssignableFrom(annotation.getClass())) {
                            try {
                                instance = (T) constructor.newInstance(new Object[]{annotation});
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        return instance;
    }


    public static List<Annotation> getServiceAnnotation(Class clazz) {
        List<Annotation> serviceAnnotations = new ArrayList<>();
        Annotation[] allAnnotation = clazz.getAnnotations();
        for (Annotation annotation : allAnnotation) {
            BeanClass customClass = annotation.annotationType().getAnnotation(BeanClass.class);
            if (customClass != null && CustomView.class.isAssignableFrom(customClass.clazz())) {
                serviceAnnotations.add(annotation);
            }
        }
        return serviceAnnotations;
    }



    public static Object fillDataObject(Annotation annotation, Object object) {
        Class enumType = annotation.annotationType();
        Map valueMap = new HashMap<>();
        for (int k = 0; k < enumType.getDeclaredMethods().length; k++) {
            Method method = enumType.getDeclaredMethods()[k];
            try {
                Object obj = method.invoke(annotation, null);
                if ((!obj.equals(method.getDefaultValue()) || (obj.getClass().isArray() || method.getAnnotation(BindMenuItem.class) != null))) {
                    Field[] declaredFields = object.getClass().getDeclaredFields();
                    Field field = TypeUtils.getField(object.getClass(), method.getName(), declaredFields);
                    if (field == null) {
                        field = TypeUtils.getField(object.getClass(), method.getName(), object.getClass().getFields());
                    }
                    if (field != null) {
                        if (obj.getClass().isArray() && !JSONArray.parseArray(JSONObject.toJSONString(obj)).isEmpty()) {
                            valueMap.put(field.getName(), JSONObject.parseObject(JSONObject.toJSONString(obj), field.getGenericType()));
                        } else {
                            valueMap.put(method.getName(), TypeUtils.cast(obj, field.getGenericType(), null));
                        }
                    }
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        OgnlUtil.setProperties(valueMap, object, false);
        return object;
    }


    public static Annotation getComboxAnnotation(Field field, ComboInputType inputType) {
        Annotation[] allAnnotation = field.getAnnotations();
        for (Annotation annotation : allAnnotation) {
            CustomClass customClass = annotation.annotationType().getAnnotation(CustomClass.class);
            if (customClass != null && Component.class.isAssignableFrom(customClass.clazz())) {
                if (customClass.viewType().equals(CustomViewType.COMBOBOX)) {
                    for (ComboInputType type : customClass.inputType()) {
                        if (type.equals(inputType)) {
                            return annotation;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Annotation getComboxAnnotation(Method method, ComboInputType inputType) {
        Set<Annotation> allAnnotation = AnnotationUtil.getAllAnnotations(method, true);
        for (Annotation annotation : allAnnotation) {
            CustomClass customClass = annotation.annotationType().getAnnotation(CustomClass.class);
            if (customClass != null && Component.class.isAssignableFrom(customClass.clazz())) {
                if (customClass.viewType().equals(CustomViewType.COMBOBOX)) {
                    for (ComboInputType type : customClass.inputType()) {
                        if (type.equals(inputType)) {
                            return annotation;
                        }
                    }
                }
            }
        }
        return null;
    }




    public static List<CustomMenu> getAllAnnotationsByMethod(Class clazz) {
        List<CustomMenu> annotations = new ArrayList<CustomMenu>();
        Annotation[] allAnnotation = clazz.getAnnotations();
        for (Annotation annotation : allAnnotation) {
            Object object = AnnotationUtil.getValue(annotation, "customMenu");
            if (object != null && object.getClass().isArray()) {
                CustomMenu[] objs = (CustomMenu[]) object;
                if (objs.length > 0) {
                    annotations.addAll(Arrays.asList(objs));
                }
            }
        }
        return annotations;
    }


}
