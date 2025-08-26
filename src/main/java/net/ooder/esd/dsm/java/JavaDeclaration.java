package net.ooder.esd.dsm.java;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.IOUtility;
import net.ooder.annotation.MethodChinaName;
import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.web.util.MethodUtil;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.Table;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class JavaDeclaration {

    private final Class[] baseClass = new Class[]{
            ModuleAnnotation.class,
            Table.class,
            LocalDate.class,
            Date.class,
            RequestMapping.class,
            Controller.class,
            APIEventAnnotation.class,
            EsbBeanAnnotation.class,
            MethodChinaName.class
    };

    @JSONField(serialize = false)
    CompilationUnit compilationUnit;
    @JSONField(serialize = false)
    NodeList<ImportDeclaration> imports = new NodeList<>();
    @JSONField(serialize = false)
    List<ConstructorDeclaration> constructors;
    @JSONField(serialize = false)
    List<MethodDeclaration> methods;
    @JSONField(serialize = false)
    PackageDeclaration javaPackage;

    boolean init = false;

    Map<String, TypeDeclaration> classDeclarationMap = new HashMap<>();

    @JSONField(serialize = false)
    JavaSrcBean javaSrcBean;

    public JavaDeclaration(JavaSrcBean javaSrcBean) throws FileNotFoundException {
        this.javaSrcBean = javaSrcBean;
        if (!init) {
            this.reSet();
        }
    }


    public void reSet() throws FileNotFoundException {
        init = true;
        imports = new NodeList<>();
        compilationUnit = StaticJavaParser.parse(javaSrcBean.getFile());
        methods = compilationUnit.findAll(MethodDeclaration.class);
        constructors = compilationUnit.findAll(ConstructorDeclaration.class);
        if (!compilationUnit.getPackageDeclaration().isPresent()) {
            compilationUnit.setPackageDeclaration(javaSrcBean.getPackageName());
        }
        javaPackage = compilationUnit.getPackageDeclaration().get();
        for (TypeDeclaration type : compilationUnit.getTypes()) {
            classDeclarationMap.put(type.getNameAsString(), type);
        }

        for (ImportDeclaration importDeclaration : compilationUnit.getImports()) {
            imports.add(importDeclaration);
        }

        imports = clearImpls();
        compilationUnit.setImports(imports);
    }


    @JSONField(serialize = false)
    public TypeDeclaration getTypeDeclaration() {
        TypeDeclaration declaration = classDeclarationMap.get(javaSrcBean.getName());
        if (declaration == null) {
            Set<String> keySet = classDeclarationMap.keySet();
            for (String key : keySet) {
                declaration = classDeclarationMap.get(key);
                if (declaration.isTopLevelType() && !declaration.isNestedType()) {
                    return declaration;
                }
            }
        }
        return declaration;
    }


    public JavaSrcBean update() throws IOException {
        File javaFile = javaSrcBean.getFile();
        TypeDeclaration typeDeclaration = this.getTypeDeclaration();
        compilationUnit.setType(0, typeDeclaration);
        if (!typeDeclaration.getName().asString().equals(javaSrcBean.getName())) {
            javaFile = new File(javaSrcBean.getFile().getParentFile(), typeDeclaration.getName().asString() + ".java");
        }
        ByteArrayInputStream input = new ByteArrayInputStream(compilationUnit.toString().getBytes("utf-8"));
        FileOutputStream output = new FileOutputStream(javaFile);
        IOUtility.copy(input, output);
        IOUtility.shutdownStream(input);
        IOUtility.shutdownStream(output);
        if (!this.getTypeDeclaration().getName().asString().equals(javaSrcBean.getName())) {
            javaSrcBean.getFile().delete();
        }
        javaSrcBean.initFile(javaFile, null);
        return javaSrcBean;
    }


    protected Set<String> getCustomClassImpls(Class... classes) {
        Set<String> imports = new HashSet<>();
        if (classes != null) {
            for (Class clazz : classes) {
                if (clazz != null) {
                    try {
                        imports.add(clazz.getName());
                        if (clazz.isAnnotation()) {
                            imports = MethodUtil.getAllImports(clazz, imports);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return imports;
    }


    public NodeList<ImportDeclaration> clearImpls() {
        NodeList<ImportDeclaration> imports = new NodeList<>();
        NodeList<ImportDeclaration> oImports = getImports();
        for (ImportDeclaration importDeclaration : oImports) {
            String implName = importDeclaration.getNameAsString();
            //判断*
            if (importDeclaration.isAsterisk()) {
                if (ClassUtility.getPackage(implName) != null && !imports.contains(importDeclaration)) {
                    imports.add(importDeclaration);
                }
            } else {
                try {
                    if (ClassUtility.loadClass(implName) != null && !imports.contains(importDeclaration)) {
                        imports.add(importDeclaration);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }

        Set<String> customClassImpls = getCustomClassImpls(baseClass);
        for (String packageName : customClassImpls) {
            ImportDeclaration importDeclaration = StaticJavaParser.parseImport("import " + packageName + ";");
            if (!imports.contains(importDeclaration)) {
                imports.add(importDeclaration);
            }
        }
        return imports;
    }


    public Map<String, TypeDeclaration> getClassDeclarationMap() {
        return classDeclarationMap;
    }

    public void setClassDeclarationMap(Map<String, TypeDeclaration> classDeclarationMap) {
        this.classDeclarationMap = classDeclarationMap;
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public void setCompilationUnit(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

    public NodeList<ImportDeclaration> getImports() {
        return imports;
    }

    public void setImports(NodeList<ImportDeclaration> imports) {
        this.imports = imports;
    }

    public List<ConstructorDeclaration> getConstructors() {
        return constructors;
    }

    public void setConstructors(List<ConstructorDeclaration> constructors) {
        this.constructors = constructors;
    }

    public List<MethodDeclaration> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodDeclaration> methods) {
        this.methods = methods;
    }

    public PackageDeclaration getJavaPackage() {
        return javaPackage;
    }

    public void setJavaPackage(PackageDeclaration javaPackage) {
        this.javaPackage = javaPackage;
    }


    @JSONField(serialize = false)
    public String getContent() {
        return compilationUnit.toString();
    }


    @Override
    public String toString() {
        return compilationUnit.toString();
    }
}
