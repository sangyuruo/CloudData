package com.distribution.data.web.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 */
public final class HeaderUtil {

    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-wamoliApp-alert", message);
        headers.add("X-wamoliApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("wamoliApp." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("wamoliApp." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("wamoliApp." + entityName + ".deleted", param);
    }

    public static HttpHeaders createEntitySwitchAlert(String entityName, String param) {
        return createAlert("wamoliApp." + entityName + ".switched", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity creation failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-wamoliApp-error", "error." + errorKey);
        headers.add("X-wamoliApp-params", entityName);
        return headers;
    }
}
