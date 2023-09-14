package com.mdrsolutions.springbootprod.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DataWebServiceApiHealthIndicator implements HealthIndicator {

    public static final String MESSAGE_KEY = "Data Web Service API";

    @Override
    public Health health() {
        if (!isServiceUp()) {
            return Health.down().withDetail(MESSAGE_KEY, "Down").build();
        }

        return Health.up().withDetail(MESSAGE_KEY, "Up").build();
    }

    private Boolean isServiceUp() {
        /* here will make health check for other service (may be using RES) */
        return true;
    }
}
