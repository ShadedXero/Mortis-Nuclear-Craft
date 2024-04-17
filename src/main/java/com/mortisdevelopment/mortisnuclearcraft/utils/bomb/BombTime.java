package com.mortisdevelopment.mortisnuclearcraft.utils.bomb;

import java.time.Duration;
import java.time.LocalDateTime;

public class BombTime {

    private final LocalDateTime time;

    public BombTime(LocalDateTime time) {
        this.time = time;
    }

    public String getTextTime() {
        Duration duration = Duration.between(LocalDateTime.now(), time);
        if (duration == null) {
            return null;
        }
        long seconds = duration.getSeconds();
        long hours = seconds / 60 / 60;
        if (hours >= 1) {
            return hours + " hours";
        }
        long minutes = seconds / 60;
        if (minutes >= 1) {
            return minutes + " minutes";
        }
        return seconds + " seconds";
    }
}
