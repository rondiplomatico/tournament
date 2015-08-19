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
import planning.model.RoundSetting;
import planning.model.StartoffTransition;
import planning.model.rounds.RoundType;
import control.MainApplication;

public class Schwabencup2 {

	static List<User> users = new ArrayList<User>();

	public static void main(String[] args) {
		SampleData.clearDB();

		Tournament t = Schwabencup2(3);

		User manager = new User("manager", "test");
		manager.isManager(true);
		List<User> l = new ArrayList<User>();
		l.add(manager);
		// t.setLeaders(l);
		// t2.setLeaders(l);
		t.setLeaders(l);
		users.add(manager);

		EntityManager em = MainApplication.getEntityManager();
		em.getTransaction().begin();
		for (User u : users) {
			em.persist(u);
		}
		// em.persist(t);
		// em.persist(t2);
		em.persist(t);
		em.getTransaction().commit();
	}

	/**
	 * 
	 * @return Weltraumliga
	 */
	public static Tournament Schwabencup2(int version) {
		int gameTime = 15;

		Calendar c = Calendar.getInstance();
		c.set(2014, 3, 25);
		Tournament t = new Tournament("1. Stuttgarter Schwabencup",
				(float) 15.0, 200, "Foll subbr!", gameTime, c.getTime());
		t.setRequiredPlayersPerTeam(10);
		t.setType(TournamentType.MultiPlayer);
		t.setState(TournamentState.signupClosed);
		t.setSportKind("Handball");

		c.set(2015, 3, 17);
		t.setExpireDate(c.getTime());

		t.setStartHour(10);
		t.setEndHour(17);

		t.setFields(new String[] { "Allmandring" });

		Version3(t);
		
		addTeam(t, "Stuttgart I");
		addTeam(t, "Stuttgart II");
		addTeam(t, "Stuttgart Oldies");
		
		addTeam(t, "Weingarten-Ravensburg II");
		addTeam(t, "Weingarten-Ravensburg");
		addTeam(t, "Hohenheim"); // 10
		
		addTeam(t, "Passau");
		addTeam(t, "Münster I"); // 15
		addTeam(t, "Augsburg II");
		
		addTeam(t, "Münster II");
		addTeam(t, "Augsburg I");
		addTeam(t, "Paderborn");
		
		addTeam(t, "Mainz");
		addTeam(t, "Mixed");

		return t;
	}


	/**
	 * 15 teams
	 * 
	 * @param t
	 */
	@SuppressWarnings("deprecation")
	private static void Version3(Tournament t) {
		t.addPlayTime(new Date(2015, 3, 18, 10, 00), new Date(2015, 3, 18, 17,
				00));
		t.getRoundSettings().add(
				new RoundSetting("Vollends durch'nand", RoundType.GruppenRunde,
						3, 0, new StartoffTransition()));

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
