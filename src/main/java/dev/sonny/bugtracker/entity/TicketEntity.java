package dev.sonny.bugtracker.entity;

import java.time.LocalDateTime;

public class TicketEntity implements Ticket {
    
    private final String id;
    private final String parentId;
    private String description;
    private Status status;
    private String link;
    private LocalDateTime createdTimestamp;


    public TicketEntity(String id, String parentId, String description, Status status, String link, LocalDateTime createdTimestamp) {
        this.id = id;
        this.description = description;
        this.parentId = parentId;
        this.status = status;
        this.link = link;
        this.createdTimestamp = createdTimestamp;
    }

    public String getId() {
        return id;
    }
    public String parentId() {
        return parentId;
    }
    public String description() {
        return description;
    }
    public Status status() {
        return status;
    }
    public String link() {
        return link;
    }
    public LocalDateTime createdTimestamp() {
        return createdTimestamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setLink(String link) {
        this.link = link;
    }

}
