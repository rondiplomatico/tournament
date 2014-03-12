package model.sampledata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import model.Team;
import model.Tournament;
import model.User;
import model.enums.TournamentState;
import model.enums.TournamentType;
import planning.control.PlanningManager;
import planning.control.ResultManager;
import planning.control.TimeManager.NoPlayTimeException;
import planning.control.mapping.Mapping;
import planning.control.score.ScoreTransfer;
import planning.model.Match;
import planning.model.RoundSetting;
import planning.model.StartoffTransition;
import planning.model.Transition;
import planning.model.rounds.RoundType;
import control.MainApplication;
import control.TournamentManager;

/**
 * Füllt die Datenbank mit Beispieldaten.
 * 
 * @author Dimitri Wegner
 */
public class SampleData {

	private static List<String> fields;
	private static User sportler, referee, manager;
	private static List<User> users;
	private static TournamentState ts;
	private static TournamentType tt;
	private static boolean persist = true;

	/**
	 * 
	 * @param nr
	 * @return Liste mit allen Turniertypen
	 * @throws NoPlayTimeException
	 */
	public static List<Tournament> allTypes(int nr) {
		List<Tournament> res = new ArrayList<Tournament>();
		for (TournamentType ltt : TournamentType.values()) {
			for (TournamentState lts : TournamentState.values()) {
				tt = ltt;
				ts = lts;
				Tournament t = new Tournament(tt + " - " + ts + " Nr."
						+ (nr + 1), rnd(500), rnd(20) + 6, "Jeder kann was",
						30, new Date());
				t.setType(tt);
				t.setState(ts);
				t.setRequiredPlayersPerTeam(rnd(5) + 2);
				t.setSportKind("Eine tolle " + tt + "-Sportart");
				t.setLocation("<html>Einsteinstrasse 64<br>48161 Münster<br>SR5</html>");
				t.setExpireDate(new Date());
				fill(t);
				t.addReferee(referee);

				if (ts == TournamentState.signupClosed) {
					t.getRoundSettings().add(
							new RoundSetting("Vorrunde",
									RoundType.GruppenRunde, rnd(4) + 2,
									rnd(30), new StartoffTransition()));
					t.getRoundSettings().add(
							new RoundSetting("Finale", RoundType.KORunde, 0,
									rnd(60), new Transition(rnd(3) + 2,
											rndST(), rndM(), rnd(60))));
				}
				if (ts == TournamentState.running) {
					t.setState(TournamentState.signupClosed);

					t.getRoundSettings().add(
							new RoundSetting("Vorrunde",
									RoundType.GruppenRunde, rnd(4) + 2, 0,
									new StartoffTransition()));
					t.getRoundSettings().add(
							new RoundSetting("Hauptrunde",
									RoundType.GruppenRunde, rnd(3) + 2, 0,
									new Transition(rnd(3) + 3, rndST(), rndM(),
											30)));
					t.getRoundSettings().add(
							new RoundSetting("Finale", RoundType.KORunde, 0,
									rnd(60), new Transition(rnd(3) + 2,
											rndST(), rndM(), rnd(60))));

					PlanningManager pm = new PlanningManager();
					if (pm.acceptPlanAndStart(t)) {
						ResultManager rm = new ResultManager(null);
						List<Match> matches = pm.getSchedule(t
								.getTournamentPlan());
						for (int x = 0; x < matches.size() / 2; x++) {
							rm.setResult(matches.get(x), rnd(10), rnd(10));
						}
					}
				}
				if (ts == TournamentState.finished) {
					t.setState(TournamentState.signupClosed);

					t.getRoundSettings().add(
							new RoundSetting("Vorrunde",
									RoundType.GruppenRunde, rnd(4) + 2, 0,
									new StartoffTransition()));
					t.getRoundSettings().add(
							new RoundSetting("Hauptrunde",
									RoundType.GruppenRunde, rnd(3) + 2, 0,
									new Transition(rnd(3) + 3, rndST(), rndM(),
											30)));
					t.getRoundSettings().add(
							new RoundSetting("Finale", RoundType.KORunde, 0,
									rnd(60), new Transition(rnd(3) + 2,
											rndST(), rndM(), rnd(60))));

					PlanningManager pm = new PlanningManager();
					if (pm.acceptPlanAndStart(t)) {
						ResultManager rm = new ResultManager(null);
						List<Match> matches = pm.getSchedule(t
								.getTournamentPlan());
						for (int x = 0; x < matches.size(); x++) {
							rm.setResult(matches.get(x), rnd(10), rnd(10));
						}
						TournamentManager.getInstance().finishTournament(t);
					}
				}
				res.add(t);
			}
		}
		return res;
	}

	/**
	 * 
	 * @return zufälliger scoretransfer
	 */
	public static ScoreTransfer rndST() {
		return ScoreTransfer.values()[rnd(ScoreTransfer.values().length)];
	}

	/**
	 * 
	 * @return zufälliges mapping
	 */
	public static Mapping rndM() {
		return Mapping.values()[rnd(Mapping.values().length)];
	}

	/**
	 * 
	 * @param max
	 * @return zufallszahl
	 */
	public static int rnd(int max) {
		return (int) (Math.random() * max);
	}

