/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.batch.AbstractRunJob
   Module Description   :

   Date Created      : 2011/3/24
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.common.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jeffma
 * 
 */
public abstract class AbstractRunJob implements Runnable {

	/** logger */
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private final String code;

	/** default constructor */
	public AbstractRunJob(String code) {
		super();
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		logger.info("start code:{}", code);
		execute();
		logger.info("end code:{}", code);
	}

	public abstract void execute();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RunJob [code=" + code + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof AbstractRunJob) && (code != null)) {
			return code.equals(((AbstractRunJob) obj).getCode());
		} else {
			return super.equals(obj);
		}
	}

}
