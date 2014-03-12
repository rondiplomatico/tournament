package model.sampledata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import control.MainApplication;
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
		addTeam(t, "Bremen");
		addTeam(t, "Hohenheim I");
		addTeam(t, "Hohenheim II");
		addTeam(t, "Essen");

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
		Calendar c = Calendar.getInstance();
		c.set(2014, 4, 26, 9, 0);
		Tournament t = new Tournament("1. Stuttgarter Schwabencup",
				(float) 15.0, 200, "Foll subbr!", 15, c.getTime());
		t.setRequiredPlayersPerTeam(10);
		t.setType(TournamentType.MultiPlayer);
		t.setState(TournamentState.signupClosed);
		t.setSportKind("Handball");
		c.set(2014, 4, 28);
		t.setExpireDate(c.getTime());
		t.setStartHour(9);
		t.setEndHour(17);

		t.setFields(new String[] { "Haupthalle" });

		t.getRoundSettings().add(
				new RoundSetting("Vorrunde", RoundType.GruppenRunde, 3, 0,
						new StartoffTransition()));
		t.getRoundSettings().add(
				new RoundSetting("Hauptrunde", RoundType.GruppenRunde, 2, 0,
						new Transition(4, ScoreTransfer.Special1,
								Mapping.CrossMapper, 30)));
		t.getRoundSettings().add(
				new RoundSetting("Finale", RoundType.KORunde, 0, 60,
						new Transition(4, ScoreTransfer.NoScores,
								Mapping.CrossMapper, 60)));

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
