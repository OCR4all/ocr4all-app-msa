/**
 * File:     SchedulerService.java
 * Package:  de.uniwuerzburg.zpd.ocr4all.application.msa.job
 * 
 * Author:   Herbert Baier (herbert.baier@uni-wuerzburg.de)
 * Date:     12.03.2024
 */
package de.uniwuerzburg.zpd.ocr4all.application.msa.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import de.uniwuerzburg.zpd.ocr4all.application.communication.message.Message;
import de.uniwuerzburg.zpd.ocr4all.application.communication.message.spi.EventSPI;
import de.uniwuerzburg.zpd.ocr4all.application.msa.message.WebSocketService;

/**
 * Defines scheduler services.
 *
 * @author <a href="mailto:herbert.baier@uni-wuerzburg.de">Herbert Baier</a>
 * @version 1.0
 * @since 1.8
 */
@Service
@ApplicationScope
public class SchedulerService {
	/**
	 * The logger.
	 */
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SchedulerService.class);

	/**
	 * The prefix to use for the names of newly created threads by task executor.
	 */
	private static final String taskExecutorThreadNamePrefix = "job";

	/**
	 * Defines thread pools.
	 *
	 * @author <a href="mailto:herbert.baier@uni-wuerzburg.de">Herbert Baier</a>
	 * @version 1.0
	 * @since 1.8
	 */
	public enum ThreadPool {
		/**
		 * The standard thread pool.
		 */
		standard("std"),
		/**
		 * The time-consuming thread pool.
		 */
		timeConsuming("tc");

		/**
		 * The label.
		 */
		private final String label;

		/**
		 * Creates a thread pool.
		 * 
		 * @param label The label.
		 * @since 1.8
		 */
		private ThreadPool(String label) {
			this.label = label;
		}

		/**
		 * Returns the label.
		 *
		 * @return The label.
		 * @since 1.8
		 */
		public String getLabel() {
			return label;
		}

	}

	/**
	 * The job id.
	 */
	private int id = 0;

	/**
	 * The jobs. The key is the job id.
	 */
	private final Set<Job> jobs = new HashSet<>();

	/**
	 * The start time.
	 */
	private final Date start = new Date();

	/**
	 * The thread pool for standard jobs.
	 */
	private final ThreadPoolTaskExecutor threadPoolStandard;

	/**
	 * The thread pool for time-consuming jobs.
	 */
	private final ThreadPoolTaskExecutor threadPoolTimeConsuming;

	/**
	 * The WebSocket service.
	 */
	private final WebSocketService webSocketService;

	/**
	 * Creates a scheduler service.
	 * 
	 * @param threadPoolCoreSize          The size of the thread pool for core jobs.
	 * @param threadPoolTimeConsumingSize The size of the thread pool for
	 *                                    time-consuming jobs.
	 * @param webSocketService            The WebSocket service.
	 * @since 17
	 */
	public SchedulerService(@Value("${ocr4all.thread.pool.size.standard}") int threadPoolCoreSize,
			@Value("${ocr4all.thread.pool.size.time-consuming}") int threadPoolTimeConsumingSize,
			WebSocketService webSocketService) {
		super();

		/*
		 * The thread pools
		 */
		threadPoolStandard = createThreadPool(ThreadPool.standard.getLabel(), threadPoolCoreSize);
		threadPoolTimeConsuming = createThreadPool(ThreadPool.timeConsuming.getLabel(), threadPoolTimeConsumingSize);

		this.webSocketService = webSocketService;
	}

	/**
	 * Creates a thread pool.
	 * 
	 * @param threadName   The thread name.
	 * @param corePoolSize The core pool size.
	 * @return The thread pool.
	 * @since 1.8
	 */
	private ThreadPoolTaskExecutor createThreadPool(String threadName, int corePoolSize) {
		String name = taskExecutorThreadNamePrefix + "-" + threadName;

		ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();

		threadPool.setThreadNamePrefix(name + "-");
		threadPool.setCorePoolSize(corePoolSize);
		threadPool.setWaitForTasksToCompleteOnShutdown(false);

		threadPool.afterPropertiesSet();

		logger.info("created thread pool '" + name + "' with size " + corePoolSize + ".");

		return threadPool;
	}

	/**
	 * Starts the job if it is not under scheduler control.
	 * 
	 * @param job The job to start.
	 * @since 1.8
	 */
	public void start(Job job) {
		if (job != null && !job.isSchedulerControl()) {
			job.schedule(
					ThreadPool.timeConsuming.equals(job.getThreadPool()) ? threadPoolTimeConsuming : threadPoolStandard,
					++id, entity -> sendEvent(entity));

			synchronized (job) {
				jobs.add(job);
			}
		}
	}

	/**
	 * Broadcasts an event to the registered clients on the WebSocket if the job
	 * state is not 'initialized'.
	 * 
	 * @param job The job. It is mandatory and cannot be null.
	 * @since 17
	 */
	private void sendEvent(Job job) {
		EventSPI.Type type;
		switch (job.getState()) {
		case scheduled:
			type = EventSPI.Type.scheduled;
			break;
		case running:
			type = EventSPI.Type.running;
			break;
		case canceled:
			type = EventSPI.Type.canceled;
			break;
		case completed:
			type = EventSPI.Type.completed;
			break;
		case interrupted:
			type = EventSPI.Type.interrupted;
			break;
		default:
			type = null;
			break;
		}

		if (type != null)
			webSocketService.broadcast(new EventSPI(type, job.getKey(), new Message(job.getMessage())));
	}

	/**
	 * Returns the job with given id.
	 * 
	 * @param id The job id.
	 * @return The job with given id. Null if unknown.
	 * @since 17
	 */
	public Job getJob(int id) {
		if (id > 0)
			for (Job job : jobs)
				if (id == job.getId())
					return job;

		return null;
	}

	/**
	 * Sorts the given jobs by id and returns it.
	 * 
	 * @param jobs The jobs to sort.
	 * @return The sorted jobs.
	 * @since 17
	 */
	private List<Job> sort(Collection<Job> jobs) {
		List<Job> sorted = new ArrayList<>(jobs);

		Collections.sort(sorted, new Comparator<Job>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(Job j1, Job j2) {
				return j1.getId() - j2.getId();
			}

		});

		return sorted;
	}

	/**
	 * Returns the jobs.
	 * 
	 * @return The jobs.
	 * @since 17
	 */
	public List<Job> getJobs() {
		return sort(jobs);
	}

	/**
	 * Returns the running jobs.
	 * 
	 * @return The running jobs.
	 * @since 17
	 */
	public List<Job> getJobsRunning() {
		return sort(jobs.stream().filter(job -> !job.isDone()).collect(Collectors.toSet()));
	}

	/**
	 * Returns the done jobs.
	 * 
	 * @return The done jobs.
	 * @since 17
	 */
	public List<Job> getJobsDone() {
		return sort(jobs.stream().filter(job -> job.isDone()).collect(Collectors.toSet()));
	}

	/**
	 * Cancels the job.
	 * 
	 * @param id The job id.
	 * @since 1.8
	 */
	public void cancel(int id) {
		cancel(getJob(id));
	}

	/**
	 * Cancels the job.
	 * 
	 * @param job The job to cancel.
	 * @since 1.8
	 */
	public void cancel(Job job) {
		if (job != null && jobs.contains(job))
			job.cancel();
	}

	/**
	 * Expunges the done jobs.
	 * 
	 * @since 1.8
	 */
	public void expunge() {
		synchronized (jobs) {
			jobs.removeIf(job -> job.isDone());
		}
	}

	/**
	 * Expunges the given job if it is done.
	 * 
	 * @param id The job id.
	 * @return True if the job could be expunged.
	 * @since 1.8
	 */
	public boolean expunge(int id) {
		return expunge(getJob(id));
	}

	/**
	 * Expunges the given job if it is done.
	 * 
	 * @param id The job.
	 * @return True if the job could be expunged.
	 * @since 1.8
	 */
	public synchronized boolean expunge(Job job) {
		if (job != null && job.isDone()) {
			synchronized (jobs) {
				return jobs.remove(job);
			}
		} else
			return false;
	}

	/**
	 * Returns the current scheduler information.
	 * 
	 * @return The current scheduler information.
	 * @since 17
	 */
	public Information getInformation() {
		return new Information();
	}

	/**
	 * Defines scheduler informations.
	 *
	 * @author <a href="mailto:herbert.baier@uni-wuerzburg.de">Herbert Baier</a>
	 * @version 1.0
	 * @since 17
	 */
	public class Information {
		/**
		 * The thread pool informations.
		 */
		private final List<ThreadPoolInformation> threadPools = new ArrayList<>();
		{
			threadPools.add(new ThreadPoolInformation("standard", threadPoolStandard));
			threadPools.add(new ThreadPoolInformation("time consuming", threadPoolTimeConsuming));
		}

		/**
		 * Default constructor for a scheduler information.
		 * 
		 * @since 17
		 */
		private Information() {
			super();
		}

		/**
		 * Returns the scheduler start time.
		 *
		 * @return The scheduler start time.
		 * @since 1.8
		 */
		public Date getStart() {
			return start;
		}

		/**
		 * Returns the thread pool informations.
		 *
		 * @return The thread pool informations.
		 * @since 17
		 */
		public List<ThreadPoolInformation> getThreadPools() {
			return threadPools;
		}

		/**
		 * Defines thread pool informations.
		 *
		 * @author <a href="mailto:herbert.baier@uni-wuerzburg.de">Herbert Baier</a>
		 * @version 1.0
		 * @since 17
		 */
		public static class ThreadPoolInformation {
			/**
			 * The name.
			 */
			private final String name;

			/**
			 * The thread name prefix.
			 */
			private final String prefix;

			/**
			 * The number of currently active threads.
			 */
			private final int activeThreads;

			/**
			 * The core pool size.
			 */
			private final int corePoolSize;

			/**
			 * Creates a thread pool information.
			 * 
			 * @param name       The name.
			 * @param threadPool The thread pool.
			 * @since 17
			 */
			public ThreadPoolInformation(String name, ThreadPoolTaskExecutor threadPool) {
				super();

				this.name = name;

				prefix = threadPool.getThreadNamePrefix();
				activeThreads = threadPool.getActiveCount();
				corePoolSize = threadPool.getCorePoolSize();
			}

			/**
			 * Returns the name.
			 *
			 * @return The name.
			 * @since 17
			 */
			public String getName() {
				return name;
			}

			/**
			 * Returns the thread name prefix.
			 *
			 * @return The thread name prefix.
			 * @since 17
			 */
			public String getPrefix() {
				return prefix;
			}

			/**
			 * Returns the number of currently active threads.
			 *
			 * @return The number of currently active threads.
			 * @since 17
			 */
			public int getActiveThreads() {
				return activeThreads;
			}

			/**
			 * Returns the core pool size.
			 *
			 * @return The core pool size.
			 * @since 17
			 */
			public int getCorePoolSize() {
				return corePoolSize;
			}

		}
	}
}
