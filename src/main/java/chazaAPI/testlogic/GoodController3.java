package chazaAPI.testlogic;
import chazaAPI.DTO.BoxOfItems;
import chazaAPI.DTO.Item;
import chazaAPI.annotations.*;
import io.javalin.http.Context;

@Chaza(group = "items" , baseUrl = "items/", accept = "application/json" , contentType = "application/json" , roles = {"admin"})
public class GoodController3 {

    @EndPoint(
            url = "/",
            method = Method.POST,
            headers = {
                    @Header(name = "Authorization", value = "Bearer token")
            },
            description = "Add items",
            requestDTO = Item.class,
            contentType = "text/plain",
            statusCodes = {@Status(code = 200 , description = "ok"),
            @Status(code = 400 , description = "bad request"),
            @Status(code = 501 , description = "Internal Server error")}
    )
    public static void addItem(Context ctx){
    }

    @EndPoint(
            url = "/box-items",
            method = Method.POST,
            headers = {
                    @Header(name = "Authorization", value = "Bearer token")
            },
            description = "Add Box items",
            requestDTO = BoxOfItems.class,
            contentType = "text/plain",
            statusCodes = {@Status(code = 200 , description = "ok"),
                    @Status(code = 400 , description = "bad request"),
                    @Status(code = 501 , description = "Internal Server error")}
    )
    public static void addBoxItems (Context ctx){
    }
}
