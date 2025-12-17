package net.ooder.esd.bean.view;

import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.NotNull;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.BlockFormAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.StretchType;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.util.OODUtil;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@AnnotationType(clazz = BlockFormAnnotation.class)
public class CustomBlockFormViewBean extends BaseFormViewBean {

    ModuleViewType moduleViewType = ModuleViewType.BLOCKCONFIG;

    Integer defaultRowHeight;

    Integer defaultColWidth;

    @NotNull
    Integer defaultLabelWidth;

    Integer col;

    @NotNull
    Integer lineSpacing;

    @NotNull
    StretchType stretchH;

    @NotNull
    StretchType stretchHeight;


    public CustomBlockFormViewBean() {
    }


    public CustomBlockFormViewBean(ModuleComponent parentModuleComponent) {
        updateModule(parentModuleComponent);
    }

    public CustomBlockFormViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        Class clazz = JSONGenUtil.getInnerReturnType(methodAPIBean.getMethod());
        BlockFormAnnotation paperAnnotation = AnnotationUtil.getClassAnnotation(clazz, BlockFormAnnotation.class);
        if (paperAnnotation != null) {
            AnnotationUtil.fillBean(paperAnnotation, this);
        }
    }


    @Override
    public List<Callable<List<JavaGenSource>>> updateModule(ModuleComponent moduleComponent) {
        AnnotationUtil.fillDefaultValue(BlockFormAnnotation.class, this);
        List<Callable<List<JavaGenSource>>> tasks = new ArrayList<>();
        BlockComponent currComponent = (BlockComponent) component;

        if (currComponent == null) {
            if (moduleComponent.getCurrComponent() instanceof BlockComponent) {
                currComponent = (BlockComponent) moduleComponent.getCurrComponent();
            } else {
                currComponent = (BlockComponent) moduleComponent.getMainBoxComponent();
            }
        }

        super.updateBaseModule(moduleComponent);
        initMenuBar();
        this.name = OODUtil.formatJavaName(currComponent.getAlias(), false);
        if (moduleBean != null) {
            moduleBean.updateComponent(currComponent);
        }
        if (containerBean == null) {
            containerBean = new ContainerBean(currComponent);
        } else {
            containerBean.update(currComponent);
        }

        List<Component> components = currComponent.getChildren();//this.cloneComponentList(currComponent.getChildren());
        for (Component component : components) {
            FieldFormConfig fieldFormConfig = findFieldByCom(component);
            this.fieldConfigMap.put(fieldFormConfig.getFieldname(), fieldFormConfig);
        }

        tasks = genChildComponent(moduleComponent, components);

        try {
            DSMFactory.getInstance().saveCustomViewEntity(this);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return tasks;
    }


    public Integer getDefaultRowHeight() {
        return defaultRowHeight;
    }

    public void setDefaultRowHeight(Integer defaultRowHeight) {
        this.defaultRowHeight = defaultRowHeight;
    }

    public Integer getDefaultColWidth() {
        return defaultColWidth;
    }

    public void setDefaultColWidth(Integer defaultColWidth) {
        this.defaultColWidth = defaultColWidth;
    }

    public Integer getDefaultLabelWidth() {
        return defaultLabelWidth;
    }

    public void setDefaultLabelWidth(Integer defaultLabelWidth) {
        this.defaultLabelWidth = defaultLabelWidth;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(Integer lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public StretchType getStretchH() {
        return stretchH;
    }

    public void setStretchH(StretchType stretchH) {
        this.stretchH = stretchH;
    }

    public StretchType getStretchHeight() {
        return stretchHeight;
    }

    public void setStretchHeight(StretchType stretchHeight) {
        this.stretchHeight = stretchHeight;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BLOCK;
    }


    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }


    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }


}
