package documentation;

import exceptions.ChazaAPIException;
import org.junit.jupiter.api.Test;
import testlogic.GoodController;
import testlogic.GoodController2;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class APIDocTest {


    @Test
    void toJsonString() throws ChazaAPIException {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setTitle("Good API");
        apiInfo.setApiVersion("1.0.0");
        apiInfo.setDescription("A good description");
        apiInfo.setTermsOfService("https://example.com/tos");
        HashMap<String, Object> contact = new HashMap<>();
        contact.put("name", "API Support");
        contact.put("url", "https://www.example.com/support");
        contact.put("email", "support@example.com");
        apiInfo.setContact(contact);

        HashMap<String, Object> license = new HashMap<>();
        license.put("name", "Apache 2.0");
        license.put("url", "https://www.apache.org/licenses/LICENSE-2.0");
        apiInfo.setLicense(license);

        //find all endpoints
        List<Endpoint> endpoints = Endpoint.scan(List.of(GoodController.class , GoodController2.class));
        APIDoc doc = new APIDoc(apiInfo, endpoints);
        assertEquals("{\"apiInfo\":{\"title\":\"Good API\",\"description\":\"A good description\",\"termsOfService\":" +
                "\"https://example.com/tos\",\"contact\":{\"name\":\"API Support\",\"url\":\"https://www.example.com/support\",\"email\":" +
                "\"support@example.com\"},\"license\":{\"name\":\"Apache 2.0\",\"url\":\"https://www.apache.org/licenses/LICENSE-2.0\"}," +
                "\"version\":\"1.0.0\"},\"endpoints\":[{\"group\":\"basic\",\"method\":\"GET\",\"url\":\"/\",\"description\":\"Greet the viewer\"," +
                "\"contentType\":\"text/plain\",\"headers\":{\"Authorization\":\"Bearer token\"},\"roles\":[\"admin\",\"user\"],\"statusCodes\"" +
                ":{\"200\":\"OK\",\"500\":\"Internal error\"}},{\"group\":\"auth\",\"method\":\"POST\",\"url\":\"auth/login\",\"description\"" +
                ":\"an endpoint to login your stuff\",\"contentType\":\"application/json\",\"request\":{\"password\":{\"type\":\"int\"},\"username\"" +
                ":{\"type\":\"string\"}},\"response\":{\"status\":{\"type\":\"boolean\"}},\"headers\":{\"Authorization\":\"Bearer token\"},\"roles\"" +
                ":[\"admin\",\"user\"],\"statusCodes\":{\"200\":\"OK\",\"500\":\"Internal error\",\"401\":\"unauthorised\"}}]}" , doc.toJsonString());
        assertEquals("{\n" +
                "  \"apiInfo\" : {\n" +
                "    \"title\" : \"Good API\",\n" +
                "    \"description\" : \"A good description\",\n" +
                "    \"termsOfService\" : \"https://example.com/tos\",\n" +
                "    \"contact\" : {\n" +
                "      \"name\" : \"API Support\",\n" +
                "      \"url\" : \"https://www.example.com/support\",\n" +
                "      \"email\" : \"support@example.com\"\n" +
                "    },\n" +
                "    \"license\" : {\n" +
                "      \"name\" : \"Apache 2.0\",\n" +
                "      \"url\" : \"https://www.apache.org/licenses/LICENSE-2.0\"\n" +
                "    },\n" +
                "    \"version\" : \"1.0.0\"\n" +
                "  },\n" +
                "  \"endpoints\" : [ {\n" +
                "    \"group\" : \"basic\",\n" +
                "    \"method\" : \"GET\",\n" +
                "    \"url\" : \"/\",\n" +
                "    \"description\" : \"Greet the viewer\",\n" +
                "    \"contentType\" : \"text/plain\",\n" +
                "    \"headers\" : {\n" +
                "      \"Authorization\" : \"Bearer token\"\n" +
                "    },\n" +
                "    \"roles\" : [ \"admin\", \"user\" ],\n" +
                "    \"statusCodes\" : {\n" +
                "      \"200\" : \"OK\",\n" +
                "      \"500\" : \"Internal error\"\n" +
                "    }\n" +
                "  }, {\n" +
                "    \"group\" : \"auth\",\n" +
                "    \"method\" : \"POST\",\n" +
                "    \"url\" : \"auth/login\",\n" +
                "    \"description\" : \"an endpoint to login your stuff\",\n" +
                "    \"contentType\" : \"application/json\",\n" +
                "    \"request\" : {\n" +
                "      \"password\" : {\n" +
                "        \"type\" : \"int\"\n" +
                "      },\n" +
                "      \"username\" : {\n" +
                "        \"type\" : \"string\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"response\" : {\n" +
                "      \"status\" : {\n" +
                "        \"type\" : \"boolean\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"headers\" : {\n" +
                "      \"Authorization\" : \"Bearer token\"\n" +
                "    },\n" +
                "    \"roles\" : [ \"admin\", \"user\" ],\n" +
                "    \"statusCodes\" : {\n" +
                "      \"200\" : \"OK\",\n" +
                "      \"500\" : \"Internal error\",\n" +
                "      \"401\" : \"unauthorised\"\n" +
                "    }\n" +
                "  } ]\n" +
                "}" , doc.toPrettyJsonString());
    }

    @Test
    void validateError() {
        Endpoint endpoint = new Endpoint();
        endpoint.setHeaders(null);
        assertThrows(ChazaAPIException.class , ()-> APIDoc.validate(endpoint));
    }
}