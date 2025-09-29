package com.nsteuerberg.virma.auth_service.util.date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class ExpiredDate {

    @Value("${token.refresh.expired-days}")
    private Integer days;

    @Bean
    public Instant getExpiredDate() {
        return Instant.now().plus(days, ChronoUnit.DAYS);
    }
}
