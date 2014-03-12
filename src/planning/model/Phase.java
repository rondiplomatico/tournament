/**
 * Created on 26.02.2009 in Project Tournament Location: plans.Phase.java
 */
package planning.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.IndexColumn;

import planning.control.PlanningManager;
import planning.model.rounds.Round;

/**
 * 
 * Repräsentiert eine Phase innerhalb einer Runde. Phasen sind nach aussen hin
 * nicht sichtbar, können aber intern benutzt werden um Runden strukturierter
 * aufbauen zu können.
 * 
 * So wird z.B. die K.O.-Runde in mehrere Phasen (Halbfinale, Finale etc)
 * aufgeteilt.
 * 
 * 26.02.2009
 * 
 * @author Daniel Wirtz Class Phase
 */
@Entity
public class Phase implements Serializable {

	private static final long serialVersionUID = 2643274044549817042L;

	@SuppressWarnings("unused")
	@Id
	@GeneratedValue
	private long id;

	@ManyToOne(cascade=CascadeType.PERSIST)
	private Round round;

	private String name;
	private int numGroups;

	@OneToMany(cascade=CascadeType.PERSIST)
	private List<Match> schedule;

	@Temporal(TemporalType.TIMESTAMP)
	private Date scheduledEnd = null;

	@JoinColumn(name = "phase_id")
	@IndexColumn(name = "groupOrder")
	@OneToMany(cascade = CascadeType.ALL)
	private List<Group> groups;

	@OneToOne(cascade=CascadeType.ALL)
	private Transition inTransition;

	@OneToOne(cascade=CascadeType.ALL)
	private Transition outTransition;

	/**
	 * JPA-erforderlicher Konstruktor
	 */
	protected Phase() {
	}

	/**
	 * Erzeugt eine neue Phase in einer Runde.
	 * 
	 * Gibt den Gruppen die Namen Gruppe A, Gruppe B etc.
	 * 
	 * @param round 
	 * @param numGroups 
	 * @param name 
	 */
	public Phase(Round round, int numGroups, String name) {
		this.round = round;
		this.name = name;
		this.numGroups = numGroups;
		groups = new ArrayList<Group>(numGroups);
		schedule = new ArrayList<Match>();
	}

	/**
	 * @pre Schedule für diese Phase wurde einmal aufgerufen
	 * 
	 * @see PlanningManager#schedule(TournamentPlan, Date)
	 * 
	 * @return Geplanter Endzeitpunkt des letzten Matches
	 */
	public Date getScheduledEndDateTime() {
		assert (scheduledEnd != null);

		return scheduledEnd;
	}
	
	/**
	 * Setzt die geplante Endzeit.
	 * @param value
	 */
	public void setScheduledEndDateTime(Date value) {
		scheduledEnd = value;
	}

	/**
	 * @return zeitlich geordnete Liste der Begegnungen
	 */
	public List<Match> getSchedule() {
		return schedule;
	}

	/**
	 * Gibt an, ob alle Spiele in der Phase beendet wurden (äquivalent dazu,
	 * dass alle Gruppen fertig sind)
	 * 
	 * @return true, falls alle Gruppen fertig sind, false sonst
	 */
	public boolean isFinished() {
		for (Group g : groups) {
			if (!g.isFinished()) return false;
		}
		return true;
	}

	/**
	 * 
	 * @return Enthaltende Runde
	 */
	public Round getRound() {
		return round;
	}

	/**
	 * 
	 * @return Eintrittsübergang
	 */
	public Transition getInTransition() {
		return inTransition;
	}
	
	/**
	 * 
	 * @return Austrittsübergang
	 */
	public Transition getOutTransition() {
		return outTransition;
	}
	
	/**
	 * Aus JPA-Gründen werden hier die Transitions gecloned, damit ein rauswerfen der
	 * Phasen aus allen Runden einfach möglich ist.
	 * @param trans
	 */
	public void setOutTransition(Transition trans) {
		//this.outTransition = trans != null ? trans.clone():null;
		this.outTransition = trans;
	}
	
	/**
	 * Aus JPA-Gründen werden hier die Transitions gecloned, damit ein rauswerfen der
	 * Phasen aus allen Runden einfach möglich ist.
	 * @param value
	 */
	public void setInTransition(Transition value) {
		//this.inTransition = value != null ? value.clone():null;
		this.inTransition = value;
	}

	/**
	 * 
	 * @return Gruppen der Phase
	 */
	public List<Group> getGroups() {
		return groups;
	}
	
	/**
	 * 
	 * @return Gruppenanzahl
	 */
	public int getNumGroups() {
		return numGroups;
	}

	/**
	 * Darstellung
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

	/**
	 * 
	 * @return Name der Phase
	 */
	public String getName() {
		return name != null ? name : super.toString();
	}

}
