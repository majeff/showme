/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.dao.CoreDao
   Module Description   :

   Date Created      : 2008/3/21
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
 * @author jeffma 由 Service Bane 自行決定操作的 entity
 */
@SuppressWarnings("rawtypes")
public interface CoreDao {
	/**
	 * Delete object
	 *
	 * @param obj
	 * @throws CoreException
	 */
	public void delete(Object obj) throws CoreException;

	/**
	 * @param objects
	 * @throws CoreException
	 */
	public void deleteBatch(Collection objects) throws CoreException;

	/**
	 * @param classObj return object
	 * @param equal key=value
	 * @param nonequal key!=value
	 * @param moreequal key>=value
	 * @param lessequal key<=value
	 * @return delete size
	 * @throws CoreException
	 */
	public int deleteByAttributes(Class classObj, CommonCriteria criteria) throws CoreException;

	/**
	 * Delete object by primary key
	 *
	 * @param classObj
	 * @param oid
	 * @throws CoreException
	 */
	public void deleteByPK(Class classObj, Serializable oid) throws CoreException;

	/**
	 * @param queryName
	 * @param attrs
	 * @return
	 * @throws CoreException
	 */
	public int executeUpdate(String queryName, Map attrs) throws CoreException;

	/**
	 * @throws CoreException
	 */
	public void flush() throws CoreException;

	/**
	 * Get objects by primary key
	 *
	 * @param classObj
	 * @param oid hibernate id
	 * @return object instance
	 * @throws CoreException
	 */
	public Object get(Class classObj, Serializable oid) throws CoreException;

	/**
	 * @param classObj
	 * @param attributeNames
	 * @param equal key=value
	 * @param nonequal key!=value
	 * @param moreequal key>=value
	 * @param lessequal key<=value
	 * @param sortOrder
	 * @return result
	 * @throws CoreException
	 */
	public List getAttributes(Class classObj, String[] attributeNames, CommonCriteria criteria, String[] sortOrder)
			throws CoreException;

	/**
	 * @param classObj
	 * @param attributeNames
	 * @param conditions
	 * @param nonconditions
	 * @param more
	 * @param less
	 * @param sortOrder
	 * @param startNode
	 * @param returnSize
	 * @return result
	 * @throws CoreException
	 */
	public List getAttributesPageable(Class classObj, String[] attributeNames, CommonCriteria criteria,
			String[] sortOrder, int startNode, int returnSize) throws CoreException;

	/**
	 * @param classObj return object
	 * @param equal key=value
	 * @param nonequal key!=value
	 * @param moreequal key>=value
	 * @param lessequal key<=value
	 * @param sortOrder
	 * @return classObj list
	 * @throws CoreException
	 */
	public List getList(Class classObj, CommonCriteria criteria, String[] sortOrder) throws CoreException;

	/**
	 * @param classObj
	 * @param conditions
	 * @param nonconditions
	 * @param more
	 * @param less
	 * @param sortOrder
	 * @param startNode
	 * @param returnSize
	 * @return classObj object list
	 * @throws CoreException
	 */
	public List getListPageable(Class classObj, CommonCriteria criteria, String[] sortOrder, int startNode, int returnSize)
			throws CoreException;

	/**
	 * @param classObj
	 * @param conditions
	 * @param nonconditions
	 * @param more
	 * @param less
	 * @return object size
	 * @throws CoreException
	 */
	public Number getListSize(Class classObj, CommonCriteria criteria) throws CoreException;

	/**
	 * @param classObj
	 * @param mapKey
	 * @param equal
	 * @param nonequal
	 * @param moreequal
	 * @param lessequal
	 * @param sortOrder
	 * @return classObj object map
	 * @throws CoreException
	 */
	public Map getMap(Class classObj, String mapKey, CommonCriteria criteria, String[] sortOrder) throws CoreException;

	/**
	 * @param queryName
	 * @param attrs
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List getNameQuery(String queryName, Map attrs, int firstResult, int maxResults) throws CoreException;

	/**
	 * @param queryString
	 * @param attrs
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws CoreException
	 */
	public List getQueryByList(String queryString, List attrs, int firstResult, int maxResults) throws CoreException;

	/**
	 * @param queryString
	 * @param attrs
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws CoreException
	 */
	public List getQueryByMap(String queryString, Map attrs, int firstResult, int maxResults) throws CoreException;

	/**
	 * @param classObj
	 * @param classObj return object
	 * @param conditions key=value
	 * @param nonconditions key!=value
	 * @param more key>value
	 * @param less key<value
	 * @param sortOrder
	 * @return object instance
	 * @throws CoreException
	 */
	public Object getSingle(Class classObj, CommonCriteria criteria, String[] sortOrder) throws CoreException;

	/**
	 * get first object by object.attribute = value
	 *
	 * @param classObj
	 * @param key object attribute name
	 * @param value condition
	 * @return first object
	 * @throws CoreException
	 */
	public Object getSingle(Class classObj, String key, Serializable value) throws CoreException;

	// /**
	//  * @param queryString
	//  * @param attrs
	//  * @param firstResult
	//  * @param maxResults
	//  * @return
	//  * @throws CoreException
	//  */
	// public List getSQLQueryByList(String queryString, List attrs, int firstResult, int maxResults) throws CoreException;

	// /**
	//  * @param classObj
	//  * @param oid hibernate id
	//  * @return object instance
	//  * @throws CoreException
	//  */
	// public Object load(Class classObj, Serializable oid) throws CoreException;

	// /**
	//  * Save object
	//  *
	//  * @param obj
	//  * @return object instance
	//  * @throws CoreException
	//  */
	// public Object save(Object obj) throws CoreException;

	/**
	 * Save or update object
	 *
	 * @param obj
	 * @return object instance
	 * @throws CoreException
	 */
	public Object saveOrUpdate(Object obj) throws CoreException;

	/**
	 * @param objs
	 * @throws CoreException
	 */
	public void saveOrUpdateBatch(Collection objs) throws CoreException;

	// /**
	//  * Update object
	//  *
	//  * @param obj
	//  * @return object instance
	//  * @throws CoreException
	//  */
	// public Object update(Object obj) throws CoreException;

}
