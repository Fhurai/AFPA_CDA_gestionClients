package logs;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe du gestionnaire de logs.
 */
public class LogManager {

    // Variables de classe
    public static final Logger logs = Logger.getLogger(LogManager.class.getName());

    /**
     * Initialisation du guestionnaire de logs
     */
    public static void init() throws IOException {
        LocalDate date = LocalDate.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileHandler fh = new FileHandler("logs/log" + date.format(df) + ".log", true);
        logs.setUseParentHandlers(false);
        logs.addHandler(fh);
        fh.setFormatter(new LogFormatter());
    }

    /**
     * Méthode de log du lancement de l'application.
     */
    public static void run(){
        logs.log(Level.INFO, "Software running...");
    }

    /**
     * Méthode de log de la fermeture de l'application.
     */
    public static void stop(){
        logs.log(Level.INFO, "Software stopped !");
    }
}
