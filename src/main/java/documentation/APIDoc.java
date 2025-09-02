package documentation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.ChazaAPIException;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIDoc {
    private ApiInfo apiInfo;
    private List<Endpoint> endpoints;

    public APIDoc(){

    }

    public APIDoc(ApiInfo apiInfo, List<Endpoint> endpoints) {
        this.apiInfo = apiInfo;
        this.endpoints = endpoints;
    }



    public String toJsonString() throws ChazaAPIException {
        ObjectMapper mapper = new ObjectMapper();
        try {

            validate(apiInfo);
            for (Endpoint endpoint : endpoints) {
                validate(endpoint);
            }


            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String toPrettyJsonString() throws ChazaAPIException {
        ObjectMapper mapper = new ObjectMapper();
        try {

            validate(apiInfo);
            for (Endpoint endpoint : endpoints) {
                validate(endpoint);
            }

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void validate(Object obj) throws ChazaAPIException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            JsonInclude annotation = field.getAnnotation(JsonInclude.class);
            if (annotation != null) {
                JsonInclude.Include includeValue = annotation.value();

                if (includeValue == JsonInclude.Include.NON_NULL || includeValue == JsonInclude.Include.NON_EMPTY) {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(obj);
                        if (value == null) {
                            throw new ChazaAPIException("API " + field.getName() + " must not be null.");
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }



}
