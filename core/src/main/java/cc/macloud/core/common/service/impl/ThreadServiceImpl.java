/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.service.impl.ThreadServiceImpl
   Module Description   :

   Date Created      : 2008/6/2
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.common.service.impl;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import cc.macloud.core.common.batch.AbstractRunJob;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.ThreadService;

/**
 * @author jeffma
 * 
 */
public final class ThreadServiceImpl implements ThreadService, InitializingBean, DisposableBean {

	/** logger */
	private final Logger logger = LoggerFactory.getLogger(ThreadServiceImpl.class);

	// private BlockingQueue<Runnable> taskQueue = null; // Blocking queue used to hold waiting threads
	// private ThreadPoolExecutor taskPool = null; // Thread pool used to hold running threads
	private ScheduledThreadPoolExecutor taskPool = null;
	private int timeout = 0;
	/** corePoolSize, 起始 thread size */
	private int corePoolSize = 10;

	/** default constructors */
	public ThreadServiceImpl(int corePoolSize) {
		super();
		this.corePoolSize = corePoolSize;
	}

	/** default constructors */
	public ThreadServiceImpl() {
		super();
	}

	/**
	 * @param timeout
	 *           the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
		if (timeout > 0) {
			taskPool.setKeepAliveTime(timeout, TimeUnit.SECONDS);
			taskPool.allowCoreThreadTimeOut(true);
		} else {
			taskPool.allowCoreThreadTimeOut(false);
		}
	}

	/**
	 * @param corePoolSize
	 *           the corePoolSize to set
	 */
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.agenda.utils.thread.ThreadManager#executeInBackground(java.lang.Runnable )
	 */
	public void executeInBackground(AbstractRunJob runnable) {
		logger.info("op: {}", runnable);

		if (taskPool.getQueue().contains(runnable)) {
			logger.error("duplicate job: " + runnable);
			throw new CoreException("errors.common.thread.duplicate", runnable.getCode());
		}

		taskPool.schedule(runnable, 0, TimeUnit.SECONDS);
	}

	public void scheduleInBackground(AbstractRunJob runnable, long second) {
		logger.info("op: {}", runnable);
		taskPool.schedule(runnable, second, TimeUnit.SECONDS);
	}

	public void scheduleFixdInBackground(AbstractRunJob runnable, long second, long initDelay) {
		logger.info("op: {}", runnable);
		taskPool.scheduleWithFixedDelay(runnable, initDelay, second, TimeUnit.SECONDS);
	}

	public void scheduleRateInBackground(AbstractRunJob runnable, long period, long initDelay) {
		logger.info("op: {}", runnable);
		taskPool.scheduleAtFixedRate(runnable, initDelay, period, TimeUnit.SECONDS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		// taskQueue = new ArrayBlockingQueue(queueSize);
		// taskPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, waitTime, TimeUnit.SECONDS, taskQueue,
		// new ThreadPoolExecutor.AbortPolicy());

		taskPool = new ScheduledThreadPoolExecutor(corePoolSize, new ThreadPoolExecutor.AbortPolicy());

		setTimeout(timeout);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					while ((taskPool.getActiveCount() > 0) || !taskPool.getQueue().isEmpty()) {
						Thread.sleep(1000);
						logger.info("wait job, active:{}, queue:{}", taskPool.getActiveCount(), taskPool.getQueue().size());
					}

					logger.info("threadService down, sleep 1 second");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
		logger.info("done");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.thread.service.ThreadService#showInfo()
	 */
	public String showInfo() {
		StringBuffer sbQ = new StringBuffer();
		for (Runnable task : taskPool.getQueue()) {
			if (sbQ.length() != 0) {
				sbQ.append(",");
			}
			if (task instanceof AbstractRunJob) {
				sbQ.append(((AbstractRunJob) task).getCode());
			} else {
				sbQ.append(task.toString());
			}
		}
		StringBuffer sbRun = new StringBuffer();

		logger.info("core:{}, complete:{}, queue size:{}, timeout:{}, jobs:{}, run:{}",
				new Object[] { taskPool.getActiveCount(), taskPool.getCompletedTaskCount(), taskPool.getQueue().size(),
						taskPool.allowsCoreThreadTimeOut(), sbQ.toString(), sbRun.toString() });
		return sbQ.toString();
	}

}
