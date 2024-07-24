package dev.sonny.bugtracker.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import dev.sonny.bugtracker.entity.Status;
import dev.sonny.bugtracker.entity.Ticket;
import dev.sonny.bugtracker.entity.TicketEntity;
import dev.sonny.bugtracker.exception.RecordNotFoundException;
import dev.sonny.bugtracker.service.FileService;

@Repository
public class CsvRepository implements BugTrackerRepository {

    public static final String BUGTRACKER_CSV_HEADER = "Id,ParentId,Description,Status,Link,CreatedTimestamp";

    private final String BUGTRACKER_CSV_FILENAME_PROPERTY = "csv.bugtracker.file.name";
    private final String LAST_ID_FILENAME_PROPERTY = "csv.latest.id.file.name";
    private final String TICKET_ID_PREFIX = "I-";
    private final String DEFAULT_TICKET_ID = "I-1";

    private Environment env;
    private FileService fileService;

    public CsvRepository(Environment env, FileService fileService) {
        this.env = env;
        this.fileService = fileService;
    }

    @Override
    public synchronized Ticket save(Ticket ticket) {
        // isValidParentId(ticket.parentId());

        Ticket created = null;
        try {
            created = writeNewTicketToCSV(ticket);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return created;
    }

    @Override
    public synchronized void close(String id) throws RecordNotFoundException {
        boolean found = false;

        try {
            String filePath = "classpath:" + env.getProperty(BUGTRACKER_CSV_FILENAME_PROPERTY);
            Reader reader = fileService.getReader(filePath);
            Iterable<CSVRecord> records = getCSVRecords(filePath, reader);

            File file = fileService.getFile(filePath);
            String path = file.getAbsolutePath();
            CSVPrinter printer = new CSVPrinter(Files.newBufferedWriter(Paths.get(path), StandardOpenOption.CREATE),
                    CSVFormat.DEFAULT);

            for (CSVRecord record : records) {
                String[] values = record.values();
                String recordId = record.get("Id");
                if (recordId.equals(id)) {
                    values[3] = Status.CLOSED.value();
                    found = true;
                }

                printer.printRecord(values);
            }

            reader.close();
            printer.close();

            if (!found) {
                throw new RecordNotFoundException("ERROR: Record not found for id " + id);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized String generateId() throws IOException {
        String fileName = env.getProperty(LAST_ID_FILENAME_PROPERTY);
        String latestId = fileService.readFile(fileName);

        if (latestId == null) {
            latestId = DEFAULT_TICKET_ID;
        } else {
            // auto increment id
            String numString = latestId.split(TICKET_ID_PREFIX)[1];
            int num = Integer.valueOf(numString);
            latestId = TICKET_ID_PREFIX + (++num);
        }

        // write to file with updated id
        fileService.writeToFile(fileName, latestId);

        return latestId;
    }

    private Ticket writeNewTicketToCSV(Ticket ticket) throws FileNotFoundException, IOException {
        String id = null;
        TicketEntity ticketEntity = null;
        try {
            id = generateId();
            LocalDateTime localDateTime = LocalDateTime.now();
            ticketEntity = new TicketEntity(id, ticket.parentId(), ticket.description(), Status.OPEN, ticket.link(), localDateTime);
            
            String csvFileName = env.getProperty(BUGTRACKER_CSV_FILENAME_PROPERTY);
            String path = "classpath:" + csvFileName;
            File file = fileService.getFile(path);
            
            CSVPrinter csvPrinter = getCSVPrinter(file, path);

            csvPrinter.printRecord(ticketEntity.getId(), ticketEntity.parentId(), ticketEntity.description(), 
                    ticketEntity.status(),
                    ticketEntity.link(),
                    ticketEntity.createdTimestamp());

            csvPrinter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ticketEntity;
    }

    private Iterable<CSVRecord> getCSVRecords(String filePath, Reader reader) throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(BUGTRACKER_CSV_HEADER.split(","))
                .build();

        Iterable<CSVRecord> records = csvFormat.parse(reader);

        return records;
    }

    private CSVPrinter getCSVPrinter(File file, String filePath) throws IOException {
        BufferedWriter writer;
        CSVPrinter csvPrinter;

        // if csv does not exist, create with headers
        if (!file.exists()) {
            writer = Files.newBufferedWriter(Paths.get(filePath));
            csvPrinter = new CSVPrinter(writer,
                    CSVFormat.DEFAULT.builder().setHeader(CsvRepository.BUGTRACKER_CSV_HEADER.split(",")).build());
        } else {
            writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.APPEND);
            csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        }

        return csvPrinter;
    }

}
