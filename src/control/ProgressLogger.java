/**
 * Created on 03.03.2009 in Project Tournament
 * Location: control.ProgressLogger.java
 */
package control;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

/**
 * 03.03.2009
 * 
 * Singleton zum Anzeigen von Meldungen während der Prgogrammausführung. Ihm
 * kann eine {@link java.awt.List} übergeben werden, in der die geloggten
 * Ereignisse eingetragen werden (Threadsicher, also über EDT-Queue). Ist keine
 * List gesetzt, wird die Console benutzt.
 * 
 * Loggen einer Nachricht über {@link ProgressLogger#getInstance()}.
 * {@link #log(String)}
 * 
 * @author Daniel Wirtz
 * 
 */
public class ProgressLogger {

    private static ProgressLogger logger;
    private JTextPane displayTextPane;
    private StringBuffer buffer;

    /**
     *
     * Singleton Methode
     * Gibt das Progresslogger Objekt zurück
     *
     * @return Prograsslogger Das aktuelle Prgresslogger Objekt
     */
    public static ProgressLogger getInstance() {
        if (logger == null) {
            logger = new ProgressLogger();
        }
        return logger;
    }

    /** Privater Konstruktor, ProgressLogger ist eine Singleton. */
    private ProgressLogger() {
        buffer = new StringBuffer();
        displayTextPane = null;
    }

    /** Löscht alle Nachrichten und setzt die Anzeige zurück. */
    public void reset() {
        buffer = new StringBuffer();
        if (displayTextPane != null) {
            displayTextPane.setText("");
        }
    }

    /**
     * Setzt die Anzeigekomponente.
     * 
     * @param value Komponente, die die Nachrichten anzeigen.
     */
    public void setTextPane(JTextPane value) {
        this.displayTextPane = value;
    }

    /**
     * Loggt eine Nachricht.
     *
     * @param message Nachricht
     */
    public void log(final String message) {
        buffer.append("[" + SimpleDateFormat.getInstance().format(new Date()) + "] ");
        buffer.append(message + "\n");

        if (displayTextPane != null) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    displayTextPane.setText(buffer.toString());
                }
            });
        } else {
            System.out.println(message);
        }
    }
}
