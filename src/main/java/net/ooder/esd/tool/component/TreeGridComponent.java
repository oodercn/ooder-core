package net.ooder.esd.tool.component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.event.GridEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.tool.properties.Cell;
import net.ooder.esd.tool.properties.GridProperties;
import net.ooder.esd.tool.properties.Header;
import net.ooder.esd.tool.properties.Row;

import java.util.ArrayList;
import java.util.List;

public class TreeGridComponent extends Component<GridProperties, GridEventEnum> implements DataComponent<List<Row>> {

    public TreeGridComponent(String alias) {
        super(ComponentType.TREEGRID, alias);
        this.setProperties( new GridProperties());

    }

    public TreeGridComponent(String alias, GridProperties properties) {
        super(ComponentType.TREEGRID, alias);
        this.setProperties(properties);
    }

    public TreeGridComponent() {
        super(ComponentType.TREEGRID);
        this.setProperties( new GridProperties());
    }

    @Override
    @JSONField(serialize = false)
    public List<Row> getData() {
        return this.getProperties().getRows();
    }

    @Override
    public void setData(List<Row> data) {
        if (data != null) {
            List<Row> rowList = new ArrayList<>();
            for (Object obj : data) {
                Row row = new Row(JSONObject.parse(JSONObject.toJSONString(obj)));
                List<Cell> cells = new ArrayList<>();
                JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(obj));
                List<Header> headers = this.getProperties().getHeader();
                for (Header header : headers) {
                    Cell cell = new Cell();
                    String id = header.getId();
                    String json = jsonObject.getString(id);
                    boolean isObject = JSONObject.isValidObject(json);
                    if (isObject) {
                        cell = JSONObject.parseObject(json, Cell.class);
                    } else {
                        cell = new Cell(id, json);
                    }
                    if (this.getProperties().getUidColumn() != null && this.getProperties().getUidColumn().equals(id)) {
                        row.setId(json);
                    }
                    cells.add(cell);
                }
                row.setCells(cells);
                rowList.add(row);
            }
            this.getProperties().setRows(rowList);
        }

    }

}
