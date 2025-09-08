package documentation;

import chazaAPI.documentation.APIDisplay;
import chazaAPI.documentation.ApiInfo;
import chazaAPI.exceptions.ChazaAPIException;
import io.javalin.Javalin;
import org.junit.jupiter.api.*;
import chazaAPI.testlogic.GoodController;
import chazaAPI.testlogic.GoodController2;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class APIDisplayTest {

    private static final String JSON_OUTPUT_PATH = "doc/api-doc.json";

    @AfterEach
    void cleanup() {
        File file = new File(JSON_OUTPUT_PATH);
        if (file.exists()) file.delete();
    }

    @Test
    void testSetApiInfoStoresDataCorrectly() {
        ApiInfo info = ApiInfo.getInstance().setTitle("Test Title");
        APIDisplay display = APIDisplay.getInstance().setApiInfo(info);

        assertNotNull(display.getApiInfo());
        assertEquals("Test Title", display.getApiInfo().getTitle());
    }

    @Test
    void testScanEndpointsLoadsEndpoints() throws ChazaAPIException {
        APIDisplay display = APIDisplay.getInstance();
        display.scanEndPoints(List.of(GoodController.class, GoodController2.class));

        assertNotNull(display.getEndpoints());
        assertFalse(display.getEndpoints().isEmpty(), "Expected endpoints to be found");
    }

    @Test
    void testHostToServerBindsEndpoints() throws ChazaAPIException {
        Javalin server = Javalin.create();

        APIDisplay display = APIDisplay.getInstance()
                .setApiInfo(ApiInfo.getInstance().setTitle("API"))
                .scanEndPoints(List.of(GoodController.class))
                .generateDocumentation();

        assertDoesNotThrow(() -> display.hostToServer(server));
    }

    @Test
    void testHostToServerThrowsOnNullServer() {
        APIDisplay display = APIDisplay.getInstance();
        Exception ex = assertThrows(ChazaAPIException.class, () -> display.hostToServer(null));
        assertEquals("Server instance cannot be null", ex.getMessage());
    }

}
