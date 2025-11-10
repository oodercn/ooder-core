package net.ooder.esd.dsm;

public interface JavaRoot {

    String[] innerPacks = new String[]{"api", "service", "module", "view", "dic"};
    String[] resiotoryPacks = new String[]{"api", "service", "module", "dic"};
    String[] domainPacks = new String[]{"domain", "resiotory", "view"};

    public String getSpace();

    public String getPackageName();

    public void setPackageName(String packageName);

    public String getClassName();

    public void setClassName(String className);

    public String getModuleName();

    public String getCnName();

    public String getBasepath();

    ;
}
