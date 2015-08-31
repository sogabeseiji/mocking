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
    public Restmock restmock = new Restmock(PORT);

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

         // Restmock
        restmock.
                when(
                    path("/api/p").
                then().
                    statusCode(SC_200_OK).
                    contentType("application/json").
                    body(person)
        );

        // Rest-assured
        given().
                log().all().
                accept("application/json").
        when().
                get("/api/p").
        then().
                log().all().
                statusCode(SC_200_OK).
                contentType("application/json").
                body("name", is("Bob")).
                body("old", is(20));

        // Restmock
        restmock.
                verify(
                    get("/api/p").
                    accept("application/json")
        );
    }

(snip)
    
}
```
