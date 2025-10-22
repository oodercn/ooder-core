package net.ooder.esd.util;

import com.alibaba.fastjson.JSONObject;
import net.ooder.annotation.FormulaParams;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.tool.properties.item.UIItem;
import net.ooder.web.util.OgnlUtil;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ESDEnumsUtil {


    static Map<Enum, TreeListItem> listItemMap = new HashMap<>();

    public static List<TreeListItem> getItems(Class<? extends Enum> enumClass, String filter) {
        List<TreeListItem> items = new ArrayList<>();
        Enum[] enums = getDefaultEnums(enumClass, filter);
        for (Enum enumstype : enums) {
            TreeListItem item = new TreeListItem(enumstype);
            items.add(item);
        }
        return items;
    }


    public static List<TreeListItem> getItems(String[] enums) {
        List<TreeListItem> items = new ArrayList<>();
        for (String enumstype : enums) {
            TreeListItem item = new TreeListItem(enumstype, enumstype);
            if (enumstype.indexOf(":") > -1) {
                String[] arrs = StringUtility.split(enumstype, ":");
                item = new TreeListItem(arrs[0], arrs[1]);
            }
            items.add(item);
        }
        return items;
    }

    public static <T extends UIItem> List<T> getEnumItems(Class<? extends Enum> enumClass, Class<T> clazz) {
        List<T> items = new ArrayList<>();
        int index = 0;
        Enum[] enums = getDefaultEnums(enumClass, null);
        for (Enum enumItem : enums) {
            try {
                if (enumItem != null) {
                    OgnlContext context = OgnlUtil.getOgnlContext();
                    T item = (T) OgnlRuntime.callConstructor(context, clazz.getName(), new Object[]{enumItem});
                    if (item != null) {
                        item.setIndex(index);
                        item.setId(enumItem.name());
                        items.add(item);
                        index++;
                    }
                }

            } catch (OgnlException e) {
                e.printStackTrace();
            }
        }
        return items;
    }


    public static List<TreeListItem> parItemExpression(String itemsExpression) {
        List<TreeListItem> items = new ArrayList<>();
        try {
            Enum[] enums = (Enum[]) JDSActionContext.getActionContext().Par(itemsExpression);
            items = getItems(enums);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return items;
    }


    public static List<TreeListItem> getTreeItems(Class<? extends Enum> enumClass, String filter) {
        Enum[] enums = getDefaultEnums(enumClass, filter);
        return getItems(enums);
    }

    private static List<TreeListItem> getItems(Enum[] enums) {
        List<TreeListItem> items = new ArrayList<>();
        if (enums.length > 0) {
            Class<Enum> enumClass = (Class<Enum>) enums[0].getClass();
            Class<Enum> parentEnum = getParentEnum(enumClass);
            if (parentEnum != null) {
                Enum[] parents = getDefaultEnums(parentEnum, null);
                for (Enum penum : parents) {
                    TreeListItem item = filter(enums, penum);
                    if (item != null) {
                        items.add(item);
                    }
                }
                items = getItems(parents);

            } else {
                for (Enum enumType : enums) {
                    TreeListItem item = createItem(enumType);
                    if (item != null) {
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    private static TreeListItem createItem(Enum enumstype) {
        TreeListItem item = listItemMap.get(enumstype);
        if (item == null) {
            item = new TreeListItem(enumstype);
            listItemMap.put(enumstype, item);
        }
        return item;
    }

    private static TreeListItem filter(Enum[] enums, Enum parent) {
        TreeListItem parentItem = createItem(parent);
        List<Enum> allEnums = new ArrayList<>();
        Enum currEnum = enums[0];
        Method typeMethod = null;
        Method[] methods = currEnum.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getParameterTypes().length == 0 && method.getReturnType().equals(parent.getClass())) {
                typeMethod = method;
            }
        }
        if (typeMethod != null) {
            for (Enum childEnum : enums) {
                try {
                    Enum parentEnum = (Enum) currEnum.getClass().getMethod(typeMethod.getName(), null).invoke(childEnum, null);
                    if (parentEnum != null && parentEnum.equals(parent)) {
                        allEnums.add(childEnum);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        List<UIItem> items = new ArrayList<>();
        for (Enum subenum : allEnums) {
            UIItem item = createItem(subenum);
            if (item != null) {
                items.add(item);
            }
        }
        parentItem.setSub(items);

        return parentItem;
    }

    public static Class<Enum> getParentEnum(Class<Enum> clazz) {
        Class<Enum> parent = null;
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getParameterTypes().length == 0 && method.getReturnType().isEnum()) {
                parent = (Class<Enum>) method.getReturnType();
            }
        }
        return parent;
    }

    public static <T extends Enum> T getEnumsByName(Class<T> enumClass, String name) {
        T enumItem = null;
        try {
            T[] enums = (T[]) enumClass.getMethod("values", null).invoke(enumClass.getEnumConstants(), null);
            for (T enumstype : enums) {
                if (enumstype.name().toUpperCase().equals(name.toUpperCase())) {
                    enumItem = enumstype;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return enumItem;

    }

    public static <T extends Enum> T[] getDefaultEnums(Class<T> clazz, String filter) {
        T[] enums = (T[]) new Enum[0];
        List<Enum> items = new ArrayList<>();
        try {
            if (clazz != null && clazz.isEnum()) {
                enums = (T[]) clazz.getMethod("values", null).invoke(clazz.getEnumConstants(), null);
                for (Enum enumstype : enums) {
                    boolean checked = true;
                    if (filter != null && !filter.equals("")) {
                        try {
                            checked = JDSActionContext.getActionContext().Par(filter, Boolean.class, enumstype);
                            //  checked = (boolean) Ognl.getValue(filter, enumstype, Boolean.class);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            checked = false;
                        }
                    }
                    if (checked) {
                        items.add(enumstype);
                    }
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return (T[]) items.toArray(new Enum[]{});

    }

    //
    public static void main(String[] args) {
        List<TreeListItem> items = getItems(FormulaParams.class, "attributetype!=null &&attributetype.toString()=='RIGHT'");
        System.out.println(JSONObject.toJSONString(items));

    }

}
