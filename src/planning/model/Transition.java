/**
 * Created on 28.02.2009 in Project Tournament
 * Location: plans.ITransition.java
 */
package planning.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import planning.control.mapping.IGroupMapper;
import planning.control.mapping.IMappingGroup;
import planning.control.mapping.Mapping;
import planning.control.score.IScoreCalculator;
import planning.control.score.ScoreTransfer;
import planning.model.rounds.Round;

/**
 * 
 * Stellt eine Transferkonfiguration einer Phase dar. Dabei bestimmen die
 * Daten/Methoden den übergang von der VORIGEN Phase in die die Transition
 * enthaltende Phase.
 *
 * 
 * @author Daniel Wirtz
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Transition implements Serializable, IGroupMapper, IScoreCalculator {

	private static final long serialVersionUID = 1942312870419540707L;

	@SuppressWarnings("unused")
	@Id
	@GeneratedValue
	private int id;

	private int numProc;
	private Mapping map;
	private ScoreTransfer scoretrans;
	private int pauseMinutes;

	/**
	 * Erstellt eine Transition mit Default-Werten
	 */
	public Transition() {
		this(1, ScoreTransfer.NoScores, Mapping.CrossMapper, 0);
	}

	/**
	 * Bequemlichkeitskonstruktor, für Verwendung z.B. in Test-Cases und in 
	 *
	 * @see Round#build(planning.control.PlanningManager, planning.model.rounds.IGroupRound)
	 * 
	 * @pre numProceedants > 0
	 * 
	 * @param numProceedants Weiterkommende Teams pro Gruppe 
	 * @param st ScoreTransfer-Strategie
	 * @param map Mapping
	 * @param pauseMinutesBetween Pause 
	 */
	public Transition(int numProceedants, ScoreTransfer st, Mapping map, int pauseMinutesBetween) {
		assert (numProceedants > 0);
		this.numProc = numProceedants;
		this.scoretrans = st;
		this.map = map;
		this.pauseMinutes = pauseMinutesBetween;
	}

	/**
	 * @param value
	 */
	public void setScoreTransferStrategy(ScoreTransfer value) {
		this.scoretrans = value;
	}

	/**
	 * 
	 * @return Punktetransfer
	 */
	public ScoreTransfer getScoreTransferStrategy() {
		return scoretrans;
	}

	/**
	 * 
	 * @param value
	 */
	public void setMappingStrategy(Mapping value) {
		this.map = value;
	}

	/**
	 * 
	 * @return Mapping-Stragie
	 */
	public Mapping getMappingStrategy() {
		return map;
	}

	/**
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Transition: " + numProc + " Teams, Mapper:" + map
				+ ", ScoreCalc:" + scoretrans;
	}

	/**
	 * 
	 * @return weiterkommende Teams pro Gruppe
	 */
	public int getNumProceedantsPerGroup() {
		return numProc;
	}

	/**
	 * 
	 * @param numProc
	 */
	public void setNumProceedantsPerGroup(int numProc) {
		this.numProc = numProc;
	}

	/**
	 * 
	 * @return Pause zwischen den Runden
	 */
	public int getPauseMinutes() {
		return pauseMinutes;
	}

	/**
	 * 
	 * @param pauseMinutes
	 */
	public void setPauseMinutes(int pauseMinutes) {
		this.pauseMinutes = pauseMinutes;
	}

	/**
	 * Benutzt die interne Strategie fürs Mapping. Wird an dieser Stelle
	 * zentralisiert, damit z.B. die StartoffTransition die Methode selber
	 * überschreiben kann.
	 * 
	 * @see planning.control.mapping.IGroupMapper#map(java.util.List,
	 *      java.util.List)
	 */
	@Override
	public void map(List<IMappingGroup> source, List<IMappingGroup> target) {
		map.getInstance().map(source, target);
	}

	/**
	 * 
	 * @see planning.control.score.IScoreCalculator#getScores(planning.model.TeamSlot, java.util.List, java.util.List)
	 */
	@Override
	public Score getScores(TeamSlot forTeamSlot, List<Match> groupMatches, List<TeamSlot> ranking) {
		return scoretrans.getInstance().getScores(forTeamSlot, groupMatches, ranking);
	}

}
