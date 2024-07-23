package dev.sonny.bugtracker.service;

import org.springframework.stereotype.Service;

import dev.sonny.bugtracker.dto.TicketDTO;
import dev.sonny.bugtracker.entity.Ticket;
import dev.sonny.bugtracker.exception.RecordNotFoundException;
import dev.sonny.bugtracker.repository.BugTrackerRepository;

@Service
public class BugTrackerService {
    
    private BugTrackerRepository repository;

    public BugTrackerService(BugTrackerRepository repository) {
        this.repository = repository;
    }

    public Ticket createTicket(TicketDTO ticketDTO) {
        Ticket saved = repository.save(ticketDTO);
        return saved;
    }

    public void closeTicket(String id) throws RecordNotFoundException {
        repository.close(id);
    }
}
