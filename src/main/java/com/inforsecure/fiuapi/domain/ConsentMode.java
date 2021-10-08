package com.inforsecure.fiuapi.domain;

import java.util.HashMap;

public enum ConsentMode {
    VIEW("VIEW"),
    STORE("STORE"),
    QUERY("QUERY"),
    STREAM("STREAM");

    private final String consentMode;

    ConsentMode(String s) {
        consentMode = s;
    }

    public String toString() {
        return this.consentMode;
    }

    private static final HashMap<String, ConsentMode> map = new HashMap<>(values().length, 1);

    static {
        for (ConsentMode c : values()) map.put(c.consentMode, c);
    }

    public static ConsentMode of(String name){
        return map.get(name);
    }
}
