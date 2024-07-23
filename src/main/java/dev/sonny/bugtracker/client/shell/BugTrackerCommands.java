package dev.sonny.bugtracker.client.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import dev.sonny.bugtracker.dto.TicketDTO;
import dev.sonny.bugtracker.entity.Ticket;
import dev.sonny.bugtracker.exception.RecordNotFoundException;
import dev.sonny.bugtracker.service.BugTrackerService;

@ShellComponent
public class BugTrackerCommands {

    private BugTrackerService bugTrackerService;

    public BugTrackerCommands(BugTrackerService bugTrackerService) {
        this.bugTrackerService = bugTrackerService;
    }

    @ShellMethod(key = "create", value = "Create a new issue ticket in the Bug Tracking system")
    public String create(
            @ShellOption String description,
            @ShellOption(defaultValue = ShellOption.NULL) String parentId,
            @ShellOption(defaultValue = ShellOption.NULL) String link) {

        TicketDTO ticketDTO = new TicketDTO(description, parentId, link, null);
        Ticket created = bugTrackerService.createTicket(ticketDTO);
        return "Created ticket: " + created.getId();
    }

    @ShellMethod(key = "close", value = "Close an existing issue ticket in the Bug Tracking system")
    public String close(@ShellOption String id) {
        String message = "Closed ticket " + id;
        try {
            bugTrackerService.closeTicket(id);
        } catch (RecordNotFoundException ex) {
            message = ex.getMessage();
        }
        
        return message;
    }
}
