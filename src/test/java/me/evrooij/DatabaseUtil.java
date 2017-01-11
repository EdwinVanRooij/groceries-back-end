package me.evrooij.util;

import me.evrooij.data.Account;
import me.evrooij.data.Feedback;
import me.evrooij.data.GroceryList;
import me.evrooij.data.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;

public class DatabaseUtil {

    private final EntityManager entityManager;

    public DatabaseUtil() {
        entityManager = HibernateUtil.getInstance().getEntityManager();
    }

    /**
     * Includes all of the persisted classes which will be emptied
     */
    private final Class<?>[] ENTITY_TYPES = {
            Feedback.class,
            GroceryList.class,
            Account.class,
            Product.class,
    };

    /**
     * Empties all of the entities given in ENTITY_TYPES
     *
     * @throws SQLException when the delete query/queries fail(s)
     */
    public void clean() throws SQLException {
        entityManager.getTransaction().begin();

        entityManager.createNativeQuery("delete from Account_Account").executeUpdate();
        entityManager.createNativeQuery("delete from GroceryList_Account").executeUpdate();
        entityManager.createNativeQuery("delete from GroceryList_Product").executeUpdate();
        for (Class<?> entityType : ENTITY_TYPES) {
            entityManager.createQuery("delete from " + entityManager.getMetamodel().entity(entityType).getName()).executeUpdate();
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }


    public static void main(String[] args) {
        try {
            new DatabaseUtil().clean();
            System.exit(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
