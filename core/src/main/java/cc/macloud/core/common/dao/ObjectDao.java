/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.dao.ObjectDao
   Module Description   :

   Date Created      : 2008/3/14
   Original Author   : jeffma
   Team              :
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.common.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.utils.dao.CommonCriteria;

/**
 * @author jeffma
 */
@SuppressWarnings("rawtypes")
public interface ObjectDao<T> {
	/**
	 * Save object
	 *
	 * @param obj
	 * @return object instance
	 * @throws CoreException
	 */
	public T save(T obj) throws CoreException;

	/**
	 * Update object
	 *
	 * @param obj
	 * @return object instance
	 * @throws CoreException
	 */
	public T update(T obj) throws CoreException;

	/**
	 * Delete object
	 *
	 * @param obj
	 * @throws CoreException
	 */
	public void delete(T obj) throws CoreException;

	/**
	 * Get object by primary key
	 *
	 * @param oid hibernate id
	 * @return object instance
	 * @throws CoreException
	 */
	public T get(Serializable oid) throws CoreException;

    /**
     * Delete a batch of objects.
     *
     * @param objects the collection of objects to delete
     * @throws CoreException if an error occurs during the deletion
     */
    public void deleteBatch(Collection<T> objects) throws CoreException;

    /**
     * Delete objects based on the specified criteria.
     *
     * @param criteria the criteria for deletion
     * @return the number of objects deleted
     * @throws CoreException if an error occurs during the deletion
     */
    public int deleteByAttributes(CommonCriteria criteria) throws CoreException;

    /**
     * Delete an object by its primary key.
     *
     * @param oid the primary key of the object to delete
     * @throws CoreException if an error occurs during the deletion
     */
    public void deleteByPK(Serializable oid) throws CoreException;

    /**
     * Executes an update query with the given query name and attributes.
     *
     * @param queryName the name of the query to execute
     * @param attrs the attributes to be used in the query
     * @return the number of rows affected by the update
     * @throws CoreException if an error occurs during the execution of the query
     */
    public int executeUpdate(String queryName, Map<String, Serializable> attrs) throws CoreException;

    /**
     * Flushes any pending changes to the underlying data store.
     *
     * @throws CoreException if an error occurs during the flush operation
     */
    public void flush() throws CoreException;

    /**
     * Retrieves the specified attributes of objects based on the given criteria.
     *
     * @param attributeNames the names of the attributes to retrieve
     * @param criteria the criteria for attribute retrieval
     * @param sortOrder the sort order for the retrieved attributes
     * @return a list of objects with the specified attributes
     * @throws CoreException if an error occurs during attribute retrieval
     */
    public List getAttributes(String[] attributeNames, CommonCriteria criteria, String[] sortOrder) throws CoreException;

    /**
     * Retrieves a pageable list of objects with the specified attributes based on the given criteria.
     *
     * @param attributeNames the names of the attributes to retrieve
     * @param criteria the criteria for attribute retrieval
     * @param sortOrder the sort order for the retrieved attributes
     * @param startNode the starting index of the pageable list
     * @param returnSize the number of objects to return in the pageable list
     * @return a list of objects with the specified attributes
     * @throws CoreException if an error occurs during attribute retrieval
     */
    public List<T> getAttributesPageable(String[] attributeNames, CommonCriteria criteria, String[] sortOrder,
	    int startNode, int returnSize) throws CoreException;

    /**
     * Retrieves a list of objects based on the given criteria.
     *
     * @param criteria the criteria for object retrieval
     * @param sortOrder the sort order for the retrieved objects
     * @return a list of objects that match the criteria
     * @throws CoreException if an error occurs during object retrieval
     */
    public List<T> getList(CommonCriteria criteria, String[] sortOrder) throws CoreException;

    /**
     * Retrieves a pageable list of objects based on the given criteria.
     *
     * @param criteria the criteria for object retrieval
     * @param sortOrder the sort order for the retrieved objects
     * @param startNode the starting index of the pageable list
     * @param returnSize the number of objects to return in the pageable list
     * @return a list of objects that match the criteria
     * @throws CoreException if an error occurs during object retrieval
     */
    public List<T> getListPageable(CommonCriteria criteria, String[] sortOrder, int startNode, int returnSize)
	    throws CoreException;

    /**
     * Retrieves the number of objects that match the given criteria.
     *
     * @param criteria the criteria for object retrieval
     * @return the number of objects that match the criteria
     * @throws CoreException if an error occurs during object retrieval
     */
    public Number getListSize(CommonCriteria criteria) throws CoreException;

    /**
     * Retrieves a map of objects with the specified map key based on the given criteria.
     *
     * @param mapKey the key to use for the map
     * @param criteria the criteria for object retrieval
     * @param sortOrder the sort order for the retrieved objects
     * @return a map of objects with the specified map key
     * @throws CoreException if an error occurs during object retrieval
     */
    public Map<String, T> getMap(String mapKey, CommonCriteria criteria, String[] sortOrder) throws CoreException;

    /**
     * Retrieves a single object based on the given criteria.
     *
     * @param criteria the criteria for object retrieval
     * @param sortOrder the sort order for the retrieved object
     * @return the single object that matches the criteria
     * @throws CoreException if an error occurs during object retrieval
     */
    public T getSingle(CommonCriteria criteria, String[] sortOrder) throws CoreException;

    /**
     * Retrieves a single object based on the given key-value pair.
     *
     * @param key the key of the attribute to match
     * @param value the value of the attribute to match
     * @return the single object that matches the key-value pair
     * @throws CoreException if an error occurs during object retrieval
     */
    public T getSingle(String key, Serializable value) throws CoreException;

    /**
     * Save or update an object.
     *
     * @param obj the object to save or update
     * @return the saved or updated object instance
     * @throws CoreException if an error occurs during save or update
     */
    public T saveOrUpdate(T obj) throws CoreException;

    /**
     * Save or update a batch of objects.
     *
     * @param objs the collection of objects to save or update
     * @throws CoreException if an error occurs during save or update
     */
    public void saveOrUpdateBatch(Collection<T> objs) throws CoreException;
}
