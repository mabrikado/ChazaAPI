package documentation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiInfo {

    private String title;

    @JsonProperty("version")
    private String apiVersion;

    private String description;
    private String termsOfService;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
    private Map<String, Object> contact;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
    private Map<String, Object> license;

    public ApiInfo(){

    }

    public static ApiInfo getInstance(){
        return new ApiInfo();
    }

    public ApiInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public ApiInfo setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public ApiInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public ApiInfo setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
        return this;
    }

    public ApiInfo setContact(Map<String, Object> contact) {
        this.contact = contact;
        return this;
    }

    public ApiInfo setLicense(Map<String, Object> license){
        this.license = license;
        return this;
    }

    public ApiInfo addContact(String key, Object value){
        if(this.contact == null){
            this.contact = new HashMap<>();
        }
        this.contact.put(key, value);
        return this;
    }

    public ApiInfo addLicense(String key, Object value){
        if(this.license == null){
            this.license  = new HashMap<>();
        }
        this.license.put(key, value);
        return this;
    }

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
