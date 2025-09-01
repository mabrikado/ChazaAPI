package testlogic;

import annotations.*;

@Chaza
public class GoodController2 {

    @EndpointDoc(
            group = "auth",
            method = Method.POST,
            url = "auth/login",
            description = "an endpoint to login your stuff",
            headers = {
                    @Header(name = "Authorization", value = "Bearer token")
            },
            request = {
                    @RequestField(name = "username" , type = "string"),
                    @RequestField(name = "password" , type = "int")
            },
            response = {
                    @ResponseField(name = "status" , type = "boolean")
            },
            statusCodes = {
                    @StatusCode(code = 200, description = "OK"),
                    @StatusCode(code = 500, description = "Internal error"),
                    @StatusCode(code = 401 , description = "unauthorised")
            },
            roles = {"admin", "user"}
    )
    public static void login(){
        System.out.println("Logging in...");
    }
}
