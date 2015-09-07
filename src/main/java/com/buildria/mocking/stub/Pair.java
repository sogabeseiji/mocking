package com.buildria.mocking.stub;

public class Pair {

    private final String name;

    private final String value;

    public Pair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
