package documentation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.ChazaAPIException;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Represents the documentation for an API, including general API information
 * and a list of its endpoints.
 *
 * This class supports serialization to JSON format, including validation
 * to ensure required fields are not null.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIDoc {

    /**
     * General information about the API.
     */
    private ApiInfo apiInfo;

    /**
     * List of API endpoints included in this documentation.
     */
    private List<Endpoint> endpoints;

    /**
     * Default constructor.
     */
    public APIDoc() {
    }

    /**
     * Constructs an APIDoc instance with the specified API information and endpoints.
     *
     * @param apiInfo   the general API information
     * @param endpoints the list of API endpoints
     */
    public APIDoc(ApiInfo apiInfo, List<Endpoint> endpoints) {
        this.apiInfo = apiInfo;
        this.endpoints = endpoints;
    }

    /**
     * Serializes this APIDoc instance to a compact JSON string after validating
     * required fields.
     *
     * @return a JSON string representation of this APIDoc
     * @throws ChazaAPIException if validation fails (required fields are null)
     */
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

    /**
     * Serializes this APIDoc instance to a pretty-printed (indented) JSON string
     * after validating required fields.
     *
     * @return a pretty-printed JSON string representation of this APIDoc
     * @throws ChazaAPIException if validation fails (required fields are null)
     */
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

    /**
     * Validates that all fields annotated with JsonInclude having NON_NULL or NON_EMPTY
     * are not null in the given object.
     *
     * @param obj the object to validate
     * @throws ChazaAPIException if any required field is null
     */
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

    /**
     * Returns a compact JSON string representation of this APIDoc instance.
     * This overrides the default Object.toString() behavior.
     *
     * @return a JSON string representing this APIDoc
     */
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
