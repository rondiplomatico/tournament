package control;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import model.Tournament;
import model.User;
import tools.SimpleSHA1;

/**
 * Singleton-Klasse für die Verwaltung von Benutzern.
 *
 * @author Osama, Sandra
 */
public final class UserManager {

    private User loggedinUser;
    private List<User> refereeList;
    private List<User> userList;
    private static final UserManager INSTANCE = new UserManager();
   
    private UserManager() {
    }

    /**
     * Singleton Methode
     * Gibt das Usermanager Objekt zurück
     *
     * @return UserManager Das aktuelle Usermanager Objekt
     */
    public static UserManager getInstance() {
        return INSTANCE;
    }

    /**
     * Gibt die Liste aller User im System zurück
     *
     * @return Liste aller User
     */
    @SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
        if (userList == null) {
            EntityManager em = MainApplication.getEntityManager();
            Query q = em.createNamedQuery("getUsers");
            userList = q.getResultList();
        }

        return userList;
    }

    /**
     * Liefer die Instanz des aktuell eingeloggten Benutzers oder null, falls
     * kein Benutzer eingeloggt ist.
     * 
     * @return Aktuell eingeloggter Benutzer oder null
     */
    public User getLoggedinUser() {
        return loggedinUser;
    }

    /**
     * Es wird ein Benutzer anhand von Username und Password eingeloggt.
     *
     * @pre Benutzer mit username muss ex. und sein Passwort muss richtig sein.
     * @post Benutzer ist eingeloggt, d.h. es ex. eine User-Instanz mit den
     *  Benutzerdaten
     *
     * @param username Benutzername
     * @param password Passwort
     */
    public void login(String username, char[] password) {
        assert (loggedinUser == null);

        String encryptedPass;

        // Encrypt Passwort
        try {
            encryptedPass = SimpleSHA1.SHA1(String.valueOf(password));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        // Suche in der Datenbank
        EntityManager em = MainApplication.getEntityManager();

        Query q = em.createQuery(
                "SELECT u FROM model.User u " +
                "WHERE u.username = :u1 AND u.password = :p1");
        q.setParameter("u1", username);
        q.setParameter("p1", encryptedPass);

        try {
            loggedinUser = (User) q.getSingleResult();
        } catch (NoResultException e) {
            Logger.getLogger(UserManager.class.getName()).log(Level.INFO,
                    "Benutzer oder Passwort falsch");
        }
    }

    /**
     * Loggt den aktuell eingeloggten Benutzer aus.
     * Voraussetzung: Es muss ein Benutzer eingeloggt sein.
     */
    public void logout() {
        assert (loggedinUser != null);

        Logger.getLogger(UserManager.class.getName()).log(Level.INFO,
                "Benutzer wurde ausgeloggt");

        loggedinUser = null;
    }

    /**
     * Registriert einen neuen Benutzer. Ex. ein Benutzer mit dem gleichen
     * Benutzernamen schon, so wird false zurückgegeben. Sonst wird der Benutzer
     * erfolgreich registriert und es wird true zurückgegeben.
     *
     * @pre Es darf kein Benutzer mit dem gleichen Benutzernamen ex.
     * @post Es ist ein neuer Benutzer angelegt; dieser ist eingeloggt.
     * @param user User-Instanz gefüllt mit Daten
     * @return True wenn die Registrierung erfolgreich war
     */
    public boolean register(User user) {
        // Aus Datenbank holen und vergleichen
        EntityManager em = MainApplication.getEntityManager();

        Query q = em.createQuery("SELECT u FROM model.User u WHERE u.username = :username");
        q.setParameter("username", user.getUsername());

        if (q.getResultList().size() != 0) {
            Logger.getLogger(UserManager.class.getName()).log(Level.INFO,
                    "Username schon vergeben");
            return false;
        } else {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

            loggedinUser = user;

            return true;
        }
    }

    /**
     * Gibt zurück, ob der aktuell eingeloggte Benutzer in dem angegebenen
     * Turnier als Schiedsrichter gemeldet ist.
     *
     * @pre Ein Benutzer muss eingeloggt sein
     * @param t Turnier, für welches die Zugehörigkeit überprüft wird.
     * @return true, falls der aktuelle Benutzer Schiedrichter in t ist.
     */
    public boolean isReferee(Tournament t) {
        assert (loggedinUser != null);

        return t.getReferees().contains(loggedinUser);
    }

    /**
     * Gibt zurück, ob der aktuell eingeloggte Benutzer in dem angegebenen
     * Turnier als Leiter gemeldet ist.
     *
     * @pre Ein Benutzer muss eingeloggt sein
     * @param t Turnier, für welches die Zugehörigkeit überprüft wird.
     * @return true, falls der aktuelle Benutzer Leiter in t ist.
     */
    public boolean isLeader(Tournament t) {
        assert (loggedinUser != null);

        return t.getLeaders().contains(loggedinUser);
    }

    /**
     * Gibt zurück, ob ein Benutzer eingeloggt ist oder nicht.
     * 
     * @return true, falls ein Benutzer eingeloggt ist, sonst false
     */
    public boolean isLoggedin() {
        return loggedinUser != null;
    }

    /**
     * Gibt die Liste aller im System global registrierten Schiedrichter
     * zurück.
     * @return Liste der Referees
     */
    @SuppressWarnings("unchecked")
	public List<User> getReferees() {
        if (refereeList == null) {
            EntityManager em = MainApplication.getEntityManager();

            Query q = em.createNamedQuery("getReferees");
            refereeList = q.getResultList();
        }

        return refereeList;
    }

    /**
     * Verändert das Passwort von dem Benutzer u.
     * 
     * @pre Altes Passwort von u ist oldPass.
     * @param u Benutzer
     * @param oldPass Altes Passwort (nicht verschlüsselt)
     * @param newPass Neues Passwort (nicht verschlüsselt)
     */
    public void changePasswort(User u, String oldPass, String newPass) {
        try {
            assert (u.getPassword().equals(SimpleSHA1.SHA1(oldPass)));
        } catch (Exception ignore) {
        }

        u.setPassword(newPass);
        EntityManager em = MainApplication.getEntityManager();
        em.getTransaction().begin();
        em.persist(u);
        em.getTransaction().commit();
    }
}
