package me.evrooij

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
