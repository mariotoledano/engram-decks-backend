package com.engramdecks.api.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApiReadyListener {

	@EventListener(ApplicationReadyEvent.class)
	public void onReady() {
		log.info("""
				========================================
				 Engram Decks API en marcha
				 Health:  http://localhost:8080/actuator/health
				 Swagger: http://localhost:8080/swagger-ui.html
				========================================
				""");
	}

}
