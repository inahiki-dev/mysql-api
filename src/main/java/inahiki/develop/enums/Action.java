package inahiki.develop.enums;

public enum Action {

    RESTRICT("RESTRICT"),
    CASCADE("CASCADE"),
    SET_NULL("SET NULL"),
    NO_ACTION("NO ACTION"),
    SET_DEFAULT("SET DEFAULT");

    private final String option;

    Action(String option) {
        this.option = option;
    }

    public String option() {
        return option;
    }
}
