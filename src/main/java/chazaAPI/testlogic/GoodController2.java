package chazaAPI.testlogic;

import chazaAPI.annotations.*;
import io.javalin.http.Context;


@Chaza(group = "auth" , contentType = "application/json" , accept = "application/json" , roles = {"admin", "user"})
public class GoodController2 {

    @EndPoint(
            method = Method.POST,
            url = "auth/login",
            description = "an endpoint to login your stuff",
            headers = {
                    @Header(name = "Authorization", value = "Bearer token")
            },
            requestFields = {
                    @Field(name = "username" , type = "string"),
                    @Field(name = "password" , type = "string")
            },
            responseFields = {
                    @Field(name = "status" , type = "boolean")
            },
            statusCodes = {
                    @Status(code = 200, description = "OK"),
                    @Status(code = 500, description = "Internal error"),
                    @Status(code = 401 , description = "unauthorised")
            }
    )
    public static void login(Context ctx) {
        ctx.json("{}"); // something here
    }
}
