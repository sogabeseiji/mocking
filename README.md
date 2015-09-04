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

    private static final int PORT = 8888;

    @Rule
    public RestMock restmock = new RestMock(PORT);

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Before
    public void setUp() throws Exception {
        // ポート番号
        RestAssured.port = PORT;
    }

    @Test
    public void testReadMeSampleCode() {
        Person person = new Person("Bob", 20);

        // RestMock
        restmock.$(
                    when("/api/p").
                    then().
                        statusCode(SC_201_CREATED).
                        contentType("application/json").
                        body(person)
        );

        // Rest-assured
        given().
                log().all().
                accept("application/json").
                contentType("application/json").
                body(person).
        when().
                post("/api/p").
        then().
                log().all().
                statusCode(SC_201_CREATED).
                contentType("application/json").
                body("name", is("Bob")).
                body("old", is(20));

        // RestMock
        restmock.$(
                    post("/api/p").
                    accept("application/json").
                    contentType("application/json; charset=ISO-8859-1").
                    body("name", is("Bob")).
                    body("old", is(20))
        );
    }

(snip)
    
}
```
