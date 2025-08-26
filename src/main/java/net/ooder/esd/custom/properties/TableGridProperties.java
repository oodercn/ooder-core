package net.ooder.esd.custom.properties;

import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.custom.TableFieldInfo;
import net.ooder.esd.dsm.repository.database.proxy.DSMColProxy;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.tool.properties.GridProperties;

import java.util.List;

public class TableGridProperties extends GridProperties {

    public TableGridProperties(DSMTableProxy proxy) {
        this.uidColumn = proxy.getPkFieldName();
        this.name = proxy.getClassName() + "Grid";
        List<DSMColProxy> colInfoList = proxy.getFieldList();
        for (DSMColProxy colInfo : colInfoList) {
            TableFieldInfo info = colInfo.getTableFieldInfo();
            TableHeader headCol = new TableHeader(info);
            if (colInfo.getDbcol().getName().equals(proxy.getPkName())) {
                headCol.setHidden(true);
                headCol.setWidth("8.0em");
                headCol.setType(ComboInputType.input);
            }
            this.header.add(headCol);
        }
        //最后一行 平铺
        this.getHeader().get(colInfoList.size() - 1).setFlexSize(true);

    }
}
