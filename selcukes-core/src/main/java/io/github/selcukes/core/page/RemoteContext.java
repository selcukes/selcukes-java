package io.github.selcukes.core.page;

import io.github.selcukes.commons.exception.SelcukesException;
import org.openqa.selenium.HasDownloads;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * This class provides functionality to manage browser downloads, including
 * listing, downloading, and deleting downloadable files.
 */
public class RemoteContext {

    HasDownloads downloads;

    /**
     * Constructs a new RemoteContext instance.
     *
     * @param driver the WebDriver instance, which must support downloads
     * @throws IllegalArgumentException if the provided WebDriver does not support downloads
     */
    public RemoteContext(WebDriver driver) {
        if (!(driver instanceof HasDownloads)) {
            throw new IllegalArgumentException("Driver does not support downloads: " + driver);
        }
        this.downloads = (HasDownloads) driver;
    }

    /**
     * Retrieves a list of all downloadable files available in the browser.
     *
     * @return a list of downloadable file names
     */
    public List<String> listAll() {
        return downloads.getDownloadableFiles();
    }

    /**
     * Downloads all available files into the specified target directory.
     *
     * @param targetDirectory the directory where the files will be downloaded
     */
    public void downloadAll(Path targetDirectory) {
        listAll().forEach(file -> safeDownload(file, targetDirectory));
    }

    /**
     * Deletes all downloadable files from the browser's download list.
     */
    public void deleteAll() {
        downloads.deleteDownloadableFiles();
    }

    /**
     * Safely downloads a file to the specified directory.
     *
     * @param file            the name of the file to be downloaded
     * @param targetDirectory the directory where the file will be saved
     * @throws SelcukesException if the file download fails
     */
    private void safeDownload(String file, Path targetDirectory) {
        try {
            downloads.downloadFile(file, targetDirectory);
        } catch (IOException e) {
            throw new SelcukesException("Failed to download file: " + file, e);
        }
    }
}
