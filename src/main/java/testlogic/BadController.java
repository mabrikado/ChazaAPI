package testlogic;

import annotations.EndpointDoc;
import annotations.Header;
import annotations.Method;
import annotations.StatusCode;

public class BadController {
    @EndpointDoc(
            group = "basic",
            method = Method.GET,
            url = "/",
            description = "Greet the viewer",
            headers = {
                    @Header(name = "Authorization", value = "Bearer token")
            },
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
