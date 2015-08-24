package com.buildria.restmock.stub;

import com.google.common.net.HttpHeaders;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

public class Call {

    private String uri;

    private String method;

    private String contentType;

    private String accept;

    private final Map<String, String> headers = new HashMap<>();

    private byte[] body;

    private Call() {
        //
    }

    public static Call fromRequest(HttpRequest req) {
        Call call = new Call();
        call.uri = req.getUri();
        call.method = req.getMethod().name();
        call.contentType = req.headers().get(HttpHeaders.CONTENT_TYPE);
        call.accept = req.headers().get(HttpHeaders.ACCEPT);
        for (Map.Entry<String, String> entry : req.headers().entries()) {
            String key = entry.getKey();
            if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(key)
                    || HttpHeaders.ACCEPT.equalsIgnoreCase(key)) {
                continue;
            }
            call.headers.put(entry.getKey(), entry.getValue());
        }
        if (req instanceof HttpContent) {
            ByteBuf buf = ((HttpContent) req).content();
            if (buf != null) {
                call.body = buf.array();
            }
        }
        
        return call;
    }

    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "Call{" + "uri=" + uri + ", method=" + method +
                ", contentType=" + contentType + ", accept=" + accept +
                ", headers=" + headers + ", body=" + body + '}';
    }


}
