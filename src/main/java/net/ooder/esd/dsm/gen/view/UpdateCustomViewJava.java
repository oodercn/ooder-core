package net.ooder.esd.dsm.gen.view;

import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.gen.GenJavaTask;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.ViewInst;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpdateCustomViewJava extends GenJavaTask {

    public String moduleName;
    public CustomViewBean viewBean;
    public JavaSrcBean javaSrcBean;
    public ChromeProxy chrome;
    public String euClassName;

    public UpdateCustomViewJava(CustomViewBean viewBean, JavaSrcBean javaSrcBean, String moduleName, ChromeProxy chrome) {
        super();
        this.viewBean = viewBean;
        this.javaSrcBean = javaSrcBean;
        this.moduleName = moduleName;
        this.chrome = chrome;
        this.euClassName = javaSrcBean.getClassName();

    }

    @Override
    public List<JavaSrcBean> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        if (euClassName != null && viewBean != null) {
            DomainInst domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(viewBean.getDomainId());
            ViewInst defaultView = domainInst.getViewInst();
            GenJava javaGen = GenJava.getInstance(domainInst.getProjectVersionName());
            AggViewRoot viewRoot = new AggViewRoot(defaultView, euClassName, viewBean);
            JavaTemp javaTemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaSrcBean.getJavaTempId());
            JavaRoot javaRoot = BuildFactory.getInstance().buildJavaRoot(viewRoot, viewBean, moduleName, javaSrcBean.getClassName());
            File file = javaGen.createJava(javaTemp, javaRoot, chrome);
            JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, defaultView, javaTemp.getJavaTempId());
            javaSrcBeans.add(srcBean);
            BuildFactory.getInstance().createSource(srcBean.getClassName(), viewRoot, javaTemp, srcBean);
            classList.add(srcBean.getClassName());
        }
        return javaSrcBeans;
    }


    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome == null) {
            chrome = new LogSetpLog();
        }
        return chrome;
    }
}
