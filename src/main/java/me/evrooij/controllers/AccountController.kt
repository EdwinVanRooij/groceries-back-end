package me.evrooij.controllers

import me.evrooij.data.Account
import me.evrooij.managers.AccountManager
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


/**
 * @author eddy on 25-1-17.
 */
@RestController
class AccountController {
    val accountManager = AccountManager()

    @RequestMapping(
            value = "/users/login",
            method = arrayOf(RequestMethod.GET),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun login(
            @RequestParam(value = "username") username: String,
            @RequestParam(value = "password") password: String): Account {
        return accountManager.login(username, password)
    }

    @RequestMapping(
            value = "/accounts/{accountId}/friends/find",
            method = arrayOf(RequestMethod.GET),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun findFriends(
            @PathVariable accountId: Int,
            @RequestParam(value = "query") query: String): List<Account> {
        return accountManager.searchFriends(accountId, query)
    }

    @RequestMapping(
            value = "/accounts/{accountId}/friends",
            method = arrayOf(RequestMethod.GET),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getFriends(
            @PathVariable accountId: Int): List<Account> {
        return accountManager.getFriends(accountId)
    }

    @RequestMapping(
            value = "/users/register",
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseBody
    fun register(
            @RequestBody stringMap: Map<String, String>): Account {
        val username = stringMap["username"]
        val email = stringMap["email"]
        val password = stringMap["password"]
        return accountManager.registerAccount(username, email, password)
    }

    @RequestMapping(
            value = "/{accountId}/myproducts/{productId}",
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseBody
    fun addProductImageUrl(
            @RequestBody stringMap: Map<String, String>,
            @PathVariable accountId: Int,
            @PathVariable productId: Int): Account {
        val username = stringMap["username"]
        val email = stringMap["email"]
        val password = stringMap["password"]
        return accountManager.registerAccount(username, email, password)
    }

//    post("/accounts/:accountId/friends/add", (request, response) -> {
//        int accountId = Integer.valueOf(request.params(":accountId"));
//
//        String json = request.body();
//        System.out.println(String.format("Received json from /accounts/:accountId/friends/add in req body: %s", json));
//
//        Account friend = new Gson().fromJson(request.body(), Account.class);
//        System.out.println(String.format("Received new friend: %s", friend.toString()));
//
//        boolean result = accountManager.addFriend(accountId, friend);
//        if (result) {
//            return new ResponseMessage("Successfully added friend.");
//        } else {
//            return new ResponseMessage("Could not add friend.");
//        }
//    }, json());
}