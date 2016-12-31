package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.Main;
import me.evrooij.data.Student;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import static me.evrooij.util.NetworkUtil.request;
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
    public void aNewUserShouldBeCreated() throws Exception {
        String name = "john";
        int age = 20;

        String json = request("GET", String.format("/student?name=%s&age=%s", name, String.valueOf(age)));
        Student student = new Gson().fromJson(json, Student.class);

        assertEquals(name, student.getName());
        assertEquals(age, student.getAge());
//        assertEquals(200, res.status);
//        assertEquals("john", map.get("name"));
//        assertEquals("john@foobar.com", map.get("email"));
//        assertNotNull(map.get("id"));
    }

}