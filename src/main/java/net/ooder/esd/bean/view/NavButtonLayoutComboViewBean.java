package net.ooder.esd.bean.view;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.util.CaselessStringKeyHashMap;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.NavButtonLayoutAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.LayoutType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ButtonLayoutComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.ButtonLayoutProperties;
import net.ooder.esd.tool.properties.item.ButtonLayoutItem;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.*;

@AnnotationType(clazz = NavButtonLayoutAnnotation.class)
public class NavButtonLayoutComboViewBean extends NavComboBaseViewBean<ButtonLayoutItem> {

    ModuleViewType moduleViewType = ModuleViewType.NAVBUTTONLAYOUTCONFIG;

    CustomButtonLayoutViewBean buttonLayoutViewBean;


    @JSONField(serialize = false)
    List<CustomModuleBean> moduleBeans = new ArrayList<>();


    public NavButtonLayoutComboViewBean() {

    }

    @Override
    public CustomViewBean getCurrViewBean() {
        return buttonLayoutViewBean;
    }

    public NavButtonLayoutComboViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        buttonLayoutViewBean = new CustomButtonLayoutViewBean(methodAPIBean);
        tabsViewBean.setCloseBtn(true);
        layoutViewBean.setLayoutType(LayoutType.vertical);

    }

    public NavButtonLayoutComboViewBean(ModuleComponent moduleComponent) {
        super();
        AnnotationUtil.fillDefaultValue(NavButtonLayoutAnnotation.class, this);
        this.updateModule(moduleComponent);
        ButtonLayoutComponent component = (ButtonLayoutComponent) moduleComponent.getCurrComponent();
        ButtonLayoutProperties layoutProperties = component.getProperties();
        this.init(layoutProperties, moduleComponent.getProjectName());
    }

    public void init(ButtonLayoutProperties layoutProperties, String projectName) {
        this.name = layoutProperties.getName();
        itemConfigMap = new CaselessStringKeyHashMap<>();
        itemNames = new LinkedHashSet<String>();
        for (ButtonLayoutItem layoutItem : layoutProperties.getItems()) {
            String euClassName = layoutItem.getEuClassName();
            if (euClassName != null) {
                try {

                    EUModule euModule = ESDFacrory.getAdminESDClient().getModule(euClassName, projectName);
                    CustomViewBean customViewBean = DSMFactory.getInstance().getViewManager().getDefaultViewBean(euModule.getComponent(), domainId);
                    CustomModuleBean customModuleBean = customViewBean.getModuleBean();
                    try {
                        Class clazz = ClassUtility.loadClass(euClassName);
                    } catch (ClassNotFoundException e) {
                        customModuleBean = new CustomModuleBean(euModule.getComponent());
                        AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(customViewBean, euClassName, projectName);
                        List<JavaGenSource> serviceList = aggRootBuild.getAggServiceRootBean();
                        if (serviceList == null || serviceList.isEmpty()) {
                            serviceList = aggRootBuild.build();
                        }
                        for (JavaGenSource genSource : serviceList) {
                            JavaSrcBean javaSrcBean = genSource.getSrcBean();
                            if (javaSrcBean.getTarget() != null && javaSrcBean.getTarget().equals(layoutItem.getId())) {
                                try {
                                    bindService = ClassUtility.loadClass(javaSrcBean.getClassName());
                                    customViewBean.reBindService(bindService);
                                    customModuleBean.reBindMethod(customViewBean.getMethodConfig());
                                } catch (ClassNotFoundException ee) {
                                    ee.printStackTrace();
                                }
                            }
                        }


                    }
                    moduleBeans.add(customModuleBean);
                    FieldModuleConfig config = new FieldModuleConfig(customModuleBean);
                    itemConfigMap.put(config.getId(), config);
                    itemNames.add(config.getId());
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(layoutProperties), Map.class), this, false, false);
        tabItems = (List<ButtonLayoutItem>) layoutProperties.getItems();

    }

    @Override
    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = super.getOtherClass();
        if (toolBar != null && toolBar.getMenuClasses() != null) {
            classSet.addAll(Arrays.asList(toolBar.getMenuClasses()));
        }
        if (menuBar != null && menuBar.getMenuClasses() != null) {
            classSet.addAll(Arrays.asList(menuBar.getMenuClasses()));
        }
        if (buttonLayoutViewBean != null) {
            classSet.addAll(buttonLayoutViewBean.getOtherClass());
        }


        return ClassUtility.checkBase(classSet);
    }

    public CustomButtonLayoutViewBean getButtonLayoutViewBean() {
        return buttonLayoutViewBean;
    }

    public void setButtonLayoutViewBean(CustomButtonLayoutViewBean buttonLayoutViewBean) {
        this.buttonLayoutViewBean = buttonLayoutViewBean;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BUTTONLAYOUT;
    }
}
