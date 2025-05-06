package logs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Classe du formatter de logs
 */
public class LogFormatter extends Formatter {
    /**
     * Méthode de formatage de logs
     *
     * @param record the log record to be formatted.
     * @return La ligne de log.
     */
    @Override
    public String format(LogRecord record) {

        return "[" +
                record.getLevel() +
                "] - " +
                getFormattedDate() +
                " | Class : " + record.getSourceClassName() +
                " | Method : " + record.getSourceMethodName() +
                "() | Message : " + record.getMessage() +
                "\n";
    }

    private String getFormattedDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }
}
