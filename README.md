# Mocking

[![Build Status](http://ci.buildria.com/job/mocking/badge/icon)](http://ci.buildria.com/job/mocking/)

Mocking is a test framework  which is inspired by [Restito](https://github.com/mkotsur/restito).

Mocking provides a DSL to:

 * Mimic rest server behavior
 * Record HTTP calls to the server
 * Perform verification against happened calls 
 * Automatic serialization

## Quick example


``` java
public class SampleCodeTest {

    private static final int PORT = 8888;

    @Rule
    public Mocking mocking = new Mocking(PORT, true);

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Before
    public void setUp() throws Exception {
        RestAssured.port = mocking.getPort();
    }

    @Test
    public void testReadMeSampleCode() {
        Person person = new Person("Bob", 20);

        // Mocking
        mocking.$(
                when("/api/p/{id}").
                        withPathParam("id", 5).
                then().
                        withStatusCode(SC_201_CREATED).
                        withContentType("application/json; charset=UTF-8").
                        withBody(person)
        );

        // Rest-assured
        given().
                pathParam("id", 5).
                accept("application/json").
                contentType("application/json; charset=UTF-8").
                body(person).
        when().
                put("/api/p/{id}").
        then().
                statusCode(SC_201_CREATED).
                contentType("application/json; charset=UTF-8").
                body("name", is("Bob")).
                body("old", is(20));

        // Mocking
        mocking.$(
                put("/api/p/{id}").
                        withPathParam("id", 5).
                then().
                        withAccept("application/json").
                        withContentType("application/json; charset=UTF-8").
                        withBody("name", is("Bob")).
                        withBody("old", is(20)).
                        withoutHeader("Referer")
        );
    }
    
(snip)
    
}
```
