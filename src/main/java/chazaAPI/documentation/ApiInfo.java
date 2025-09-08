package chazaAPI.documentation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents general information about an API, such as title, version,
 * description, terms of service, contact details, and licensing information.
 * <p>
 * This class supports JSON serialization with Jackson annotations.
 * It uses fluent setters to allow chaining of method calls.
 * </p>
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiInfo {

    /**
     * The title of the API.
     */
    private String title;

    /**
     * The version of the API.
     */
    @JsonProperty("version")
    private String apiVersion;

    /**
     * A short description of the API.
     */
    private String description;

    /**
     * The terms of service URL or description for the API.
     */
    private String termsOfService;

    /**
     * Contact information for the API provider.
     * Represented as a map of keys and values.
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
    private Map<String, Object> contact;

    /**
     * Licensing information for the API.
     * Represented as a map of keys and values.
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
    private Map<String, Object> license;

    /**
     * Default constructor.
     */
    public ApiInfo() {
    }

    /**
     * Creates a new instance of ApiInfo.
     *
     * @return a new ApiInfo instance.
     */
    public static ApiInfo getInstance() {
        return new ApiInfo();
    }

    /**
     * Sets the API title.
     *
     * @param title the API title
     * @return this ApiInfo instance for chaining
     */
    public ApiInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the API version.
     *
     * @param apiVersion the API version
     * @return this ApiInfo instance for chaining
     */
    public ApiInfo setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    /**
     * Sets the API description.
     *
     * @param description the API description
     * @return this ApiInfo instance for chaining
     */
    public ApiInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the terms of service information.
     *
     * @param termsOfService the terms of service
     * @return this ApiInfo instance for chaining
     */
    public ApiInfo setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
        return this;
    }

    /**
     * Sets the contact information map.
     *
     * @param contact a map of contact information
     * @return this ApiInfo instance for chaining
     */
    public ApiInfo setContact(Map<String, Object> contact) {
        this.contact = contact;
        return this;
    }

    /**
     * Sets the license information map.
     *
     * @param license a map of license information
     * @return this ApiInfo instance for chaining
     */
    public ApiInfo setLicense(Map<String, Object> license) {
        this.license = license;
        return this;
    }

    /**
     * Adds a key-value pair to the contact information map.
     * If the map is null, it will be initialized.
     *
     * @param key   the contact key
     * @param value the contact value
     * @return this ApiInfo instance for chaining
     */
    public ApiInfo addContact(String key, Object value) {
        if (this.contact == null) {
            this.contact = new HashMap<>();
        }
        this.contact.put(key, value);
        return this;
    }

    /**
     * Adds a key-value pair to the license information map.
     * If the map is null, it will be initialized.
     *
     * @param key   the license key
     * @param value the license value
     * @return this ApiInfo instance for chaining
     */
    public ApiInfo addLicense(String key, Object value) {
        if (this.license == null) {
            this.license = new HashMap<>();
        }
        this.license.put(key, value);
        return this;
    }

    /**
     * Returns a string representation of this ApiInfo object,
     * including all fields.
     *
     * @return string representation of ApiInfo
     */
    @Override
    public String toString() {
        return "ApiInfo{" +
                "title='" + title + '\'' +
                ", apiVersion='" + apiVersion + '\'' +
                ", description='" + description + '\'' +
                ", termsOfService='" + termsOfService + '\'' +
                ", contact=" + contact +
                ", license=" + license +
                '}';
    }

}