	/**
	 * 
	 * @return Weltraumliga
	 */
	public static Tournament Weltraum() {
		Tournament t = new Tournament("SV Adler Weltraumliga", (float) 49.99,
				50, "Spiel mit! Da ist jeder gut.", 30, new Date());
		t.setType(TournamentType.MultiPlayer);
		t.setState(TournamentState.signupClosed);
		t.setSportKind("Handball");
		t.setExpireDate(new Date());

		t.getRoundSettings().add(
				new RoundSetting("Vorrunde", RoundType.GruppenRunde, 4, 0,
						new StartoffTransition()));
		t.getRoundSettings().add(
				new RoundSetting("Hauptrunde", RoundType.GruppenRunde, 2, 0,
						new Transition(4, ScoreTransfer.Special1,
								Mapping.CrossMapper, 30)));
		t.getRoundSettings().add(
				new RoundSetting("Finale", RoundType.KORunde, 0, 60,
						new Transition(4, ScoreTransfer.NoScores,
								Mapping.CrossMapper, 60)));

		fill(t);

		return t;
	}

	private static String[] rndFields() {
		Collections.shuffle(fields);
		int num = rnd(fields.size() - 3) + 3;
		String[] res = new String[num];
		for (int x = 0; x < num; x++) {
			res[x] = fields.get(x);
		}
		return res;
	}

	private static void fill(Tournament t) {
		addRandomTeams(t);
		addRefsAndLeader(t);
		t.setFields(rndFields());
	}

	private static void addRefsAndLeader(Tournament t) {
		if (ts != TournamentState.canceled || ts != TournamentState.created) {
			Collections.shuffle(users);
			int numRefs = rnd(10) + 2;
			for (int x = 0; x < numRefs; x++) {
				User u = users.get(x);
				u.isReferee(true);
				t.addReferee(u);
			}
			t.getLeaders().add(t.getReferees().get(0));
		}
	}

	private static void addRandomTeams(Tournament t) {
		if (ts != TournamentState.canceled && ts != TournamentState.created) {
			Collections.shuffle(users);
			int idx = 0;
			for (int x = 0; x < t.getMaxParticipatingTeams(); x++) {

				Team team = new Team("RandomTeam " + (x + 1));

				int players = tt == TournamentType.SinglePlayer ? 1 : rnd(5)
						+ t.getRequiredPlayersPerTeam();
				for (int uidx = 0; uidx < players; uidx++) {
					team.addPlayer(users.get(idx++));
					/*
					 * Die meisten Spieler voranmelden, aber nicht alle
					 */
					if (t.getState() != TournamentState.published
							&& Math.random() > .2) {
						team.getConfirmedPlayers().add(users.get(uidx));
					}
					idx %= users.size();
				}
				t.addTeam(team);
			}
		}
	}

	private static void initUsers() {
		int total = 100;
		// Benutzer
		sportler = new User("sportler", "test");

		referee = new User("referee", "test");
		referee.isReferee(true);

		manager = new User("manager", "test");
		manager.isManager(true);

		users = new ArrayList<User>(total + 3);
		users.add(sportler);
		users.add(referee);
		users.add(manager);

		for (int cnt = 0; cnt < total; cnt++) {
			users.add(new User("Person " + (cnt + 1), "user"));
		}
	}

	/**
	 * 
	 * @return erfolgreiches Leeren
	 */
	public static boolean clearDB() {
		if (!persist)
			return true;
		try {
			// The newInstance() call is a work around for some
			// broken Java implementations
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager
					.getConnection("jdbc:mysql://localhost/tournament?user=tournament_usr&password=schwabencup");
			// Connection conn =
			// DriverManager.getConnection("jdbc:mysql://www.danielwirtz.de/sopra?user=sopra_usr&password=sopra0809");
			Statement s = conn.createStatement();
			s.execute("SET foreign_key_checks = 0");
			ResultSet rs = s.executeQuery("show tables");
			while (rs.next()) {
				System.out.println("Leere Tabelle '" + rs.getString(1) + "'");
				Statement s2 = conn.createStatement();
				s2.execute("truncate " + rs.getString(1));
			}
			s.execute("SET foreign_key_checks = 1");
			s.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Füllt die Datenbank mit Beispieldaten.
	 * 
	 * Erstellt drei Benutzer: sportler, referee und manager mit dem Passwort
	 * 'test' jeweils und den Rechten, die dem Namen entsprechen, sowie 6
	 * Turniere die alle 6 unterschiedliche Zustände eines Turniers abdecken.
	 * 
	 * @param args
	 * @throws NoPlayTimeException
	 */
	public static void main(String[] args) {
		// persist = false;
		if (true) {
			initUsers();

			fields = new ArrayList<String>(5);
			fields.add("Haupthalle");
			fields.add("Leichtbauh.");
			fields.add("LeoCampus 1");
			fields.add("HBS");
			fields.add("Sentr. Höhe");

			List<Tournament> trns = new ArrayList<Tournament>();

			// 5 Sätze aller Turnierarten erzeugen
			for (int i = 0; i < 5; i++) {
				trns.addAll(allTypes(i));
			}

			trns.add(Weltraum());
			// trns.add(Boese());

			// Speichern
			if (persist) {
				EntityManager em = MainApplication.getEntityManager();
				em.getTransaction().begin();
				for (User u : users) {
					em.persist(u);
				}
				for (Tournament t : trns) {
					em.persist(t);
				}
				em.getTransaction().commit();
			}
		}
	}
}
