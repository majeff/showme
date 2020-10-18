/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.dao.impl.PropertiesHibernateDaoImpl
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
package cc.macloud.core.common.dao.impl;

import java.util.Properties;

import org.hibernate.SessionFactory;

/**
 * @author jeffma
 * 
 */
@SuppressWarnings("rawtypes")
public class PropertiesHibernateDaoImpl extends HibernateObjectDaoImpl {

	protected Properties hqlProps;

	/**
	 * default constructors
	 * 
	 * @throws ClassNotFoundException
	 */
	public PropertiesHibernateDaoImpl(String className) throws ClassNotFoundException {
		super(className);
	}

	/**
	 * default constructors
	 * 
	 * @throws ClassNotFoundException
	 */
	public PropertiesHibernateDaoImpl(String entityName, SessionFactory sessionFactory) throws ClassNotFoundException {
		super(entityName, sessionFactory);
	}

	/**
	 * @param hqlProps the hqlProps to set
	 */
	public void setHqlProps(Properties hqlProps) {
		this.hqlProps = hqlProps;
	}
}
