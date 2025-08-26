package net.ooder.esd.bean;

import net.ooder.common.JDSException;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RequestMethodBean;
import net.ooder.web.util.AnnotationUtil;
import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LayoutItemClass {

    private Class ctClass;

    public String name;


    public String id;

    public String desc;

    public APIConfig apiConfig;

    public List<RequestMethodBean> methodList = new ArrayList<>();

    public List<EUModule> modules = new ArrayList<>();


    public LayoutItemClass(Class ctClass, String projectName,Map<String, Object> valueMap) {
        this.ctClass = ctClass;
        MethodChinaName chinaName = AnnotationUtil.getClassAnnotation(ctClass,MethodChinaName.class);
        if (chinaName != null) {
            desc = chinaName.cname();
        }

        this.name = ctClass.getSimpleName();
        try {
            apiConfig = APIConfigFactory.getInstance().getAPIConfig(ctClass.getName());
            this.id = apiConfig.getClassName();
            List<RequestMethodBean> methods = apiConfig.getMethods();
            for (RequestMethodBean methodBean : methods) {
                ESDClient client = ESDFacrory.getAdminESDClient();
                EUModule module = client.getCustomModule(methodBean.getUrl(), projectName,valueMap);

                if (module != null) {
                    id = module.getPackageName();
                    methodList.add(methodBean);
                    modules.add(module);
                }

            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    public Class getCtClass() {
        return ctClass;
    }

    public void setCtClass(Class ctClass) {
        this.ctClass = ctClass;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<RequestMethodBean> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<RequestMethodBean> methodList) {
        this.methodList = methodList;
    }

    public APIConfig getApiConfig() {
        return apiConfig;
    }


    public List<EUModule> getModules() {
        return modules;
    }

    public void setModules(List<EUModule> modules) {
        this.modules = modules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setApiConfig(APIConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

}
