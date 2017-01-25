package me.evrooij.controllers

import me.evrooij.Config
import me.evrooij.data.Account
import me.evrooij.data.ResponseMessage
import me.evrooij.managers.AccountManager
import me.evrooij.storage.FileSystemStorageService
import me.evrooij.storage.StorageService
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.servlet.MultipartConfigElement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMapping


/**
 * @author eddy on 25-1-17.
 */
@RestController
class AccountController {
    val accountManager = AccountManager()

    @RequestMapping(value = "/users/login", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun login(@RequestParam(value = "username") username: String, @RequestParam(value = "password") password: String): Account {
        return accountManager.login(username, password)
    }

    @RequestMapping(value = "/users/register", method = arrayOf(RequestMethod.POST), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseBody
    fun register(@RequestBody stringMap: Map<String, String>): Account {
        val username = stringMap["username"]
        val email = stringMap["email"]
        val password = stringMap["password"]
        return accountManager.registerAccount(username, email, password)
    }

    @RequestMapping(value = "/accounts/{accountId}/friends/find", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun findFriends(@PathVariable accountId: Int, @RequestParam(value = "query") query: String): List<Account> {
        return accountManager.searchFriends(accountId, query)
    }

    @RequestMapping(value = "/accounts/{accountId}/friends", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getFriends(@PathVariable accountId: Int): List<Account> {
        return accountManager.getFriends(accountId)
    }

    @RequestMapping(value = "/accounts/{accountId}/friends/add", method = arrayOf(RequestMethod.POST), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseBody
    fun addFriend(@PathVariable accountId: Int, @RequestBody friend: Account): ResponseMessage {
        if (accountManager.addFriend(accountId, friend)) {
            return ResponseMessage("Successfully added friend.")
        } else {
            return ResponseMessage("Could not add friend.")
        }
    }

    @RequestMapping(value = "/{accountId}/myproducts/{productId}", method = arrayOf(RequestMethod.POST), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseBody
    fun addProductImageUrl(
            @PathVariable accountId: Int,
            @PathVariable productId: Int,
            @RequestParam("file") file: MultipartFile
    ): ResponseMessage {
        val saveDir = getSavePath()
        val fileName = productId.toString()

        val path = Paths.get(String.format("%s/%s", saveDir, fileName))
        Files.copy(file.inputStream, path)

        val ip = Config.getInstance().EXTERNAL_BASE_URL
        val imageUrl = String.format("%s/products/%s/image", ip, productId)

        println(String.format("URL will be %s", imageUrl))

        accountManager.addProductImageUrl(accountId, productId, imageUrl)
        return ResponseMessage(imageUrl)
    }

    @RequestMapping(
            value = "/products/{productId}/image",
            method = arrayOf(RequestMethod.GET),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getImage(
            @PathVariable productId: Int): Account {
        val saveDir = getSavePath()
        val fileName = productId.toString()
        val path = Paths.get(String.format("%s/%s", saveDir, fileName))
        val data = Files.readAllBytes(path)

//        val storage = FileSystemStorageService()
//
//            val file = StorageService.loadAsResource(filename)
//            return ResponseEntity
//                    .ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
//                    .body<Resource>(file)
//
//        return accountManager.login(username, password)
        return Account()
    }

//    @RequestMapping(value = "user/avatar/{userId}", method = arrayOf(RequestMethod.GET))
//    @ResponseBody
//    fun downloadUserAvatarImage(@PathVariable userId: Long?): ResponseEntity<InputStreamResource> {
//        val gridFsFile = FileService.findUserAccountAvatarById(userId)
//
//        return ResponseEntity.ok()
//                .contentLength(gridFsFile.getLength())
//                .contentType(MediaType.parseMediaType(gridFsFile.getContentType()))
//                .body<T>(InputStreamResource(gridFsFile.getInputStream()))
//    }

    fun getSavePath(): String? {
        val path = "./config.properties"
        try {
            FileInputStream(path).use { fis ->
                BufferedReader(InputStreamReader(fis)).use { inputStream ->
                    val prop = Properties()
                    prop.load(inputStream)
                    return prop.getProperty("path")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}