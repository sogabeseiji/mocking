/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Seiji Sogabe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.buildria.mocking;

import com.buildria.mocking.builder.action.ActionSpec;
import com.buildria.mocking.builder.rule.RuleSpec;
import com.buildria.mocking.stub.StubHttpServer;
import java.io.IOException;
import java.net.ServerSocket;
import javax.annotation.Nonnull;
import org.junit.rules.ExternalResource;

public class Mocking extends ExternalResource {

    private static final int PORT_MIN = 0;

    private static final int PORT_MAX = 65535;

    private StubHttpServer server;

    private final int port;

    private final boolean logging;

    public Mocking() {
        this(PORT_MIN, true);
    }

    public Mocking(boolean logging) {
        this(PORT_MIN, logging);
    }

    public Mocking(int port, boolean logging) {
        if (port < PORT_MIN || port > PORT_MAX) {
            throw new IllegalArgumentException("port should be between 0 and 65535");
        }
        if (port == PORT_MIN) {
            port = availablePort();
        }
        this.port = port;
        this.logging = logging;
    }

    // CHECKSTYLE:OFF
    @Override
    protected void before() throws Throwable {
        // CHECKSTYLE:ON
        super.before();
        server = new StubHttpServer(this).run();
    }

    @Override
    protected void after() {
        if (server != null) {
            server.stop();
            server = null;
        }
        super.after();
    }

    private int availablePort() {
        int availablePort;
        try (ServerSocket socket = new ServerSocket(0)) {
            availablePort = socket.getLocalPort();
        } catch (IOException e) {
            throw new MockingException("faield to assign a port.", e);
        }
        return availablePort;
    }

    public int getPort() {
        return port;
    }

    public boolean isLogging() {
        return logging;
    }

    @Nonnull
    public void $(ActionSpec spec) {
        server.addActions(spec.getActions());
    }

    @Nonnull
    public void $(RuleSpec spec) {
        spec.validate(server.getCalls());
    }
}
