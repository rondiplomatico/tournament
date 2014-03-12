package control;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JFrame;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import view.MainView;
import view.SplashScreen;
import view.forms.LoginDialog;

/**
 * Die Hauptklasse der Anwendung.
 * 
 * @author Dimitri Wegner
 */
public class MainApplication extends SingleFrameApplication {

    private MainView mainView;
    private LoginDialog loginDialog;
    /**
     * Verwaltet globale Datenbank-Verbindung.
     */
    private static EntityManagerFactory emf;
    /**
     * Verwaltet globale Datenbank-Verbindung.
     */
    private static EntityManager em;

    /** Zeigt den Einloggen-Dialog an. */
    public void showLoginDialog() {
        if (loginDialog == null) {
            // Bei erster Anzeige kein Reset
            loginDialog = new LoginDialog();
        } else {
            loginDialog.reset();
        }

        show(loginDialog);
    }

    /**
     * Diese Methode wird beim Start der Anwendung ausgeführt.
     * Dabei werden die gespeicherten (falls vorhanden) Einstellungen
     * vom Fenster geladen, Datenbank-Verbindung hergestellt und
     * die Fenster angezeigt.
     *
     * @see org.jdesktop.application.SessionStorage
     */
    @Override
    protected void startup() {
        mainView = new MainView(this);

        // Einstellungen laden
        try {
            getContext().getSessionStorage().restore(mainView.getFrame(), "session.xml");
        } catch (IOException ex) {
            Logger.getLogger(MainApplication.class.getName()).log(Level.SEVERE, 
                "Session konnte nicht wiederhergestellt werden.", ex);
        }

        // Verbindung zur Datenbank herstellen
        getEntityManager();

        // Zuerst einloggen, dann das Hauptfenster
        showLoginDialog();
        mainView.resetMainView();
        show(mainView);
    }

    /**
     * Wird beim Beenden der Anwendung aufgeführt. Sorgt dafür, dass die Fenster-
     * einstellungen (Position, Größe etc...) gespeichert und Daten
     * persistiert werden.
     *
     * @see org.jdesktop.application.SessionStorage
     * @see TournamentManager#saveAllTournaments() 
     */
    @Override
    protected void shutdown() {
        // Einstellungen speichern
        try {
            getContext().getSessionStorage().save(mainView.getFrame(), "session.xml");
        } catch (IOException ex) {
            Logger.getLogger(MainApplication.class.getName()).log(Level.SEVERE,
                "Session konnte nicht wiederhergestellt werden.", ex);
        }

        // Daten Speichern
        TournamentManager.getInstance().saveAllTournaments();
    }

    /**
     * Gibt Hauptfenster der Anwendung zurück.
     * Die Methode getMainFrame von SingleFrameApplication ist buggy, kann aber
     * wegen final nicht überschrieben werden.
     * Deshalb wird diese Methode zur Verfügung gestellt und soll benutzt werden.
     *
     * {@link <a href="https://appframework.dev.java.net/issues/show_bug.cgi?id=58">appframework.dev.java.net/issues/show_bug.cgi?id=58</a>}
     * @return Hauptfenster
     */
    public JFrame getMainframe() {
        return mainView.getFrame();
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     *
     * @param root Der Frame auf dem das Fenster liegt
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * Statischer Getter für die Hauptanwendung.
     * @return Instanz von MainApplication
     */
    public static MainApplication getApplication() {
        return Application.getInstance(MainApplication.class);
    }

    /**
     * Gibt JPA EntityManager zurück. Dieser wird nur einmal erzeugt, um
     * die Verwendung der Datenbank-Verbindung zu optimieren.
     *
     * @return JPA EntityManager
     */
    public static EntityManager getEntityManager() {
        if (em == null) {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory("db");
            }

            em = emf.createEntityManager();
        }

        return em;
    }

    /**
     * Hauptmethode zum Starten der Anwendung.
     * Es wird sofort eine Verbindung zu der Datenbank aufgebaut.
     *
     * @param args Argumente der Programmzeile
     */
    public static void main(String[] args) {
        SplashScreen splash = new SplashScreen();
        new Thread(splash).run();

        launch(MainApplication.class, args);

        splash.dispose();
    }
}
