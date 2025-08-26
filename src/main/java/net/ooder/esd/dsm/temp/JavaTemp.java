package net.ooder.esd.dsm.temp;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.dsm.domain.enums.*;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.enums.RepositoryType;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.RefType;
import net.ooder.annotation.UserSpace;
public class JavaTemp {

    String javaTempId;

    String fileId;

    String thumbnailFileId;

    String image;

    String name;

    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    ViewType viewType = ViewType.BLOCK;

    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    RangeType rangeType = RangeType.NONE;

    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    DSMType dsmType;

    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    RepositoryType repositoryType;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    UserSpace userSpace;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    AggregationType aggregationType;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    CustomDomainType customDomainType;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    BpmDomainType bpmDomainType;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    MsgDomainType msgDomainType;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    NavDomainType navDomainType;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    OrgDomainType orgDomainType;
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    RefType refType = RefType.NONE;

    String packagePostfix;

    String path;

    String expression;

    String content;

    String desc;

    String namePostfix;

    public JavaTemp() {

    }


    public JavaTemp clone(JavaTemp temp) {
        fill(temp);
        return this;
    }

    public JavaTemp fill(JavaTemp temp) {
        this.name = temp.getName();
        this.viewType = temp.getViewType();
        this.content = temp.getContent();
        this.bpmDomainType = temp.getBpmDomainType();
        this.orgDomainType = temp.getOrgDomainType();
        this.navDomainType = temp.getNavDomainType();
        this.msgDomainType = temp.getMsgDomainType();
        this.customDomainType = temp.getCustomDomainType();
        this.aggregationType = temp.getAggregationType();
        this.fileId = temp.getFileId();
        this.javaTempId = temp.getJavaTempId();
        this.path = temp.getPath();
        this.repositoryType = temp.getRepositoryType();
        this.dsmType = temp.getDsmType();
        this.refType = temp.getRefType();
        this.rangeType = temp.getRangeType();
        this.namePostfix = temp.getNamePostfix();
        this.packagePostfix = temp.getPackagePostfix();
        this.expression = temp.getExpression();
        this.userSpace = temp.getUserSpace();

        return this;
    }

    public BpmDomainType getBpmDomainType() {
        return bpmDomainType;
    }

    public void setBpmDomainType(BpmDomainType bpmDomainType) {
        this.bpmDomainType = bpmDomainType;
    }

    public MsgDomainType getMsgDomainType() {
        return msgDomainType;
    }

    public void setMsgDomainType(MsgDomainType msgDomainType) {
        this.msgDomainType = msgDomainType;
    }

    public NavDomainType getNavDomainType() {
        return navDomainType;
    }

    public void setNavDomainType(NavDomainType navDomainType) {
        this.navDomainType = navDomainType;
    }

    public OrgDomainType getOrgDomainType() {
        return orgDomainType;
    }

    public void setOrgDomainType(OrgDomainType orgDomainType) {
        this.orgDomainType = orgDomainType;
    }

    public CustomDomainType getCustomDomainType() {
        return customDomainType;
    }

    public void setCustomDomainType(CustomDomainType customDomainType) {
        this.customDomainType = customDomainType;
    }

    public RepositoryType getRepositoryType() {
        return repositoryType;
    }

    public void setRepositoryType(RepositoryType repositoryType) {
        this.repositoryType = repositoryType;
    }

    public AggregationType getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(AggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }

    public UserSpace getUserSpace() {
        return userSpace;
    }

    public void setUserSpace(UserSpace userSpace) {
        this.userSpace = userSpace;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RangeType getRangeType() {
        return rangeType;
    }

    public void setRangeType(RangeType rangeType) {
        this.rangeType = rangeType;
    }

    public String getJavaTempId() {
        return javaTempId;
    }

    public void setJavaTempId(String javaTempId) {
        this.javaTempId = javaTempId;
    }

    public DSMType getDsmType() {
        return dsmType;
    }

    public void setDsmType(DSMType dsmType) {
        this.dsmType = dsmType;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public RefType getRefType() {
        return refType;
    }

    public void setRefType(RefType refType) {
        this.refType = refType;
    }

    public String getPackagePostfix() {
        return packagePostfix;
    }

    public void setPackagePostfix(String packagePostfix) {
        this.packagePostfix = packagePostfix;
    }

    public String getNamePostfix() {
        return namePostfix;
    }

    public void setNamePostfix(String namePostfix) {
        this.namePostfix = namePostfix;
    }

    public String getThumbnailFileId() {
        return thumbnailFileId;
    }

    public void setThumbnailFileId(String thumbnailFileId) {
        this.thumbnailFileId = thumbnailFileId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
