/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.exception.CoreException
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
package cc.macloud.core.common.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeffma
 * 
 */
public class CoreException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = 5901382665132444540L;

	/** parameter */
	private List<String> parameters = new ArrayList<String>();

	/** ERROR_DB */
	public static final String ERROR_DB = "errors.system.db";
	/** ERROR_NOTSUPPORT */
	public static final String ERROR_NOTSUPPORT = "errors.system.nosupport";

	/** default constructor */
	public CoreException() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getMessage() + ", parameters=" + parameters;
	}

	/**
	 * default constructor
	 * 
	 * @param i18nKey
	 */
	public CoreException(String i18nKey) {
		super(i18nKey);
	}

	/**
	 * default constructor
	 * 
	 * @param i18nKey
	 * @param arg0
	 *           parameter
	 */
	public CoreException(String i18nKey, String arg0) {
		super(i18nKey);
		parameters.add(arg0);
	}

	/**
	 * default constructor
	 * 
	 * @param i18nKey
	 * @param arg0
	 *           parameter
	 * @param arg1
	 *           parameter
	 */
	public CoreException(String i18nKey, String arg0, String arg1) {
		super(i18nKey);
		parameters.add(arg0);
		parameters.add(arg1);
	}

	/**
	 * default constructor
	 * 
	 * @param i18nKey
	 * @param arg0
	 *           parameter
	 * @param arg1
	 *           parameter
	 * @param arg2
	 *           parameter
	 */
	public CoreException(String i18nKey, String arg0, String arg1, String arg2) {
		super(i18nKey);
		parameters.add(arg0);
		parameters.add(arg1);
		parameters.add(arg2);
	}

	/**
	 * default constructor
	 * 
	 * @param i18nKey
	 * @param e
	 */
	public CoreException(String i18nKey, Throwable e) {
		super(i18nKey, e);
	}

	/**
	 * default constructor
	 * 
	 * @param i18nKey
	 * @param e
	 * @param arg0
	 */
	public CoreException(String i18nKey, Throwable e, String arg0) {
		super(i18nKey, e);
		parameters.add(arg0);
	}

	/**
	 * default constructor
	 * 
	 * @param i18nKey
	 * @param e
	 * @param arg0
	 *           parameter
	 * @param arg1
	 *           parameter
	 */
	public CoreException(String i18nKey, Throwable e, String arg0, String arg1) {
		super(i18nKey, e);
		parameters.add(arg0);
		parameters.add(arg1);
	}

	/**
	 * default constructor
	 * 
	 * @param i18nKey
	 * @param e
	 * @param arg0
	 *           parameter
	 * @param arg1
	 *           parameter
	 * @param arg2
	 *           parameter
	 */
	public CoreException(String i18nKey, Throwable e, String arg0, String arg1, String arg2) {
		super(i18nKey, e);
		parameters.add(arg0);
		parameters.add(arg1);
		parameters.add(arg2);
	}

	/**
	 * @return Returns the parameter.
	 */
	public List<String> getParameter() {
		return this.parameters;
	}

	/**
	 * @param parameter
	 *           The parameter to set.
	 * @return exception instance
	 */
	public CoreException setParameter(List<String> parameter) {
		this.parameters = parameter;
		return this;
	}
}
