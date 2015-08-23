package com.buildria.restmock.builder;

/**
 *
 * @author sogabe
 */
public interface Function<I, O> {

    O apply(I input);
}
