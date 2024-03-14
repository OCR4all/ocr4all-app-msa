/**
 * File:     JobResponse.java
 * Package:  de.uniwuerzburg.zpd.ocr4all.application.msa.api.domain
 * 
 * Author:   Herbert Baier (herbert.baier@uni-wuerzburg.de)
 * Date:     14.03.2024
 */
package de.uniwuerzburg.zpd.ocr4all.application.msa.api.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.uniwuerzburg.zpd.ocr4all.application.msa.job.Job;
import de.uniwuerzburg.zpd.ocr4all.application.msa.job.SchedulerService;

/**
 * Defines job responses for the api.
 *
 * @author <a href="mailto:herbert.baier@uni-wuerzburg.de">Herbert Baier</a>
 * @version 1.0
 * @since 17
 */
public class JobResponse implements Serializable {
	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The id.
	 */
	private final int id;

	/**
	 * The state.
	 */
	private final Job.State state;

	/**
	 * The created time.
	 */
	@JsonProperty("created-time")
	private final Date created;

	/**
	 * The start time.
	 */
	@JsonProperty("start-time")
	private final Date start;

	/**
	 * The end time.
	 */
	@JsonProperty("end-time")
	private final Date end;

	/**
	 * The thread pool.
	 */
	@JsonProperty("thread-pool")
	private final SchedulerService.ThreadPool threadPool;

	/**
	 * The key.
	 */
	private final String key;

	/**
	 * The description.
	 */
	private final String description;

	/**
	 * The message.
	 */
	private final String message;

	/**
	 * Creates a job response for the api.
	 * 
	 * @since 17
	 */
	public JobResponse(Job job) {
		super();

		id = job.getId();
		state = job.getState();
		created = job.getCreated();
		start = job.getStart();
		end = job.getEnd();
		threadPool = job.getThreadPool();
		key = job.getKey();
		description = job.getDescription();
		message = job.getMessage();
	}

	/**
	 * Returns the id.
	 *
	 * @return The id.
	 * @since 17
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the state.
	 *
	 * @return The state.
	 * @since 17
	 */
	public Job.State getState() {
		return state;
	}

	/**
	 * Returns the created time.
	 *
	 * @return The created time.
	 * @since 17
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * Returns the start time.
	 *
	 * @return The start time.
	 * @since 17
	 */
	public Date getStart() {
		return start;
	}

	/**
	 * Returns the end time.
	 *
	 * @return The end time.
	 * @since 17
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * Returns the thread pool.
	 *
	 * @return The thread pool.
	 * @since 17
	 */
	public SchedulerService.ThreadPool getThreadPool() {
		return threadPool;
	}

	/**
	 * Returns the key.
	 *
	 * @return The key.
	 * @since 17
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Returns the description.
	 *
	 * @return The description.
	 * @since 17
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the message.
	 *
	 * @return The message.
	 * @since 17
	 */
	public String getMessage() {
		return message;
	}

}
