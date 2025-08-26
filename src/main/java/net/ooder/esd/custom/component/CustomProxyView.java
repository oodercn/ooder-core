package net.ooder.esd.custom.component;

import net.ooder.esd.tool.component.BlockComponent;

public class CustomProxyView<M extends BlockComponent> extends CustomModuleComponent<M> {

//
//    CustomProxyView() {
//        super();
//    }
//
//
//    public CustomProxyView(EUModule module, RequestMethodBean methodBean, Map valueMap) throws ClassNotFoundException {
//        super(module, methodBean, valueMap);
//        M currComponent = (M) new BlockPanelComponent<BlockProperties, DivEventEnum>(Dock.fill, module.getName() + DefaultBoxfix);
//
//        this.setCurrComponent(currComponent);
//        ModuleProxyAnnotation dynAnnotation = AnnotationUtil.getMethodAnnotation(methodBean.getSourceMethod(), ModuleProxyAnnotation.class);
////        if (dynAnnotation != null) {
////            if (dynAnnotation.saveUrl() != null && !dynAnnotation.saveUrl().equals("")) {
////                this.saveUrl = dynAnnotation.saveUrl();
////            }
////            if (dynAnnotation.dataUrl() != null && !dynAnnotation.dataUrl().equals("")) {
////                this.dataPath = dynAnnotation.dataUrl();
////            }
////        }
//
//        if (!dynAnnotation.projectName().equals("")) {
//            String projectName = dynAnnotation.projectName();
//            if (projectName.equals("")) {
//                projectName = this.getProjectName();
//            }
//            ProjectVersion projectVersion = null;
//            try {
//                projectVersion = ESDFacrory.getESDClient().getProjectVersionByName(projectName);
//                EUModule realModule = projectVersion.getModule(dynAnnotation.proxyCls());
//                if (realModule != null) {
//                    ModuleComponent moduleComponent = realModule.getComponent();
//                    switch (dynAnnotation.append()) {
//                        case ref:
//                            moduleComponent = new ModuleComponent();
//                            moduleComponent.setClassName(realModule.getClassName());
//                            moduleComponent.setAlias(realModule.getComponent().getAlias());
//                            currComponent.addChildren(moduleComponent);
//                            break;
//                        case dyn:
//                            this.setCustomFunctions(moduleComponent.getCustomFunctions());
//                            this.setFormulas(moduleComponent.getFormulas());
//                            this.setDependencies(moduleComponent.getDependencies());
//                            this.setFunctions(moduleComponent.getFunctions());
//                            this.setModuleVar(moduleComponent.getModuleVar());
//                            this.setProperties(moduleComponent.getProperties());
//                            this.setCS(moduleComponent.getCS());
//                            String customAppend = realModule.getComponent().getCustomAppend();
//                            if (customAppend != null && !customAppend.equals("")) {
//                                this.setAfterAppend(customAppend);
//                            }
//                            Component fristComponent = realModule.getComponent().getLastBoxComponent();
//                            if (fristComponent instanceof DialogComponent) {
//                                DialogComponent dialogComponent = (DialogComponent) fristComponent;
//                                if (dialog != null) {
//                                    dialog.getEvents().putAll(dialogComponent.getEvents());
//                                    dialog.setAlias(dialogComponent.getAlias());
//                                }
//                            }
//                            this.addChildren(genAPIComponent(methodBean, currComponent, dynAnnotation));
//                            break;
//                        case append:
//                            this.setProperties(moduleComponent.getProperties());
//                            this.setFunctions(moduleComponent.getFunctions());
//                            this.setDependencies(moduleComponent.getDependencies());
//                            this.setViewConfig(moduleComponent.getViewConfig());
//                            this.setRequired(moduleComponent.getRequired());
//                            this.setEvents(moduleComponent.getEvents());
//                            this.setFormulas(moduleComponent.getFormulas());
//                            this.setCustomFunctions(moduleComponent.getCustomFunctions());
//                            this.setModuleVar(moduleComponent.getModuleVar());
//                            this.setCustomAppend(moduleComponent.getCustomAppend());
//                            this.setCS(moduleComponent.getCS());
//                            fristComponent = realModule.getComponent().getLastBoxComponent();
//                            if (fristComponent instanceof DialogComponent) {
//                                DialogComponent dialogComponent = (DialogComponent) fristComponent;
//                                if (dialog != null) {
//                                    dialog.getEvents().putAll(dialogComponent.getEvents());
//                                    dialog.setAlias(dialogComponent.getAlias());
//                                    ComponentList<Component> components = fristComponent.getChildren();
//                                    if (components != null) {
//                                        for (Component ccomponent : components) {
//                                            dialog.addChildren(ccomponent);
//                                        }
//                                    }
//                                }
//                            } else {
//                                currComponent.addChildren(fristComponent);
//                            }
//                            break;
//                    }
//                }
//            } catch (JDSException e) {
//                e.printStackTrace();
//            }
//
//
//        } else {
//            this.addChildren(genAPIComponent(methodBean, currComponent, dynAnnotation));
//
//        }
//        fillAction(dynAnnotation, currComponent);
//        this.addChildLayoutNav(currComponent);
//    }
//
//
//    //数据对象
//    @JSONField(serialize = false)
//    APICallerComponent[] genAPIComponent(RequestMethodBean methodBean, Component boxComponent, DynLoadAnnotation customAnnotation) {
//        List<APICallerComponent> apiCallerComponents = new ArrayList<APICallerComponent>();
//        if (dataPath != null && !dataPath.equals("")) {
//            RequestMethodBean dataMethodBean = methodBean.getApiConfig().getMethodByName(dataPath);
//            try {
//                if (dataMethodBean == null) {
//                    dataMethodBean = ESDFacrory.getESDClient().getRequestMethodBean(dataPath, null);
//                }
//                if (dataMethodBean == null) {
//                    dataMethodBean = ESDFacrory.getESDClient().getRequestMethodBean(methodBean.getApiConfig().getUrl() + dataPath, null);
//                }
//            } catch (JDSException e) {
//                e.printStackTrace();
//            }
//            APICallerComponent reloadAPI = new APICallerComponent(methodBean);
//            reloadAPI.setAlias(CustomFormAction.Reload.getTarget());
//
//            //刷新调用
//            APICallerProperties reloadProperties = reloadAPI.getProperties();
//            UrlPathData treepathData = new UrlPathData(this.getCtxBaseComponent().getAlias(), RequestPathTypeEnum.form, "");
//            reloadProperties.addRequestData(treepathData);
//
//            UrlPathData formData = new UrlPathData(boxComponent.getAlias(), ResponsePathTypeEnum.component, "data");
//            reloadProperties.addResponseData(formData);
//
//            UrlPathData formCtxData = new UrlPathData(this.getCtxBaseComponent().getAlias(), ResponsePathTypeEnum.form, "data");
//            reloadProperties.addResponseData(formCtxData);
//            reloadProperties.setAutoRun(true);
//
//            String url = reloadProperties.getQueryURL();
//
//            if (url.indexOf("?") > -1) {
//                String httpUrl = url.split("\\?")[0];
//                String queryStr = url.split("\\?")[1];
//                if (httpUrl.endsWith(".dyn")) {
//                    httpUrl = httpUrl + ".dyn";
//                }
//                url = httpUrl + "?" + queryStr;
//            } else {
//                if (!url.endsWith(".dyn")) {
//                    url = url + ".dyn";
//                }
//            }
//            reloadProperties.setQueryURL(url);
//            apiCallerComponents.add(reloadAPI);
//        }
//
//
//
//        return apiCallerComponents.toArray(new APICallerComponent[]{});
//    }


}
