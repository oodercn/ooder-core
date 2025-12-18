package net.ooder.esd.util;

import net.ooder.common.util.StringUtility;
import net.ooder.esd.bean.field.CustomBlockFieldBean;
import net.ooder.esd.tool.component.ModuleComponent;

import java.util.HashMap;
import java.util.Map;

public class OODUtil {
    static String[] skipChars = new String[]{"get", "oodui", "ood_ui_"};

    static Map<String, String> aliasMap = new HashMap<>();

    public static String formatJavaName(String name, boolean firstCharUpperCase) {
        String neName = aliasMap.get(name + firstCharUpperCase);
        if (neName == null) {
            if (name.indexOf(".") > -1) {
                String[] nameArr = StringUtility.split(name, ".");
                neName = formatAliasPath(nameArr, firstCharUpperCase);
            } else {
                neName = StringUtility.formatJavaName(name, firstCharUpperCase);
                if (neName.endsWith(CustomBlockFieldBean.skipStr)) {
                    neName = neName.substring(0, neName.length() - CustomBlockFieldBean.skipStr.length());
                }
                for (String skipChar : skipChars) {
                    if (neName.toLowerCase().startsWith(skipChar)) {
                        neName = neName.substring(skipChar.length());
                    }
                }

            }
        }
        if (!neName.equals(ModuleComponent.DefaultTopBoxfix) && neName.endsWith(ModuleComponent.DefaultTopBoxfix)) {
            neName = neName.substring(0, neName.length() - ModuleComponent.DefaultTopBoxfix.length());
        }
        aliasMap.put(name + firstCharUpperCase, neName);
        return neName;

    }

    public static final String formatAliasPath(String[] names, boolean firstCharUpperCase) {
        String javaName = "";
        for (int k = 0; k < names.length; k++) {
            String name = names[k];
            name = formatJavaName(name, firstCharUpperCase);
            if (k > k - 1) {
                name = name.toLowerCase();
            }
            if (k > 0) {
                javaName = javaName + "." + name;
            } else {
                javaName = name;
            }
        }
        return javaName;
    }


    public static final String getGetMethodName(String name) {
        String methodName = name;
        if (methodName != null && !methodName.startsWith("get")) {
            methodName = "get" + formatJavaName(name, true);
        }
        return methodName;
    }

    public static final String getSetMethodName(String name) {
        String methodName = name;
        if (methodName != null && !methodName.startsWith("set")) {
            methodName = "set" + formatJavaName(name, true);
        }
        return methodName;
    }
}
