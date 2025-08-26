package net.ooder.esd.bean;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.RouteCustomMenu;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.annotation.RouteToType;
import net.ooder.web.util.AnnotationUtil;

import java.util.HashSet;
import java.util.Set;

@AnnotationType(clazz = RouteCustomMenu.class)
public class RouteMenuBean implements CustomBean {

    Set<RouteToType> routeType = new HashSet<>();


    public RouteMenuBean() {
        AnnotationUtil.fillDefaultValue(RouteCustomMenu.class, this);

    }

    public Set<RouteToType> getRouteType() {
        return routeType;
    }

    public void setRouteType(Set<RouteToType> routeType) {
        this.routeType = routeType;
    }

    public RouteMenuBean(RouteCustomMenu annotation) {
        fillData(annotation);
    }

    public RouteMenuBean fillData(RouteCustomMenu annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
