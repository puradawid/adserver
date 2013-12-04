/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.pb.adserver.model.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pl.edu.pb.adserver.model.Category;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import pl.edu.pb.adserver.model.controller.exceptions.NonexistentEntityException;

/**
 *
 * @author dawid
 */
public class CategoryJpaController implements Serializable {

    public CategoryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Category category) {
        if (category.getChilds() == null) {
            category.setChilds(new ArrayList<Category>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category parent = category.getParent();
            if (parent != null) {
                parent = em.getReference(parent.getClass(), parent.getId());
                category.setParent(parent);
            }
            List<Category> attachedChilds = new ArrayList<Category>();
            for (Category childsCategoryToAttach : category.getChilds()) {
                childsCategoryToAttach = em.getReference(childsCategoryToAttach.getClass(), childsCategoryToAttach.getId());
                attachedChilds.add(childsCategoryToAttach);
            }
            category.setChilds(attachedChilds);
            em.persist(category);
            if (parent != null) {
                Category oldParentOfParent = parent.getParent();
                if (oldParentOfParent != null) {
                    oldParentOfParent.setParent(null);
                    oldParentOfParent = em.merge(oldParentOfParent);
                }
                parent.setParent(category);
                parent = em.merge(parent);
            }
            for (Category childsCategory : category.getChilds()) {
                Category oldParentOfChildsCategory = childsCategory.getParent();
                childsCategory.setParent(category);
                childsCategory = em.merge(childsCategory);
                if (oldParentOfChildsCategory != null) {
                    oldParentOfChildsCategory.getChilds().remove(childsCategory);
                    oldParentOfChildsCategory = em.merge(oldParentOfChildsCategory);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Category category) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category persistentCategory = em.find(Category.class, category.getId());
            Category parentOld = persistentCategory.getParent();
            Category parentNew = category.getParent();
            List<Category> childsOld = persistentCategory.getChilds();
            List<Category> childsNew = category.getChilds();
            if (parentNew != null) {
                parentNew = em.getReference(parentNew.getClass(), parentNew.getId());
                category.setParent(parentNew);
            }
            List<Category> attachedChildsNew = new ArrayList<Category>();
            for (Category childsNewCategoryToAttach : childsNew) {
                childsNewCategoryToAttach = em.getReference(childsNewCategoryToAttach.getClass(), childsNewCategoryToAttach.getId());
                attachedChildsNew.add(childsNewCategoryToAttach);
            }
            childsNew = attachedChildsNew;
            category.setChilds(childsNew);
            category = em.merge(category);
            if (parentOld != null && !parentOld.equals(parentNew)) {
                parentOld.setParent(null);
                parentOld = em.merge(parentOld);
            }
            if (parentNew != null && !parentNew.equals(parentOld)) {
                Category oldParentOfParent = parentNew.getParent();
                if (oldParentOfParent != null) {
                    oldParentOfParent.setParent(null);
                    oldParentOfParent = em.merge(oldParentOfParent);
                }
                parentNew.setParent(category);
                parentNew = em.merge(parentNew);
            }
            for (Category childsOldCategory : childsOld) {
                if (!childsNew.contains(childsOldCategory)) {
                    childsOldCategory.setParent(null);
                    childsOldCategory = em.merge(childsOldCategory);
                }
            }
            for (Category childsNewCategory : childsNew) {
                if (!childsOld.contains(childsNewCategory)) {
                    Category oldParentOfChildsNewCategory = childsNewCategory.getParent();
                    childsNewCategory.setParent(category);
                    childsNewCategory = em.merge(childsNewCategory);
                    if (oldParentOfChildsNewCategory != null && !oldParentOfChildsNewCategory.equals(category)) {
                        oldParentOfChildsNewCategory.getChilds().remove(childsNewCategory);
                        oldParentOfChildsNewCategory = em.merge(oldParentOfChildsNewCategory);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = category.getId();
                if (findCategory(id) == null) {
                    throw new NonexistentEntityException("The category with id " + id + " no longer exists.");
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
            Category category;
            try {
                category = em.getReference(Category.class, id);
                category.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The category with id " + id + " no longer exists.", enfe);
            }
            Category parent = category.getParent();
            if (parent != null) {
                parent.setParent(null);
                parent = em.merge(parent);
            }
            List<Category> childs = category.getChilds();
            for (Category childsCategory : childs) {
                childsCategory.setParent(null);
                childsCategory = em.merge(childsCategory);
            }
            em.remove(category);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Category> findCategoryEntities() {
        return findCategoryEntities(true, -1, -1);
    }

    public List<Category> findCategoryEntities(int maxResults, int firstResult) {
        return findCategoryEntities(false, maxResults, firstResult);
    }

    private List<Category> findCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Category.class));
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

    public Category findCategory(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }
    
    public Category findCategory(String name) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery
                    ("SELECT c FROM Category c WHERE c.name = '" + name + "'");
            return (Category)q.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    public Category getRootCategory() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery
                    ("SELECT c FROM Category c WHERE c.name = 'root'");
            return (Category)q.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    public void attachCategory(String base, String children) throws Exception
    {
        Category baseCategory = findCategory(base);
        Category childrenCategory = new Category(children);
        create(childrenCategory);
        List<Category> baseCategories = baseCategory.getChilds();
        baseCategories.add(childrenCategory);
        baseCategory.setChilds(baseCategories);
        try {
        edit(baseCategory);
        } catch (Exception e) {throw e;} 
    }

    public int getCategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Category> rt = cq.from(Category.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
