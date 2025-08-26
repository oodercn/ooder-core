package net.ooder.esd.manager.service;

import net.ooder.common.JDSException;
import net.ooder.common.expression.ParseException;
import net.ooder.common.expression.function.AbstractFunction;
import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDClientImpl;
import net.ooder.esd.engine.ESDFacrory;


@EsbBeanAnnotation(id = "ESDClient",
        name = "装载远程应用",
        expressionArr = "GetESDClient()",
        desc = "装载远程应用")

public class GetESDClient<T extends ESDClient> extends AbstractFunction<T> {


    public T perform() throws ParseException {
        ESDClientImpl client = null;
        try {
            client = (ESDClientImpl) ESDFacrory.getAdminESDClient();
        } catch (JDSException e) {
            throw new ParseException(e);
        }
        return (T) client;

    }


}
