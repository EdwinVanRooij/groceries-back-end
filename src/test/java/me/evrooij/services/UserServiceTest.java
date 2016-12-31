package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.data.Student;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author eddy on 5-12-16.
 */
public class UserServiceTest {

//    @BeforeClass
//    public static void beforeClass() {
//        Main.main(null);
//    }
//
//    @AfterClass
//    public static void afterClass() {
//        Spark.stop();
//    }

    @Test
    public void aNewUserShouldBeCreated() {
        String json = request("GET", "/student?name=john&age=20");
        Student student = new Gson().fromJson(json, Student.class);

        assertEquals("john", student.getName());
        assertEquals(20, student.getAge());
//        assertEquals(200, res.status);
//        assertEquals("john", map.get("name"));
//        assertEquals("john@foobar.com", map.get("email"));
//        assertNotNull(map.get("id"));
    }

    private String request(String method, String path) {
        try {
            URL url = new URL("http://localhost:4567" + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.connect();
            return IOUtils.toString(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Sending request failed: " + e.getMessage());
            return null;
        }
    }
}