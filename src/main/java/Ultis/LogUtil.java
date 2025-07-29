package Ultis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for writing logs to file.
 * Automatically appends timestamp and creates file/folder if not exists.
 * 
 * Example usage:
 * new LogUtil().logToFile("logs/inventory_log.txt", "User admin updated product ID 123");
 */
public class LogUtil {

    /**
     * Writes a log message to the specified file.
     * Creates the file and its parent directories if they do not exist.
     *
     * @param filename The relative or absolute path to the log file.
     * @param message The message to log.
     */
    public static void logToFile(String filename, String message) {
        BufferedWriter bw = null;
        try {
            File file = new File(filename);
            
            // Create parent folders if they don't exist
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            // Append mode
            FileWriter fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);

            // Timestamp
            String timeStamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Format log line
            String logEntry = String.format("[%s] %s", timeStamp, message);
            
            // Write
            bw.write(logEntry);
            bw.newLine();
            

        } catch (IOException e) {
            e.printStackTrace(); // You can replace this with a logger
        } finally {
            try {
                if (bw != null) bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
