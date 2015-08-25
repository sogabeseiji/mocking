package com.buildria.restmock.serialize;

import java.io.IOException;

public interface ObjectSerializer {

    String seriaize(ObjectSerializeContext ctx) throws IOException;

}
