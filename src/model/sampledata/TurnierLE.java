package model.sampledata;

import java.text.DateFormat;
import java.time.LocalDate;
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

public class TurnierLE {

	static List<User> users = new ArrayList<User>();

	public static void main(String[] args) {
		SampleData.clearDB();

		Tournament t = TurnierLE2017();

		User manager = new User("manager", "test");
		manager.isManager(true);
		List<User> l = new ArrayList<User>();
		l.add(manager);
		t.setLeaders(l);
		users.add(manager);

		EntityManager em = MainApplication.getEntityManager();
		em.getTransaction().begin();
		for (User u : users) {
			em.persist(u);
		}
		em.persist(t);
		em.getTransaction().commit();
	}

	@SuppressWarnings("deprecation")
	public static Tournament TurnierLE2017() {
		Calendar c = Calendar.getInstance();
		c.set(2017, 8, 2);
		Tournament t = new Tournament("HSG TurnierLE 2017", (float) 15.0, 200,
				"Foll subbr!", 35, 10, c.getTime());
		t.setRequiredPlayersPerTeam(10);
		t.setType(TournamentType.MultiPlayer);
		t.setState(TournamentState.signupClosed);
		t.setSportKind("Handball");

		t.setExpireDate(c.getTime());

		t.setStartHour(9);
		t.setEndHour(17);

		t.setFields(new String[] { "Goldäcker", "PMH" });

		c.set(Calendar.MINUTE, 00);
		c.set(Calendar.HOUR, 11);
		Date start = c.getTime();
		c.set(Calendar.HOUR, 18);
		t.addPlayTime(start, c.getTime());
		RoundSetting rs = new RoundSetting("Gruppenrunde", RoundType.GruppenRunde, 2, 0,
				new StartoffTransition());
		rs.setHint("2x15 Minuten mit 5 Minuten Wechselpause");
		t.getRoundSettings().add(rs);

		rs = new RoundSetting("Platzierungsspiele",
				RoundType.GruppenRunde, 2, 0, new Transition(5,
						ScoreTransfer.AllScores, Mapping.EqualMapping, 10));
		rs.setPairwiseMatching(true);
		rs.setHint("2x15 Minuten mit 5 Minuten Wechselpause");
		t.getRoundSettings().add(rs);

		addTeam(t, "HSG Leinfelden-Echterdingen", "N. Elbert"); // BK
		addTeam(t, "SV Vaihingen 1", "A. Stammhammer"); // KL-A
		addTeam(t, "TSV Remshalden 2", "N. Elbert"); // KL-B
		addTeam(t, "Oberstenfeld 2", "R. Fink"); // KL-A
		addTeam(t, "HSG Ostfildern A-Jgd", "B. Schwarz"); // BUL
		addTeam(t, "SV Fellbach 2", "A. Stammhammer"); // BK
		addTeam(t, "SV Vaihingen 2", "B. Schwarz"); // KL-A
		addTeam(t, "HSG Ostfildern 2", "R. Fink"); // KL-A
		return t;
	}

	public static void addRef(Tournament t, String name) {
		User u = new User(name, "pw");
		u.isReferee(true);
		t.addReferee(u);
	}

	public static void addTeam(Tournament t, String name, String refName) {
		Team team = new Team(name);
		for (int i = 1; i < 11; i++) {
			User u;
			if (i == 1) {
				u = new User(refName, "pw");
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

	public static void addTeam(Tournament t, String name) {
		addTeam(t, name, name);
	}

}
