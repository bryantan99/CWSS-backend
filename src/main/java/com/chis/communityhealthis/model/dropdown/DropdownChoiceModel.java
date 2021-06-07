package com.chis.communityhealthis.model.dropdown;

public class DropdownChoiceModel {
    private String text;
    private String value;

    public DropdownChoiceModel() {
    }

    public DropdownChoiceModel(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
