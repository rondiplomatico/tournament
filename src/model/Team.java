/**
 * Created on 26.02.2009 in Project Tournament
 * Location: plans.Team.java
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * Realisiert ein Team.
 * 
 * Ein Team ist eine Menge von Benutzern, die an einem Turnier teilnimmt. Ist
 * das Turnier ein Ein-Spieler-Turnier, so besteht jedes Team in dem Turnier aus
 * genau einem Spieler.
 * 
 * Bei der Voranmeldung zu einem Turnier gibt der sich anmeldende Benutzer,
 * seine Mannschaft an, falls das Turnier ein Mannschaftsturnier ist.
 */
@Entity
public class Team implements Serializable, Comparable<Team> {

	private static final long serialVersionUID = -2487858842323976298L;

	@SuppressWarnings("unused")
	@Id
	@GeneratedValue
	private long id;

	/** Name der Mannschaft */
	private String name;

	private boolean disqualified;

	/** Vorangemeldete Spieler */
	@ManyToMany(fetch=FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "team_players")
	private List<User> players;

	/** Bestätigte Spieler */
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "team_confirmed")
	private List<User> confirmed;

	/**
	 * Nach JPA-Spezifikation Standard-Konstrukt ohne Parameter.
	 */
	protected Team() {
		players = new ArrayList<User>(10);
		confirmed = new ArrayList<User>(10);
		disqualified = false;
	}

    /**
     * Erzeugt eine neue Mannschaft mit dem angegebenen Namen.
     * @param name Name der Mannschaft
     */
	public Team(String name) {
		this();
		this.name = name;
	}

	/**
	 * Gibt den Namen (Bezeichnung) der Mannschaft zurück.
	 * 
	 * @return Name der Mannschaft
	 */
	public String getName() {
		return name+(disqualified ? " (disqu.)":"");
	}

	/**
	 * Fügt einen neuen Benutzer zum Team hinzu.
	 * 
	 * @param player
	 *            Benutzer
	 */
	public void addPlayer(User player) {
		players.add(player);
	}

	/**
	 * Gibt die Liste der aller Spieler der Mannschaft zurück. (unabhängig von
	 * dem Bestätigundsstatus der Voranmeldung.)
	 * 
	 * @return Liste der Benutzer
	 */
	public List<User> getPlayers() {
		return players;
	}

	/**
	 * Gibt die Liste der Spieler der Mannschaft, dessen Voranmeldung
	 * vorbestätigt wurde.
	 * 
	 * @return Liste der Benutzer
	 */
	public List<User> getConfirmedPlayers() {
		return confirmed;
	}

	/**
	 * Gibt zurück, ob die Mannschaft vollständig leer, d.h. es gibt keine
	 * vorangemeldeten Spieler in der Mannschaft sowie keine bestätigten
	 * Spieler.
	 * 
	 * @return true, s. oben, false sonst
	 */
	public boolean isEmpty() {
		return players.isEmpty();
	}

	/**
	 * Prüft ob dieses Team disqualifiziert wurde.
	 * 
	 * @return boolean
	 */
	public boolean isDisqualified() {
		return disqualified;
	}

	/**
	 * Markiert dieses Team als disqualifiziert.
	 */
	public void disqualify() {
		if (!disqualified) { // Namensänderung nur einmal..
			this.disqualified = true;
		}
	}

	/**
	 * Verwendung bisher nur in @see
	 * GroupManager#matchResultUpdated(planning.model.Group)
	 * 
	 * @param value
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Prüft ob zwei Teams gleich sind. In unserem Fall bedeutet gleiches Team
	 * gleicher Name. (NICHT Datenbankmäßige ID, da die Dummyteams das Equals
	 * auf das innere Team zurückführen!)
	 * 
     * @param o
     * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Team) {
			return ((Team) o).name.equals(name);
		}
		return false;
	}

	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * Vergleich zwei Teams anhand des Namen.
	 * 
	 * @param t
	 *            Team
	 * @return 0, falls die Namen gleich sind, -1, falls this.name < t.name,
	 *         sonst 1.
	 */
	public int compareTo(Team t) {
		return getName().compareTo(t.getName());
	}
}
