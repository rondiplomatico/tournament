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
				new Date(2016, 9, 3, 19, 00));
		t.getRoundSettings().add(
				new RoundSetting("Vollends durch'nand", RoundType.GruppenRunde,
						2, 0, new StartoffTransition()));

		RoundSetting rs = new RoundSetting("Nu wirds gmischd",
				RoundType.GruppenRunde, 2, 0, new Transition(5,
						ScoreTransfer.AllScores, Mapping.CrossMapper, 0));
//		rs.setPairwiseMatching(true);
		t.getRoundSettings().add(rs);

		/*
		 * A1 - B1, B2, B5
		 * A2 - B1, B3, B2
		 * A3 - B2, B4, B3
		 * A4 - B3, B5, B4
		 * A5 - B4, B5, B1
		 * 
		 * A1 - B1 | A5 - B5	14:10
		 * A2 - B2 | A4 - B4	14:35
		 * A3 - B3 				15:00
		 * 	       | A1 - B2
		 * A2 - B3 | A3 - B4 	15:25
		 * A4 - B5 | A5 - B1 	15:50
		 * 
		 * A3 - B2 | A5 - B4    16:15
		 * A1 - B5 | A2 - B1    16:40
		 * A4 - B3	  	        17:05
		 * 
		 * 17:30 Ende
		 * 
		 * 
		 * A5 - B4 | A4 - B3
		 * A3 - B2 | A2 - B1
		 * A1 - B5 | A5 - B1
		 * A3 - B4 | A2 - B3
		 * A4 - B5 | A1 - B2
		 * A5 - B5 | A4 - B4
		 * A2 - B2 | A3 - B3
		 * A1 - B1  
		 * 
		 */
		addTeam(t, "HSG L-E M1"); // BK
		addTeam(t, "HSG L-E M2"); // KL-A
		
		addTeam(t, "HSG Schönaich 2"); // BL
		addTeam(t, "HSG Ostfildern A-Jgd"); // BUL
		
		addTeam(t, "TV Bittenfeld 3"); // BK
		addTeam(t, "SV Fellbach 2"); // BK
		
		addTeam(t, "HSG Ostfildern 2"); // KL-A
		addTeam(t, "SV Vaihingen 2"); // KL-A
		
		addTeam(t, "HB Filderstadt"); // KL-B
		addTeam(t, "TSV Deizisau 2"); // KL-B

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
