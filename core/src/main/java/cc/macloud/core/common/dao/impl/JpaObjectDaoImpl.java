package cc.macloud.core.common.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import cc.macloud.core.common.dao.ObjectDao;
import cc.macloud.core.common.exception.CoreException;

/**
 * This class is an implementation of the ObjectDao interface using JPA (Java
 * Persistence API).
 * It provides methods for saving, updating, deleting, and retrieving objects
 * from the database.
 * The class uses an EntityManager to interact with the database.
 *
 * @param <T> the type of objects managed by this DAO
 */
public class JpaObjectDaoImpl<T> implements ObjectDao<T> {

    /**
     * The name of the persistence unit.
     */
    private String persistenceUnitName;
    /**
     * The entity manager used for managing entities in the database.
     */
    private EntityManager em;

    /**
     * Sets the entity manager for this DAO.
     *
     * @param entityManager the entity manager to be set
     */
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    /**
     * Sets the name of the persistence unit.
     *
     * @param persistenceUnitName the name of the persistence unit
     */
    public void setPersistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    /**
     * Saves an object of type T to the database.
     *
     * @param obj the object to be saved
     * @return the saved object
     * @throws CoreException if an error occurs during the save operation
     */
    @Override
    public T save(T obj) throws CoreException {
        em.persist(obj);
        return obj;
    }

    /**
     * Updates the given object in the database.
     *
     * @param obj the object to be updated
     * @return the updated object
     * @throws CoreException if an error occurs during the update process
     */
    @Override
    public T update(T obj) throws CoreException {
        em.persist(obj);
        return obj;
    }

    /**
     * Deletes the specified object from the database.
     *
     * @param obj the object to be deleted
     * @throws CoreException if an error occurs while deleting the object
     */
    @Override
    public void delete(T obj) throws CoreException {
        em.remove(obj);
    }

    /**
     * Retrieves an object of type T from the database based on the specified object
     * identifier.
     *
     * @param oid the object identifier
     * @return the object of type T with the specified identifier, or null if not
     *         found
     * @throws CoreException if an error occurs while retrieving the object
     */
    @Override
    public T get(Serializable oid) throws CoreException {
        try {
            return (T) em.find(Class.forName(persistenceUnitName), oid);
        } catch (ClassNotFoundException e) {
            throw new CoreException(e.getMessage());
        }
    }

    /**
     * Deletes a batch of objects from the database.
     *
     * @param objects the collection of objects to be deleted
     * @throws CoreException if an error occurs during the deletion process
     */
    @Override
    public void deleteBatch(Collection<T> objects) throws CoreException {
        for (T obj : objects) {
            em.remove(obj);
        }
    }

    /**
     * Deletes objects from the database based on the given criteria.
     *
     * @param criteria the criteria used to filter the objects to be deleted
     * @return the number of objects deleted
     * @throws CoreException if an error occurs during the deletion process
     */
    @Override
    public int deleteByAttributes(CommonCriteria criteria) throws CoreException {
        // TODO 效能需優化
        List<T> objs = getList(criteria, null);
        for (T obj : objs) {
            em.remove(obj);
        }
        return objs.size();
    }

    /**
     * Deletes an object from the database by its primary key.
     *
     * @param oid the primary key of the object to be deleted
     * @throws CoreException if an error occurs while deleting the object
     */
    @Override
    public void deleteByPK(Serializable oid) throws CoreException {
        T obj = get(oid);
        delete(obj);
    }

    @Override
    public int executeUpdate(String queryName, Map<String, Serializable> attrs) throws CoreException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'executeUpdate'");
    }

    /**
     * Flushes the changes made to the entity manager.
     *
     * @throws CoreException if an error occurs while flushing the changes.
     */
    @Override
    public void flush() throws CoreException {
        em.flush();
    }

    @Override
    public List getAttributes(String[] attributeNames, CommonCriteria criteria, String[] sortOrder)
            throws CoreException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAttributes'");
    }

    @Override
    public List<T> getAttributesPageable(String[] attributeNames, CommonCriteria criteria, String[] sortOrder,
            int startNode, int returnSize) throws CoreException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAttributesPageable'");
    }

    @Override
    public List<T> getList(CommonCriteria criteria, String[] sortOrder) throws CoreException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getList'");
    }

    @Override
    public List<T> getListPageable(CommonCriteria criteria, String[] sortOrder, int startNode, int returnSize)
            throws CoreException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getListPageable'");
    }

    @Override
    public Number getListSize(CommonCriteria criteria) throws CoreException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getListSize'");
    }

    @Override
    public Map<String, T> getMap(String mapKey, CommonCriteria criteria, String[] sortOrder) throws CoreException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMap'");
    }

    @Override
    public List getNameQuery(String queryName, Map<String, Serializable> attrs, int firstResult, int maxResults)
            throws CoreException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNameQuery'");
    }

    @Override
    public List getQueryByList(String queryString, List attrs, int firstResult, int maxResults) throws CoreException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getQueryByList'");
    }

    @Override
    public List getQueryByMap(String queryString, Map attrs, int firstResult, int maxResults) throws CoreException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getQueryByMap'");
    }

    @Override
    public T getSingle(CommonCriteria criteria, String[] sortOrder) throws CoreException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSingle'");
    }

    /**
     * Retrieves a single object of type T from the database based on the specified
     * key-value pair.
     *
     * @param key   the name of the column to search for
     * @param value the value to search for in the specified column
     * @return the single object of type T that matches the specified key-value pair
     * @throws CoreException if there is an error executing the query or if no
     *                       result is found
     */
    @Override
    public T getSingle(String key, Serializable value) throws CoreException {
        return (T) em.createQuery("from " + persistenceUnitName + " where " + key + " = :key")
                .setParameter("key", value).getSingleResult();
    }

    /**
     * Saves or updates an object of type T in the database.
     *
     * @param obj the object to be saved or updated
     * @return the saved or updated object
     * @throws CoreException if there is an error while saving or updating the
     *                       object
     */
    @Override
    public T saveOrUpdate(T obj) throws CoreException {
        em.persist(obj);
        return obj;
    }

    /**
     * Saves or updates a collection of objects in the database.
     *
     * @param objs the collection of objects to be saved or updated
     * @throws CoreException if an error occurs while saving or updating the objects
     */
    @Override
    public void saveOrUpdateBatch(Collection<T> objs) throws CoreException {
        for (T obj : objs) {
            em.persist(obj);
        }
    }

}
