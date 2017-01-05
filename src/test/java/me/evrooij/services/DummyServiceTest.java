package me.evrooij.services;

import com.google.gson.Gson;
import me.evrooij.data.Student;
import me.evrooij.util.NetworkUtil;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author eddy on 5-12-16.
 */
public class DummyServiceTest {

    //    @BeforeClass
//    public static void beforeClass() {
//        Main.main(null);
//    }
//
//    @AfterClass
//    public static void afterClass() {
//        Spark.stop();
//    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void aNewUserShouldBeCreated() throws Exception {
        String name = "john";
        int age = 20;

        String json = NetworkUtil.get(String.format("/student?name=%s&age=%s", name, age));
        System.out.println(json);
        Student student = new Gson().fromJson(json, Student.class);

        assertEquals(name, student.getName());
        assertEquals(age, student.getAge());
//        assertEquals(200, res.status);
//        assertEquals("john", map.get("name"));
//        assertEquals("john@foobar.com", map.get("email"));
//        assertNotNull(map.get("id"));
    }

}