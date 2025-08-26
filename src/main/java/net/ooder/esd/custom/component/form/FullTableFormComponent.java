package net.ooder.esd.custom.component.form;

import net.ooder.common.database.dao.DAOException;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.custom.component.form.table.TablePanel;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.DefaultMenubarComponent;
import net.ooder.esd.tool.component.MenuBarComponent;
import java.sql.SQLException;


public class FullTableFormComponent extends ModuleComponent {

    public FullTableFormComponent(EUModule module, DSMTableProxy DSMTableProxy) throws SQLException, DAOException {

        BlockComponent blockPanelComponent = new BlockComponent(Dock.fill, DSMTableProxy.getFieldName() + "Block");
        MenuBarComponent barComonent = new DefaultMenubarComponent(DSMTableProxy.getFieldName() + "ToolBar");
        TablePanel formLayoutPanel = new TablePanel(module, DSMTableProxy, null, 2);
        blockPanelComponent.addChildren(barComonent, formLayoutPanel);

        this.getProperties().setDesc(DSMTableProxy.getCnname());
        this.addChildren(blockPanelComponent);
    }

    public FullTableFormComponent(EUModule module, DSMTableProxy DSMTableProxy, String uuid) throws SQLException, DAOException {
        super(module);
        MenuBarComponent barComonent = new DefaultMenubarComponent(DSMTableProxy.getFieldName() + "ToolBar");
        TablePanel formLayoutPanel = new TablePanel(module, DSMTableProxy, null, 2);
        this.getProperties().setDesc(DSMTableProxy.getCnname());
        this.addChildren(barComonent, formLayoutPanel);


    }


}
