/**
 * File:     ConfigurationService.java
 * Package:  de.uniwuerzburg.zpd.ocr4all.application.msa.configuration
 * 
 * Author:   Herbert Baier (herbert.baier@uni-wuerzburg.de)
 * Date:     09.04.2024
 */
package de.uniwuerzburg.zpd.ocr4all.application.msa.configuration;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 * Defines configuration services.
 *
 * @author <a href="mailto:herbert.baier@uni-wuerzburg.de">Herbert Baier</a>
 * @version 1.0
 * @since 17
 */
@Service
@ApplicationScope
@Configuration
public class ConfigurationService {
	/**
	 * The environment that this component runs.
	 */
	private final Environment environment;

	/**
	 * The server properties for a web server (e.g. port and path settings).
	 */
	private final ServerProperties serverProperties;

	/**
	 * Creates a configuration service.
	 *
	 * @param environment      The environment that this component runs.
	 * @param serverProperties The server properties for a web server (e.g. port and
	 *                         path settings).
	 * @since 1.8
	 */
	public ConfigurationService(Environment environment,
			ServerProperties serverProperties) {
		super();

		this.environment = environment;
		this.serverProperties = serverProperties;
	}

	/**
	 * Returns the server port.
	 *
	 * @return The server port.
	 * @since 1.8
	 */
	public int getServerPort() {
		return serverProperties.getPort();
	}

	/**
	 * Returns the active profiles.
	 *
	 * @return The active profiles.
	 * @since 1.8
	 */
	public String[] getActiveProfiles() {
		return environment.getActiveProfiles();
	}

	/**
	 * Returns the active profiles separated by commas.
	 *
	 * @return The active profiles separated by commas.
	 * @since 1.8
	 */
	public String getActiveProfilesCSV() {
		StringBuffer buffer = new StringBuffer();

		for (String profile : environment.getActiveProfiles()) {
			if (buffer.length() > 0)
				buffer.append(", ");

			buffer.append(profile);
		}

		return buffer.toString();
	}

}
