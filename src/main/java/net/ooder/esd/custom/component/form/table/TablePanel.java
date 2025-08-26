package net.ooder.esd.custom.component.form.table;

import net.ooder.common.database.dao.DBMap;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.BlockComponent;

public class TablePanel extends BlockComponent {


    public TablePanel(EUModule module, DSMTableProxy proxy, DBMap dbMap, int colnum) {
        TableFormComponent formLayoutPanel = new TableFormComponent(module, proxy, dbMap, colnum);
        this.setAlias(proxy.getFieldName());
        this.getProperties().setDesc(proxy.getCnname());
        this.getProperties().setName(proxy.getFieldName());
        this.addChildren(formLayoutPanel);
        this.addChildren(new TableBaseInfo(proxy, dbMap));

    }


}
