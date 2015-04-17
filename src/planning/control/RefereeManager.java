/**
 * 
 */
package planning.control;

import java.util.Collections;
import java.util.List;

import model.Team;
import model.Tournament;
import model.User;
import planning.model.Group;
import planning.model.Match;
import planning.model.Phase;
import planning.model.rounds.Round;

/**
 * @author CreaByte
 * 
 */
public class RefereeManager {

	public void assignRefereesLinear(Phase p) {
		List<Match> matches = p.getMatches();
		if (matches.size() > 0) {
			Match prev = matches.get(matches.size() - 1);
			// Only one match in phase
			if (matches.size() == 1) {
				prev.setReferee(findRandomRef(prev));
				return;
			}
			// Otherwise..
			for (int k = 0; k < matches.size(); k++) {
				Match m = matches.get(k);
				User ref = findRefForPrevMatch(m, prev);
				if (ref != null) {
					m.setReferee(ref);
				}
				prev = m;
			}
		}
	}

	public void checkAndAssignNextPhase(Phase p) {
		if (p.isFinished()) {
			Phase next = null;
			// Determine next phase
			int idx = p.getRound().getPhases().indexOf(p);
			if (idx < p.getRound().getPhases().size() - 1) {
				next = p.getRound().getPhases().get(idx + 1);
				assignRefereesLinear(next);
			} else {
				// Else the last phase is currently finished; take the first one
				// of the next round (if present)
				List<Round> rounds = p.getRound().getPlan().getRounds();
				int ridx = rounds.indexOf(p.getRound());
				// Only do something if its not the last round anyways
				if (ridx < rounds.size() - 1) {
					next = rounds.get(ridx + 1).getPhases().get(0);
				}
			}
			if (next != null) {
				assignRefereesLinear(next);
			}
		}
	}

	public void assignReferees(Phase p) {
		for (Group g : p.getGroups()) {
			assignReferees(g);
		}
	}

	public void assignReferees(Group g) {
		for (int midx = 0; midx < g.getMatches().size(); midx++) {
			findRefereeForMatch(g, midx);
		}
	}

	private void findRefereeForMatch(Group curGr, int matchidx) {
		User ref = null;
		Match target = curGr.getMatches().get(matchidx);
		// Ref aus vorigem spiel nur möglich wenns mehr als ein spiel hat in der
		// phase
		if (curGr.getMatches().size() > 1) {
			// Voriges Match holen, beim ersten das letzte
			Match prev = matchidx > 0 ? curGr.getMatches().get(matchidx - 1)
					: curGr.getMatches().get(curGr.getMatches().size() - 1);
			ref = findRefForPrevMatch(target, prev);
		}
		// Kein ref gefunden in beiden (verschiedenen) vorteams: nimm irgend
		// einen.
		if (ref == null) {
			ref = findRandomRef(target);
		}
		assert (ref != null);
		target.setReferee(ref);
	}

	private User findRefForPrevMatch(Match target, Match prev) {
		User ref = null;
		// Versuch 1: Heimteam des vorigen Spiels
		Team team = prev.getHomeTeam().getTeam();
		if (team == target.getHomeTeam().getTeam()
				|| team == target.getGuestTeam().getTeam()) {
			// Spielt das vorige Heimteam jetzt wieder, das Gastteam
			// probieren
			team = prev.getGuestTeam().getTeam();
			if (team == target.getHomeTeam().getTeam()
					|| team == target.getGuestTeam().getTeam()) {
				// Spielt auch das Gastteam (warum auch immer), kann so kein
				// Schiri gewählt werden
				team = null;
			}
		}
		// Ist ein mögliches Team gefunden, schau ob es dort Schiris hat
		if (team != null) {
			Tournament t = target.getGroup().getPhase().getRound()
					.getTournament();
			List<User> prevrefs = t.getReferees(team);
			if (prevrefs.size() > 0) {
				// Nimm irgend einen
				Collections.shuffle(prevrefs);
				ref = prevrefs.get(0);
			}
		}
		return ref;
	}

	private User findRandomRef(Match target) {
		Tournament t = target.getGroup().getPhase().getRound().getTournament();
		List<User> refs = t.getReferees(target.getHomeTeam().getTeam(), true);
		Collections.shuffle(refs);
		User ref = refs.get(0);
		int idx = 1;
		while (target.getGuestTeam().getTeam() != null
				&& target.getGuestTeam().getTeam().getPlayers().contains(ref)
				&& idx < t.getReferees().size()) {
			ref = t.getReferees().get(idx++);
		}
		if (idx == t.getReferees().size()) {
			throw new RuntimeException(
					"Kein Schiedsrichter verfügbar, der nicht in einem der spielenden Teams in Match "
							+ target.toString() + " ist");
		}
		return ref;
	}

}
