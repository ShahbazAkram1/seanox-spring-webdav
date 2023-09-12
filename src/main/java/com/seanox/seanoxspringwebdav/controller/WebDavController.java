package com.seanox.seanoxspringwebdav.controller;

import com.seanox.webdav.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class WebDavController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDavController.class);

    /**
     * Mapping to list directory contents.
     *
     * @param path The path to list.
     * @return A {@link MetaProperties} object representing the directory listing.
     */
    @WebDavMapping(path = "/webdav/listDirectory")
    public MetaProperties listDirectory(String path) {
        List<MetaProperties> entries = new ArrayList<>();

        // Simulate listing directories and files
        File directory = new File(path);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        entries.add(createDirectoryEntry(file.getName()));
                    } else {
                        entries.add(createFileEntry(file.getName(), file.length()));
                    }
                }
            }
        }

        // Create a MetaProperties object with the directory listing
        return MetaProperties.builder()
                .isReadOnly(true)
                .isHidden(false)
                .isAccepted(true)
                .isPermitted(true)
                .creationDate(new Date())
                .lastModified(new Date())
                .uri(URI.create(path))
                .build();
    }

    /**
     * Mapping to create a directory.
     *
     * @param path The path to create the directory.
     */
    @WebDavMapping(path = "/webdav/createDirectory")
    public void createDirectory(String path) {
        // Implement logic to create a directory at the given path
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                // Log the successful creation
                LOGGER.info("Created directory: {}", path);
            } else {
                // Log the failure to create
                LOGGER.error("Failed to create directory: {}", path);
            }
        } else {
            // Log that the directory already exists
            LOGGER.warn("Directory already exists: {}", path);
        }
    }

    // Utility method to create a MetaProperties entry for a directory
    private MetaProperties createDirectoryEntry(String name) {
        return MetaProperties.builder()
                .isReadOnly(true)
                .isHidden(false)
                .isAccepted(true)
                .isPermitted(true)
                .creationDate(new Date())
                .lastModified(new Date())
                .uri(URI.create(name))
                .build();
    }

    // Utility method to create a MetaProperties entry for a file
    private MetaProperties createFileEntry(String name, long size) {
        return MetaProperties.builder()
                .isReadOnly(true)
                .isHidden(false)
                .isAccepted(true)
                .isPermitted(true)
                .creationDate(new Date())
                .lastModified(new Date())
                .uri(URI.create(name))
                .contentLength((int) size)
                .build();
    }

    /**
     * Mapping to write data to a file.
     *
     * @param path  The path to the file.
     * @param input The input stream containing the data to write.
     * @throws IOException if an I/O error occurs.
     */
    @WebDavMapping(path = "/webdav/writeFile")
    public void writeFile(String path, MetaInputStream input) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            // Create a buffer to read data from the input stream in chunks
            byte[] buffer = new byte[4096];
            int bytesRead;

            // Read from the input stream and write to the output stream
            while ((bytesRead = input.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Flush and close the output stream
            outputStream.flush();
        } catch (IOException e) {
            // Handle any errors that occur during the file write operation
            LOGGER.error("Failed to write data to file: {}", path, e);
        }

        // Log the successful write operation
        LOGGER.info("Successfully wrote data to file: {}", path);
    }

    /**
     * Mapping to delete a file.
     *
     * @param path The path to the file to delete.
     */
    @WebDavMapping(path = "/webdav/deleteFile")
    public void deleteFile(String path) {
        // Implement logic to delete the file at the given path
        File fileToDelete = new File(path);
        if (fileToDelete.exists() && fileToDelete.isFile()) {
            boolean deleted = fileToDelete.delete();
            if (deleted) {
                // Log the successful deletion
                LOGGER.info("Deleted file: {}", path);
            } else {
                // Log the failure to delete
                LOGGER.error("Failed to delete file: {}", path);
            }
        } else {
            // Log that the file does not exist
            LOGGER.warn("File does not exist: {}", path);
        }
    }
}
