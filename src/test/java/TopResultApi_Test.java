import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.*;
import org.json.JSONObject;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class TopResultApi_Test {
    private int MAX_TEST_VALUE;
    private int maxCount;
    private String Username, Passwd;

    @DataProvider(name = "DataForApi")
    public Object[][] dataForApi() {
        return new Object[][] {{2}, {3}};
//
//        Object[][] data = new Object{{1,2},{3,4}};
//        return data;
    }

    @Parameters({"maxTestVal", "uname", "pwd","baseUrl"})
    @BeforeTest
    public void BefTestMethod(String testValue, String uname, String pwd, String baseUrl) {
        RestAssured.baseURI = baseUrl;
        this.Username = uname;
        this.Passwd = pwd;

        MAX_TEST_VALUE = Integer.parseInt(testValue);
        System.out.println("Test Value Received from Runner XML is -" + testValue);

        Response rsp = getApiResponse(uname, pwd, MAX_TEST_VALUE);
        String jsonRespAsString = rsp.asString();

        System.out.println("Response Body is -" + jsonRespAsString);
        JSONObject jo = new JSONObject(jsonRespAsString);
        System.out.println("Response Body Size -" + jo.length());
        maxCount = jo.length();
    }

    @Test(dataProvider = "DataForApi")
    public void GetTopResultsApi_Test(int count) {

        Response response = given().auth()
                .preemptive()
                .basic(this.Username, this.Passwd)
                .header("Content-Type", "application/json")
                .get("/getTopResultsApi?count=" + count)
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", is(count))
                .extract().response();

    }

    @Test
    public void E_401_getAPITest_InCorrectCredentials()
    {
        given().auth()
                .preemptive()
                .basic(this.Username, "")
                .header("Content-Type", "application/json")
                .get("/getTopResultsApi?count=" + MAX_TEST_VALUE)
                .then()
                .assertThat()
                .statusCode(401)
                .body("error",equalTo("Unauthorized"))
                .body("message",equalTo("Bad credentials"))
                .body(matchesJsonSchemaInClasspath("401-error-schema.json"))
                .extract().response();
    }

    @Test
    public void E_500_getAPITest_NegCount()
    {
        int count = -MAX_TEST_VALUE;
        given().auth()
                .preemptive()
                .basic(this.Username, this.Passwd)
                .header("Content-Type", "application/json")
                .get("/getTopResultsApi?count=" + count)
                .then()
                .assertThat()
                .statusCode(500)
                .body("error",equalTo("Internal Server Error"))
                .body("message",equalTo(String.valueOf(count)))
                .body(matchesJsonSchemaInClasspath("500-error-schema.json"))
                .extract().response();
    }

    @Test
    public void validateSchema() {
        get("https://jsonplaceholder.typicode.com/todos/1").then()
                .assertThat().body(matchesJsonSchemaInClasspath("products-schema.json"));
    }

    private Response getApiResponse(String uname, String pwd, int count) {
        Response rsp = given().auth()
                .preemptive()
                .basic(uname, pwd)
                .header("Content-Type", "application/json")
                .get("/getTopResultsApi?count=" + count)
                .then()
                .contentType(ContentType.JSON)
                .extract().response();

        return rsp;
    }

}
