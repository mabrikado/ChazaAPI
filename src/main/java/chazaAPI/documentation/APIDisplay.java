package chazaAPI.documentation;
import chazaAPI.exceptions.ChazaAPIException;
import chazaAPI.testlogic.GoodController3;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import lombok.Data;
import chazaAPI.testlogic.GoodController;
import chazaAPI.testlogic.GoodController2;

import java.util.List;


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

    public APIDisplay scanEndPoints(List<Class<?>> controllers) throws ChazaAPIException {
        endpoints = Endpoint.scan(controllers);
        return this;
    }

    public APIDisplay generateDocumentation() {
        doc = new APIDoc(apiInfo , endpoints);
        return this;
    }

    public static void configureServer(JavalinConfig config){
        config.staticFiles.add("/public", Location.CLASSPATH);
    }

    public void hostToServer(Javalin server) throws ChazaAPIException {

        if (server == null) {

            throw new ChazaAPIException("Server instance cannot be null");
        }
        server.get("/chazaAPI", ctx -> ctx.redirect("APIDoc.html"));
        server.get("/chaza-json", ctx -> {
            ctx.json(doc.toPrettyJsonString());
        });
    }


    /**
     * Main entry point to start a Javalin server with generated API documentation for ChazaAPI.
     *
     * Note: This method is intended only for internal use to test API capabilities and
     * documentation generation. It should not be called or invoked by any external
     * application or production environment.
     *
     * @param args command-line arguments (not used)
     * @throws ChazaAPIException if there is an error during API documentation generation or server setup
     */
    public static void main(String[] args) throws ChazaAPIException {
        Javalin app = Javalin.create(APIDisplay::configureServer);
        APIDisplay.getInstance()
                .setApiInfo(ApiInfo.getInstance()
                        .setTitle("API Documentation")
                        .setApiVersion("1")
                        .setDescription("API Documentation")
                        .setTermsOfService("Terms of Service")
                        .addContact("email" , "abcdf@gmail.com")
                        .addContact("phone" , "94836")
                        .addLicense("title" , "ChazaDoc")
                        .addLicense("url" , "www.chazaAPI.com"))
                .scanEndPoints(List.of(GoodController.class , GoodController2.class , GoodController3.class))
                .generateDocumentation()
                .hostToServer(app);
        app.start(8080);
    }
    }
