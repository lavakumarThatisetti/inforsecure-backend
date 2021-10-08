package com.inforsecure.fiuapi.domain;

import java.util.HashMap;

public enum FetchType {
    ONETIME("ONETIME"),
    PERIODIC("PERIODIC");

    private final String fetchType;

    FetchType(String s) {
        fetchType = s;
    }

    public String toString() {
        return this.fetchType;
    }

    private static final HashMap<String, FetchType> map = new HashMap<>(values().length, 1);

    static {
        for (FetchType c : values()) map.put(c.fetchType, c);
    }

    public static FetchType of(String name){
        return map.get(name);
    }
}
