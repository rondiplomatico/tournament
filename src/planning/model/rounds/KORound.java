/**
 * Created on 26.02.2009 in Project Tournament
 * Location: plans.KORound.java
 */
package planning.model.rounds;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import model.Tournament;
import planning.control.PlanningException;
import planning.control.PlanningManager;
import planning.control.mapping.Mapping;
import planning.control.score.ScoreTransfer;
import planning.model.Group;
import planning.model.Phase;
import planning.model.TeamSlot;
import planning.model.Transition;

/**
 * 26.02.2009
 * 
 * @author Daniel
 */
@Entity
public class KORound extends Round implements IFinalRound, IMultiphaseRound {

	private static final long serialVersionUID = 8371403514469549854L;

	/**
	 * Standardwert für die Pausendauer zwischen den Finalrunden. Einstellbar über
	 * {@link KORound#setPauseMinutes(int)}
	 */
	private int pauseMinutes = 30;

	/**
	 * Nach JPA-Spezifikation Standard-Konstruktor ohne Parameter
	 */
	protected KORound() {
		super();
	}

	/**
	 * 
	 * @param t
	 * @param name
	 */
	public KORound(Tournament t, String name) {
		super(t, name);
	}

	/**
	 * 
	 * @see planning.model.rounds.IMultiphaseRound#setPauseMinutes(int)
	 */
	@Override
	public void setPauseMinutes(int value) {
		this.pauseMinutes = value;
	}

	/**
	 * 
	 * @see planning.model.rounds.IMultiphaseRound#getPauseMinutes()
	 */
	@Override
	public int getPauseMinutes() {
		return pauseMinutes;
	}

	/**
	 * Die Phasen werden erst gebastelt, wenn die Anzahl der Teams aus der vorigen
	 * Runde feststehen, also build aufgerufen wird.
	 * 
	 * Die KO-Runde erzeugt auch ein Spiel um Platz 3.
	 * 
	 * Ist die Anzahl der Teams keine glatte zweierpotenz, wird die nächst höhere
	 * erforderliche Phase erzeugt (5,6,7 Teams -> Viertelfinale) und evtl. übrig
	 * bleibende Teams werden in einer Gruppe "geparkt" und kommen automatisch
	 * weiter.
	 * 
	 * @param groupSource
	 * @throws PlanningException
	 * @see Round#build(PlanningManager, IGroupRound)
	 */
	@Override
	public void build(PlanningManager pm, IGroupRound groupSource) throws PlanningException {
		phases.clear();
		// Anzahl der Teams bestimmen
		int totalTeams = 0;
		for (Group g : groupSource.getGroups()) {
			totalTeams += g.getActualNumProceedants();
		}

		// Anzahl der benötigten Phasen bestimmen & sie erzeugen, jede Phase
		// enthält die Hälfte
		// der Teams, also Zweierlogarithus
		int totalPhases = (int) Math.ceil(Math.log(totalTeams) / Math.log(2));

		// Gleiche Transition für alle
		Transition t = new Transition(1, ScoreTransfer.NoScores, Mapping.CrossMapper, pauseMinutes);
		// Für den letzten Übergang, um das Spiel um Platz 3 zu erzeugen
		Transition last = new Transition(2, ScoreTransfer.NoScores, Mapping.CrossMapper, pauseMinutes);

		// Erste Phase erhält die Gruppentransition und die sourceGroups
		int currentNumGroups = (int) Math.ceil(totalTeams / 2.0);

		totalPhases -= 1;
//		currentNumGroups -= 1;
		
		Phase next = new Phase(this, currentNumGroups, getCaption(0, totalPhases));
		next.setInTransition(inTransition);
		next.setOutTransition(totalPhases == 2 ? last : t);
		pm.buildPhase(next, groupSource.getGroups());
		if (totalPhases == 1) {
			setFinalPhaseLabels(next);
		}
		phases.add(next);

		for (int phaseIdx = 1; phaseIdx < totalPhases; phaseIdx++) {
			// Halb so viele Gruppen erstellen wie Teams
			currentNumGroups = (int) Math.ceil(currentNumGroups / 2.0);

			/**
			 * Feststellen obs ein Spiel um Platz 3 gibt.
			 */
			boolean finalRoundWith3rdGame = false;
			// Es ist die letzte Phase
			if (currentNumGroups == 1) {
				int teams = 0;
				for (Group g : next.getGroups()) {
					teams += g.getProceedingSlots().size();
				}
				if (teams > 2) {
					finalRoundWith3rdGame = true;
				}
			}
			// Normalfall
			if (!finalRoundWith3rdGame) {
				next = new Phase(this, currentNumGroups, getCaption(phaseIdx, totalPhases));
				next.setInTransition(t);

				// Die Finalrunde hat zwei Gruppen, Finale & Spiel um Platz 3
			} else {
				next = new Phase(this, 2, getCaption(phaseIdx, totalPhases));
				next.setInTransition(last);
			}
			/*
			 * Immer die Richtigen outTransitions setzen. Im letzten Fall gibt's keine
			 * outTransition.
			 */
			if (phaseIdx < totalPhases - 2) {
				next.setOutTransition(t);
			} else if (phaseIdx < totalPhases - 1) {
				next.setOutTransition(last);
			}

			pm.buildPhase(next, phases.get(phaseIdx - 1).getGroups());

			/*
			 * Für die Finalphase, Schönheitsoperation
			 */
			if (currentNumGroups == 1) {
				setFinalPhaseLabels(next);
			}
			phases.add(next);
		}
		/*
		 * Noch "schöne" Namen für die Finalrunden
		 */
		// for (int pidx = 0; pidx < phases.size() - 1; pidx++) {
		// Phase p = phases.get(pidx);
		// for (int k = 1; k <= p.getGroups().size(); k++) {
		// p.getGroups().get(k - 1)
		// .setLongName(getCaption(pidx, totalPhases) + " " + k);
		// }
		// }

	}

	private void setFinalPhaseLabels(Phase next) {
		next.getGroups().get(0).setLongName("Finale");
		if (next.getGroups().size() > 1) {
			next.getGroups().get(1).setLongName("Spiel um Platz 3");
		}
		// Swap groups to have game for 3rd place scheduled first!
		next.getGroups().add(next.getGroups().get(0));
		next.getGroups().remove(0);
	}

	private String getCaption(int phaseIdx, int totalPhases) {
		if (phaseIdx == totalPhases - 1) {
			return "Finale";
		} else {
			return "1/" + (int) Math.pow(2, totalPhases - phaseIdx - 1) + " Finale";
		}
	}

	/**
	 * 
	 * Gibt die ersten vier Gewinner der Finalrunde aus.
	 * 
	 * @see planning.model.rounds.IFinalRound#getFinalRanking()
	 * 
	 * @pre {@link Round#isFinished()} == true
	 */
	@Override
	public List<TeamSlot> getFinalRanking() {
		List<TeamSlot> ranking = new ArrayList<TeamSlot>();
		Phase last = phases.get(phases.size() - 1);
		assert (last.isFinished()) : "Finalrunde ist nicht beendet!";

		// Finalgruppe in Ranking-reihenfolge
		ranking.addAll(last.getGroups().get(0).getSlots());
		if (last.getGroups().size() > 1) {
			// Spiel um Platz 3-Gruppe in Ranking-Reihenfolge
			ranking.addAll(last.getGroups().get(1).getSlots());
		}

		return ranking;
	}
}
