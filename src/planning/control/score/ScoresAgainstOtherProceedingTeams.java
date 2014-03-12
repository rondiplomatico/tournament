package planning.control.score;

import java.util.List;

import planning.model.Match;
import planning.model.Score;
import planning.model.TeamSlot;

/**
 * 
 * Berechnet die Punkte die gegen alle ebenfalls weiterkommenden Teams erreicht
 * wurden und gibt sie zurück.
 * 
 * @author Daniel Wirtz
 * 
 */
public class ScoresAgainstOtherProceedingTeams implements IScoreCalculator {

	private static final long serialVersionUID = 1258954367613496854L;

	/**
	 * @see planning.control.score.IScoreCalculator#getScores(TeamSlot,
	 *      java.util.List, java.util.List)
	 */
	@Override
	public Score getScores(TeamSlot forTeamSlot, List<Match> groupMatches, List<TeamSlot> ranking) {
		// return new Score(0,0);
		int numProc = forTeamSlot.getGroup().getActualNumProceedants();
		int ptsp = 0, ptsm = 0, glsp = 0, glsm = 0;
		for (int x = 0; x < numProc; x++) {
			TeamSlot ts = ranking.get(x);
			// Nicht die Punkte gegen sich selbst.
			if (!ts.equals(forTeamSlot)) {
				for (Match m : groupMatches) {
					if (m.isFinished() && m.participating(ts)) {
						ptsp += m.getPoints(forTeamSlot);
						ptsm += m.getPoints(ts);
						glsp += m.getGoals(forTeamSlot);
						glsm += m.getGoals(ts);
					}
				}
			}
		}
		return new Score(ptsp, ptsm, glsp, glsm);
	}

}
