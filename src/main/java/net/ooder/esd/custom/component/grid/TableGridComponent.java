package net.ooder.esd.custom.component.grid;

import net.ooder.esd.custom.properties.TableGridProperties;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.tool.component.TreeGridComponent;

public class TableGridComponent extends TreeGridComponent {
    public TableGridComponent(DSMTableProxy proxy) {
        super(proxy.getFieldName() + "Grid");
        this.setProperties( new TableGridProperties(proxy));
    }
}
