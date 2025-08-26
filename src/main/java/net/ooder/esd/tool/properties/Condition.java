package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.CustomCondition;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.util.json.SymbolCodec;

import java.lang.annotation.Annotation;

public class Condition implements CustomCondition {

    String left;
    @JSONField(deserializeUsing = SymbolCodec.class, serializeUsing = SymbolCodec.class)
    SymbolType symbol = SymbolType.equal;
    String right;
    String expression;

    public Condition(CustomCondition condition) {
        this.expression = condition.expression();
        this.symbol = condition.symbol();
        this.right = condition.right();
        this.left = condition.left();
    }

    @JSONField(serialize = false)
    public String getConditionId() {
        return right + "_" + symbol + "_" + left;
    }


    public Condition(String left, SymbolType symbol, String right) {
        this.left = left;
        this.symbol = symbol;
        this.right = right;
    }

    public Condition(String expression) {
        this.expression = expression;
    }

    public Condition() {


    }

    public String getLeft() {
        return left;
    }

    public SymbolType getSymbol() {
        return symbol;
    }

    public String getRight() {
        return right;
    }


    public void setLeft(String left) {
        this.left = left;
    }

    public void setSymbol(SymbolType symbol) {
        this.symbol = symbol;
    }

    public void setRight(String right) {
        this.right = right;
    }


    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String left() {
        return left;
    }

    @Override
    public SymbolType symbol() {
        return symbol;
    }

    @Override
    public String right() {
        return right;
    }

    @Override
    public String expression() {
        return expression;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return CustomCondition.class;
    }
}
