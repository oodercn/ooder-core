package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.action.CustomPageAction;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.event.TensorEventEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.FileUploadFieldBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.TensorComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.TensorProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomTensorComponent extends TensorComponent {


    public CustomTensorComponent(EUModule euModule, FieldFormConfig<FileUploadFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getId());

        FileUploadFieldBean inputFieldBean = field.getWidgetConfig();
        if (value != null) {
            if (value instanceof FileUploadFieldBean) {
                String json = JSON.toJSONString(value);
                String obj = (String) TemplateRuntime.eval(json, value);
                OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), inputFieldBean, false, false);
            }

        }


        this.addAction( new Action(CustomPageAction.RELOAD,TensorEventEnum.uploadcomplete));
        FileUploadFieldBean uploadFieldBean = field.getWidgetConfig();
        TensorProperties properties = new TensorProperties(uploadFieldBean);
        properties.setId(field.getId());
        properties.setName(field.getFieldname());
        properties.setDesc(field.getAggConfig().getCaption());

        if (uploadFieldBean.getUploadUrl() != null && !uploadFieldBean.getUploadUrl().equals("")) {
            properties.setUploadUrl(uploadFieldBean.getUploadUrl());
        } else if (uploadFieldBean.getBindClass() != null && !uploadFieldBean.getBindClass().equals(Void.class)&&!uploadFieldBean.getBindClass().equals(Enum.class)) {
            try {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(uploadFieldBean.getBindClass().getName());
                MethodConfig methodConfig = apiClassConfig.getFieldEvent(CustomFieldEvent.UPLOAD);
                properties.setUploadUrl(methodConfig.getUrl());
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        this.setProperties( properties);
        this.setTarget(target);

    }
}
