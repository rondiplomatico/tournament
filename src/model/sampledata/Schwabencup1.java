package model.sampledata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

		Tournament t = Schwabencup1();

		User manager = new User("manager", "test");
		manager.isManager(true);
		List<User> l = new ArrayList<User>();
		l.add(manager);
		t.setLeaders(l);
		users.add(manager);

		addTeam(t, "Stuttgart I");
		addTeam(t, "Stuttgart II");
		addTeam(t, "Mainz I");
		addTeam(t, "Mainz II");
		addTeam(t, "PH Weingarten");
		addTeam(t, "Karlsruhe");
		addTeam(t, "Münster I");
		addTeam(t, "Münster II");
		addTeam(t, "Hohenheim I");
		addTeam(t, "Hohenheim II");
		addTeam(t, "Essen");
		addTeam(t, "Hamburg");
		addTeam(t, "Augsburg");
		addTeam(t, "Mixed I");
		addTeam(t, "Mixed II");
		addTeam(t, "Mixed III");

		EntityManager em = MainApplication.getEntityManager();
		em.getTransaction().begin();
		for (User u : users) {
			em.persist(u);
		}
		em.persist(t);
		em.getTransaction().commit();
	}

	/**
	 * 
	 * @return Weltraumliga
	 */
	public static Tournament Schwabencup1() {
		int gameTime = 15;
		int gameTimeFinals = 20;

		Calendar c = Calendar.getInstance();
		c.set(2014, 4, 26);
		Tournament t = new Tournament("1. Stuttgarter Schwabencup",
				(float) 15.0, 200, "Foll subbr!", gameTime, c.getTime());
		t.setRequiredPlayersPerTeam(10);
		t.setType(TournamentType.MultiPlayer);
		t.setState(TournamentState.signupClosed);
		t.setSportKind("Handball");

		t.addPlayTime(new Date(2014, 4, 27, 9, 30), new Date(2014, 4, 27, 13,
				00));
		t.addPlayTime(new Date(2014, 4, 27, 14, 00), new Date(2014, 4, 27, 17,
				30));
		t.addPlayTime(new Date(2014, 4, 28, 9, 30), new Date(2014, 4, 28, 15,
				00));
		// Auffänger für config-testspielraum
		t.addPlayTime(new Date(2014, 4, 29, 6, 0), new Date(2014, 4, 29, 22, 0));

		c.set(2014, 4, 28);
		t.setExpireDate(c.getTime());

		t.setStartHour(9);
		t.setEndHour(17);

		t.setFields(new String[] { "Allmandring" });

		t.getRoundSettings().add(
				new RoundSetting("Vorrunde", RoundType.GruppenRunde, 4, 0,
						new StartoffTransition()));
		RoundSetting rs = new RoundSetting("Hauptrunde",
				RoundType.GruppenRunde, 2, 0, new Transition(4,
						ScoreTransfer.AllScores, Mapping.CombineMapper, 0));
		rs.setPairwiseMatching(true);
		t.getRoundSettings().add(rs);
		t.getRoundSettings().add(
				new RoundSetting("Finalrunden", RoundType.KORunde, 0, 10,
						new Transition(4, ScoreTransfer.NoScores,
								Mapping.TwoToPairwise, 20), 20));

		// t.getRoundSettings().add(
		// new RoundSetting("Vorrunde", RoundType.GruppenRunde, 4, 0,
		// new StartoffTransition()));
		// t.getRoundSettings().add(
		// new RoundSetting("Hauptrunde", RoundType.GruppenRunde, 2, 0,
		// new Transition(3, ScoreTransfer.NoScores,
		// Mapping.CrossMapper, 0)));
		// t.getRoundSettings().add(
		// new RoundSetting("Finalrunden", RoundType.KORunde, 0, 10,
		// new Transition(2, ScoreTransfer.NoScores,
		// Mapping.CrossMapper, 20),20));

		return t;
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
