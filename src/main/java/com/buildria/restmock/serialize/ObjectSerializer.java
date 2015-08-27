package com.buildria.restmock.serialize;

import java.io.IOException;

public interface ObjectSerializer {

    String serialize(ObjectSerializeContext ctx) throws IOException;

}
