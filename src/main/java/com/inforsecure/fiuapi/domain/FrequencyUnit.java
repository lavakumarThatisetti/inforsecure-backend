package com.inforsecure.fiuapi.domain;

import java.util.HashMap;

public enum FrequencyUnit {
    HOUR("HOUR"),
    MONTH("MONTH"),
    YEAR("YEAR"),
    DAY("DAY"),
    INF("INF");

    private final String frequencyUnit;

    FrequencyUnit(String s) {
        frequencyUnit = s;
    }

    public String toString() {
        return this.frequencyUnit;
    }

    private static final HashMap<String, FrequencyUnit> map = new HashMap<>(values().length, 1);

    static {
        for (FrequencyUnit c : values()) map.put(c.frequencyUnit, c);
    }

    public static FrequencyUnit of(String name){
        return map.get(name);
    }
}
