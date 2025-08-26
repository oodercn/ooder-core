package net.ooder.esd.engine.config.dsm;

import net.ooder.esb.config.formula.FormulaInst;

import java.util.ArrayList;
import java.util.List;

public class FormulasConfig {

    List<FormulaInst> formulas = new ArrayList<>();

    public FormulasConfig() {


    }

    public List<FormulaInst> getFormulas() {
        return formulas;
    }

    public void setFormulas(List<FormulaInst> formulas) {
        this.formulas = formulas;
    }
}
