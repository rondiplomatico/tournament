package planning.control.score;

import java.util.List;

import planning.model.Match;
import planning.model.Score;
import planning.model.TeamSlot;

/**
 * 
 * Überträgt einfach alle Punkte und Tore der Mannschaften in die nächste Runde.
 * 
 * @author Daniel Wirtz
 * 
 */
public class AllScoreTransfer implements IScoreCalculator {

	private static final long serialVersionUID = 4527148003030206052L;

	/**
	 * @see planning.control.score.IScoreCalculator#getScores(planning.model.TeamSlot,
	 *      java.util.List, java.util.List)
	 */
	@Override
	public Score getScores(TeamSlot forTeamSlot, List<Match> groupMatches, List<TeamSlot> ranking) {
		return forTeamSlot.Score;
	}

}
