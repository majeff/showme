/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 Module Name          : com.showmoney.core.common.dao
 Module Description   :

 Date Created      : Apr 23, 2008
 Original Author   : jeffma
 Team              : 
 ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 MODIFICATION HISTORY
 ------------------------------------------------------------------------------
 Date Modified       Modified by       Comments
 ------------------------------------------------------------------------------
 ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

package com.showmoney.core.common.dao;

import java.io.Serializable;

import com.showmoney.core.common.exception.CoreException;

/**
 * 將操作的 entity 與 dao 綁定
 * 
 * @author jeffma
 */
public interface BasicDao<T> {

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
}
