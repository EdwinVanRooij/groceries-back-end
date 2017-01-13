package me.evrooij.daos;

import me.evrooij.data.Account;
import me.evrooij.data.Feedback;
import me.evrooij.data.Product;
import me.evrooij.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author eddy on 8-12-16.
 */

public class ProductDAO {
    private final EntityManager entityManager;

    public ProductDAO() {
        entityManager = HibernateUtil.getInstance().getEntityManager();
    }

    public void updateDeletionTime(Product product) {
        entityManager.getTransaction().begin();

        Query query = entityManager.createQuery(
                "update Product p set deletionDate = now() where p.id = :id"
        );
        query.setParameter("id", product.getId());
        query.executeUpdate();

        entityManager.getTransaction().commit();
    }
}
