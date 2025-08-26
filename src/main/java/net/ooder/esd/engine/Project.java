package net.ooder.esd.engine;

import net.ooder.common.JDSException;
import net.ooder.common.database.metadata.TableInfo;
import net.ooder.config.BPDProjectConfig;
import net.ooder.esb.config.formula.FormulaInst;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.manager.plugins.api.node.APIPaths;
import net.ooder.esd.manager.plugins.font.node.FontConfig;
import net.ooder.esd.manager.plugins.img.node.ImgConfig;
import net.ooder.esd.manager.plugins.style.node.StyleConfig;
import net.ooder.esd.engine.config.ProjectConfig;
import net.ooder.esd.engine.config.dsm.DSMProjectConfig;
import net.ooder.esd.engine.enums.ProjectDefAccess;
import net.ooder.esd.engine.enums.ProjectResourceType;
import net.ooder.esd.engine.enums.ProjectRoleType;
import net.ooder.org.Person;
import net.ooder.vfs.Folder;

import java.util.List;

public interface Project {

    public String getId();

    public String getDesc();

    public String getPath();

    public BPDProjectConfig getBpdProjectConfig();

    public RepositoryInst getRepository();

    public DSMProjectConfig getDsmProjectConfig();

    public List<FormulaInst> getFormulas();

    public FormulaInst getFormula(String formulaId);

    public ProjectConfig getConfig();

    public String getProjectName();

    public ProjectResourceType getResourceType();

    public ProjectDefAccess getProjectType();

    public Person getOwner();

    void updateConfig(ProjectConfig config) throws JDSException;

    public String getPublicPath();

    public String getWorkSpace();

    public List<String> getApiFilter();

    public List<APIPaths> getApiPaths() throws JDSException;

    public List<FontConfig> getFonts() throws JDSException;

    public List<EUModule> getComponents() throws JDSException;

    public List<ImgConfig> getImgs() throws JDSException;

    public List<TableInfo> getTables() throws JDSException;

    public List<StyleConfig> getStyles() throws JDSException;

    public String getPublicServerUrl();

    public ProjectVersion getActiveProjectVersion() throws JDSException;

    public List<ProjectVersion> getAllEIProjectVersion() throws JDSException;

    public ProjectVersion createVersion() throws JDSException;

    public Folder getRootfolder();

    public List<Person> getDevPersons(ProjectRoleType type) throws JDSException;


}
