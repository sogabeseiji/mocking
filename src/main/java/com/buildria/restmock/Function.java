package com.buildria.restmock;

/**
 *
 * @author sogabe
 */
public interface Function<I, O> {

    O apply(I input);
}
