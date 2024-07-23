package dev.sonny.bugtracker.entity;

import java.time.LocalDateTime;

public interface Ticket {
    
    public String getId();
    public String parentId();
    public String description();
    public Status status();
    public String link();
    public LocalDateTime createdTimestamp();
}
