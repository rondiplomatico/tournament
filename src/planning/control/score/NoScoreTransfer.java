package planning.control.score;

import java.util.List;

import planning.model.Match;
import planning.model.Score;
import planning.model.TeamSlot;


/**
 * 
 * Es werden keine Punkte weitergegeben.
 * 
 * 
 * @author Daniel Wirtz
 * 
 */
public class NoScoreTransfer implements IScoreCalculator {

	private static final long serialVersionUID = -3962650705608418755L;

	/**
	 * 
	 * @see planning.control.score.IScoreCalculator#getScores(planning.model.TeamSlot, java.util.List, java.util.List)
	 */
	@Override
	public Score getScores(TeamSlot forTeamSlot, List<Match> groupMatches, List<TeamSlot> ranking) {
		return new Score();
	}

}
