/**
 * Created on 26.02.2009 in Project Tournament 
 * Location: plans.Match.java
 */
package planning.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.User;

/**
 * 
 * Repräsentiert eine Begegnung zwischen zwei Teams.
 * 
 * 26.02.2009
 * 
 * @author Daniel Wirtz
 * 
 */
@Entity(name = "GroupMatch")
public class Match implements Comparable<Match>, Serializable {

	private static final long serialVersionUID = -2533240821411073534L;

	@SuppressWarnings("unused")
	@Id
	@GeneratedValue
	private long id;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "GroupMatch_HomeSlots")
	private TeamSlot home = null;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "GroupMatch_GuestSlots")
	private TeamSlot guest = null;

	private int goals_home, goals_guest;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Group containingGroup;

	/*
	 * Daten fürs Scheduling
	 */
	private int fieldNumber;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "match_referee")
	private User referee;

	/**
	 * Nach JPA-Spezifikation Standard-Konstruktor ohne Parameter.
	 */
	protected Match() {
		goals_guest = -1;
		goals_home = -1;
	}

	/**
	 * Erzeugt eine Begegnung
	 * @param containingGroup enthaltende Gruppe
	 * @param home Heimteam
	 * @param guest Gastteam
	 */
	public Match(Group containingGroup, TeamSlot home, TeamSlot guest) {
		this();
		this.containingGroup = containingGroup;
		this.home = home;
		this.guest = guest;
	}

	/**
	 * Setzt die Informationen für die Zeitplanung ein
	 * @param time Zeitpunkt
	 * @param fieldNumber Feldnummer
	 * @param referee Schiedsrichter:User
	 */
	public void setScheduleData(Date time, int fieldNumber) {
		this.startTime = time;
		this.fieldNumber = fieldNumber;
	}
	
	/**
	 * Setzt einen User als Schiedsrichter
	 * @param ref
	 */
	public void setReferee(User ref) {
		referee = ref;
	}

	/**
	 * @return true falls das Match beendet wurde, false sonst
	 */
	public boolean isFinished() {
		return goals_home > -1 && home.getTeam() != null
				&& guest.getTeam() != null;
	}

	/**
	 * 
	 * @return Heimteam
	 */
	public TeamSlot getHomeTeam() {
		return home;
	}

	/**
	 * 
	 * @return Gastteam
	 */
	public TeamSlot getGuestTeam() {
		return guest;
	}

	/**
	 * 
	 * @return Startzeitpunkt
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * 
	 * @return Feldnummer
	 */
	public int getField() {
		return fieldNumber;
	}

	/**
	 * 
	 * @return Schiedsrichter
	 */
	public User getReferee() {
		return referee;
	}

	/**
	 * Gibt die Anzahl der Tore des TeamSlots in diesem Match zurück. Ist der
	 * TeamSlot nicht enthalten, wird -1 zurückgegeben.
	 * 
	 * -1 wird auch zurückgegeben, falls das Match noch nicht beendet ist.
	 * 
	 * @param ts
	 *            Der TeamSlot
	 * 
	 * @return Anzahl der Tore für TeamSlot ts
	 */
	public int getGoals(TeamSlot ts) {

		return ts.equals(home) ? goals_home : ts.equals(guest) ? goals_guest : -1;
	}

	/**
	 * Gibt die Punkte die in einem TeamSlot ts erzielt wurden zurück. Ist der
	 * TeamSlot nicht für dieses Match eingetragen, wird 0 zurückgegeben.<br>
	 * <br>
	 * Leider gibt es hier eine unschöne Kette der Verknüpfungen; wegen dieser
	 * Kette braucht man (bisher) an dieser einzigen Stelle die Getter für
	 * Round,Tournament und Phase! Aber ein lokales Speichern einer Referenz
	 * erübrigt nicht die Notwendigkeit der Getter (wird höchstens
	 * aufgesplittet) und ist je weiter unten in der Hierarchie nur
	 * Speicherplatzverbrauch. (Zwar gering, aber was solls?)
	 * 
	 * @param ts
	 *            TeamSlot
	 * 
	 * @return int
	 */
	public int getPoints(TeamSlot ts) {
		if (participating(ts)) {
			if (hasWinner()) {
				// Gewonnen
				if (getWinner().equals(ts)) {
					return containingGroup.getPhase().getRound().getTournament().pointsForVictory();
					// Verloren
				} else {
					return containingGroup.getPhase().getRound().getTournament().pointsForLoss();
				}
				// Unentschieden
			} else {
				return containingGroup.getPhase().getRound().getTournament().pointsForRemis();
			}
		} else
			return 0;
	}

	/**
	 * Prüft ob es einen Gewinner gibt. Dazu muss das Match beendet sein und die
	 * Toranzahl unterschiedlich.
	 * 
	 * @return true falls es einen Gewinner gibt, false sonst
	 */
	public boolean hasWinner() {
		return isFinished() && goals_home != goals_guest;
	}

	/**
	 * Gibt den Gewinner einer Begegnung zurück. Wird die DBC-Bedingung nicht
	 * eingehalten, wird immer das Gastteam zurückgegeben.
	 * 
	 * @pre hasWinner() == true
	 * @return Gewinner des Matches
	 */
	public TeamSlot getWinner() {
		assert (hasWinner());
		return goals_home > goals_guest ? home : guest;
	}

	/**
	 * 
	 * @return enthaltende Gruppe
	 */
	public Group getGroup() {
		return containingGroup;
	}

	/**
	 * Bequemlichkeitsmethode
	 * @param goals_home Heimtore
	 * @param goals_guest Gasttore
	 */
	public void setResult(int goals_home, int goals_guest) {
		this.goals_home = goals_home;
		this.goals_guest = goals_guest;
	}

	/**
	 * Stellt fest ob ein gegebener TeamSlot im Match beteiligt ist.
	 * 
	 * @param ts TeamSlot
	 * @return true, falls <b>ts</b> am Match teilnimmt, false sonst
	 */
	public boolean participating(TeamSlot ts) {
		return ts.equals(home) || ts.equals(guest);
	}

	/**
	 * Prüft ob eine der Mannschaften des gegebenen Matches am aktuellen Match
	 * beteiligt ist.
	 * 
	 * @param m Match
	 * @return true, falls einer der Teilnehmer aus <b>m</b> an diesem Match teilnimmt.
	 */
	public boolean containsParticipant(Match m) {
		return participating(m.guest) || participating(m.home);
	}

	/**
	 * Gibt den Gegner des übergebenen TeamSlots in diesem Match aus. Gibt null
	 * zurück, falls der TeamSlot nicht an diesem Match beteiligt ist.
	 * 
	 * @param ofTeamSlot
	 * @return TeamSlot Gegner, <b>null</b> falls <b>ofTeamSlot</b> nicht am Match beteiligt ist
	 */
	public TeamSlot opponent(TeamSlot ofTeamSlot) {
		return ofTeamSlot.equals(home) ? guest : ofTeamSlot.equals(guest) ? home : null;
	}

	/**
	 * Bequemlichkeitsmethode für Debugging etc.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		DateFormat f = new SimpleDateFormat("d.M, H:mm");
		return "Begegnung in "
				+ containingGroup
				+ ": ["
				+ f.format(startTime)
				+ ", "
				+ getGroup().getPhase().getRound().getTournament().getFieldNames()[fieldNumber]
				+ ", "
				+ referee.getName()
				+ "] "
				+ home
				+ " gegen "
				+ guest
				+ (isFinished() ? " - (" + goals_home + ":" + goals_guest + ")" : "");
	}

	/**
	 * Vergleicht eine Begegnung mit einer Anderen anhand des geplanten
	 * Zeitpunktes.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @see java.util.Date#compareTo(Date)
	 */
	@Override
	public int compareTo(Match o) {
		return startTime.compareTo(o.startTime);
	}

}
