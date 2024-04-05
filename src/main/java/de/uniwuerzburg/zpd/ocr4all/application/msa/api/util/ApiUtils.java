/**
 * File:     ApiUtils.java
 * Package:  de.uniwuerzburg.zpd.ocr4all.application.msa.api.util
 * 
 * Author:   Herbert Baier (herbert.baier@uni-wuerzburg.de)
 * Date:     05.04.2024
 */
package de.uniwuerzburg.zpd.ocr4all.application.msa.api.util;

import de.uniwuerzburg.zpd.ocr4all.application.communication.msa.api.domain.JobResponse;
import de.uniwuerzburg.zpd.ocr4all.application.communication.msa.api.domain.SystemJobResponse;
import de.uniwuerzburg.zpd.ocr4all.application.msa.job.Job;
import de.uniwuerzburg.zpd.ocr4all.application.msa.job.SystemJob;

/**
 * Defines api utilities.
 *
 * @author <a href="mailto:herbert.baier@uni-wuerzburg.de">Herbert Baier</a>
 * @version 1.0
 * @since 17
 */
public class ApiUtils {
	/**
	 * Returns the job response for the api.
	 * 
	 * @param job The job.
	 * @return The job response for the api.
	 * @since 17
	 */
	public static JobResponse getJobResponse(Job job) {
		return new JobResponse(job.getId(), job.getState(), job.getCreated(), job.getStart(), job.getEnd(),
				job.getThreadPool(), job.getKey(), job.getDescription(), job.getMessage());
	}

	/**
	 * Returns the system job response for the api.
	 * 
	 * @param job The system job.
	 * @return The system job response for the api.
	 * @since 17
	 */
	public static SystemJobResponse getSystemJobResponse(SystemJob job) {
		return new SystemJobResponse(job.getId(), job.getState(), job.getStandardOutput(), job.getStandardError(),
				job.getExitValue());
	}

}
