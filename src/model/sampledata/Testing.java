/**
 * Created on 27.02.2009 in Project Tournament
 * Location: plans.Testing.java
 */
package model.sampledata;

import java.util.Date;

import model.Team;
import model.Tournament;
import model.User;
import model.enums.TournamentState;
import planning.control.PlanningManager;
import planning.control.mapping.Mapping;
import planning.control.score.ScoreTransfer;
import planning.model.RoundSetting;
import planning.model.StartoffTransition;
import planning.model.TournamentPlan;
import planning.model.Transition;
import planning.model.rounds.RoundType;
import planning.view.TournamentPlanView;

/**
 * 
 * Testzwecke
 * 
 * 27.02.2009
 * 
 * @author Daniel Wirtz Class Testing
 * 
 */
public class Testing {

    /**
     *
     */
    public static Tournament t;
    /**
     *
     */
    public static PlanningManager pm;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		pm = new PlanningManager();
		testMultigroup();
		// testLiga();
	}
	
    /**
     *
     * @param t
     * @param numRefs
     * @return User, der zum Leader gemacht wurde
     */
    public static User addRefs(Tournament t, int numRefs) {
		for (int x = 0; x < numRefs; x++) {
			User u = new User("Schiri " + (x + 1), "nix");
			u.isReferee(true);
			t.addReferee(u);	
		}
		t.getLeaders().add(t.getReferees().get(0));
		return t.getReferees().get(0);
	}

    /**
     *
     */
    public static void testMultigroup() {
		t = new Tournament("SV Adler Weltraumliga", 0, 100, "Alle können mitmachen! :-)", 15, new Date());
		t.setFields(5);
		t.setFields(new String[] { "Haupthalle", "Leichtbauh.", "LeoCampus 1", "HBS", "Sentr. Höhe" });
		t.setSportKind("Handball");
		t.setState(TournamentState.signupClosed);

		t.getRoundSettings().add(new RoundSetting("Vorrunde", RoundType.GruppenRunde, 4, 0, new StartoffTransition()));
		t.getRoundSettings().add(new RoundSetting("Hauptrunde", RoundType.GruppenRunde, 4, 0, new Transition(4, ScoreTransfer.Special1, Mapping.CrossMapper, 15)));
		t.getRoundSettings().add(new RoundSetting("Finale", RoundType.KORunde, 0, 45, new Transition(2, ScoreTransfer.NoScores, Mapping.CrossMapper, 30)));

		addRandomTeams(t, 16, false);
		
		User u = addRefs(t,5);

		TournamentPlan tp = pm.generateTournamentPlan(t, true);

		pm.schedule(tp, new Date());

		new TournamentPlanView(tp, u);
	}

    /**
     *
     */
    public static void testLiga() {
		t = new Tournament("SV Adler Weltraumliga", 0, 100, "Alle können mitmachen! :-)", 15, new Date());
		t.setFields(new String[] { "Haupth.", "Leichtbauh.", "Leo 1" });

		t.getRoundSettings().add(new RoundSetting("Liga", RoundType.LigaGruppenRunde, 2, 0, new StartoffTransition()));

		addRandomTeams(t, 9, false);

		User leader = addRefs(t, 5);

		TournamentPlan tp = pm.generateTournamentPlan(t, false);

		pm.schedule(tp, new Date());

		// pm.printSchedule(tp);

		new TournamentPlanView(tp, leader);
	}

    /**
     *
     * @param t
     * @param num
     * @param singleplayer
     */
    public static void addRandomTeams(Tournament t, int num, boolean singleplayer) {
		// TournamentManager tm = TournamentManager.getInstance();
		int ucnt = 1;
		for (int x = 0; x < num; x++) {
			Team team = new Team("Team " + (x + 1));
			for (int y = 0; y < t.getRequiredPlayersPerTeam(); y++) {
				User u = new User("User " + ucnt++, "test");
				team.addPlayer(u);
				team.getConfirmedPlayers().add(u);
			}
			t.addTeam(team);
		}
	}

}
