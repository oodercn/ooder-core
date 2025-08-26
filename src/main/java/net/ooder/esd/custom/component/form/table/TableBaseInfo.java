package net.ooder.esd.custom.component.form.table;

import net.ooder.common.database.dao.DBMap;
import net.ooder.common.util.CnToSpell;
import net.ooder.esd.annotation.ui.VisibilityType;
import net.ooder.esd.custom.component.form.table.emums.CustomFieldEnums;
import net.ooder.esd.dsm.repository.database.proxy.DSMColProxy;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.HiddenInputComponent;
import net.ooder.esd.tool.properties.BlockProperties;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class TableBaseInfo extends BlockComponent {

    public TableBaseInfo(DSMTableProxy proxy, DBMap dbMap) {
        super(CnToSpell.getFullSpell(proxy.getTableName() + "BaseInfo"),  new BlockProperties());
        BlockProperties properties =  this.getProperties();
        properties.setVisibility(VisibilityType.hidden);
        Set<DSMColProxy> hiddenColNames = proxy.getHiddenFields();
        if (dbMap != null) {
            for (DSMColProxy colProxy : hiddenColNames) {
                this.addChildren(new HiddenInputComponent(colProxy.getFieldName(), colProxy.getFieldName(), dbMap.get(colProxy.getFieldName())));
            }
            for (CustomFieldEnums customFieldEnum : CustomFieldEnums.values()) {
                this.addChildren(new HiddenInputComponent(customFieldEnum.getType(), customFieldEnum.getType(), getValue(dbMap, customFieldEnum.getType())));
            }
            this.addChildren(new HiddenInputComponent(dbMap.getPkName(), dbMap.getPkName(), dbMap.getPkValue()));
        } else {
            for (DSMColProxy colProxy : hiddenColNames) {
                this.addChildren(new HiddenInputComponent(colProxy.getFieldName(), colProxy.getFieldName(), null));
            }
            for (CustomFieldEnums customFieldEnum : CustomFieldEnums.values()) {
                this.addChildren(new HiddenInputComponent(customFieldEnum.getType(), customFieldEnum.getType(), null));
            }
            this.addChildren(new HiddenInputComponent(proxy.getPkFieldName(), proxy.getPkFieldName(), null));

        }
    }


    Object getValue(DBMap dbMap, String fieldName) {
        String getMthodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Object obj = null;
        try {
            obj = dbMap.getClass().getMethod(getMthodName, null).invoke(dbMap, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return obj;
    }

    ;
}
