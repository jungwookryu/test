package com.ht.connected.home.backend.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditLogger {
	private static final Marker AUDIT_MARKER = MarkerFactory.getMarker("AUDIT");

	public static void log(Class<?> clazz, String msg) {
		Logger logger = LoggerFactory.getLogger(clazz);
		logger.info(AUDIT_MARKER, msg);
	}

	public static void startLog(Class<?> clazz, String msg) {
		String userEmail = "";
		if (SecurityContextHolder.getContext() != null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof String) {
				userEmail = (String) principal;
			}
		}
		log(clazz, "[START - " + userEmail + "] " + msg);
	}

	public static void endLog(Class<?> clazz, String msg) {
		log(clazz, "[END] " + msg);
	}

	public static void serviceLog(Class<?> clazz, String msg) {
		log(clazz, "  [SERVICE] " + msg);
	}

}
