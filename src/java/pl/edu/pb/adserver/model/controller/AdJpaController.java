/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.pb.adserver.model.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pl.edu.pb.adserver.model.Ad;
import pl.edu.pb.adserver.model.Ad.ContentType;
import pl.edu.pb.adserver.model.Ad.Orientation;
import pl.edu.pb.adserver.model.Category;
import pl.edu.pb.adserver.model.User;
import pl.edu.pb.adserver.model.controller.exceptions.NonexistentEntityException;
import pl.edu.pb.adserver.model.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author dawid
 */
public class AdJpaController implements Serializable {

    public AdJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ad ad) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(ad);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAd(ad.getId()) != null) {
                throw new PreexistingEntityException("Ad " + ad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ad ad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ad = em.merge(ad);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = ad.getId();
                if (findAd(id) == null) {
                    throw new NonexistentEntityException("The ad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ad ad;
            try {
                ad = em.getReference(Ad.class, id);
                ad.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ad with id " + id + " no longer exists.", enfe);
            }
            em.remove(ad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ad> findAdEntities() {
        return findAdEntities(true, -1, -1);
    }

    public List<Ad> findAdEntities(int maxResults, int firstResult) {
        return findAdEntities(false, maxResults, firstResult);
    }

    private List<Ad> findAdEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ad.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Ad findAd(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ad.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<Ad> findAds(Category c, ContentType t, Orientation o)
    {
        List<Ad> result = new LinkedList<Ad>();
        EntityManager em = getEntityManager();
        
        try
        {
            Query q = em.createQuery("SELECT a FROM Ad a WHERE a.category = :c "
                    + "AND a.contentType = :t AND a.orientation = :o");
            q.setParameter("c", c);
            q.setParameter("t", t);
            q.setParameter("o", o);
            
            result = q.getResultList();
        } finally
        {
            em.close();
        }
        return result;
    }

    public int getAdCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ad> rt = cq.from(Ad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    /* Expand functionallity to ModelController */
    
    public List<Ad> getUserAds(User u)
    {
        List<Ad> list = new LinkedList<Ad>();
        
        EntityManager em = getEntityManager();
        Query q = em.createQuery(
                "SELECT a FROM Ad a WHERE a.user = :user");
        q.setParameter("user", u);
        
        return q.getResultList();
    }
    
    
}
