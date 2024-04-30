/**
 * File:     CoreApiController.java
 * Package:  de.uniwuerzburg.zpd.ocr4all.application.msa.api.worker
 * 
 * Author:   Herbert Baier (herbert.baier@uni-wuerzburg.de)
 * Date:     13.03.2024
 */
package de.uniwuerzburg.zpd.ocr4all.application.msa.api.worker;

/**
 * Defines core controllers for the api.
 *
 * @author <a href="mailto:herbert.baier@uni-wuerzburg.de">Herbert Baier</a>
 * @version 1.0
 * @since 17
 */
public class CoreApiController {
	/**
	 * The api context path.
	 */
	public static final String apiContextPath = "/api";

	/**
	 * The api version 1.0 prefix path.
	 */
	public static final String apiContextPathVersion_1_0 = apiContextPath + "/v1.0";

	/**
	 * The application type json.
	 */
	public static final String applicationJson = "application/json";

	/**
	 * The ping request mapping.
	 */
	public static final String pingRequestMapping = "/ping";

	/**
	 * The information request mapping.
	 */
	public static final String informationRequestMapping = "/information";

	/**
	 * The job request mapping.
	 */
	public static final String jobRequestMapping = "/job";

	/**
	 * The jobs request mapping.
	 */
	public static final String jobsRequestMapping = "/jobs";

	/**
	 * The all request mapping.
	 */
	public static final String allRequestMapping = "/all";

	/**
	 * The running request mapping.
	 */
	public static final String runningRequestMapping = "/running";

	/**
	 * The done request mapping.
	 */
	public static final String doneRequestMapping = "/done";

	/**
	 * The cancel request mapping.
	 */
	public static final String cancelRequestMapping = "/cancel";

	/**
	 * The expunge request mapping.
	 */
	public static final String expungeRequestMapping = "/expunge";

	/**
	 * The id path variable.
	 */
	public static final String idPathVariable = "/{id}";

	/**
	 * The logger.
	 */
	protected final org.slf4j.Logger logger;

	/**
	 * Creates a core controller for the api.
	 *
	 * @param logger The logger class.
	 * @since 17
	 */
	public CoreApiController(Class<?> logger) {
		super();

		this.logger = org.slf4j.LoggerFactory.getLogger(logger);
	}

	/**
	 * Logs the exception.
	 *
	 * @param exception The exception to log.
	 * @since 1.8
	 */
	protected void log(Exception exception) {
		logger.error("throws exception " + exception.getClass().getName(), exception);
	}

}
