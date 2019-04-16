package com.efida.esearch.model;

public class AppQueryTplDetail extends AppQueryTplDetailKey {
    private boolean isSimpleCondition;

    private String logic;

    private String condition;

    public boolean getIsSimpleCondition() {
        return isSimpleCondition;
    }

    public void setIsSimpleCondition(boolean isSimpleCondition) {
        this.isSimpleCondition = isSimpleCondition;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic == null ? null : logic.trim();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition == null ? null : condition.trim();
    }
}