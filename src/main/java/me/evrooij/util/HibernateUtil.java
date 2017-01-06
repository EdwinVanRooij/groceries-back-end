package me.evrooij.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author eddy on 6-1-17.
 */
public class HibernateUtil {

    private static HibernateUtil ourInstance = new HibernateUtil();

    private static EntityManagerFactory entityManagerFactory;

    public static HibernateUtil getInstance() {
        return ourInstance;
    }

    private HibernateUtil() {
        try {
            String path = "./hibernate.properties";
            FileInputStream fis = new FileInputStream(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Properties prop = new Properties();
            prop.load(in);
            in.close();
            entityManagerFactory = Persistence.createEntityManagerFactory("GroceriesPersistenceUnit", prop);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
