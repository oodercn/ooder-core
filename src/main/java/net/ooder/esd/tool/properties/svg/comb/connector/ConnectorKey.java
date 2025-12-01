package net.ooder.esd.tool.properties.svg.comb.connector;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.esd.annotation.svg.ConnectorKeyAnnotation;
import net.ooder.esd.annotation.ui.CursorType;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.esd.tool.properties.svg.comb.Key;
import net.ooder.esd.tool.properties.svg.comb.circle.CircleKey;
import net.ooder.esd.tool.properties.svg.comb.rect.RectKey;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.json.JSONUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = ConnectorKeyAnnotation.class)
public class ConnectorKey extends Key {
    @JSONField(serialize = false)
    public Key start;
    @JSONField(serialize = false)
    public Key end;

    String path;

    @JSONField(name = "strok-width")
    Integer strokeWidth;

    @JSONField(name = "arrow-start")
    String arrowStart;
    @JSONField(name = "arrow-end")
    String arrowEnd;

    private CursorType cursor;
    ;

    public CursorType getCursor() {
        return cursor;
    }

    public void setCursor(CursorType cursor) {
        this.cursor = cursor;
    }

    public ConnectorKey() {

    }

    public ConnectorKey(SVGProperties properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public ConnectorKey(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(ConnectorKeyAnnotation.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ConnectorKeyAnnotation) {
                fillData((ConnectorKeyAnnotation) annotation);
            }
        }

    }


    public ConnectorKey(ConnectorKeyAnnotation annotation) {
        fillData(annotation);
    }

