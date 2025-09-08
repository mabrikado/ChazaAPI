package documentation;


import chazaAPI.annotations.Chaza;
import chazaAPI.annotations.EndPoint;
import chazaAPI.documentation.Endpoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import chazaAPI.exceptions.ChazaAPIException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import chazaAPI.testlogic.BadController;
import chazaAPI.testlogic.GoodController;
import chazaAPI.testlogic.GoodController2;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EndpointTest {

    private static Endpoint expected;

    @BeforeAll
    static void init() {
        expected = new Endpoint();
        expected.setGroup("basic");
        expected.setMethod(chazaAPI.annotations.Method.GET);
        expected.setUrl("/");
        expected.setDescription("Greet the viewer");

        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Bearer token");
        expected.setHeaders(headers);

        Map<String, String> statusCodes = new HashMap<>();
        statusCodes.put("200", "OK");
        statusCodes.put("500", "Internal error");
        expected.setStatusCodes(statusCodes);

        expected.setRoles(List.of("admin", "user"));
    }

    @Test
    void testFromAnnotation() throws Exception {
        Method greetMethod = GoodController.class.getDeclaredMethod("greet");

        EndPoint endPoint = greetMethod.getAnnotation(EndPoint.class);
        assertNotNull(endPoint, "Expected @EndpointDoc to be present on method");

        Endpoint actual = Endpoint.fromAnnotation(endPoint ,  GoodController.class.getAnnotation(Chaza.class));

        expected.setAccept("text/plain");
        expected.setContentType("text/plain");
        expected.setGroup("basic");
        expected.setMethod(chazaAPI.annotations.Method.GET);
        expected.setUrl("/");
        expected.setDescription("Greet the viewer");

        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Bearer token");
        expected.setHeaders(headers);

        Map<String, String> statusCodes = new HashMap<>();
        statusCodes.put("200", "OK");
        statusCodes.put("500", "Internal error");
        expected.setStatusCodes(statusCodes);

        expected.setRoles(List.of("user", "admin"));
        assertEquals(expected, actual);
    }

    @Test
    void testScan() throws JsonProcessingException, ChazaAPIException {
        List<Endpoint> endpoints = Endpoint.scan(List.of(GoodController.class , GoodController2.class));
        ObjectMapper mapper = new ObjectMapper();
        assertEquals("[{\"group\":\"basic\",\"method\":\"GET\",\"accept\":\"text/plain\",\"url\":\"/\",\"description\":" +
                "\"Greet the viewer\",\"contentType\":\"text/plain\",\"headers\":" +
                "{\"Authorization\":\"Bearer token\"},\"roles\":[\"user\",\"admin\"],\"statusCodes\":{\"200\":\"OK\",\"500\":" +
                "\"Internal error\"}},{\"group\":\"auth\",\"method\":\"POST\",\"accept\":\"application/json\",\"url\":\"/auth/login\",\"description\":" +
                "\"an endpoint to login your stuff\",\"contentType\":\"application/json\",\"request\":{\"password\":{\"type\":\"string\"},\"username\":" +
                "{\"type\":\"string\"}},\"response\":{\"status\":{\"type\":\"boolean\"}},\"headers\":{\"Authorization\":\"Bearer token\"},\"roles\":" +
                "[\"admin\",\"user\"],\"statusCodes\":{\"200\":\"OK\",\"500\":\"Internal error\",\"401\":\"unauthorised\"}}]" , mapper.writeValueAsString(endpoints));
    }

    @Test
    void testScanThrows () {
        assertThrows(ChazaAPIException.class , ()-> Endpoint.scan(List.of(GoodController.class , BadController.class)));
    }
}