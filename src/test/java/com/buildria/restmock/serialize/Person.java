package com.buildria.restmock.serialize;

public class Person {

    private String name;

    private int old;

    public Person() {
        //
    }

    public Person(String name, int old) {
        this.name = name;
        this.old = old;
    }

    public String getName() {
        return name;
    }

    public int getOld() {
        return old;
    }

}
