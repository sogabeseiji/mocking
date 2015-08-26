package com.buildria.restmock.serialize;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "person")
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

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public int getOld() {
        return old;
    }

}
