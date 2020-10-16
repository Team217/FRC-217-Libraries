package org.team217;

import java.io.*;

/**
 * Saves data to a log file.
 * 
 * @author ThunderChickens 217
 */
public class Logger {
    private String fileName;
    private File file;

    /**
     * Saves data to a log file.
     * 
     * @param fileName
     *        The name of the log file, including extensions
     * 
     * @author ThunderChickens 217
     */
    public Logger(String fileName) {
        this.fileName = fileName;
        file = new File(fileName);
    }

    /**
     * Saves data to a log file.
     * 
     * @param file
     *        The target log file
     * 
     * @author ThunderChickens 217
     */
    public Logger(File file) {
        fileName = file.getName();
        this.file = file;
    }

    /**
     * Returns the log file.
     */
    public File getFile() {
        return file;
    }

    /**
     * Returns the name of the log file.
     */
    public String getName() {
        return fileName;
    }

    /**
     * Writes data to the log file.
     * 
     * @param data
     *        The data to log
     * 
     * @exception IOException if the log file cannot be opened
     */
    public void log(String data) throws IOException {
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        writer.append(data);
        writer.newLine();

        writer.close();
    }
}