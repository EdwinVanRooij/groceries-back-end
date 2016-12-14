package me.evrooij.util;

import me.evrooij.domain.Account;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;

public class DatabaseUtil {

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GroceriesPersistenceUnit");
    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public DatabaseUtil() {
    }

    /**
     * Includes all of the persisted classes which will be emptied
     */
    private final Class<?>[] ENTITY_TYPES = {
            Account.class
    };

    /**
     * Empties all of the entities given in ENTITY_TYPES
     *
     * @throws SQLException when the delete query/queries fail(s)
     */
    public void clean() throws SQLException {
        entityManager.getTransaction().begin();

        for (Class<?> entityType : ENTITY_TYPES) {
            entityManager.createQuery("delete from " + entityManager.getMetamodel().entity(entityType).getName()).executeUpdate();
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }


    public static void main(String[] args) {
        try {
            new DatabaseUtil().clean();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
