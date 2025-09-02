package testlogic;
import annotations.*;

@Chaza
public class GoodController {

    @EndPoint(
            group = "basic",
            method = Method.GET,
            url = "/",
            description = "Greet the viewer",
            headers = {
                    @Header(name = "Authorization", value = "Bearer token")
            },
            contentType = "text/plain",
            statusCodes = {
                    @StatusCode(code = 200, description = "OK"),
                    @StatusCode(code = 500, description = "Internal error")
            },
            roles = {"admin", "user"}
    )
    public static void greet(){
        System.out.println("Sawubona!");
    }
}
