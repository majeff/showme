/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.common.service.DomainService
   Module Description   :

   Date Created      : 2008/12/19
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core.common.service;

import java.io.Serializable;
import java.util.List;

import com.showmoney.core.common.dao.impl.CommonCriteria;
import com.showmoney.core.common.exception.CoreException;

/**
 * @author jeffma
 * 
 */
public interface DomainService<T> {

	public T get(Serializable oid) throws CoreException;

	public T save(T entity) throws CoreException;

	public void delete(T entity) throws CoreException;

	public List<T> getAll(int firstResult, int maxResults, String[] sortOrder) throws CoreException;

	public Number getAllSize() throws CoreException;

	public List<T> getList(int firstResult, int maxResults, CommonCriteria criteria, String[] sortOrder)
			throws CoreException;

	public Number getListSize(CommonCriteria criteria) throws CoreException;

	public T getSingle(CommonCriteria criteria, String[] sortOrder) throws CoreException;
}
