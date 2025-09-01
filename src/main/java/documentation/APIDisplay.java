package documentation;
import file.FileHandler;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import lombok.Data;
import testlogic.GoodController;
import testlogic.GoodController2;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class APIDisplay {

    private ApiInfo apiInfo;
    private APIDoc doc;
    private List<Endpoint> endpoints;

    private APIDisplay(){
        /// default
    }

    public static APIDisplay getInstance(){
        return new APIDisplay();
    }
    public APIDisplay setApiInfo(ApiInfo apiInfo){
        this.apiInfo = apiInfo;
        return this;
    }

    public APIDisplay scanEndpoints(List<Class<?>> controllers) throws ChazaAPIException {
        endpoints = Endpoint.scan(controllers);
        return this;
    }

    public APIDisplay generateDocumentation() {
        doc = new APIDoc(apiInfo , endpoints);
        try {
            File outputFile = new File("doc/api-doc.json");
            if (outputFile.getParentFile() != null) {
                outputFile.getParentFile().mkdirs();
            }
            FileHandler.writeJsonToFile(doc, outputFile.getPath());
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void configureServer(JavalinConfig config){
        config.staticFiles.add("doc", Location.EXTERNAL);
    }

    /**
     * Hosts the API documentation redirect endpoint on the provided Javalin server.
     *
     * @param server the Javalin server instance
     * @throws ChazaAPIException if the server is already running
     */
    public void hostToServer(Javalin server) throws ChazaAPIException {

        if (server == null) {

            throw new ChazaAPIException("Server instance cannot be null");
        }
        server.get("/doc", ctx -> ctx.redirect("APIDoc.html"));
    }


    public static void main(String[] args) throws ChazaAPIException {
        Javalin app = Javalin.create(config -> APIDisplay.configureServer(config));
        APIDisplay.getInstance()
                .setApiInfo(ApiInfo.getInstance()
                        .setTitle("API Documentation")
                        .setApiVersion("1")
                        .setDescription("API Documentation")
                        .setTermsOfService("Terms of Service")
                        .setContact(Map.of("Contact 1" , "fffs"))
                        .setLicense(Map.of("title" , "SOme Licence")))
                .scanEndpoints(List.of(GoodController.class , GoodController2.class))
                .generateDocumentation()
                .hostToServer(app);
        app.start(8080);
    }
    }
