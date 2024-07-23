package dev.sonny.bugtracker.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private ResourceLoader resourceLoader;

    public FileService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public File getFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        return file;
    }

    public Reader getReader(String filePath) throws FileNotFoundException {
        Reader reader = new FileReader(filePath);
        return reader;
    }

    public String readFile(String fileName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + fileName);
        InputStream inputStream = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }

    public void writeToFile(String fileName, String data) throws IOException {
        PrintWriter writer = new PrintWriter(resourceLoader.getResource("classpath:" + fileName).getFile());
        // writer.flush();
        writer.write(data);
        writer.close();
    }
}
