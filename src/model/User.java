package model;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Allgemeine Benutzer-Entität
 *
 * @author Dimitri Wegner
 */
@Entity(name = "TUser")
@NamedQueries({
    @NamedQuery(name = "getReferees", query = "SELECT u FROM model.User u WHERE isReferee = true"),
    @NamedQuery(name = "getUsers", query = "SELECT u FROM model.User u")
})
public class User implements Serializable, Comparable<User> {

	private static final long serialVersionUID = -4969474703580649785L;
	
	@SuppressWarnings("unused")
	@Id
    @GeneratedValue
    private long id;
	
    private String username;
    private String password;
    private String email;
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;
    private String address;
    private boolean isReferee;
    private boolean isManager;

    /**
     * Nach JPA-Spezifikation Standard-Konstruktor ohne Parameter.
     */
    protected User() {
        isReferee(false);
        isManager(false);
    }

    /**
     * Erstellt einen Benutzer aus übergegeben Daten. Das Password
     * wird mit SHA1-Algorithmus verschlüsselt.
     *
     * @param username Benutzername
     * @param password Passwort
     */
    public User(String username, String password) {
        this();
        this.name = username;
        this.username = username;
        setPassword(password);
    }

    /**
     * Gibt die Adresse des Benutzers zurück
     * @return Adresse des Benutzers
     */
    public String getAdress() {
        return address;
    }

    /**
     * Setzt die Adresse des Benutzers
     * @param val Adresse des Benutzers
     */
    public void setAdress(String val) {
        this.address = val;
    }

    /**
     * Gibt das Geburtsdatum des Benutzers zurück
     * @return Geburtsdatum des Benutzers
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * Setzt das Geburtsdatums des Benutzers
     * @param val Geburtsdatum des Benutzers
     */
    public void setBirthday(Date val) {
        this.birthday = val;
    }

    /**
     * Gibt die Email Adresse des Benutzers zurück
     * @return Mail Adresse des Benutzers
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setzt die Email-Adresse des Benutzers
     * @param val Mail-Adresse des Benutzers
     */
    public void setEmail(String val) {
        this.email = val;
    }

    /**
     * Liefert den Vor- und Nachnamen des Benutzers
     * @return Vor- und Nachname
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param val
     */
    public void setName(String val) {
        this.name = val;
    }

    /**
     * Gibt das Passwort des Benutzters zurück
     * @return Passwort des Benutzers
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setzt das Passwort des Benutzers. Dieser wird dabei mit SHA1-Algorithmus
     * verschlüsselt.
     * 
     * @param val neues Passwort
     */
    public void setPassword(String val) {
        try {
            password = tools.SimpleSHA1.SHA1(val);
        } catch (Exception ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Gibt von dem Benutzer bei der Registrierung ausgewählten eindeutigen
     * Benutzernamen zurück.
     * @return Benutzername
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gibt zurück, ob der Benutzer als Schiedsrichter global im System
     * gemeldet ist.
     * 
     * @return true, falls User ein Schiedsrichter, sonst false
     */
    public boolean isReferee() {
        return isReferee;
    }

    /**
     * Setzt den Benutzer als Schiedsricher global im System entsprechend
     * abhängig vom Parameter.
     *
     * @param isReferee true = Schiedsrichter, false = nicht
     */
    public void isReferee(boolean isReferee) {
        this.isReferee = isReferee;
    }

    /**
     * Gibt zurück, ob der Benutzer Manager (Turnierverantwortlicher) ist.
     *
     * @return true, falls der Benutzer Manager ist.
     */
    public boolean isManager() {
        return isManager;
    }

    /**
     * Setzt, ob der Benutzer Manager (Turnierverantwortlicher) ist.
     *
     * @param isManager true = Manager
     */
    public void isManager(boolean isManager) {
        this.isManager = isManager;
    }

    /**
     * Zwei User-Objekte werden anhand von Username und Password verglichen.
     * Beim Registrieren wird überprüft, ob Username unique ist.
     *
     * @param obj Das Object mit welchem verglichen wird
     * @return true, falls Username und Password von beiden Objekten identisch sind.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return ((User) obj).username.equals(username) &&
                    ((User) obj).password.equals(password);
        }

        return false;
    }

    /**
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.username != null ? this.username.hashCode() : 0);
        hash = 89 * hash + (this.password != null ? this.password.hashCode() : 0);
        return hash;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * Vergleich den Benutzer mit einem anderen Benutzer u anhand des Benutzernamen.
     *
     * @param u Benutzer
     * @return 0, falls die beiden gleich sind, -1 falls this.usernamen < u.username
     *  sonst 1
     */
    public int compareTo(User u) {
        return username.compareTo(u.getUsername());
    }
}
