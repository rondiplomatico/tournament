/**
 * Created on 05.03.2009 in Project Tournament
 * Location: planning.control.GroupManager.java
 */
package planning.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.Team;
import planning.model.Group;
import planning.model.Match;
import planning.model.Score;
import planning.model.TeamSlot;
import control.TournamentManager;

/**
 * 05.03.2009
 * 
 * @author Daniel Wirtz
 * 
 */
public class GroupManager {

	/**
	 * Berechnet die Begegnungen für eine Gruppe. Ein erneuter Aufruf löscht
	 * alle vorigen Matches.
	 * 
	 * Alle Begegnungen innerhalb einer Spielgruppe werden stets
	 * jeder-gegen-jeden ausgeführt. Dabei werden die Matches in einer
	 * Reihenfolge generiert, die einer Aufteilung in "Spieltage" entspricht;
	 * ein "Spieltag" bedeutet das jede Mannschaft genau einmal spielt, und es
	 * gibt <b>n-1</b> Spieltage bei <b>n</b> Teams<br>
	 * <br>
	 * Ist die Anzahl der Mannschaften ungerade, setzt jede Mannschaft einen
	 * Spieltag aus.
	 * 
	 * @post Alle unter den zum Zeitpunkt des Aufrufs eingetragenen Teams
	 *       auszutragenden Matches dieser Runde sind berechnet.
	 *   
	 * @param g Gruppe
	 */
	public void calculateMatches(Group g) {
		g.getMatches().clear();

		LinkedList<TeamSlot> hlp = new LinkedList<TeamSlot>(g.getSlots());
		int n = g.getSlots().size();
		boolean even = n % 2 == 0;
		for (int st = 0; st < (even ? n - 1 : n); st++) {
			int left = 0;
			int right = n - 1;
			if (!even) right--;
			// Matches zuweisen
			while (left < right) {
				g.getMatches().add(new Match(g, hlp.get(left), hlp.get(right)));
				left++;
				right--;
			}
			// Rotieren
			TeamSlot move = even ? hlp.remove(n - 2) : hlp.remove(n - 1);
			hlp.addFirst(move);
		}
	}

	/**
	 * Erzeugt TeamSlots für die weiterkommenden Teams dieser Runde in Anzahl
	 * gemäß der Regeln der Phase. Dabei ist die Liste sortiert nach späterem
	 * Rang des Teams im Slot.
	 * 
	 * @pre Die outTransition der Phase ist gesetzt.
	 * 
	 * @param g Gruppe
	 */
	public void generateTransitionSlots(Group g) {
		g.getProceedingSlots().clear();
		/*
		 * Kommt nur ein Team weiter, einfach den Slot kopieren, da es in der
		 * Gruppe eh keine Spiele geben wird.
		 */
		if (g.getSlots().size() == 1) {
			g.getProceedingSlots().add(g.getSlots().get(0));
		} else {
			/*
			 * Normalerweise sollten so viele Slots erzeugt werden, wie die
			 * Transition angibt, aber sollte die Gruppe (als Restgruppe z.B.)
			 * weniger TeamSlots enthalten, dürfen natürlich auch nur so viele
			 * zurückgegeben werden.
			 */
			for (int x = 0; x < g.getActualNumProceedants(); x++) {
				// Gibts nur eine Gruppe, den Phasennamen verwenden. Sonst den
				// Gruppennamen.
				String name = (x + 1)
						+ ". "
						+ (g.getPhase().getGroups().size() > 1 ? g.getPhase().getName()
								+ " " + g.getShortName() : g.getPhase().getName());
				g.getProceedingSlots().add(new TeamSlot(name, g.getColor()));
			}
		}
	}

