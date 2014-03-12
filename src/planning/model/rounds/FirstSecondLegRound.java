/**
 * Created on 02.03.2009 in Project Tournament
 * Location: plans.rounds.FirstSecondLegRound.java
 */
package planning.model.rounds;

import java.util.List;

import javax.persistence.Entity;

import model.Tournament;
import planning.control.PlanningManager;
import planning.control.mapping.Mapping;
import planning.control.score.ScoreTransfer;
import planning.model.Group;
import planning.model.Phase;
import planning.model.Transition;

/**
 * 
 * Rundentyp "Hin & Rückrunde"
 * 
 * Zusammengebaut aus zwei Phasen, zwischen denen alle Punkte mitgenommen
 * werden.
 * 
 * @author Daniel Wirt
 * 
 */
@Entity
public class FirstSecondLegRound extends StandardGroupRound implements
		IMultiphaseRound {

	private static final long serialVersionUID = 5881088292330926750L;

	// Standardpausenzeit in Minuten
    /**
     *
     */
    protected int stdPause = 30;

	/** 
	 * Nach JPA-Spezifikation Standard-Konstruktor ohne Parameter 
	 */
	protected FirstSecondLegRound() {
	}

	/**
	 * 
	 * @param t
	 * @param name
	 */
	public FirstSecondLegRound(Tournament t, String name) {
		super(t, name);
	}

	/**
	 * Setzt die Pausenzeit zwischen den Phasen.
	 * 
	 * @see planning.model.rounds.IMultiphaseRound#setPauseMinutes(int)
	 */
	@Override
	public void setPauseMinutes(int value) {
		stdPause = value;
	}

	/**
	 * Gibt die Pausenzeit in Minuten zwischen den Phasen aus.
	 * 
	 * @see planning.model.rounds.IMultiphaseRound#getPauseMinutes()
	 */
	@Override
	public int getPauseMinutes() {
		return stdPause;
	}

	/**
	 * 
	 * Erstellt die Runde mit zwei Phasen, und übernimmt im Übergang alle Punkte
	 * der Vorrunde mit in die Rückrunde.
	 * 
	 * @see planning.model.rounds.Round#build(PlanningManager, IGroupRound)
	 */
	@Override
	public void build(PlanningManager pm, IGroupRound round) {
		phases.clear();
		// Hinrunde
		Phase hin = new Phase(this, numGroups, "Hinrunde");
		hin.setInTransition(inTransition);
		phases.add(hin);

		// Rückrunde. Alle Teams, Punkte & Tore direkt in identische Gruppen
		// übernehmen (StraightMapper)
		Transition trans = new Transition(Integer.MAX_VALUE, ScoreTransfer.AllScores, Mapping.CrossMapper, stdPause);
		hin.setOutTransition(trans);

		Phase rueck = new Phase(this, numGroups, "Rückrunde");
		rueck.setInTransition(trans);
		rueck.setOutTransition(outTransition);
		phases.add(rueck);

		pm.buildPhase(phases.get(0), round.getGroups());
		pm.buildPhase(phases.get(1), phases.get(0).getGroups());
	}

	/**
	 * @see planning.model.rounds.IGroupRound#getGroups()
	 */
	@Override
	public List<Group> getGroups() {
		return phases.get(1).getGroups();
	}

}
