package documentation;


import annotations.EndPoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.ChazaAPIException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testlogic.BadController;
import testlogic.GoodController;
import testlogic.GoodController2;

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
        expected.setMethod(annotations.Method.GET);
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

        Endpoint actual = Endpoint.fromAnnotation(endPoint);

        expected.setGroup("basic");
        expected.setMethod(annotations.Method.GET);
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

        assertEquals(expected, actual);
    }

    @Test
    void testScan() throws JsonProcessingException, ChazaAPIException {
        List<Endpoint> endpoints = Endpoint.scan(List.of(GoodController.class , GoodController2.class));
        ObjectMapper mapper = new ObjectMapper();
        assertEquals("[{\"group\":\"basic\",\"method\":\"GET\",\"url\":\"/\",\"description\":\"Greet the viewer\"," +
                "\"contentType\":\"text/plain\",\"headers\":{\"Authorization\":\"Bearer token\"},\"roles\":[\"admin\",\"user\"]," +
                "\"statusCodes\":{\"200\":\"OK\",\"500\":\"Internal error\"}},{\"group\":\"auth\",\"method\":\"POST\",\"url\":\"auth/login\"," +
                "\"description\":\"an endpoint to login your stuff\",\"contentType\":\"application/json\",\"request\":{\"password\":" +
                "{\"type\":\"int\"},\"username\":{\"type\":\"string\"}},\"response\":{\"status\":{\"type\":\"boolean\"}},\"headers\":" +
                "{\"Authorization\":\"Bearer token\"},\"roles\":[\"admin\",\"user\"],\"statusCodes\":{\"200\":\"OK\",\"500\":" +
                "\"Internal error\",\"401\":\"unauthorised\"}}]" , mapper.writeValueAsString(endpoints));
    }

    @Test
    void testScanThrows () {
        assertThrows(ChazaAPIException.class , ()-> Endpoint.scan(List.of(GoodController.class , BadController.class)));
    }
}