package me.evrooij

import me.evrooij.services.AccountService
import me.evrooij.services.FeedbackService
import me.evrooij.services.GroceryListService
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class Main {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
//            port(Config.getInstance().PORT);

            SpringApplication.run(Main::class.java, *args)
//            AccountService()
//            GroceryListService()
//            FeedbackService()
        }
    }
}
