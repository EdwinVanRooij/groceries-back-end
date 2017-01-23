package me.evrooij;

import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import me.evrooij.services.AccountService;
import me.evrooij.services.FeedbackService;
import me.evrooij.services.GroceryListService;

import static spark.Spark.*;

@SwaggerDefinition(host = "localhost:4567",
        info = @Info(description = "Groceries API",
                version = "1.0.0",
                title = "Back-end service for the Groceries application.",
                contact = @Contact(name = "Edwin", url = "https://evrooij.me/")),
        schemes = {SwaggerDefinition.Scheme.HTTP},
        consumes = {"application/json"},
        produces = {"application/json"},
        tags = {@Tag(name = "api")})
public class Main {

    public static final String APP_PACKAGE = "me.evrooij";

    public static void main(String[] args) {
        try {
            port(Config.getInstance().PORT);

            // Quite unsafe!
            before(new CorsFilter());

            new AccountService();
            new GroceryListService();
            new FeedbackService();

            // Build swagger json description
            final String swaggerJson = SwaggerParser.getSwaggerJson(APP_PACKAGE);
            get("/swagger", (req, res) -> swaggerJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