	/**
	 * Teilt der Gruppe mit, das ein Match beendet wurde. Dabei wird das Ranking
	 * automatisch aktualisiert.
	 * 
	 * Hier reicht ein einfacher Aufruf zur Collection-Klasse, da TeamSlot
	 * Comparable implementiert.
	 * 
	 * @see TeamSlot#compareTo(TeamSlot)
	 * 
	 * @param inGroup Zielgruppe
	 */
	public void matchResultUpdated(Group inGroup) {
		Collections.sort(inGroup.getSlots());

		/*
		 * Sind alle Begegnungen gespielt, die Gruppe finalisieren.
		 */
		for (Match m : inGroup.getMatches()) {
			if (!m.isFinished()) return;
		}
		finalizeGroup(inGroup);
		new PlanningManager().assignReferees(inGroup); 
	}

	/**
	 * Markiert alle Spiele des Slots als 1:0 verloren.
	 * 
	 * Ist der Gegner ebenfalls disqualifiziert, so wird das Spiel unentschieden
	 * eingetragen.
	 * 
	 * 
	 * @see TournamentManager#disqualify(model.Tournament, Team)
	 * @param ts TeamSlot des disqualifizierten Teams
	 */
	public void markAllMatchesLost(TeamSlot ts) {
		ResultManager rm = new ResultManager();
		for (Match m : ts.getGroup().getMatches()) {
			if (m.participating(ts)) {
				if (m.getHomeTeam().equals(ts)) {
					if (m.getGuestTeam().getTeam() != null
							&& m.getGuestTeam().getTeam().isDisqualified()) {
						rm.setResult(m, 0, 0);
					} else {
						rm.setResult(m, 0, 1);
					}
				} else {
					if (m.getHomeTeam().getTeam() != null
							&& m.getHomeTeam().getTeam().isDisqualified()) {
						rm.setResult(m, 0, 0);
					} else {
						rm.setResult(m, 1, 0);
					}
				}
			}
		}
	}

	/**
	 * Sind alle Matches gespielt, können die Teams in den proceeding-TeamSlots
	 * gesetzt werden.
	 * 
	 * Da die proceeding-Liste gemäß der späteren Reihenfolge der
	 * weiterkommenden Mannschaften sortiert ist, und die TeamSlots immer nach
	 * ihren Stats sortiert werden, ist hier ein einfaches first-to-first,
	 * 2nd-to-2nd etc vorzunehmen.
	 * 
	 * Ist eine Mannschaft disqualifiziert, wird sie unabhängig von ihrer
	 * aktuellen Position in der Gruppe nicht berücksichtigt.
	 */
	private void finalizeGroup(Group g) {
		List<Team> disq = new ArrayList<Team>();

		// Überhaupt nur etwas tun, falls es weiterkommende Teams gibt. Ist dies
		// der Fall, so gibt es auf jeden Fall auch eine OutTransition
		if (g.getProceedingSlots().size() > 0) {
			int idx = 0;
			for (TeamSlot ts : g.getProceedingSlots()) {
				if (idx < g.getSlots().size()) {
					Team team = g.getSlots().get(idx).getTeam();
					if (!team.isDisqualified()) {
						ts.setTeam(team, g.getPhase().getOutTransition().getScores(g.getSlots().get(idx), g.getMatches(), g.getSlots()));
					} else
						disq.add(team);
				} else {
					/**
					 * Spezialfall bei disqualifizierten Teams:<br>
					 * Es sind weniger Teams über (der Rest ist disqualifiziert)
					 * als weiterkommend vorgesehen.
					 * 
					 * Also werden die disqualifizierten Teams in Ghost-Teams
					 * ohne Spieler umgewandelt, gegen die man immer gewinnt.
					 * 
					 * Es kann nicht vorkommen das hier eine
					 * OutOfBounds-Exception geworfen wird, denn es werden
					 * maximal so viele proceedingSlots erzeugt wie Teams in der
					 * Gruppe sind.
					 * 
					 * @see GroupManager#generateTransitionSlots
					 */
					Team ghost = disq.get(0);
					// ghost.getPlayers().clear();
					// ghost.setName("Ghost-Team");
					ts.setTeam(ghost, new Score());
					// Alle Begegnungen in der nächsten Runde schonmal für die
					// Geisterteams als verloren eintragen.
					markAllMatchesLost(ts);
					disq.remove(ghost);
				}
				idx++;
			}
		}
		g.setFinished();
	}

}
