package com.inforsecure.fiuapi.domain;

import java.util.HashMap;

public enum FiType {
    // APNB
    DEPOSIT("DEPOSIT"),
    TERM_DEPOSIT("TERM_DEPOSIT"),
    CREDIT_CARD("CREDIT_CARD"),
    // APNI
    INSURANCE_POLICIES("INSURANCE_POLICIES"),
    // APPF
    EPF("EPF"),
    PPF("PPF"),
    // APMF
    BONDS("BONDS"),
    MUTUAL_FUNDS("MUTUAL_FUNDS"),
    SIP("SIP"),
    EQUITIES("EQUITIES");
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