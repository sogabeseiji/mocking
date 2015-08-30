# Restmock

[![Build Status](http://ci.buildria.com/job/restmock/badge/icon)](http://ci.buildria.com/job/restmock/)

Restmock is a test framework  which is inspired by [Restito](https://github.com/mkotsur/restito).

Restmock provides a DSL to:

 * Mimic rest server behavior
 * Record HTTP calls to the server
 * Perform verification against happened calls (TODO)


## Quick example


``` java
public class SampleCodeTest {

    private StubHttpServer server;

    private static final int PORT = 8888;

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Before
    public void setUp() throws Exception {
        // ポート番号
        RestAssured.port = PORT;
        server = new StubHttpServer(PORT).run();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testReadMeSampleCode() {
        Person person = new Person("Bob", 20);

        // Restmock
        when(server).
                path("/api/p").
        then().
                statusCode(HttpStatus.SC_200_OK).
                contentType("application/json").
                body(person);

        // Rest-assured
        given().
                log().all().
                accept("application/json").
        when().
                get("/api/p").
        then().
                log().all().
                statusCode(200).
                contentType("application/json").
                body("name", is("Bob")).
                body("old", is(20));

        // Restmock
        verify(server).
                get("/api/p").
                accept("application/json");
    }

(snip)
}
```
