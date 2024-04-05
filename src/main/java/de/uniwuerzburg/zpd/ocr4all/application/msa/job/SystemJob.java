/**
 * File:     SystemJob.java
 * Package:  de.uniwuerzburg.zpd.ocr4all.application.msa.job
 * 
 * Author:   Herbert Baier (herbert.baier@uni-wuerzburg.de)
 * Date:     05.04.2024
 */
package de.uniwuerzburg.zpd.ocr4all.application.msa.job;

/**
 * Defines system jobs.
 *
 * @author <a href="mailto:herbert.baier@uni-wuerzburg.de">Herbert Baier</a>
 * @version 1.0
 * @since 17
 */
public interface SystemJob extends CoreJob {
	/**
	 * Returns the system process standard output.
	 *
	 * @return The system process standard output.
	 * @since 1.8
	 */
	public String getStandardOutput();

	/**
	 * Returns the system process standard error.
	 *
	 * @return The system process standard error.
	 * @since 1.8
	 */
	public String getStandardError();

	/**
	 * Returns the exit value. By convention, the value 0 indicates normal
	 * termination. -1 if the exit value is not set.
	 *
	 * @return The exit value.
	 * @since 1.8
	 */
	public int getExitValue();

}
