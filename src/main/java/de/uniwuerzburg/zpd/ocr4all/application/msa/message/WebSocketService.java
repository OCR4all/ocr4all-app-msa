/**
 * File:     WebSocketService.java
 * Package:  de.uniwuerzburg.zpd.ocr4all.application.msa.message
 * 
 * Author:   Herbert Baier (herbert.baier@uni-wuerzburg.de)
 * Date:     12.03.2024
 */
package de.uniwuerzburg.zpd.ocr4all.application.msa.message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import de.uniwuerzburg.zpd.ocr4all.application.communication.message.spi.EventSPI;

/**
 * Defines WebSocket services.
 *
 * @author <a href="mailto:herbert.baier@uni-wuerzburg.de">Herbert Baier</a>
 * @version 1.0
 * @since 17
 */
@Service
public class WebSocketService {
	/**
	 * The message template for sending messages to the registered clients.
	 */
	private final SimpMessagingTemplate simpMessagingTemplate;

	/**
	 * The target destination.
	 */
	private final String destination;

	/**
	 * Creates a WebSocket service.
	 * 
	 * @param simpMessagingTemplate The message template for sending messages to the
	 *                              registered clients.
	 * @param prefix                The prefix to filter destinations targeting the
	 *                              message broker.
	 * @param destination           The message destination.
	 * @since 17
	 */
	public WebSocketService(SimpMessagingTemplate simpMessagingTemplate,
			@Value("${ocr4all.message.topic.prefix}") String prefix,
			@Value("${ocr4all.message.topic.destination}") String destination) {
		super();

		this.simpMessagingTemplate = simpMessagingTemplate;

		this.destination = prefix + destination;
	}

	/**
	 * Broadcast the event to the registered clients.
	 * 
	 * @param event The event to broadcast.
	 * @since 17
	 */
	public void broadcast(EventSPI event) {
		simpMessagingTemplate.convertAndSend(destination, event);
	}

}
