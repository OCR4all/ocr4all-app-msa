/**
 * File:     SchedulerController.java
 * Package:  de.uniwuerzburg.zpd.ocr4all.application.msa.api.worker
 * 
 * Author:   Herbert Baier (herbert.baier@uni-wuerzburg.de)
 * Date:     13.03.2024
 */
package de.uniwuerzburg.zpd.ocr4all.application.msa.api.worker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.uniwuerzburg.zpd.ocr4all.application.msa.api.domain.JobResponse;
import de.uniwuerzburg.zpd.ocr4all.application.msa.job.Job;
import de.uniwuerzburg.zpd.ocr4all.application.msa.job.SchedulerService;

/**
 * Defines scheduler controllers for the api.
 *
 * @author <a href="mailto:herbert.baier@uni-wuerzburg.de">Herbert Baier</a>
 * @version 1.0
 * @since 17
 */
@Profile("msa-api")
@RestController
@RequestMapping(path = SchedulerController.contextPath, produces = CoreApiController.applicationJson)
public class SchedulerController extends CoreApiController {
	/**
	 * The context path.
	 */
	public static final String contextPath = apiContextPathVersion_1_0 + "/scheduler";

	/**
	 * The scheduler service.
	 */
	private final SchedulerService service;

	/**
	 * Creates a scheduler controller for the api.
	 * 
	 * @param service The scheduler service.
	 * @since 17
	 */
	public SchedulerController(SchedulerService service) {
		super(SchedulerController.class);

		this.service = service;
	}

	/**
	 * Ping the scheduler.
	 * 
	 * @return The status ok in the response body.
	 * @since 1.8
	 */
	@GetMapping(pingRequestMapping)
	public ResponseEntity<Void> ping() {
		return ResponseEntity.ok().build();
	}

