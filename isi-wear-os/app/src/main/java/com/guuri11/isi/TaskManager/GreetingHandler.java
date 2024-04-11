package com.guuri11.isi.TaskManager;

import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GreetingHandler {

    private final List<String> greetings;

    public GreetingHandler() {
        // List of greetings to use at different times of day
        this.greetings = new ArrayList<>();
        this.greetings.add("¡Buenos días!");
        this.greetings.add("¡Buenas tardes!");
        this.greetings.add("¡Buenas noches!");
        this.greetings.add("¡Que tal!");
    }

    public String getGreeting() {
        // Get the current time
        LocalTime now = LocalTime.now();

        // Define time ranges for greetings
        LocalTime morningStart = LocalTime.of(6, 0);  // 6:00 AM
        LocalTime morningEnd = LocalTime.of(12, 0);   // 12:00 PM
        LocalTime afternoonEnd = LocalTime.of(18, 0); // 6:00 PM
        LocalTime eveningEnd = LocalTime.of(22, 0);   // 10:00 PM

        if (now.isAfter(morningStart) && now.isBefore(morningEnd)) {
            return greetings.get(0); // Morning
        } else if (now.isAfter(morningEnd) && now.isBefore(afternoonEnd)) {
            return greetings.get(1); // Afternoon
        } else if (now.isAfter(afternoonEnd) && now.isBefore(eveningEnd)) {
            return greetings.get(2); // Evening
        } else {
            return greetings.get(3); // Any
        }
    }
}
