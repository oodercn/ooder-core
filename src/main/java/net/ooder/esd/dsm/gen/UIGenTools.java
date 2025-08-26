package net.ooder.esd.dsm.gen;

import net.ooder.common.JDSException;
import net.ooder.common.database.dao.DAOException;
import net.ooder.common.database.metadata.MetadataFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.custom.component.form.FullTableFormComponent;
import net.ooder.esd.custom.component.grid.FullTableGridComponent;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.engine.EUModule;
import javassist.NotFoundException;

import java.sql.SQLException;
import java.util.HashMap;

public class UIGenTools {

    private MetadataFactory metadataFactory;
    private ProjectVersion version;

    public UIGenTools(ProjectVersion version, String configKey) throws JDSException {
        this.version = version;
        this.metadataFactory = this.getClient().getDbFactory(configKey);
        //ProviderConfig config = project.getConfig().getDbConfig().getDbConfig();
        // this.metadataFactory = new MetadataFactory(config);
    }


    public EUModule genTableGridModule(String tableName, String packageName, String projectVersionName) {

        EUModule module = null;
        try {

            DSMTableProxy proxy = DSMFactory.getInstance().getRepositoryManager().getTableProxyByName(tableName, projectVersionName);
            if (packageName == null) {
                packageName = "App." + proxy.getClassName();
            }

            String className = packageName + "." + proxy.getClassName() + "Grid";
            module = version.getModule(className);
            if (module == null) {
                module = version.createModule(className);
            }
            module.setComponent(new FullTableGridComponent(module, proxy));


            try {
                Class clazz = ClassUtility.loadClass("net.ooder.fdt.server.service.DAOFromService");
                module.addBindService(clazz, null, new HashMap<>());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }

            getClient().saveModule(module,false);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return module;
    }

    public FullTableFormComponent genTableFormComponents(EUModule module, String tableName, String projectVersionName) throws SQLException, DAOException, JDSException {
        DSMTableProxy proxy = DSMFactory.getInstance().getRepositoryManager().getTableProxyByName(tableName, projectVersionName);
        if (module == null) {
            module = version.createModule("db." + proxy.getClassName());
        }
        FullTableFormComponent formLayoutPanel = new FullTableFormComponent(module, proxy);
        formLayoutPanel.getProperties().setDesc("表单");
        return formLayoutPanel;
    }

//    public FullTableFormComponent genGridComponents(String tableName, String packageName) throws SQLException {
//        TableInfo tableInfo = metadataFactory.getTableInfo(tableName);
//
//        FullTableGridComponent tableFormComponent=new FullTableGridComponent(tableInfo,tableInfo);
//
//        DSMTableProxy proxy = new DSMTableProxy(tableInfo);
//        BlockPanelComponent blockPanelComponent = new BlockPanelComponent(Dock.fill, proxy.getFieldName() + "Block");
//        blockPanelComponent.getProperties().setDesc("定位容器");
//
//        //新建工具栏
//        MenuBarComponent<MenuBarProperties, MenuEventEnum> barComonent = new DefaultMenubarComponent(proxy.getFieldName() + "ToolBar");
//        barComonent.getProperties().setDesc("工具栏");
//        //添加列表
//        TableGridComponent gridComponent = new TableGridComponent(proxy);
//        gridComponent.getProperties().setDesc("查询列表");
//        //添加pagebar
//        PageBarComponent pageBarComponent = new PageBarComponent(proxy.getFieldName() + "PageBar");
//        pageBarComponent.getProperties().setDesc("分页控制");
//        DivComponent<DivProperties, DivEventEnum> divComponent = new DivComponent(pageBarComponent, Dock.bottom);
//        divComponent.getProperties().setDesc("定位容器");
//        //设置pagebar 高度为2.5
//        divComponent.getProperties().setHeight("2.5em");
//        blockPanelComponent.addChildren(barComonent, gridComponent, divComponent);
//
//
//        return blockPanelComponent;
//    }


    public EUModule genTableFormModule(String tableName, String packageName, String dsmId) {
        EUModule module = null;
        try {

            DSMTableProxy proxy = DSMFactory.getInstance().getRepositoryManager().getTableProxyByName(tableName, dsmId);
            if (packageName == null) {
                packageName = "App." + proxy.getClassName();
            }

            String className = packageName + "." + proxy.getClassName() + "Form";
            module = version.getModule(className);
            if (module == null) {
                module = version.createModule(className);
            }


            try {
                module.setComponent(genTableFormComponents(module, tableName, dsmId));
                Class clazz = ClassUtility.loadClass("net.ooder.fdt.server.service.DAOFromServic");
                module.addBindService(clazz, null, new HashMap<>());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (DAOException e) {
                e.printStackTrace();
            }

            getClient().saveModule(module,false);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return module;
    }

    ESDClient getClient() throws JDSException {

        ESDClient client = ESDFacrory.getAdminESDClient();

        return client;
    }

}
