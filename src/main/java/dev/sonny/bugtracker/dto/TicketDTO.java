package dev.sonny.bugtracker.dto;

import java.time.LocalDateTime;

import dev.sonny.bugtracker.entity.Status;
import dev.sonny.bugtracker.entity.Ticket;

public record TicketDTO(String description, String parentId, String link, LocalDateTime createdTimestamp) implements Ticket {

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Status status() {
        return null;
    }
    
}
