package com.inforsecure.fiuapi.domain;

import java.util.HashMap;

public enum DataLifeUnit {
    MONTH("MONTH"),
    YEAR("YEAR"),
    DAY("DAY"),
    INF("INF");

    private final String dataLifeUnit;

    DataLifeUnit(String s) {
        dataLifeUnit = s;
    }

    public String toString() {
        return this.dataLifeUnit;
    }

    private static final HashMap<String, DataLifeUnit> map = new HashMap<>(values().length, 1);

    static {
        for (DataLifeUnit c : values()) map.put(c.dataLifeUnit, c);
    }

    public static DataLifeUnit of(String name){
        return map.get(name);
    }
}
