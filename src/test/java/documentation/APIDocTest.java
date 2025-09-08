package documentation;

import chazaAPI.documentation.APIDoc;
import chazaAPI.documentation.ApiInfo;
import chazaAPI.documentation.Endpoint;
import chazaAPI.exceptions.ChazaAPIException;
import org.junit.jupiter.api.Test;
import chazaAPI.testlogic.GoodController;
import chazaAPI.testlogic.GoodController2;

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
                "\"version\":\"1.0.0\"},\"endpoints\":[{\"group\":\"basic\",\"method\":\"GET\",\"accept\":\"text/plain\",\"url\":\"/\",\"description\":" +
                "\"Greet the viewer\",\"contentType\":\"text/plain\",\"headers\":{\"Authorization\":\"Bearer token\"},\"roles\":[\"user\",\"admin\"]," +
                "\"statusCodes\":{\"200\":\"OK\",\"500\":\"Internal error\"}},{\"group\":\"auth\",\"method\":\"POST\",\"accept\":\"application/json\"," +
                "\"url\":\"/auth/login\",\"description\":\"an endpoint to login your stuff\",\"contentType\":\"application/json\",\"request\"" +
                ":{\"password\":{\"type\":\"string\"},\"username\":{\"type\":\"string\"}},\"response\":{\"status\":{\"type\":\"boolean\"}}," +
                "\"headers\":{\"Authorization\":\"Bearer token\"},\"roles\":[\"admin\",\"user\"],\"statusCodes\":{\"200\":\"OK\",\"500\":\"Internal error\"," +
                "\"401\":\"unauthorised\"}}]}" , doc.toJsonString());
    }

    @Test
    void validateError() {
        Endpoint endpoint = new Endpoint();
        endpoint.setHeaders(null);
        assertThrows(ChazaAPIException.class , ()-> APIDoc.validate(endpoint));
    }
}