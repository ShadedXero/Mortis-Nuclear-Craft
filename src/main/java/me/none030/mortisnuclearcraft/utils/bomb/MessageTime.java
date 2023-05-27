package me.none030.mortisnuclearcraft.utils.bomb;

import java.time.LocalDateTime;

public class MessageTime {

    private final String message;

    public MessageTime(String message) {
        this.message = message;
    }

    public String getTextTime() {
        try {
            if (message.contains("s")) {
                int time = Integer.parseInt(message.replace("s", ""));
                return time + " seconds";
            }
            if (message.contains("m")) {
                int time = Integer.parseInt(message.replace("m", ""));
                return time + " minutes";
            }
            if (message.contains("h")) {
                int time = Integer.parseInt(message.replace("h", ""));
                return time + " hours";
            }
        } catch (NumberFormatException exp) {
            return null;
        }
        return null;
    }

    public LocalDateTime getTime() {
        LocalDateTime current = LocalDateTime.now();
        try {
            if (message.contains("s")) {
                int time = Integer.parseInt(message.replace("s", ""));
                return current.plusSeconds(time);
            }
            if (message.contains("m")) {
                int time = Integer.parseInt(message.replace("m", ""));
                return current.plusMinutes(time);
            }
            if (message.contains("h")) {
                int time = Integer.parseInt(message.replace("h", ""));
                return current.plusHours(time);
            }
        } catch (NumberFormatException exp) {
            return null;
        }
        return null;
    }

    public boolean isOverLimit() {
        try {
            int limit = 24;
            if (message.contains("s")) {
                int time = Integer.parseInt(message.replace("s", ""));
                int hours = time / 60 / 60;
                return hours > limit;
            }
            if (message.contains("m")) {
                int time = Integer.parseInt(message.replace("m", ""));
                int hours = time / 60;
                return hours > limit;
            }
            if (message.contains("h")) {
                int time = Integer.parseInt(message.replace("h", ""));
                return time > limit;
            }
        } catch (NumberFormatException exp) {
            return true;
        }
        return true;
    }
}
