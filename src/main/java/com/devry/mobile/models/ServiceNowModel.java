package com.devry.mobile.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceNowModel {

    private String value;

    public ServiceNowModel(String value) {
    	this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ServiceNowModel{" +
                ", value=" + value +
                '}';
    }
}