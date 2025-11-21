package net.ooder.esd.dsm.view.context;


import net.ooder.annotation.IconEnumstype;
import net.ooder.esd.annotation.TreeItem;
import net.ooder.esd.dsm.DSMInst;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.web.util.MethodUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class DicViewRoot<T extends TabListItem> implements JavaRoot {

    public String packageName;

    public String className;

    public String cnName;

    public DSMInst dsmBean;

    public String space;

    public String moduleName;

    public Set<String> imports = new LinkedHashSet<>();

    private List<T> items = new ArrayList<>();

    private static final Class[] customClass = new Class[]{
            TreeItem.class,
            IconEnumstype.class
    };


    public DicViewRoot(DSMInst dsmBean, String moduleName, List<T> treeListItems, String fullClassName) {

        this.dsmBean = dsmBean;
        this.moduleName = moduleName;
        if (fullClassName.indexOf(".") == -1) {
            fullClassName = dsmBean.getRootPackage().getPackageName() + "." + moduleName + "." + fullClassName;
        }
        this.packageName = fullClassName.substring(0, fullClassName.lastIndexOf("."));
        this.className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        this.cnName = dsmBean.getDesc();
        this.space = dsmBean.getSpace();
        this.items = treeListItems;
        try {
            for (Class clazz : customClass) {
                imports.add(clazz.getPackage().getName() + ".*");
                if (clazz.isAnnotation()) {
                    imports = MethodUtil.getAllImports(clazz, imports);
                }
            }

            for (TabListItem listItem : treeListItems) {
                if (listItem.getBindClass() != null) {
                    for (Class clazz : listItem.getBindClass()) {
                        imports.add(clazz.getName());
                    }
                } else if (listItem.getEuClassName() != null) {
                    imports.add(listItem.getEuClassName());
                }
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String basePackage = dsmBean.getPackageName();// + "." + dsmBean.getSpace().toLowerCase();
        for (Package pack : Package.getPackages()) {
            for (String packName : innerPacks) {
                if (pack.getName().startsWith(basePackage + "." + packName)) {
                    this.imports.add(pack.getName() + ".*");
                }
            }
        }
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }

    public Class[] getCustomClass() {
        return customClass;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public DSMInst getDsmBean() {
        return dsmBean;
    }

    public void setDsmBean(DSMInst dsmBean) {
        this.dsmBean = dsmBean;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCnName() {
        return cnName;
    }

    @Override
    public String getBasepath() {
        return packageName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }


}
