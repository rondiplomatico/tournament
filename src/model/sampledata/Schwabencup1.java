package model.sampledata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import model.Team;
import model.Tournament;
import model.User;
import model.enums.TournamentState;
import model.enums.TournamentType;
import planning.control.mapping.Mapping;
import planning.control.score.ScoreTransfer;
import planning.model.RoundSetting;
import planning.model.StartoffTransition;
import planning.model.Transition;
import planning.model.rounds.RoundType;
import control.MainApplication;

public class Schwabencup1 {

	static List<User> users = new ArrayList<User>();

	public static void main(String[] args) {
		SampleData.clearDB();

		Tournament t = Schwabencup1(1);
		Tournament t2 = Schwabencup1(2);
		Tournament t3 = Schwabencup1(3);

		User manager = new User("manager", "test");
		manager.isManager(true);
		List<User> l = new ArrayList<User>();
		l.add(manager);
		t.setLeaders(l);
		t2.setLeaders(l);
		t3.setLeaders(l);
		users.add(manager);

		EntityManager em = MainApplication.getEntityManager();
		em.getTransaction().begin();
		for (User u : users) {
			em.persist(u);
		}
		em.persist(t);
		em.persist(t2);
		em.persist(t3);
		em.getTransaction().commit();
	}

	/**
	 * 
	 * @return Weltraumliga
	 */
	public static Tournament Schwabencup1(int version) {
		int gameTime = 15;
		int gameTimeFinals = 20;

		Calendar c = Calendar.getInstance();
		c.set(2014, 4, 26);
		Tournament t = new Tournament("1. Stuttgarter Schwabencup [v" + version
				+ "]", (float) 15.0, 200, "Foll subbr!", gameTime, c.getTime());
		t.setRequiredPlayersPerTeam(10);
		t.setType(TournamentType.MultiPlayer);
		t.setState(TournamentState.signupClosed);
		t.setSportKind("Handball");

		c.set(2014, 4, 28);
		t.setExpireDate(c.getTime());

		t.setStartHour(9);
		t.setEndHour(17);

		t.setFields(new String[] { "Allmandring" });

		switch (version) {
		case 1:
			Version1(t);
			break;
		case 2:
			Version2(t);
			break;
		case 3:
			Version3(t);
			break;
		}

		addTeam(t, "Stuttgart I");
		addTeam(t, "Mainz I");
		addTeam(t, "Münster I");
		addTeam(t, "Augsburg"); // 15

		addTeam(t, "PH Weingarten");
		addTeam(t, "Heidelberg");
		addTeam(t, "Karlsruhe");
		addTeam(t, "Mainz II");

		addTeam(t, "Hamburg");
		addTeam(t, "Paderborn");
		addTeam(t, "Hohenheim"); // 10
		addTeam(t, "Münster II");

		addTeam(t, "Essen");
		addTeam(t, "Tübingen");
		addTeam(t, "PH Karlsruhe"); // Nordsüdachse
		addTeam(t, "Stuttgart AH");

		return t;
	}

	private static void Version1(Tournament t) {
		t.addPlayTime(new Date(2014, 4, 27, 9, 00), new Date(2014, 4, 27, 13,
				00));
		t.addPlayTime(new Date(2014, 4, 27, 14, 00), new Date(2014, 4, 27, 17,
				30));
		t.addPlayTime(new Date(2014, 4, 28, 9, 00), new Date(2014, 4, 28, 14,
				00));

		t.getRoundSettings()
				.add(new RoundSetting("Positionierungsrunde",
						RoundType.GruppenRunde, 4, 0, new StartoffTransition()));
		RoundSetting rs = new RoundSetting("Zwischenrunde",
				RoundType.GruppenRunde, 2, 0, new Transition(4,
						ScoreTransfer.AllScores, Mapping.CombineMapper, 0));
		rs.setPairwiseMatching(true);
		t.getRoundSettings().add(rs);
		t.getRoundSettings().add(
				new RoundSetting("Finalrunden", RoundType.KORunde, 0, 10,
						new Transition(4, ScoreTransfer.NoScores,
								Mapping.TwoToPairwise, 20), 20));
	}

	private static void Version2(Tournament t) {
		t.addPlayTime(new Date(2014, 4, 27, 9, 00), new Date(2014, 4, 27, 13,
				00));
		t.addPlayTime(new Date(2014, 4, 27, 14, 00), new Date(2014, 4, 27, 17,
				30));
		t.addPlayTime(new Date(2014, 4, 28, 9, 00), new Date(2014, 4, 28, 14,
				00));
		t.getRoundSettings().add(
				new RoundSetting("Vollends durch'nand", RoundType.GruppenRunde,
						4, 0, new StartoffTransition()));

		RoundSetting rs = new RoundSetting("Nu wirds gmischd",
				RoundType.GruppenRunde, 2, 0, new Transition(4,
						ScoreTransfer.AllScores, Mapping.CombineMapper, 0));
		rs.setPairwiseMatching(true);
		t.getRoundSettings().add(rs);

		rs = new RoundSetting("Spreu vom Weiza trenna", RoundType.GruppenRunde,
				2, 0, new Transition(8, ScoreTransfer.AllScores,
						Mapping.CrossMapper, 0));
		rs.setPairwiseMatching(true);
		t.getRoundSettings().add(rs);

		t.getRoundSettings().add(
				new RoundSetting("Schlachd um dr Cup", RoundType.KORunde, 0,
						10, new Transition(4, ScoreTransfer.NoScores,
								Mapping.AlternatingCrossMapper, 0), 15));
	}

	/**
	 * 15 teams
	 * 
	 * @param t
	 */
	private static void Version3(Tournament t) {
		t.addPlayTime(new Date(2014, 4, 27, 9, 00), new Date(2014, 4, 27, 13,
				00));
		t.addPlayTime(new Date(2014, 4, 27, 14, 00), new Date(2014, 4, 27, 17,
				30));
		t.addPlayTime(new Date(2014, 4, 28, 10, 00), new Date(2014, 4, 28, 14,
				00));
		t.getRoundSettings().add(
				new RoundSetting("Vollends durch'nand", RoundType.GruppenRunde,
						4, 0, new StartoffTransition()));

		RoundSetting rs = new RoundSetting("Nu wirds gmischd",
				RoundType.GruppenRunde, 2, 0, new Transition(4,
						ScoreTransfer.AllScores, Mapping.CombineMapper, 0));
		rs.setPairwiseMatching(true);
		t.getRoundSettings().add(rs);

		rs = new RoundSetting("Spreu vom Weiza trenna", RoundType.GruppenRunde,
				2, 0, new Transition(8, ScoreTransfer.AllScores,
						Mapping.CrossMapper, 0));
		rs.setPairwiseMatching(true);
		t.getRoundSettings().add(rs);

		t.getRoundSettings().add(
				new RoundSetting("Schlachd um dr Cup", RoundType.KORunde, 0,
						10, new Transition(2, ScoreTransfer.NoScores,
								Mapping.AlternatingCrossMapper, 0), 20));
	}

	public static void addTeam(Tournament t, String name) {
		Team team = new Team(name);
		for (int i = 1; i < 11; i++) {
			User u;
			if (i == 1) {
				u = new User(name, "pw");
				u.isReferee(true);
				t.addReferee(u);
			} else {
				u = new User(name + " Spieler " + i, "pw");
			}
			team.addPlayer(u);
			team.getConfirmedPlayers().add(u);

			users.add(u);
		}
		t.addTeam(team);
	}

}