    public ConnectorKey fillData(ConnectorKeyAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public ConnectorKey(Key start, Key end) {
        this.start = start;
        this.end = end;
    }

    public String getPath() {
        if (start != null && end != null) {
            path = "M,";
            int sx = 0;
            int sy = 0;
            int ssx = 0;
            int ssy = 0;

            int ex = 0;
            int ey = 0;
            int eex = 0;
            int eey = 0;
            int eew = 0;
            int eeh = 0;
            int ssw = 0;
            int ssh = 0;

            if (start instanceof CircleKey) {
                CircleKey circleKey = (CircleKey) start;
                ssx = TypeUtils.castToInt(circleKey.getCx());
                ssy = TypeUtils.castToInt(circleKey.getCy());
            } else if (start instanceof RectKey) {
                RectKey rectKey = (RectKey) start;
                ssx = TypeUtils.castToInt(rectKey.getX());
                ssy = TypeUtils.castToInt(rectKey.getY());
                ssw = rectKey.getWidth();
                ssh = rectKey.getHeight();
            }

            if (end instanceof CircleKey) {
                CircleKey circleKey = (CircleKey) end;
                eex = TypeUtils.castToInt(circleKey.getCx());
                eey = TypeUtils.castToInt(circleKey.getCy());
            } else if (end instanceof RectKey) {
                RectKey rectKey = (RectKey) end;
                eex = TypeUtils.castToInt(rectKey.getX());
                eew = rectKey.getWidth();
                eeh = rectKey.getHeight();
                eey = TypeUtils.castToInt(rectKey.getY());
            }


            if (start instanceof CircleKey) {
                CircleKey circleKey = (CircleKey) start;
                int cSx = TypeUtils.castToInt(circleKey.getCx()) - TypeUtils.castToInt(circleKey.getR());
                int cEx = eex + eew / 2;
                if (cSx > cEx) {
                    sx = cSx - 2;
                } else if (cSx <= cEx && cSx > cEx - TypeUtils.castToInt(circleKey.getR()) * 2) {
                    sx = cSx + TypeUtils.castToInt(circleKey.getR());
                } else {
                    sx = cSx + TypeUtils.castToInt(circleKey.getR()) * 2 + 2;
                }

                int csy = TypeUtils.castToInt(circleKey.getCy()) - TypeUtils.castToInt(circleKey.getR());
                int cEy = eey - eeh / 2;
                if (csy > cEy) {
                    sy = csy - 2;
                } else if (csy <= cEy - TypeUtils.castToInt(circleKey.getR()) * 2) {
                    sy = csy + TypeUtils.castToInt(circleKey.getR()) * 2 + 2;
                } else {
                    sy = csy + TypeUtils.castToInt(circleKey.getR());
                }


            } else if (start instanceof RectKey) {
                RectKey rectKey = (RectKey) start;
                Integer with = rectKey.getWidth();
                Integer height = rectKey.getHeight();
                sx = TypeUtils.castToInt(rectKey.getX());
                sy = TypeUtils.castToInt( rectKey.getY());
                if (sx < eex) {
                    if (sx <= (eex - with)) {
                        sx = sx + with + 5;
                    } else if (sx <= (eex - (rectKey.getWidth() / 2))) {
                        sx = sx + (with / 2);
                    } else {
                        sx = sx + (with / 2);
                    }
                } else {
                    if (sx >= (eex + with)) {
                        sx = sx - 5;
                    } else if (sx >= eex + (with / 2)) {
                        sx = sx + (with / 2);
                    } else {
                        sx = sx + (with / 2);
                    }
                }


                if (sy <= eey) {
                    if (sy <= (eey - height)) {
                        sy = sy + height + 5;
                    } else if (sy <= (eey - (rectKey.getHeight() / 2))) {
                        sy = sy + (height / 2);
                    } else {
                        sy = sy + (height / 2);
                        ;
                    }
                } else {
                    if (sy >= (eey + rectKey.getHeight())) {
                        sy = sy - 5;
                    } else if (sy >= (eey + (rectKey.getHeight() / 2))) {
                        sy = sy + (height / 2);
                    } else {
                        sy = sy + (height / 2);
                    }
                }


                if (sx <= ssx || sx >= (ssx + with)) {
                    sy = ssy + (height / 2);
                } else {
                    if (sy <= eey) {
                        sy = ssy + height + 5;
                    } else {
                        sy = ssy - 5;
                    }
                }

            }


            if (end instanceof CircleKey) {
                CircleKey circleKey = (CircleKey) end;
                int cEx = TypeUtils.castToInt(circleKey.getCx()) - TypeUtils.castToInt(circleKey.getR());
                int cSx = ssx + ssw / 2;

                if (cEx > cEx) {
                    ex = cSx - 5;
                } else if (cEx <= cEx && cEx > cEx - TypeUtils.castToInt(circleKey.getR()) * 2) {
                    ex = cEx + TypeUtils.castToInt(circleKey.getR());
                } else {
                    ex = cEx + TypeUtils.castToInt(circleKey.getR()) * 2 + 5;
                }

                int cEy = TypeUtils.castToInt(circleKey.getCy()) - TypeUtils.castToInt(circleKey.getR());
                int cSy = ssy - ssh / 2;
                if (cEy > cSy) {
                    ey = cEy - 5;
                } else if (cEy <= cSy - TypeUtils.castToInt(circleKey.getR()) * 2) {
                    ey = cEy + TypeUtils.castToInt(circleKey.getR()) * 2 + 5;
                } else {
                    ey = cEy + TypeUtils.castToInt(circleKey.getR());
                }

            } else if (end instanceof RectKey) {
                RectKey rectKey = (RectKey) end;
                int with = rectKey.getWidth();
                int height = rectKey.getHeight();
                ex =  TypeUtils.castToInt(rectKey.getX());
                ey = TypeUtils.castToInt( rectKey.getY());
                if (ex < ssx) {
                    if (ex <= (ssx - with)) {
                        ex = ex + with + 5;
                    } else if (ex <= (ssx - (rectKey.getWidth() / 2))) {
                        ex = ex + (with / 2);
                    } else {
                        ex = ex + (with / 2);
                    }
                } else {
                    if (ex >= (ssx + with)) {
                        ex = ex - 5;
                    } else if (ex >= ssx + (with / 2)) {
                        ex = ex + (with / 2);
                    } else {
                        ex = ex + (with / 2);
                    }
                }

                if (ex <= eex || ex >= (eex + with)) {
                    ey = eey + (height / 2);
                } else {
                    if (ey <= ssy) {
                        ey = eey + height + 5;
                    } else {
                        ey = eey - 5;
                    }
                }
            }


            path = path + sx + "," + sy;
            path = path + "C," + ((sx + ex) / 2) + "," + sy;
            path = path + "," + ((sx + ex) / 2) + "," + ey;
            path = path + "," + ex + "," + ey;

        }

        return path;
    }

    public ConnectorKey clone() {
        ConnectorKey text = new ConnectorKey();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(this), Map.class), text, false, false);
        return text;
    }


    public Key getStart() {
        return start;
    }

    public void setStart(Key start) {
        this.start = start;
    }

    public Key getEnd() {
        return end;
    }

    public void setEnd(Key end) {
        this.end = end;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArrowStart() {
        return arrowStart;
    }

    public void setArrowStart(String arrowStart) {
        this.arrowStart = arrowStart;
    }

    public String getArrowEnd() {
        return arrowEnd;
    }

    public void setArrowEnd(String arrowEnd) {
        this.arrowEnd = arrowEnd;
    }

    public Integer getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(Integer strokeWidth) {
        this.strokeWidth = strokeWidth;
    }
}
