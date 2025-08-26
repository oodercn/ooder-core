package net.ooder.esd.custom.action;

import net.ooder.common.EventKey;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.tool.properties.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MShowPageAction extends Action {
    Args args;

    public MShowPageAction(EventKey eventKey) {

        super(eventKey);
        init();
    }

    public MShowPageAction(String src,EventKey eventKey) {
        super(eventKey);
        init();
        this.setTarget(src);
    }

    public MShowPageAction(MethodConfig fieldMethodConfig, EventKey eventKey) {
        super(eventKey);
        init();
        this.setDesc("弹出" + fieldMethodConfig.getCaption());
        Args args = new Args();
        setTarget(fieldMethodConfig.getEUClassName());
        args.setParams(fieldMethodConfig.getTagVar());
        this.setArgs(args.toArr());
    }


    public MShowPageAction(TreeListItem item, EventKey eventKey) {
        super(eventKey);
        init();
        this.setDesc("点击" + item.getCaption());
        this.setTarget(item.getEuClassName());
        Args args = new Args(item);
        this.setArgs(args.toArr());
    }

    void init() {
        this.setType(ActionTypeEnum.page);
        this.setRedirection("page");
        this.setMethod("show2");
        Args args = new Args();
        this.setArgs(args.toArr());
    }


    public class Args {

        String methodName = "{page.show2()}";
        String target;
        String childName;
        Map<String, Object> params;
        String data = "{page.getData()}";

        public Args(TreeListItem item) {
            this.params = item.getTagVar();
        }

        public Args() {

        }

        List<Object> toArr() {
            List<Object> args = new ArrayList<>();
            args.add("{page.show2()}");
            args.add(null);
            args.add(null);
            args.add(target);
            args.add(childName);
            args.add(params);
            args.add(data);
            args.add("{page}");
            return args;
        }


        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }

        public Map<String, Object> getParams() {
            return params;
        }

        public void setParams(Map<String, Object> params) {
            this.params = params;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

    }


}