	/**
	 * Returns the scheduler current information in the response body.
	 * 
	 * @return The scheduler current information in the response body.
	 * @since 1.8
	 */
	@GetMapping(informationRequestMapping)
	public ResponseEntity<InformationResponse> information() {
		try {
			return ResponseEntity.ok().body(new InformationResponse(service.getInformation()));
		} catch (Exception ex) {
			log(ex);

			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	/**
	 * Returns the job in the response body.
	 * 
	 * @param id The job id.
	 * @return The job in the response body.
	 * @since 1.8
	 */
	@GetMapping(jobRequestMapping + idPathVariable)
	public ResponseEntity<JobResponse> getJob(@PathVariable int id) {
		Job job = service.getJob(id);

		if (job == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return ResponseEntity.ok().body(new JobResponse(job));
	}

	/**
	 * Returns the response entity for given jobs.
	 * 
	 * @param jobs The jobs.
	 * @return The response entity.
	 * @since 17
	 */
	private ResponseEntity<List<JobResponse>> getJobs(List<Job> jobs) {
		List<JobResponse> response = new ArrayList<>();

		for (Job job : jobs)
			response.add(new JobResponse(job));

		return ResponseEntity.ok().body(response);

	}

	/**
	 * Returns the jobs with given key sorted by id in the response body.
	 * 
	 * @param key The key.
	 * @return The jobs in the response body.
	 * @since 1.8
	 */
	@GetMapping(jobsRequestMapping)
	public ResponseEntity<List<JobResponse>> getJobs(@RequestParam String key) {
		return getJobs(service.getJobs(key));
	}

	/**
	 * Returns the all jobs sorted by id in the response body.
	 * 
	 * @return The jobs in the response body.
	 * @since 1.8
	 */
	@GetMapping(jobsRequestMapping + allRequestMapping)
	public ResponseEntity<List<JobResponse>> getJobs() {
		return getJobs(service.getJobs());
	}

	/**
	 * Returns the running jobs sorted by id in the response body.
	 * 
	 * @return The running jobs in the response body.
	 * @since 1.8
	 */
	@GetMapping(jobsRequestMapping + runningRequestMapping)
	public ResponseEntity<List<JobResponse>> getJobsRunning() {
		return getJobs(service.getJobsRunning());
	}

	/**
	 * Returns the done jobs sorted by id in the response body.
	 * 
	 * @return The running jobs in the response body.
	 * @return The status ok in the response body.
	 * @since 1.8
	 */
	@GetMapping(jobsRequestMapping + doneRequestMapping)
	public ResponseEntity<List<JobResponse>> getJobsDone() {
		return getJobs(service.getJobsDone());
	}

	/**
	 * Cancels the job.
	 * 
	 * @param id The job id.
	 * @return The status ok in the response body.
	 * @since 1.8
	 */
	@GetMapping(cancelRequestMapping + idPathVariable)
	public ResponseEntity<Void> cancel(@PathVariable int id) {
		service.cancel(id);

		return ResponseEntity.ok().build();
	}

	/**
	 * Cancels the jobs with given key.
	 * 
	 * @param key The key.
	 * @return The status ok in the response body.
	 * @since 1.8
	 */
	@GetMapping(cancelRequestMapping)
	public ResponseEntity<Void> cancel(@RequestParam String key) {
		service.cancel(key);

		return ResponseEntity.ok().build();
	}

	/**
	 * Expunges the done jobs.
	 * 
	 * @return The status ok in the response body.
	 * @since 1.8
	 */
	@GetMapping(expungeRequestMapping + doneRequestMapping)
	public ResponseEntity<Void> expunge() {
		service.expunge();

		return ResponseEntity.ok().build();
	}

	/**
	 * Expunges the job if it is done.
	 * 
	 * @param id The job id.
	 * @return The status ok in the response body.
	 * @since 1.8
	 */
	@GetMapping(expungeRequestMapping + idPathVariable)
	public ResponseEntity<Void> expunge(@PathVariable int id) {
		service.expunge(id);

		return ResponseEntity.ok().build();
	}

	/**
	 * Expunges the done jobs with given key.
	 * 
	 * @param key The key.
	 * @return The status ok in the response body.
	 * @since 1.8
	 */
	@GetMapping(expungeRequestMapping)
	public ResponseEntity<Void> expunge(@RequestParam String key) {
		service.expunge(key);

		return ResponseEntity.ok().build();
	}

	/**
	 * Defines information responses for the api.
	 *
	 * @author <a href="mailto:herbert.baier@uni-wuerzburg.de">Herbert Baier</a>
	 * @version 1.0
	 * @since 17
	 */
	public static class InformationResponse implements Serializable {
		/**
		 * The serial version UID.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * The start time.
		 */
		@JsonProperty("start-time")
		private final Date startTime;

		/**
		 * The thread pool informations.
		 */
		@JsonProperty("thread-pools")
		private final List<ThreadPoolInformationResponse> threadPools = new ArrayList<>();

		/**
		 * Creates an information responses for the api.
		 * 
		 * @param information The scheduler current information.
		 * @since 17
		 */
		public InformationResponse(SchedulerService.Information information) {
			super();

			startTime = information.getStart();

			for (SchedulerService.Information.ThreadPoolInformation threadPool : information.getThreadPools())
				threadPools.add(new ThreadPoolInformationResponse(threadPool));
		}

		/**
		 * Returns the start time.
		 *
		 * @return The start time.
		 * @since 17
		 */
		public Date getStartTime() {
			return startTime;
		}

		/**
		 * Returns the thread pool informations.
		 *
		 * @return The thread pool informations.
		 * @since 17
		 */
		public List<ThreadPoolInformationResponse> getThreadPools() {
			return threadPools;
		}

		/**
		 * Defines thread pool information responses for the api.
		 *
		 * @author <a href="mailto:herbert.baier@uni-wuerzburg.de">Herbert Baier</a>
		 * @version 1.0
		 * @since 17
		 */
		public static class ThreadPoolInformationResponse {
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
			@JsonProperty("active-threads")
			private final int activeThreads;

			/**
			 * The core pool size.
			 */
			@JsonProperty("core-pool-size")
			private final int corePoolSize;

			/**
			 * Creates a thread pool information.
			 * 
			 * @param name       The name.
			 * @param threadPool The thread pool.
			 * @since 17
			 */
			public ThreadPoolInformationResponse(SchedulerService.Information.ThreadPoolInformation threadPool) {
				super();

				name = threadPool.getName();

				prefix = threadPool.getPrefix();
				activeThreads = threadPool.getActiveThreads();
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
