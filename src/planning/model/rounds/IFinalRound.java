/**
 * Created on 27.02.2009 in Project Tournament
 * Location: plans.IFinalRound.java
 */
package planning.model.rounds;

import java.util.List;

import planning.model.TeamSlot;

/**
 * 
 * Indiziert das eine Runde eine letzte Runde eines Turniers ist;
 * man kann sich ein finales Ranking ausgeben lassen.
 * 
 * @author Daniel Wirtz
 * 
 */
public interface IFinalRound {

	/**
	 * Gibt das finale Ranking der Runde aus.
	 * 
	 * @pre Round.build wurde ausgeführt
	 * 
	 * @return Finales Ranking
	 */
	public List<TeamSlot> getFinalRanking();
}
