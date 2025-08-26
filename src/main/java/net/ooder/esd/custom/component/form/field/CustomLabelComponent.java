package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.RequestPathTypeEnum;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.field.LabelFieldBean;
import net.ooder.esd.bean.field.LabelBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.LabelComponent;
import net.ooder.esd.tool.properties.UrlPathData;
import net.ooder.esd.tool.properties.form.LabelProperties;

import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomLabelComponent extends LabelComponent {
    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomLabelComponent.class);

    public CustomLabelComponent(EUModule euModule, FieldFormConfig<LabelFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getId());
        LabelFieldBean labelFieldBean = field.getWidgetConfig();
        FieldBean fieldBean = field.getFieldBean();
        String caption = field.getAggConfig().getCaption();
        if (value != null && labelFieldBean!=null) {
            if (value instanceof LabelFieldBean) {
                String json = JSON.toJSONString(value);
                String obj = (String) TemplateRuntime.eval(json, value);
                OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), labelFieldBean, false, false);
            } else if (value instanceof String && !value.equals("")) {
                caption = (String) value;
            }
        }

        LabelProperties labelProperties = new LabelProperties(labelFieldBean,field.getContainerBean());
        labelProperties.setId(field.getId());
        labelProperties.setName(field.getFieldname());
        labelProperties.setDesc(caption);
        labelProperties.setCaption(caption);
        CustomFieldBean customFieldBean = field.getCustomBean();
        if (fieldBean != null) {
            if (fieldBean.getExpression() != null && !fieldBean.getExpression().equals("")) {
                labelProperties.setExpression(fieldBean.getExpression());
            }
        }

        if (customFieldBean != null) {
            if (customFieldBean.getCaption() != null && !customFieldBean.getCaption().equals("")) {
                labelProperties.setDesc(customFieldBean.getCaption());
            }
            if (customFieldBean.getReadonly() != null && customFieldBean.getReadonly()) {
                labelProperties.setReadonly(true);
            }
            if (customFieldBean.getDisabled() != null && customFieldBean.getDisabled()) {
                labelProperties.setDisabled(true);
            }
            if (customFieldBean.getImageClass() != null && !customFieldBean.getImageClass().equals("")) {
                labelProperties.setImageClass(customFieldBean.getImageClass());
            }
        }

        LabelBean labelBean = field.getLabelBean();
        if (labelBean != null && labelBean.getLabelCaption() != null) {
            labelProperties.setCaption(labelBean.getLabelCaption());

        }

        if (labelProperties.getExpression() != null && !labelProperties.getExpression().equals("")) {
            try {
                caption = EsbUtil.parExpression(labelProperties.getExpression(), JDSActionContext.getActionContext().getContext(), labelProperties, String.class);
                if (caption != null && !caption.equals("")) {
                    labelProperties.setCaption(caption);
                }
            } catch (Throwable e) {
                logger.error(e);
            }
        }
        this.setProperties(labelProperties);
        this.setTarget(target);

    }

    private void lazyLoad(EUModule euModule, FieldFormConfig<LabelFieldBean, ?> field, CustomLabelComponent labelComponent, Class bindClass) {
        try {
            ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClass.getName());
            MethodConfig loadMethod = config.getMethodByItem(CustomMenuItem.RELOAD);
            if (loadMethod != null) {
                APICallerComponent apiCallerComponent = new APICallerComponent(loadMethod);
                UrlPathData ctxData = new UrlPathData(euModule.getComponent().getTopComponentBox().getAlias(), RequestPathTypeEnum.FORM, "");
                apiCallerComponent.getProperties().addRequestData(ctxData);
                UrlPathData pageBarPathData = new UrlPathData(labelComponent.getAlias(), ResponsePathTypeEnum.COMPONENT, "data");
                apiCallerComponent.getProperties().addResponseData(pageBarPathData);
                euModule.getComponent().addChildren(apiCallerComponent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
