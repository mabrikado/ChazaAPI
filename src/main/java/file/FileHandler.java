package file;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class FileHandler {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void writeJsonToFile(Object object, String filePath) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), object);
    }

    public static <T> T readJsonFromFile(String filePath, Class<T> clazz) throws IOException {
        return mapper.readValue(new File(filePath), clazz);
    }
}
