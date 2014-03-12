/**
 * Created on 12.03.2009 in Project Tournament
 * Location: planning.model.rounds.LeagueRound.java
 */
package planning.model.rounds;

import java.util.List;

import javax.persistence.Entity;

import model.Tournament;
import planning.control.PlanningManager;
import planning.control.mapping.Mapping;
import planning.control.score.ScoreTransfer;
import planning.model.Phase;
import planning.model.TeamSlot;
import planning.model.Transition;

/**
 * 
 * Eine Runde im Liga-Stil mit Hin- und Rückrunde.
 * Implementiert auch das IFinalRound-interface, ist also
 * ein in sich geschlossener Turnierverlauf mit nur einer Runde.
 * 
 * @author DanielWirtz
 *
 */
@Entity
public class LeagueRound extends Round implements IFinalRound, IMultiphaseRound {

	private static final long serialVersionUID = 8570729430135954412L;
	
	private int stdPause;
	
	/**
	 * JPA-Erforderlicher Konstruktor
	 */
	protected LeagueRound() {
		
	}
	
	/**
	 * Weiterleitung auf {@link Round#Round(Tournament, String)}.
	 * Nur Notwendig wegen protected-Konstruktor für JPA.
	 * 
	 * @param t
	 * @param name
	 */
	public LeagueRound(Tournament t, String name) {
		super(t, name);
	}

	/**
	 * @see planning.model.rounds.IFinalRound#getFinalRanking()
	 */
	@Override
	public List<TeamSlot> getFinalRanking() {
		return phases.get(1).getGroups().get(0).getSlots();
	}

	/**
	 * Erstellt die Ligarunde.
	 * 
	 * Es gibt zwei Phasen (Hin-Rück), und die letzte ist zugleich die letzte Phase des Turniers.
	 * 
	 * @see planning.model.rounds.Round#build(planning.control.PlanningManager, planning.model.rounds.IGroupRound)
	 */
	@Override
	public void build(PlanningManager pm, IGroupRound round) {
		phases.clear();
		// Hinrunde
		Phase hin = new Phase(this, 1, "Hinrunde");
		hin.setInTransition(inTransition);
		phases.add(hin);

		// Rückrunde. Alle Teams, Punkte & Tore direkt in identische Gruppen
		// übernehmen (StraightMapper)
		Transition trans = new Transition(Integer.MAX_VALUE, ScoreTransfer.AllScores, Mapping.CrossMapper, stdPause);
		hin.setOutTransition(trans);

		Phase rueck = new Phase(this, 1, "Rückrunde");
		rueck.setInTransition(trans);
		phases.add(rueck);

		pm.buildPhase(phases.get(0), round.getGroups());
		pm.buildPhase(phases.get(1), phases.get(0).getGroups());
	}

	/**
	 * 
	 * @see planning.model.rounds.IMultiphaseRound#getPauseMinutes()
	 */
	@Override
	public int getPauseMinutes() {
		return stdPause;
	}

	/**
	 * 
	 * @see planning.model.rounds.IMultiphaseRound#setPauseMinutes(int)
	 */
	@Override
	public void setPauseMinutes(int value) {
		stdPause = value;
	}

}
