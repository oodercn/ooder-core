package net.ooder.esd.dsm.java;

enum Step {
    startCreatView("1.1创建视图", 1),
    endCreatView("1.2创建视图结束", 2),
    startCreateVO("2.1创建仓储实体", 3),
    endCreateVO("2.2创建仓储实体结束", 4),
    startCreateDAO("2.3创建仓储库接口", 5),
    endCreateDAO("2.4创建仓储库接口结束", 6),

    startCompileView("3.1预编译", 7),
    endCompileView("3.2预编译结束", 8),

    startGenRootBean("4.1创建跟Web接口", 9),
    endGenRootBean("4.2创建跟Web接口", 10),
    startGenAggMap("4.3创建跟Web接口", 11),
    endGenAggMap("4.4创建跟Web接口", 12),
    startBindResourceService("4.5重新绑定资源层", 13),
    endBindResourceService("4.6重新绑定资源层", 14),

    startReBindService("4.3重新绑定", 11),



    endReBindService("4.4重新绑定", 12),
    updateViewBean("5更新配置", 13),
    genChildJava("6更新子集合", 14);

    String name;

    int step;


    Step(String name, int step) {
        this.name = name;
        this.step = step;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
