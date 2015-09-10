package com.buildria.mocking.serializer;

import java.io.IOException;
import javax.annotation.Nonnull;

public interface ObjectSerializer {

    byte[] serialize(@Nonnull Object obj) throws IOException;

}
