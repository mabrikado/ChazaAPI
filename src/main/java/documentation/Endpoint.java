package documentation;

import annotations.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.ChazaAPIException;
import lombok.Data;

import java.util.*;

/**
 * Represents an API endpoint definition.
 *
 * This class is used to build a representation of an endpoint
 * from annotations, and can be serialized into structured formats
 * like JSON. It supports extracting metadata such as HTTP method,
 * URL, headers, request/response fields, and status codes.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Endpoint {

    private String group;
    private Method method;
    private String url;
    private String description;
    private String contentType;

    private Map<String, Object> request;
    private Map<String, Object> response;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> headers;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("statusCodes")
    private Map<String, String> statusCodes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> roles = List.of("any");

    /**
     * Default constructor. Initializes all internal maps.
     */
    public Endpoint() {
        headers = new HashMap<>();
        statusCodes = new HashMap<>();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endpoint endpoint = (Endpoint) o;

        return Objects.equals(group, endpoint.group) &&
                Objects.equals(method, endpoint.method) &&
                Objects.equals(url, endpoint.url) &&
                Objects.equals(description, endpoint.description) &&
                Objects.equals(request, endpoint.request) &&
                Objects.equals(response, endpoint.response) &&
                Objects.equals(headers, endpoint.headers) &&
                Objects.equals(statusCodes, endpoint.statusCodes) &&
                Objects.equals(roles, endpoint.roles);
    }

    /**
     * Generates a hash code for this endpoint.
     */
    @Override
    public int hashCode() {
        return Objects.hash(group, method, url, description, request, response, headers, statusCodes, roles);
    }

    /**
     * Builds an Endpoint instance from the provided EndpointDoc annotation.
     *
     * @param endPoint the annotation to read from
     * @return the resulting Endpoint object
     */
    public static Endpoint fromAnnotation(EndPoint endPoint) {
        Endpoint endpoint = new Endpoint();

        endpoint.setGroup(endPoint.group());
        endpoint.setMethod(endPoint.method());
        endpoint.setUrl(endPoint.url());
        endpoint.setDescription(endPoint.description());

        Map<String, Object> headersMap = new HashMap<>();
        for (Header header : endPoint.headers()) {
            headersMap.put(header.name(), header.value());
        }
        endpoint.setHeaders(headersMap);

        Map<String, Object> requestMap = new HashMap<>();
        for (RequestField field : endPoint.request()) {
            Map<String, String> fieldInfo = new HashMap<>();
            fieldInfo.put("type", field.type());
            requestMap.put(field.name(), fieldInfo);
        }
        endpoint.setRequest(requestMap.isEmpty() ? null : requestMap);

        Map<String, Object> responseMap = new HashMap<>();
        for (ResponseField field : endPoint.response()) {
            Map<String, String> fieldInfo = new HashMap<>();
            fieldInfo.put("type", field.type());
            responseMap.put(field.name(), fieldInfo);
        }
        endpoint.setResponse(responseMap.isEmpty() ? null : responseMap);

        Map<String, String> statusCodesMap = new HashMap<>();
        for (StatusCode sc : endPoint.statusCodes()) {
            statusCodesMap.put(String.valueOf(sc.code()), sc.description());
        }
        endpoint.setStatusCodes(statusCodesMap.isEmpty() ? null : statusCodesMap);

        endpoint.setRoles(Arrays.asList(endPoint.roles()));

        endpoint.setContentType(endPoint.contentType());

        return endpoint;
    }

    /**
     * Scans a list of controller classes for annotated endpoints.
     *
     * Only classes annotated with @Chaza will be processed.
     *
     * @param controllers a list of controller classes to scan
     * @return a list of extracted Endpoint objects
     * @throws ChazaAPIException if a class is not annotated with @Chaza
     */
    public static List<Endpoint> scan(List<Class<?>> controllers) throws ChazaAPIException {
        List<Endpoint> endpoints = new ArrayList<>();

        for (Class<?> controllerClass : controllers) {
            if (!controllerClass.isAnnotationPresent(Chaza.class)) {
                throw new ChazaAPIException("class " + controllerClass.getName() + " is not annotated with @Chaza");
            }

            for (java.lang.reflect.Method method : controllerClass.getDeclaredMethods()) {
                EndPoint annotation = method.getAnnotation(EndPoint.class);
                if (annotation != null) {
                    Endpoint endpoint = Endpoint.fromAnnotation(annotation);
                    endpoints.add(endpoint);
                }
            }
        }

        return endpoints;
    }
}
