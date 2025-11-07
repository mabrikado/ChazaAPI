package chazaAPI.testlogic;
import chazaAPI.annotations.*;


@Chaza(group = "basic" ,
        contentType = "text/plain",
        roles = {"user" , "admin"}
)
public class GoodController {

    @EndPoint(
            method = Method.GET,
            url = "/",
            description = "Greet the viewer",
            headers = {
                    @Header(name = "Authorization", value = "Bearer token")
            },
            statusCodes = {
                    @Status(code = 200, description = "OK"),
                    @Status(code = 500, description = "Internal error")
            }
    )
    public static void greet() {
        System.out.println("Sawubona!");
    }
}
