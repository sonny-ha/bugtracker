package dev.sonny.bugtracker.repository;

import org.springframework.stereotype.Repository;

import dev.sonny.bugtracker.entity.Ticket;
import dev.sonny.bugtracker.exception.RecordNotFoundException;

@Repository
public interface BugTrackerRepository {
    
    public Ticket save(Ticket ticket);

    public void close(String id) throws RecordNotFoundException;
}
