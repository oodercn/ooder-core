package net.ooder.esd.manager.formula;

import net.ooder.common.FormulaType;
import net.ooder.common.JDSException;
import net.ooder.config.ListResultModel;
import net.ooder.esb.config.manager.EsbBeanFactory;
import net.ooder.esb.config.manager.ExpressionParameter;
import net.ooder.esb.config.manager.ServiceBean;
import net.ooder.esd.engine.MySpace;
import net.ooder.jds.core.esb.EsbUtil;


import java.util.*;

public class FormulaFactory {
    public Map<String, ParticipantSelect> formulaCache = new HashMap<String, ParticipantSelect>();

    static Map<String, FormulaFactory> managerMap = new HashMap<String, FormulaFactory>();

    private final MySpace space;

    public static final String THREAD_LOCK = "Thread Lock";

    public static FormulaFactory getInstance(MySpace space) {
        String path = space.getPath();
        FormulaFactory manager = managerMap.get(path);
        if (manager == null) {
            synchronized (THREAD_LOCK) {
                if (manager == null) {
                    manager = new FormulaFactory(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }

    FormulaFactory(MySpace space) {
        this.space = space;
        try {
            this.reLoadFormulas();
            this.initLocalFormula();
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public void initLocalFormula() {
        List<? extends ServiceBean> serviceBeans = EsbBeanFactory.getInstance().getFormula(FormulaType.values());
        for (ServiceBean serviceBean : serviceBeans) {
            if (serviceBean.getType() != null && !serviceBean.getType().equals(FormulaType.UNKNOW)) {
                ParticipantSelect formula = new CtParticipantSelect(serviceBean);
                formulaCache.put(formula.getParticipantSelectId(), formula);
            }
        }

    }


    public void addFormulaParameters(ExpressionParameter parameter) throws JDSException {
        if (parameter != null) {
            formulaCache.remove(parameter.getParticipantSelectId());
            getFormulaService().addFormulaParameters(parameter).get();
        }
    }

    public ExpressionParameter getParameter(String formulaId, String parameterCode) throws JDSException {
        ParticipantSelect participantSelect = this.getFormulaById(formulaId);
        ExpressionParameter parameter = null;
        if (participantSelect != null) {
            List<ExpressionParameter> parameters = participantSelect.getParameterList();
            for (ExpressionParameter oparameter : parameters) {
                if (oparameter.getParameterCode().equals(parameterCode)) {
                    parameter = oparameter;
                }
            }
        }
        return parameter;
    }


    public void delParameters(String parameterId, String formulaId) throws JDSException {
        formulaCache.remove(formulaId);
        getFormulaService().delParameters(parameterId).get();
    }


    public void delParticipantSelect(String participantSelectId) throws JDSException {
        formulaCache.remove(participantSelectId);
        getFormulaService().delParticipantSelect(participantSelectId).get();
    }

    public void updateParticipantSelect(ParticipantSelect participantSelect) throws JDSException {
        if (participantSelect != null) {
            if (participantSelect.getParticipantSelectId() == null) {
                String formulaId = UUID.randomUUID().toString();
                participantSelect.setParticipantSelectId(formulaId);
            }
            formulaCache.put(participantSelect.getParticipantSelectId(), participantSelect);
            this.getFormulaService().addParticipantSelect(participantSelect).get();
        }
    }


    public ParticipantSelect getFormulaById(String formulaId) throws JDSException {
        ParticipantSelect formula = formulaCache.get(formulaId);
        if (formula == null) {
            ParticipantSelect select = this.getFormulaService().getParticipantSelect(formulaId).get();
            formula = new CtParticipantSelect(select);
            formulaCache.put(formulaId, formula);
        }
        return formula;
    }

    public List<ParticipantSelect> getFormulas(FormulaType type) {
        List<ParticipantSelect> formulas = new ArrayList<>();
        for (ParticipantSelect formula : formulaCache.values()) {
            if (formula != null && formula.getFormulaType() != null && formula.getFormulaType().equals(type)) {
                formulas.add(formula);
            }
        }
        return formulas;
    }

    public ListResultModel<List<ParticipantSelect>> formulaSearch(FormulaType type, String forumla, String name) {
        ListResultModel<List<ParticipantSelect>> model = new ListResultModel();
        ListResultModel<List<ParticipantSelect>> rselects = getFormulaService().formulaSearch(type, forumla, name);
        List<ParticipantSelect> data = rselects.getData();
        List<ParticipantSelect> ctSelects = new ArrayList<>();
        for (ParticipantSelect select : data) {
            if (select != null) {
                CtParticipantSelect ctselect = new CtParticipantSelect(select);
                formulaCache.put(select.getParticipantSelectId(), select);
                ctSelects.add(ctselect);
            }
        }
        model.setSize(rselects.getSize());
        model.setData(ctSelects);
        return model;
    }

    public List<ParticipantSelect> reLoadFormulas() throws JDSException {
        List<ParticipantSelect> selects = getFormulaService().getFormulas(null).get();
        List<ParticipantSelect> ctSelects = new ArrayList<>();
        for (ParticipantSelect select : selects) {
            if (select != null) {
                select = new CtParticipantSelect(select);
                formulaCache.put(select.getParticipantSelectId(), select);
                ctSelects.add(select);
            }
        }
        return ctSelects;
    }

    public FormulaService getFormulaService() {
        FormulaService service = (FormulaService) EsbUtil.parExpression("$FormulaService");
        return service;
    }
}
