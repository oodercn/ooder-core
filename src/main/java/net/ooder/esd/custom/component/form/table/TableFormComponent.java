package net.ooder.esd.custom.component.form.table;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.database.dao.DBMap;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.form.FormLayoutModule;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.FormLayoutComponent;
import net.ooder.esd.tool.properties.form.FormLayoutProperties;

import java.util.ArrayList;
import java.util.List;

public class TableFormComponent extends FormLayoutComponent {


    @JSONField(serialize = false)
    private final FormLayoutModule formLayout;

    public FormLayoutModule getFormLayout() {
        return formLayout;
    }

    private EUModule euModule;


    public TableFormComponent(EUModule euModule, DSMTableProxy proxy, DBMap dbMap, int col) {
        super(Dock.fill);
        this.euModule = euModule;
        List<FieldFormConfig> fieldList = new ArrayList<>();
        ESDClass esdClass = new ESDClass(proxy);
        MethodConfig methodConfig = euModule.getComponent().getMethodAPIBean();
        for (ESDField esdField : esdClass.getFieldList()) {
            fieldList.add(new FieldFormConfig(esdField, methodConfig.getSourceClassName(),methodConfig.getMethodName()));
        }


        this.formLayout = new FormLayoutModule(euModule,  this, fieldList, dbMap, null);
//        List<Component> inputComponents = formLayout.getInputComponents();
////        for (Component component : inputComponents) {
////            this.addChildren(component);
////        }
        FormLayoutProperties formLayoutProperties = this.getProperties();
        formLayoutProperties.setDesc(proxy.getCnname() + "表单");
    }

}
