package net.ooder.esd.engine.event;

import net.ooder.cluster.service.SysEventWebManager;
import net.ooder.common.EsbFlowType;
import net.ooder.common.JDSException;
import net.ooder.config.ListResultModel;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esb.config.manager.EsbBeanFactory;
import net.ooder.esb.config.manager.ExpressionTempBean;
import net.ooder.esb.config.manager.ServiceBean;

import net.ooder.jds.core.esb.EsbUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = {"/admin/event/"})
public class EventService {

    @MethodChinaName(cname = "获取所有可订阅消息")
    @RequestMapping(value = {"getAllEventBeans"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<ExpressionTempBean>> getAllEventBeans() {
        ListResultModel<List<ExpressionTempBean>> userStatusInfo = new ListResultModel<List<ExpressionTempBean>>();
        SysEventWebManager webManager = EsbUtil.parExpression(SysEventWebManager.class);
        List<ExpressionTempBean> serviceBeans = null;
        try {
            serviceBeans = (List<ExpressionTempBean>) webManager.getRegisterEventByCode("-all-").get();
        } catch (JDSException e) {
            e.printStackTrace();
        }
        userStatusInfo.setData(serviceBeans);
        return userStatusInfo;
    }


    @MethodChinaName(cname = "获取所有注册监听器")
    @RequestMapping(value = {"getAllListeners"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<? extends ServiceBean>> getAllListeners() {
        ListResultModel<List<? extends ServiceBean>> userStatusInfo = new ListResultModel<List<? extends ServiceBean>>();
        SysEventWebManager webManager = EsbUtil.parExpression(SysEventWebManager.class);
        List<? extends ServiceBean> serviceBeans = EsbBeanFactory.getInstance().getServiceBeanByFlowType(EsbFlowType.listener);
        userStatusInfo.setData(serviceBeans);
        return userStatusInfo;
    }


}
