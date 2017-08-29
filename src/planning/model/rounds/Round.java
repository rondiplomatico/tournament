/**
 * Created on 26.02.2009 in Project Tournament
 * Location: plans.IRound.java
 */
package planning.model.rounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import model.Tournament;

import org.hibernate.annotations.IndexColumn;

import planning.control.PlanningException;
import planning.control.PlanningManager;
import planning.model.Match;
import planning.model.Phase;
import planning.model.TournamentPlan;
import planning.model.Transition;

/**
 * Basisklasse einer Runde. Enthält die Phasenliste und inTransition.
 * 
 * Wegen mangelnder Mehrfachvererbung in Java müssen die verschiedenen
 * Eigenschaften von Runden über Interfaces ausgedrückt werden, die leider zu
 * einer beliebigen lokalen Implementierung führt; bestes Beispiel ist
 * IMultiphaseRound, hier haben FirstSecondLegRound und KORound eigene private
 * Attribute zum Speichern der Pausenzeit.<br>
 * Evtl. wäre ein Decorator.-Pattern nicht verkehrt gewesen.
 * 
 * Welche Runde implementiert welches Interface: <br>
 * IFinalRound:<br>
 * <ul>
 * <li>FinalSingleGroupRound</li>
 * <li>KORound</li>
 * <li>LeagueRound</li>
 * </ul>
 * 
 * IMultiphaseRound<br>
 * <ul>
 * <li>FirstSecondLegRound</li>
 * <li>KORound</li>
 * <li>LeagueRound</li>
 * </ul>
 * 
 * IGroupRound<br>
 * <ul>
 * <li>FirstSecondLegRound</li>
 * <li>StandardGroupRound</li>
 * </ul>
 * 
 * @see IGroupRound
 * @see IFinalRound
 * @see IMultiphaseRound
 * 
 * @author Daniel Wirtz
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Round implements Serializable {

	private static final long serialVersionUID = -476503157298509036L;

	@Id
	@GeneratedValue
	private long id;

	/**
     *
     */
	@JoinColumn(name = "round_id")
	@IndexColumn(name = "phaseOrder")
	@OneToMany(cascade = CascadeType.ALL)
	protected List<Phase> phases;

	/** Das der Runde zugeordnete Turnier */
	@ManyToOne
	protected Tournament tournament;

	/** Der Name der Runde */
	protected String name;
	protected String hint;

	protected int gameTime = Integer.MIN_VALUE;
	protected boolean pairwiseMatching = false;

	/**
     *
     */
	@OneToOne(cascade = CascadeType.ALL)
	protected Transition inTransition;

	@ManyToOne
	public TournamentPlan plan;

	/**
	 * Erstellt eine leere Runde.
	 */
	protected Round() {
		phases = new ArrayList<Phase>(0);
		inTransition = null;
	}

	/**
	 * Basiskonstruktor, der Turnier und Name entgegennimmt.
	 * 
	 * @param t
	 * @param name
	 */
	public Round(Tournament t, String name) {
		this();
		this.tournament = t;
		this.name = name;
	}

	/**
	 * 
	 * @return <b>true</b> falls alle Spiele beendet sind, <b>false</b> sonst.
	 */
	public boolean isFinished() {
		for (Phase p : phases) {
			if (!p.isFinished())
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @return Turnier das die Runde enthält
	 */
	public Tournament getTournament() {
		return tournament;
	}

	/**
	 * 
	 * @return Turnier das die Runde enthält
	 */
	public TournamentPlan getPlan() {
		return plan;
	}

	/**
	 * 
	 * @return Die Phasen der Runden
	 */
	public List<Phase> getPhases() {
		return phases;
	}

	public List<Match> getMatches() {
		List<Match> res = new ArrayList<Match>();
		for (Phase p : phases) {
			res.addAll(p.getMatches());
		}
		Collections.sort(res);
		return res;
	}

	public int getGameTime() {
		return gameTime < 0 ? tournament.getGameDuration() : gameTime;
	}

	public int getGameTimeInclPause() {
		return getGameTime() + tournament.getGamePause();
	}

	public void setGameTime(int value) {
		gameTime = value;
	}

	public boolean getPairwiseMatching() {
		return pairwiseMatching;
	}

	public void setPairwiseMatching(boolean value) {
		pairwiseMatching = value;
	}

	/**
	 * Erstellt eine Runde mit allen inneren Phasen.<br>
	 * <br>
	 * Hinweis für <b>eigene Implementierungen</b>:
	 * {@link PlanningManager#buildPhase(Phase, List)} darf erst aufgerufen
	 * werden, wenn {@link Phase#setOutTransition(Transition)} aufgerufen wurde.<br>
	 * Dies liegt daran, das beim erstellen einer Phase die TeamSlots der
	 * weiterkommenden Teams generiert werden. Die Anzahl wird aber aus der
	 * outTransition der Phase (falls gesetzt) festgestellt.
	 * 
	 * @param pm
	 *            PlanningManager
	 * @param round
	 *            Quellrunde
	 */
	public abstract void build(PlanningManager pm, IGroupRound round)
			throws PlanningException;

	/**
	 * 
	 * @param value
	 *            Der Übergang aus der vorigen Runde
	 */
	public void setInTransition(Transition value) {
		// inTransition = value != null ? value.clone():null;
		inTransition = value;
	}

	/**
	 * 
	 * @return Übergang aus voriger Runde
	 */
	public Transition getInTransition() {
		return inTransition;
	}

	/**
	 * 
	 * @return Name der Runde
	 */
	public String getName() {
		return name != null ? name : super.toString();
	}

	/**
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
}
