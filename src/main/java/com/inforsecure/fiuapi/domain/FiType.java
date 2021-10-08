package com.inforsecure.fiuapi.domain;

import java.util.HashMap;

public enum FiType {
    // APNB
    DEPOSIT("DEPOSIT"),
    TERM_DEPOSIT("TERM_DEPOSIT"),
    RECURRING_DEPOSIT("RECURRING_DEPOSIT"),
    CREDIT_CARD("CREDIT_CARD"),
    CD("CD"),
    IDR("IDR"),
    // APNI
    INSURANCE_POLICIES("INSURANCE_POLICIES"),
    ULIP("ULIP"),
    // APPF
    EPF("EPF"),
    PPF("PPF"),
    // APMF
    BONDS("BONDS"),
    MUTUAL_FUNDS("MUTUAL_FUNDS"),
    DEBENTURES("DEBENTURES"),
    ETF("ETF"),
    NPS("NPS"),
    GOVT_SECURITIES("GOVT_SECURITIES"),
    CP("CP"),
    REIT("REIT"),
    INVIT("INVIT"),
    AIF("AIF");
    private final String fiType;

    FiType(String s) {
        fiType = s;
    }

    public String toString() {
        return this.fiType;
    }

    public static final HashMap<String, FiType> map = new HashMap<>(values().length, 1);

    public static FiType of(String name){
        return map.get(name);
    }
}