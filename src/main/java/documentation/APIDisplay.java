package documentation;
import exceptions.ChazaAPIException;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import lombok.Data;
import testlogic.GoodController;
import testlogic.GoodController2;

import java.io.File;
import java.io.IOException;
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
        return this;
    }

    public static void configureServer(JavalinConfig config){
        config.staticFiles.add("/public", Location.CLASSPATH);
    }

    public void hostToServer(Javalin server) throws ChazaAPIException {

        if (server == null) {

            throw new ChazaAPIException("Server instance cannot be null");
        }
        server.get("/chaza", ctx -> ctx.redirect("APIDoc.html"));
        server.get("/chaza-json", ctx -> {
            ctx.json(doc.toPrettyJsonString());
        });
    }


    public static void main(String[] args) throws ChazaAPIException {
        Javalin app = Javalin.create(config -> APIDisplay.configureServer(config));
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
                .scanEndpoints(List.of(GoodController.class , GoodController2.class))
                .generateDocumentation()
                .hostToServer(app);
        app.start(8080);
    }
    }
