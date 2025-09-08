package documentation;

import chazaAPI.documentation.ApiInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiInfoTest {

    @Test
    void TestJsonString() throws JsonProcessingException {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setApiVersion("1");
        apiInfo.setDescription("A good description");
        apiInfo.setTitle("A good title");
        apiInfo.setTermsOfService("A good terms");
        apiInfo.setLicense(new HashMap<>(Map.of("row1" , "Some Licence")));
        apiInfo.setContact(new HashMap<>(Map.of("row1" , "Some Contact")));

        ObjectMapper mapper = new ObjectMapper();
        assertEquals("{\"title\":\"A good title\",\"description\":" +
                "\"A good description\",\"termsOfService\":" +
                "\"A good terms\",\"contact\":" +
                "{\"row1\":\"Some Contact\"},\"license\":{\"row1\":\"Some Licence\"},\"version\":\"1\"}" ,
                mapper.writeValueAsString(apiInfo));
    }

}