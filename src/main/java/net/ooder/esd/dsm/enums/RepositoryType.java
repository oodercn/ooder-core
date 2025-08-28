package net.ooder.esd.dsm.enums;


import net.ooder.annotation.IconEnumstype;
import net.ooder.annotation.UserSpace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RepositoryType implements IconEnumstype {
    VO("实体对象(CRUD)", "fas fa-database", 0, UserSpace.CRUD),
    DO("库表映射(CRUD)", "fas fa-table", 1, UserSpace.CRUD),
    
    REPOSITORY("仓储接口(CRUD)", "fas fa-code-branch", 2, UserSpace.CRUD),
    REPOSITORYIMPL("仓储实现(CRUD)", "fas fa-tools", 3, UserSpace.CRUD),
    
    BPMBEAN("表单实体(BPM)", "fas fa-file-alt", 0, UserSpace.FORM),
    BPMRSERVICE("流程存储服务(BPM)", "fas fa-server", 1, UserSpace.FORM),
    
    USERBEAN("通用实体(User)", "fas fa-user", 0, UserSpace.USER),
    USERSERVICE("存储接口(User)", "fas fa-users-cog", 1, UserSpace.USER),
    
    VIEWBEAN("实体(View)", "fas fa-eye", 0, UserSpace.VIEW),
    VIEWSERVICE("服务(View)", "fas fa-server", 1, UserSpace.VIEW);
    private final String name;
    private final Integer buildIndex;
    private final String imageClass;
    private final UserSpace catType;


    RepositoryType(String name, String imageClass, Integer buildIndex, UserSpace catType) {
        this.name = name;
        this.imageClass = imageClass;
        this.catType = catType;
        this.buildIndex = buildIndex;
    }


    public static List<RepositoryType> getRepositoryTypeByCat(UserSpace... cat) {
        List<RepositoryType> repositoryTypes = new ArrayList<>();
        for (RepositoryType repositoryType : RepositoryType.values()) {
            if (Arrays.asList(cat).contains(repositoryType.getCatType())) {
                repositoryTypes.add(repositoryType);
            }
        }
        return repositoryTypes;
    }


    public UserSpace getCatType() {
        return catType;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public String getType() {
        return name();
    }

    @Override
    public String getName() {
        return name;
    }

    public String getImageClass() {
        return imageClass;
    }
}
