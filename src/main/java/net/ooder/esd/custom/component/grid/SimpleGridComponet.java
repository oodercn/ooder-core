package net.ooder.esd.custom.component.grid;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.properties.SimpleGridProperties;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.tool.component.TreeGridComponent;
import org.mvel2.DataConversion;

import java.util.*;


public class SimpleGridComponet extends TreeGridComponent {

    public SimpleGridComponet(Object obj) {
        super();
        List<Object> list = new ArrayList<>();
        if (obj != null) {
            if (obj.getClass().isArray() || Iterable.class.isAssignableFrom(obj.getClass())) {
                Map<String, Object> objectMap = new HashMap<>();
                List<Object> objects = DataConversion.convert(obj, List.class);
                if (objects.size() > 0) {
                    Object item = objects.get(0);
                    if (item.getClass().getName().startsWith("net.ooder.")) {
                        ESDClass esdClass = null;
                        try {
                            esdClass = BuildFactory.getInstance().getClassManager().loadViewClass(item.getClass().getName());
                            SimpleGridProperties properties = null;
                            if (esdClass != null) {
                                properties = new SimpleGridProperties(esdClass);
                            } else {
                                Map objMap = (Map) JSON.toJSON(item);
                                properties = new SimpleGridProperties(objMap.keySet());
                            }
                            properties.setRawData(objects);
                            this.setProperties( properties);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (JDSException e) {
                            e.printStackTrace();
                        }


                    } else if (item instanceof Map) {
                        for (Object object : objects) {
                            Map objMap = (Map) object;
                            properties =new SimpleGridProperties(objMap.keySet());
                            list.add(objMap);
                        }
                        // properties.setRawData(list);
                    } else {
                        SimpleGridProperties properties =new SimpleGridProperties(List.class.getSimpleName());
                        for (Object object : objects) {
                            Map objMap = new HashMap();
                            objMap.put("objValue", JSON.toJSONString(object));
                            list.add(objMap);
                        }
                        properties.setRawData(list);
                        this.setProperties(properties);
                    }
                }
            } else if (obj.getClass().getName().startsWith("net.ooder.")) {
                ESDClass esdClass = null;
                try {
                    esdClass = BuildFactory.getInstance().getClassManager().loadViewClass(obj.getClass().getName());
                    if (esdClass != null) {
                        SimpleGridProperties properties =  new SimpleGridProperties(esdClass);
                        list.add(obj);
                        properties.setRawData(list);
                    } else {
                        String objStr = JSON.toJSONString(obj);
                        Map<String, String> valueMap = new HashMap<>();
                        valueMap.put("valueObj", objStr);
                        list.add(valueMap);
                        SimpleGridProperties properties =new SimpleGridProperties(objStr);
                        properties.setRawData(list);
                    }


                    this.setProperties( properties);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            } else if (obj instanceof Map) {
                Map objMap = (Map) obj;
                Map<String, String> strMap = new HashMap();
                Set<String> keys = objMap.keySet();
                SimpleGridProperties properties =  new SimpleGridProperties(objMap.keySet());

                for (String key : keys) {
                    Object mapObj = objMap.get(key);
                    if (mapObj != null) {
                        strMap.put(key, JSON.toJSONString(mapObj));
                    }

                }

                list.add(strMap);
                properties.setRawData(list);
                this.setProperties(properties);
            } else {
                String objStr = JSON.toJSONString(obj);
                Map<String, String> valueMap = new HashMap<>();
                valueMap.put("valueObj", objStr);
                list.add(valueMap);
                SimpleGridProperties properties =  new SimpleGridProperties(objStr);
                properties.getHeader().get(0).setFlexSize(true);
                properties.setRawData(list);
                this.setAlias("value" + ComponentType.TREEGRID.name());
                this.setProperties( properties);
            }
        }

        setAlias(ComponentType.TREEGRID.name());
    }

    public SimpleGridComponet(String obj) {
        super();
        SimpleGridProperties properties =  new SimpleGridProperties(obj);
        this.setAlias("value" + ComponentType.TREEGRID.name());
        this.setProperties( properties);
    }


}
