package net.ooder.esd.manager.service;

import net.ooder.common.expression.ParseException;
import net.ooder.common.expression.function.AbstractFunction;
import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.org.OrgManager;
import net.ooder.server.OrgManagerFactory;


@EsbBeanAnnotation(id = "OrgManager",
        name = "装载远程应用",
        expressionArr = "GetOrgManager()",
        desc = "装载远程应用")

public class GetOrgManager<T extends OrgManager> extends AbstractFunction<T> {


    public T perform() throws ParseException {
        OrgManager client = OrgManagerFactory.getOrgManager();
        return (T) client;
    }


}

