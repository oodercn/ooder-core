package net.ooder.esd.custom.properties;


import net.ooder.esd.annotation.GridAnnotation;
import net.ooder.esd.annotation.RowHead;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.view.field.FieldGridConfig;
import net.ooder.esd.tool.properties.GridProperties;
import net.ooder.esd.tool.properties.Header;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

;


public class SimpleGridProperties extends GridProperties {

    public SimpleGridProperties(String caption) {
        super();
        Header header = new Header("objValue", caption);
        header.setFlexSize(true);
        this.getHeader().add(header);
    }

    public SimpleGridProperties(Set<String> keys) {
        for (String key : keys) {
            this.getHeader().add(new Header(key, key));
        }
    }

//    public SimpleGridProperties() {
//        super();
//    }

    ;

    public SimpleGridProperties(ESDClass esdClass) {
        super();

        this.name = esdClass.getName() + ComponentType.TREEGRID.name();
        GridAnnotation annotation = AnnotationUtil.getClassAnnotation(esdClass.getCtClass(), GridAnnotation.class);
        RowHead rowHead = AnnotationUtil.getClassAnnotation(esdClass.getCtClass(), RowHead.class);
        List<ESDField> fieldList = esdClass.getFieldList();
        if (annotation != null) {
            this.setRowNumbered(rowHead.rowNumbered());
            this.setSelMode(rowHead.selMode());
        }

        List<CustomHeader> displayList = new ArrayList<CustomHeader>();

        for (ESDField fieldInfo : fieldList) {
            FieldGridConfig fieldGridConfig = new FieldGridConfig(fieldInfo, esdClass.getSourceClass().getClassName(), null);
            CustomHeader headCol = new CustomHeader(fieldGridConfig);
            this.getHeader().add(headCol);
            if (fieldInfo.isSerialize() && !fieldInfo.isHidden()) {
                displayList.add(headCol);
            }
        }
        //最后一行 平铺
        displayList.get(displayList.size() - 1).setFlexSize(true);
        this.setDesc(esdClass.getDesc());

    }

}
