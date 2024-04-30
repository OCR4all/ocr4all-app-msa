/**
 * File:     CoreJob.java
 * Package:  de.uniwuerzburg.zpd.ocr4all.application.msa.job
 * 
 * Author:   Herbert Baier (herbert.baier@uni-wuerzburg.de)
 * Date:     05.04.2024
 */
package de.uniwuerzburg.zpd.ocr4all.application.msa.job;

import de.uniwuerzburg.zpd.ocr4all.application.communication.msa.job.State;

/**
 * Defines core jobs.
 *
 * @author <a href="mailto:herbert.baier@uni-wuerzburg.de">Herbert Baier</a>
 * @version 1.0
 * @since 17
 */
public interface CoreJob {
	/**
	 * Returns the id. 0 if not set, this means, it is not under the control of the
	 * scheduler.
	 *
	 * @return The id.
	 * @since 1.8
	 */
	public int getId();

	/**
	 * Returns true if the job is done.
	 *
	 * @return True if the job is done.
	 * @since 1.8
	 */
	public boolean isDone();

	/**
	 * Returns the state.
	 *
	 * @return The state.
	 * @since 1.8
	 */
	public State getState();

}
