package chazaAPI.testlogic;

import chazaAPI.annotations.EndPoint;
import chazaAPI.annotations.Header;
import chazaAPI.annotations.Method;
import chazaAPI.annotations.Status;

public class BadController {
    @EndPoint(
            group = "basic",
            method = Method.GET,
            url = "/",
            description = "Greet the viewer",
            headers = {
                    @Header(name = "Authorization", value = "Bearer token")
            },
            statusCodes = {
                    @Status(code = 200, description = "OK"),
                    @Status(code = 500, description = "Internal error")
            },
            roles = {"admin", "user"}
    )
    public static void greet(){
        System.out.println("Sawubona!");
    }
}
