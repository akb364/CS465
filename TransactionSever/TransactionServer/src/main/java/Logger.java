/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bakesmac
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.*;

public class Logger {
    private static Logger LOGGER = null;
//if logger doesn't exist yet
    public static Logger getInstance() {
        if (LOGGER == null) {
            LOGGER = new Logger();
        }
        return LOGGER;
    }
    private PrintStream outPutStream = System.out;
    
    private PrintStream errorStream = System.err;
    public void LoggingToFile() {
        var outPutFile = new File("Loginfo.log");
        var errorFile = new File("Logerr.log");
        try {
            outPutFile.createNewFile();
            errorFile.createNewFile();
            outPutStream = new PrintStream(outPutFile);
            errorStream = new PrintStream(errorFile);
        } catch (IOException e) {
            System.err.println("Logger could not be set to log to files");
        }

    }

    public void logInfoLess(String message) {
        logFormatLine(message, outPutStream);
    }

    public void logInfoMore(int transactionId, String message) {
        logLine(transactionId, message, outPutStream);
    }

    public void logError(String errorText) {
        logFormatLine(errorText, errorStream);
    }

    public void logError(Exception error) {
        logFormatLine(error.getMessage(), errorStream);
    }

    public void logError(int transnId, String errorText) {
        logLine(transnId, errorText, errorStream);
    }

    public void logError(int transId, Exception error) {
        logLine(transId, error.getMessage(), errorStream);
    }

    private void logFormatLine(String msg, PrintStream stream) {
        var dateTime = LocalDateTime.now();
        String DateFormat = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        stream.printf("# %s # %s\n", DateFormat, msg);
    }

    private void logLine(int transId, String message, PrintStream stream) {
        logFormatLine(String.format("Transaction %d: %s", transId, message), stream);
    }

}
