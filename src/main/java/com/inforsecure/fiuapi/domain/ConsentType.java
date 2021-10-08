package com.inforsecure.fiuapi.domain;

import java.util.HashMap;

public enum ConsentType {
    TRANSACTIONS ("TRANSACTIONS"),
    PROFILE ("PROFILE"),
    SUMMARY ("SUMMARY");

    private final String consentType;

    ConsentType(String s) {
        consentType = s;
    }

    public String toString() {
        return this.consentType;
    }

    private static final HashMap<String, ConsentType> map = new HashMap<>(values().length, 1);

    static {
        for (ConsentType c : values()) map.put(c.consentType, c);
    }

    public static ConsentType of(String name){
        return map.get(name);
    }
}
