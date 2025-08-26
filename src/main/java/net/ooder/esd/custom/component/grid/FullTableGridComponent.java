package net.ooder.esd.custom.component.grid;

import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.*;

;

public class FullTableGridComponent extends ModuleComponent {


    public FullTableGridComponent(EUModule module, DSMTableProxy proxy) {
        super(module);
        BlockComponent blockPanelComponent = new BlockComponent(Dock.fill, proxy.getFieldName() + "Block");

        //新建工具栏
        MenuBarComponent barComonent = new DefaultMenubarComponent(proxy.getFieldName() + "ToolBar");
        //添加列表
        TableGridComponent gridComponent = new TableGridComponent(proxy);
        //添加pagebar
        PageBarComponent pageBarComponent = new PageBarComponent(proxy.getFieldName() + "PageBar");
        DivComponent divComponent = new DivComponent(pageBarComponent, Dock.bottom);
        //设置pagebar 高度为2.5
        divComponent.getProperties().setHeight("2.5em");
        blockPanelComponent.addChildren(barComonent, gridComponent, divComponent);
        this.addChildren(blockPanelComponent);
        this.getProperties().setDesc(proxy.getCnname() + "表格");


    }


}
