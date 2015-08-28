package com.buildria.restmock;

public interface Predicate<T> {

    boolean apply(T input);

}
