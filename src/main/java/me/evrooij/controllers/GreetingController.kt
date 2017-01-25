package me.evrooij;

import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicLong

/**
 * @author eddy on 25-1-17.
 */
@RestController
class GreetingController {
    val counter = AtomicLong()

    @RequestMapping(value = "/greeting", method = arrayOf(RequestMethod.GET))
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): Greeting {
        return Greeting(counter.incrementAndGet(), "Hello, $name")
    }
}
