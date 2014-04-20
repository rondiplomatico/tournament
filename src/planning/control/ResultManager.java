/**
 * Created on 06.03.2009 in Project Tournament
 * Location: planning.control.ResultManager.java
 */
package planning.control;

import model.Team;
import planning.model.Match;
import planning.model.Score;
import planning.model.TeamSlot;
import planning.view.TournamentPlanView;
import control.TournamentManager;

/**
 * 
 * Der ResultManager ermöglicht das Eintragen von Spielergebnissen und
 * aktualisieren der Stats eines Teams.
 * 
 * @author Daniel Wirtz
 * 
 */
public class ResultManager {

	private TournamentPlanView view;

	public ResultManager(TournamentPlanView planview) {
		this.view = planview;
	}

	/**
	 * Erlaubt es das Ergebnis der Begegnung einzutragen oder zu ändern. Dabei
	 * müssen beide Werte größer gleich null sein.
	 * 
	 * @param m
	 *            Match
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
		// Frameinhalt neu laden falls gruppe fertig ist
		if (m.getGroup().isFinished() && view != null) {
			view.reloadMatchPanel();
		}
	}

	/**
	 * Markiert alle Spiele des Slots als 1:0 verloren.
	 * 
	 * Ist der Gegner ebenfalls disqualifiziert, so wird das Spiel unentschieden
	 * eingetragen.
	 * 
	 * 
	 * @see TournamentManager#disqualify(model.Tournament, Team)
	 * @param ts
	 *            TeamSlot des disqualifizierten Teams
	 */
	public void markAllMatchesLost(TeamSlot ts) {
		for (Match m : ts.getGroup().getMatches()) {
			if (m.participating(ts)) {
				if (m.getHomeTeam().equals(ts)) {
					if (m.getGuestTeam().getTeam() != null
							&& m.getGuestTeam().getTeam().isDisqualified()) {
						setResult(m, 0, 0);
					} else {
						setResult(m, 0, 1);
					}
				} else {
					if (m.getHomeTeam().getTeam() != null
							&& m.getHomeTeam().getTeam().isDisqualified()) {
						setResult(m, 0, 0);
					} else {
						setResult(m, 1, 0);
					}
				}
			}
		}
	}

	/**
	 * Aktualisiert die Stats eines TeamSlots
	 * 
	 * @see TeamSlot
	 * @param ts
	 *            Team
	 */
	public void updateStats(TeamSlot ts) {
		if (ts.getGroup() == null) {
			return;
		}
		
		Score s = ts.Score;
		s.initFrom(ts.getInitialScore());
		
		// Alle Matches durchgehen und wenn nötig Daten akkumulieren
		for (Match m : ts.getGroup().getMatches()) {
			if (m.isFinished()) {
				if (m.participating(ts)) {
					if (!m.hasWinner()) {
						s.remis++;
					} else {
						if (m.getWinner().equals(ts))
							s.won++;
						else
							s.lost++;
					}
					s.goals_plus += m.getGoals(ts);
					s.goals_minus += m.getGoals(m.opponent(ts));
					s.points_plus += m.getPoints(ts);
					s.points_minus += m.getPoints(m.opponent(ts));
				}
			}
		}
	}
}
