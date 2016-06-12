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

public class TurnierLE {

	static List<User> users = new ArrayList<User>();

	public static void main(String[] args) {
		SampleData.clearDB();

		Tournament t = TurnierLE2016();

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
	public static Tournament TurnierLE2016() {
		Calendar c = Calendar.getInstance();
		c.set(2014, 3, 25);
		Tournament t = new Tournament("HSG TurnierLE 2016", (float) 15.0, 200,
				"Foll subbr!", 25, 5, c.getTime());
		t.setRequiredPlayersPerTeam(10);
		t.setType(TournamentType.MultiPlayer);
		t.setState(TournamentState.signupClosed);
		t.setSportKind("Handball");

		c.set(2016, 9, 3);
		t.setExpireDate(c.getTime());

		t.setStartHour(9);
		t.setEndHour(17);

		t.setFields(new String[] { "Sportpark Goldäcker", "PMH" });

		t.addPlayTime(new Date(2016, 9, 3, 10, 00),
				new Date(2016, 9, 3, 18, 00));
		t.getRoundSettings().add(
				new RoundSetting("Vollends durch'nand", RoundType.GruppenRunde,
						2, 0, new StartoffTransition()));

		RoundSetting rs = new RoundSetting("Nu wirds gmischd",
				RoundType.GruppenRunde, 2, 0, new Transition(6,
						ScoreTransfer.AllScores, Mapping.CombineMapper, 0));
		rs.setPairwiseMatching(true);
		t.getRoundSettings().add(rs);

		// t.getRoundSettings().add(
		// new RoundSetting("Schlachd um dr Cup", RoundType.KORunde, 0,
		// 10, new Transition(2, ScoreTransfer.NoScores,
		// Mapping.AlternatingCrossMapper, 0), 20));

		addTeam(t, "HSG L-E M1");
		addTeam(t, "HSG L-E M2");
		addTeam(t, "TG Nürtingen 2");
		addTeam(t, "TSV Denkendorf 2"); // 15
		addTeam(t, "Neckartenzlingen");
		addTeam(t, "SSV Hohenacker 2");

		addTeam(t, "TSV Owen/Teck");
		addTeam(t, "Plochingen 2");
		addTeam(t, "Unterensingen 2");
		addTeam(t, "Uhingen-Holzhausen 2");
		addTeam(t, "HSG Oberer Neckar");
		addTeam(t, "SV Remshalden 2");

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
