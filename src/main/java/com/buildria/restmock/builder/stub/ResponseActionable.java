package com.buildria.restmock.builder.stub;

import io.netty.handler.codec.http.HttpResponse;

/**
 *
 * @author sogabe
 */
public interface ResponseActionable {

    HttpResponse apply(HttpResponse input);
}
