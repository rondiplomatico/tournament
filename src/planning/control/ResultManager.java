/**
 * Created on 06.03.2009 in Project Tournament
 * Location: planning.control.ResultManager.java
 */
package planning.control;

import planning.model.Match;
import planning.model.Score;
import planning.model.TeamSlot;
import planning.model.TeamStats;

/**
 * 
 * Der ResultManager ermöglicht das Eintragen von Spielergebnissen und
 * aktualisieren der Stats eines Teams.
 * 
 * @author Daniel Wirtz
 * 
 */
public class ResultManager {

	/**
	 * Erlaubt es das Ergebnis der Begegnung einzutragen oder zu ändern. Dabei
	 * müssen beide Werte größer gleich null sein.
	 * @param m Match
	 * 
	 * @param home_goals
	 *            Tore der Heimmannschaft
	 * @param guest_goals
	 *            Tore der Gastmannschaft
	 * 
	 * @see Match
	 * 
	 * @pre home_goals >= 0
	 * @pre guest_goals >= 0
	 */
	public void setResult(Match m, int home_goals, int guest_goals) {
		assert (home_goals >= 0 && guest_goals >= 0);

		m.setResult(home_goals, guest_goals);
		// Stats in den Slots updaten
		updateStats(m.getHomeTeam());
		updateStats(m.getGuestTeam());
		// ggf. die Gruppe finalisieren
		new GroupManager().matchResultUpdated(m.getGroup());
	}

	/**
	 * Aktualisiert die Stats eines TeamSlots
	 * 
	 * @see TeamSlot
	 * @param ts
	 *            Team
	 */
	public void updateStats(TeamSlot ts) {
		// Wir können den Fehler nicht mehr auf die schnelle finden.
		// Vielleicht gibt's noch ein Wunder.
		if (ts.getGroup() == null) {
			return;
		}

		int played = 0, dp = 0, remis = 0, won = 0, dg = 0;

		int goals = ts.getInitialScore().getGoals();
		int points = ts.getInitialScore().getPointsPlus();

		// Alle Matches durchgehen und wenn nötig Daten akkumulieren
		for (Match m : ts.getGroup().getMatches()) {
			if (m.isFinished()) {
				if (m.participating(ts)) {
					if (!m.hasWinner()) {
						remis++;
					} else {
						if (m.getWinner().equals(ts)) won++;
					}
					dg += m.getGoals(m.opponent(ts));
					goals += m.getGoals(ts);
					points += m.getPoints(ts);
					dp += m.getPoints(m.opponent(ts));
					played++;
				}
			}
		}

		// Tore
		Score sc = ts.getStats().getScores();
		sc.setGoalsPlus(goals);
		sc.setGoalsMinus(dg);
		sc.setPointsPlus(points);
		sc.setPointsMinus(dp);

		// Gespielte Spiele
		ts.getStats().setField(TeamStats.GAMES, played + "/"
				+ (ts.getGroup().getSlots().size() - 1));
		// Gewonnen / Unentsch. / Verloren
		ts.getStats().setField(TeamStats.STAT, won + "/" + remis + "/"
				+ (played - won - remis));
		ts.getStats().setField(TeamStats.NAME, ts.getName());
	}
}
