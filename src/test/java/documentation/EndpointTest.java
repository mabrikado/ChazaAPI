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

        Endpoint actual = Endpoint.fromAnnotation(endPoint, GoodController.class.getAnnotation(Chaza.class), greetMethod);

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
        List<Endpoint> endpoints = Endpoint.scan(List.of(GoodController.class, GoodController2.class));
        ObjectMapper mapper = new ObjectMapper();
        assertEquals("[{\"group\":\"basic\",\"method\":\"GET\",\"accept\":\"text/plain\",\"url\":\"/\",\"description\":" +
                "\"Greet the viewer\",\"contentType\":\"text/plain\",\"headers\":" +
                "{\"Authorization\":\"Bearer token\"},\"roles\":[\"user\",\"admin\"],\"statusCodes\":{\"200\":\"OK\",\"500\":" +
                "\"Internal error\"}},{\"group\":\"auth\",\"method\":\"POST\",\"accept\":\"application/json\",\"url\":\"/auth/login\",\"description\":" +
                "\"an endpoint to login your stuff\",\"contentType\":\"application/json\",\"request\":{\"password\":{\"type\":\"string\"},\"username\":" +
                "{\"type\":\"string\"}},\"response\":{\"status\":{\"type\":\"boolean\"}},\"headers\":{\"Authorization\":\"Bearer token\"},\"roles\":" +
                "[\"admin\",\"user\"],\"statusCodes\":{\"200\":\"OK\",\"500\":\"Internal error\",\"401\":\"unauthorised\"}}]", mapper.writeValueAsString(endpoints));
    }

    @Test
    void testScanThrows() {
        assertThrows(ChazaAPIException.class, () -> Endpoint.scan(List.of(GoodController.class, BadController.class)));
    }

    @Test
    void testFromAnnotationThrowsOnRequestDTOAndFieldsConflict() throws NoSuchMethodException {
        @Chaza(group = "conflict", baseUrl = "/conflict/")
        class ConflictController {
            @EndPoint(
                    url = "/conflict",
                    method = chazaAPI.annotations.Method.POST,
                    requestDTO = chazaAPI.DTO.Item.class,
                    requestFields = {@chazaAPI.annotations.Field(name = "foo", type = "string") }
            )
            public void conflictMethod() {}
        }

        Method method = ConflictController.class.getDeclaredMethod("conflictMethod");
        EndPoint ep = method.getAnnotation(EndPoint.class);
        Chaza chaza = ConflictController.class.getAnnotation(Chaza.class);

        assertThrows(ChazaAPIException.class, () -> Endpoint.fromAnnotation(ep, chaza, method));
    }

    @Test
    void testFromAnnotationThrowsOnResponseDTOAndFieldsConflict() throws NoSuchMethodException {
        @Chaza(group = "conflict", baseUrl = "/conflict/")
        class ConflictResponseController {
            @EndPoint(
                    url = "/conflict-response",
                    method = chazaAPI.annotations.Method.GET,
                    responseDTO = chazaAPI.DTO.Item.class,
                    responseFields = {@chazaAPI.annotations.Field(name = "foo", type = "string")}
            )
            public void conflictResponseMethod() {}
        }

        Method method = ConflictResponseController.class.getDeclaredMethod("conflictResponseMethod");
        EndPoint ep = method.getAnnotation(EndPoint.class);
        Chaza chaza = ConflictResponseController.class.getAnnotation(Chaza.class);

        ChazaAPIException ex = assertThrows(ChazaAPIException.class, () -> Endpoint.fromAnnotation(ep, chaza, method));
        assertTrue(ex.getMessage().contains("Cannot use both responseFields and responseDTO"));
    }


    @Test
    void testRecursiveRequestDTOParsing() throws Exception {
        @Chaza(group = "test", baseUrl = "/test/")
        class TestController {
            @EndPoint(
                    url = "/box",
                    method = chazaAPI.annotations.Method.POST,
                    requestDTO = chazaAPI.DTO.BoxOfItems.class
            )
            public void boxMethod() {}
        }

        Method method = TestController.class.getDeclaredMethod("boxMethod");
        EndPoint ep = method.getAnnotation(EndPoint.class);
        Chaza chaza = TestController.class.getAnnotation(Chaza.class);

        Endpoint result = Endpoint.fromAnnotation(ep, chaza, method);

        assertNotNull(result.getRequest());
        assertTrue(result.getRequest().containsKey("items"));

        Object itemsField = result.getRequest().get("items");
        assertNotNull(itemsField);

    }

    @Test
    void testFallbackToChazaDefaults() throws Exception {
        @Chaza(
                group = "fallback-group",
                baseUrl = "/fallback/",
                accept = "application/xml",
                contentType = "application/xml",
                roles = {"guest"}
        )
        class FallbackController {
            @EndPoint(
                    url = "/fallback",
                    method = chazaAPI.annotations.Method.GET
            )
            public void fallbackMethod() {}
        }

        Chaza chaza = FallbackController.class.getAnnotation(Chaza.class);
        Method method = FallbackController.class.getDeclaredMethod("fallbackMethod");
        EndPoint ep = method.getAnnotation(EndPoint.class);

        Endpoint result = Endpoint.fromAnnotation(ep, chaza, method);

        assertEquals("fallback-group", result.getGroup());
        assertEquals("application/xml", result.getAccept());
        assertEquals("application/xml", result.getContentType());
    }

    @Test
    void testScanWithNoEndpoints() throws ChazaAPIException {
        @Chaza(group = "empty", baseUrl = "/empty/")
        class EmptyController {
            public void noEndpointMethod() {}
        }

        List<Endpoint> endpoints = Endpoint.scan(List.of(EmptyController.class));
        assertTrue(endpoints.isEmpty());
    }

    @Test
    void testScanThrowsIfClassMissingChaza() {
        class MissingChazaController {
            @EndPoint(url = "/", method = chazaAPI.annotations.Method.GET)
            public void method() {}
        }

        assertThrows(ChazaAPIException.class, () -> Endpoint.scan(List.of(MissingChazaController.class)));
    }


    @Test
    void testFromAnnotationResponseDTOParsing() throws Exception {
        @Chaza(group = "response-test", baseUrl = "/response/")
        class ResponseController {
            @EndPoint(
                    url = "/response",
                    method = chazaAPI.annotations.Method.GET,
                    responseDTO = chazaAPI.DTO.Item.class
            )
            public void responseMethod() {}
        }

        Method method = ResponseController.class.getDeclaredMethod("responseMethod");
        EndPoint ep = method.getAnnotation(EndPoint.class);
        Chaza chaza = ResponseController.class.getAnnotation(Chaza.class);

        Endpoint result = Endpoint.fromAnnotation(ep, chaza, method);

        assertNotNull(result.getResponse());
        assertTrue(result.getResponse().containsKey("name"));
        assertTrue(result.getResponse().containsKey("description"));
        assertTrue(result.getResponse().containsKey("price"));
        assertTrue(result.getResponse().containsKey("category"));
    }

    @Test
    void testFromAnnotationBoxOfItemsParsing() throws Exception {
        @Chaza(group = "box-test", baseUrl = "/box/")
        class BoxController {
            @EndPoint(
                    url = "/box",
                    method = chazaAPI.annotations.Method.POST,
                    requestDTO = chazaAPI.DTO.BoxOfItems.class
            )
            public void boxMethod() {}
        }

        Method method = BoxController.class.getDeclaredMethod("boxMethod");
        EndPoint ep = method.getAnnotation(EndPoint.class);
        Chaza chaza = BoxController.class.getAnnotation(Chaza.class);

        Endpoint result = Endpoint.fromAnnotation(ep, chaza, method);

        assertNotNull(result.getRequest());

        assertTrue(result.getRequest().containsKey("name"));
        assertTrue(result.getRequest().containsKey("description"));
        assertTrue(result.getRequest().containsKey("items"));

        Object itemsField = result.getRequest().get("items");
        assertNotNull(itemsField);
        assertTrue(itemsField instanceof List);

        List<?> itemsList = (List<?>) itemsField;
        assertFalse(itemsList.isEmpty());

        Object firstItemFields = itemsList.get(0);
        assertTrue(firstItemFields instanceof Map);

        Map<?, ?> itemFields = (Map<?, ?>) firstItemFields;

        assertTrue(itemFields.containsKey("name"));
        assertTrue(itemFields.containsKey("description"));
        assertTrue(itemFields.containsKey("price"));
        assertTrue(itemFields.containsKey("category"));
    }


    @Test
    void testHeadersAreNullWhenNotProvided() throws Exception {
        @Chaza(group = "noheaders", baseUrl = "/noheaders/")
        class NoHeadersController {
            @EndPoint(
                    url = "/noheaders",
                    method = chazaAPI.annotations.Method.GET
                    // no headers defined
            )
            public void noHeadersMethod() {}
        }

        Method method = NoHeadersController.class.getDeclaredMethod("noHeadersMethod");
        EndPoint ep = method.getAnnotation(EndPoint.class);
        Chaza chaza = NoHeadersController.class.getAnnotation(Chaza.class);

        Endpoint result = Endpoint.fromAnnotation(ep, chaza, method);

        assertNotNull(result.getHeaders());
        assertTrue(result.getHeaders().isEmpty());
    }

    @Test
    void testRolesDefaultToAnyWhenNotSpecified() throws Exception {
        @Chaza(group = "rolesdefault", baseUrl = "/rolesdefault/")
        class RolesDefaultController {
            @EndPoint(
                    url = "/rolesdefault",
                    method = chazaAPI.annotations.Method.GET
                    // no roles specified
            )
            public void rolesDefaultMethod() {}
        }

        Method method = RolesDefaultController.class.getDeclaredMethod("rolesDefaultMethod");
        EndPoint ep = method.getAnnotation(EndPoint.class);
        Chaza chaza = RolesDefaultController.class.getAnnotation(Chaza.class);

        Endpoint result = Endpoint.fromAnnotation(ep, chaza, method);

        assertEquals(List.of("any"), result.getRoles());
    }

    @Test
    void testContentTypeAndAcceptPrecedence() throws Exception {
        @Chaza(
                group = "precedence",
                baseUrl = "/precedence/",
                accept = "application/xml",
                contentType = "application/xml"
        )
        class PrecedenceController {
            @EndPoint(
                    url = "/precedence",
                    method = chazaAPI.annotations.Method.POST,
                    accept = "application/json",
                    contentType = "application/json"
            )
            public void precedenceMethod() {}
        }

        Method method = PrecedenceController.class.getDeclaredMethod("precedenceMethod");
        EndPoint ep = method.getAnnotation(EndPoint.class);
        Chaza chaza = PrecedenceController.class.getAnnotation(Chaza.class);

        Endpoint result = Endpoint.fromAnnotation(ep, chaza, method);

        assertEquals("application/json", result.getAccept());
        assertEquals("application/json", result.getContentType());
    }

    @Test
    void testEmptyStatusCodesResultsInEmptyMap() throws Exception {
        @Chaza(group = "nostatus", baseUrl = "/nostatus/")
        class NoStatusController {
            @EndPoint(
                    url = "/nostatus",
                    method = chazaAPI.annotations.Method.GET
            )
            public void noStatusMethod() {}
        }

        Method method = NoStatusController.class.getDeclaredMethod("noStatusMethod");
        EndPoint ep = method.getAnnotation(EndPoint.class);
        Chaza chaza = NoStatusController.class.getAnnotation(Chaza.class);

        Endpoint result = Endpoint.fromAnnotation(ep, chaza, method);

        assertNotNull(result.getStatusCodes());
        assertTrue(result.getStatusCodes().isEmpty());
    }
}
