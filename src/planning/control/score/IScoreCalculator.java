package planning.control.score;

import java.io.Serializable;
import java.util.List;

import planning.model.Match;
import planning.model.Score;
import planning.model.TeamSlot;

/**
 * 
 * Enthält vorläufig die Einstellungen für das Weiterkommen von Teams von der
 * einen Runde in die Nächste.
 * 
 * 
 * @author Daniel Wirtz
 * 
 */
public interface IScoreCalculator extends Serializable {

	/**
	 * Ermöglicht es, Punkte aus einer Runde in die Nächste mitzunehmen. Dafür
	 * werden alle Notwendigen informationen wie vergangene Begegnungen und die
	 * weiterkommenden Team(Slots) zur Verfügung gestellt.
	 * 
	 * @param forTeamSlot
	 * @param groupMatches
	 * @param ranking
	 * 
	 * @return Score
	 */
	public Score getScores(TeamSlot forTeamSlot, List<Match> groupMatches, List<TeamSlot> ranking);
}
