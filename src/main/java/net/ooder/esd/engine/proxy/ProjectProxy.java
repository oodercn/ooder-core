package net.ooder.esd.engine.proxy;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.database.metadata.TableInfo;
import net.ooder.config.BPDProjectConfig;
import net.ooder.esb.config.formula.FormulaInst;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.Project;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.engine.config.ProjectConfig;
import net.ooder.esd.engine.config.dsm.DSMProjectConfig;
import net.ooder.esd.engine.enums.ProjectDefAccess;
import net.ooder.esd.engine.enums.ProjectResourceType;
import net.ooder.esd.engine.enums.ProjectRoleType;
import net.ooder.esd.engine.inner.INProject;
import net.ooder.esd.engine.inner.INProjectVersion;
import net.ooder.esd.manager.plugins.api.node.APIPaths;
import net.ooder.esd.manager.plugins.font.node.FontConfig;
import net.ooder.esd.manager.plugins.img.node.ImgConfig;
import net.ooder.esd.manager.plugins.style.node.StyleConfig;
import net.ooder.org.Person;
import net.ooder.vfs.Folder;

import java.util.ArrayList;
import java.util.List;

public class ProjectProxy implements Project {

    @JSONField(serialize = false)
    private final INProject inProject;
    @JSONField(serialize = false)
    private final ESDClient client;


    public ProjectProxy(INProject inProject, ESDClient client) {
        this.inProject = inProject;
        this.client = client;

    }


    @Override
    public String getId() {
        return inProject.getId();
    }

    @Override
    public String getDesc() {
        return inProject.getDesc();
    }

    @Override
    public String getPath() {
        return inProject.getPath();
    }

    @Override
    public BPDProjectConfig getBpdProjectConfig() {
        return inProject.getBpdProjectConfig();
    }

    @Override
    public RepositoryInst getRepository() {
        return inProject.getRepository();
    }

    @Override
    public DSMProjectConfig getDsmProjectConfig() {
        return inProject.getDsmProjectConfig();
    }

    @Override
    public List<FormulaInst> getFormulas() {
        return inProject.getFormulas();
    }

    @Override
    public FormulaInst getFormula(String formulaId) {
        return inProject.getFormula(formulaId);
    }

    @Override
    public ProjectConfig getConfig() {
        return inProject.getConfig();
    }

    @Override
    public String getProjectName() {
        return inProject.getProjectName();
    }

    @Override
    public ProjectResourceType getResourceType() {
        return inProject.getResourceType();
    }

    @Override
    public ProjectDefAccess getProjectType() {
        return inProject.getDefAccess();
    }

    @Override
    public Person getOwner() {
        return inProject.getOwner();
    }

    @Override
    public ProjectVersion getActiveProjectVersion() throws JDSException {
        return client.getActivityProjectVersion(this.getId());
    }

    @Override
    @JSONField(serialize = false)
    public List<ProjectVersion> getAllEIProjectVersion() throws JDSException {
        List<INProjectVersion> inversions = inProject.getVersions();
        List<ProjectVersion> versions = new ArrayList<ProjectVersion>();
        for (INProjectVersion version : inversions) {
            versions.add(client.getProjectVersionById(version.getVersionId()));
        }
        return versions;
    }

    @Override
    public ProjectVersion createVersion() throws JDSException {
        return client.createProcessVersion(this.getProjectName());
    }

    @Override
    @JSONField(serialize = false)
    public Folder getRootfolder() {
        try {
            return client.getFolderByPath(this.getPath());
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @JSONField(serialize = false)
    public List<Person> getDevPersons(ProjectRoleType type) throws JDSException {
        return inProject.getDevPersons(type);
    }


    @Override
    public void updateConfig(ProjectConfig config) throws JDSException {
        if (config == null) {
            config = this.getConfig();
        }
        this.client.updateProjectConfig(this.getId(), config);
    }


    @Override
    public String getPublicPath() {
        String publicPath = inProject.getConfig().getPublicPath();
        return publicPath;
    }

    @Override
    public String getWorkSpace() {
        return inProject.getSpace().getPath();
    }

    @Override
    public List<String> getApiFilter() {
        return inProject.getConfig().getApiFilter();
    }

    @Override
    public List<APIPaths> getApiPaths() throws JDSException {
        return this.client.getAPIPathsByProject(this.getProjectName());

    }

    @Override
    public List<FontConfig> getFonts() throws JDSException {
        return this.client.getFontByProject(this.getProjectName());
    }

    @Override
    public List<EUModule> getComponents() throws JDSException {
        return this.client.getComponentByProject(this.getProjectName());
    }

    @Override
    public List<ImgConfig> getImgs() throws JDSException {
        return this.client.getImgByProject(this.getProjectName());
    }

    @Override
    public List<TableInfo> getTables() throws JDSException {
        return this.client.getTablesByProject(this.getProjectName(), null);
    }

    @Override
    public List<StyleConfig> getStyles() throws JDSException {
        return this.client.getStyleByProject(this.getProjectName());
    }

    @Override
    public String getPublicServerUrl() {
        String publicUrl = inProject.getConfig().getPublicServerUrl();
        if (publicUrl == null && this.client.getOwnSystem() != null) {
            publicUrl = this.client.getOwnSystem().getUrl();
        }
        return publicUrl;

    }


}
