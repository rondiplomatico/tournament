/**
 * Created on 26.02.2009 in Project Tournament
 * Location: plans.Group.java
 */
package planning.model;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import planning.control.mapping.IMappingGroup;

/**
 * 26.02.2009
 * 
 * Repräsentiert eine Gruppe in einer Turnierphase.
 * 
 * Eine Gruppe enthält alle Teams und Begegnungen zwischen diesen.
 * 
 * @author Daniel Wirtz
 * @see Phase
 * @see TeamSlot
 * @see Match
 * 
 */
@Entity(name = "PhaseGroup")
public class Group implements Serializable, IMappingGroup {

	private static final long serialVersionUID = 2537294750381004704L;

	@Id
	@GeneratedValue
	private long id;

	@OneToMany(mappedBy = "containingGroup", cascade = CascadeType.ALL)
	@JoinTable(name = "group_teamslots")
	private List<TeamSlot> teamslots;

	@ManyToMany(cascade=CascadeType.ALL)
	//@JoinTable(name = "group_proceeding")
	private List<TeamSlot> proceeding;

	@OneToMany(mappedBy = "containingGroup", cascade = CascadeType.ALL)
	private List<Match> matches = null;

	private String name, shortName;
	private Color color = null;

	@ManyToOne(cascade=CascadeType.PERSIST)
	private Phase phase;

	/**
	 * Erstellt eine leer Gruppe.
	 */
	protected Group() {
		teamslots = new ArrayList<TeamSlot>();
		matches = new ArrayList<Match>();
		proceeding = new ArrayList<TeamSlot>();
	}

	/**
	 * Erstellt eine neue Gruppe in einer Phase.
	 * 
	 * @param phase Enthaltende Phase
	 * @param longName vollständiger Name
	 * @param shortName kurzer Name, für Anzeige in Team-Platzhaltern
	 */
	public Group(Phase phase, String longName, String shortName) {
		this();

		this.phase = phase;
		this.name = longName;
		this.shortName = shortName;
	}

	/**
	 * Gibt die Anzahl der Teams an, die wirklich weiterkommen können.
	 * Ist das Minimum aus in der Transition festgelegter Anzahl und der tatsächlichen TeamSlots in der Gruppe.
	 * 
	 * Gibt es keine nächste Phase, wird 0 zurückgegeben.
	 * 
	 * @return int Anzahl weiterkommender Teams
	 */
	public int getActualNumProceedants() {
		if (phase.getOutTransition() == null) return 0;
		return Math.min(phase.getOutTransition().getNumProceedantsPerGroup(), teamslots.size());
	}

	/**
	 * Gibt die TeamSlots der Gruppe zurück, als unveränderbare Liste.
	 * 
	 * Sortiert in der aktuellen Rangfolge, sofern schon bestimmbar.
	 * 
	 * @return alle Teamslots
	 */
	public List<TeamSlot> getSlots() {
		return teamslots;
	}

	/**
	 * @see planning.control.mapping.IMappingGroup#getProceedingSlots()
	 */
	public List<TeamSlot> getProceedingSlots() {
		return proceeding;
	}

	/**
	 * Gibt die Begegnungen der Gruppe in der geplanten Reihenfolge (sofern das
	 * Scheduling schon durchgeführt wurde) als unveränderbare Liste zurück.
	 * 
	 * @return Liste der Begegnungen 
	 */
	public List<Match> getMatches() {
		return matches;
	}

	/**
	 * Farbe der Gruppe für die visuelle Zuordnung der TeamSlots in fortlaufenden Gruppen.
	 * @return (zufällige) Gruppenfarbe
	 */
	public Color getColor() {
		if (color == null) {
			int seed = (int) (Math.random() * 255);
			// % 200 für nicht zu helle Farben
			color = new Color(seed * 17 % 200, seed * 9 % 200, seed * 60 % 200);
		}
		return color;
	}

	/**
	 * 
	 * @return Enthaltende Phase
	 */
	public Phase getPhase() {
		return phase;
	}

	/**
	 * 
	 * @return true falls alle Begegnungen beendet sind, false sonst.
	 */
	public boolean isFinished() {
		if (teamslots.size() < 2) return true;
		for (Match m : getMatches()) {
			if (!m.isFinished())
				return false;
		}
		return true;
	}

	/**
	 * @see planning.control.mapping.IMappingGroup#addTeamSlot(planning.model.TeamSlot)
	 */
	public void addTeamSlot(TeamSlot ts) {
		teamslots.add(ts);
		/**
		 * Rückbezug herstellen
		 * 
		 * @see TeamSlot#setGroup()
		 */
		ts.setGroup(this);
	}

	/**
	 * 
	 * @return vollständiger Name
	 */
	public String getLongName() {
		return name;
	}

	/**
	 * 
	 * @return kurzer Name
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * Debug
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return phase + "." + name;
	}

	/**
	 * @param name the name to set
	 */
	public void setLongName(String name) {
		this.name = name;
	}
}
