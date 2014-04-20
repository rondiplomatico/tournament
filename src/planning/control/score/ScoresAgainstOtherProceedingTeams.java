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
	public Score getScores(TeamSlot forTeamSlot, List<Match> groupMatches,
			List<TeamSlot> ranking) {
		// return new Score(0,0);
		int numProc = forTeamSlot.getGroup().getActualNumProceedants();
		int ptsp = 0, ptsm = 0, glsp = 0, glsm = 0, won = 0, remis = 0, lost = 0, g1, g2;
		for (int x = 0; x < numProc; x++) {
			TeamSlot ts = ranking.get(x);
			// Nicht die Punkte gegen sich selbst.
			if (!ts.equals(forTeamSlot)) {
				for (Match m : groupMatches) {
					if (m.isFinished() && m.participating(ts)) {
						ptsp += m.getPoints(forTeamSlot);
						ptsm += m.getPoints(ts);
						g1 = m.getGoals(forTeamSlot);
						g2 = m.getGoals(ts);
						glsp += g1;
						glsm += g2;
						if (g1 > g2) {
							won++;
						} else if (g1 < g2) {
							lost++;
						} else
							remis++;
					}
				}
			}
		}
		return new Score(ptsp, ptsm, glsp, glsm, won, remis, lost);
	}

}
