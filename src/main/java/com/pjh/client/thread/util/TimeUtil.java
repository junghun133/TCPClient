package com.pjh.client.thread.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtil {
    LocalDateTime last;

    public TimeUtil() {
        updateLastTime();
    }

    public void updateLastTime() {
        last = LocalDateTime.now();
    }

    public boolean isExpired(long timeMS) {
        LocalDateTime now = LocalDateTime.now();
        Duration d = Duration.between(last, now);
        return d.toMillis() > timeMS;
    }
}
