package com.chis.communityhealthis.model.dropdown;

public class DropdownChoiceModel<T> {
    private String text;
    private T value;

    public DropdownChoiceModel() {
    }

    public DropdownChoiceModel(T value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
