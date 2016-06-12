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
import planning.control.PlanningException;
import planning.control.mapping.IGroupMapper;
import planning.control.mapping.IMappingGroup;
import planning.control.mapping.Mapping;
import planning.control.score.ScoreTransfer;
import planning.model.RoundSetting;
import planning.model.StartoffTransition;
import planning.model.TeamSlot;
import planning.model.Transition;
import planning.model.rounds.RoundType;
import control.MainApplication;

public class ErnaFeinauerTurnier {

	static List<User> users = new ArrayList<User>(),
			refs = new ArrayList<User>();

	private static class EFMapping implements IGroupMapper {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1829874400050571129L;

		@Override
		public void map(List<IMappingGroup> source, List<IMappingGroup> target)
				throws PlanningException {
			if (source.size() != 3 || target.size() != 2) {
				throw new PlanningException(
						"Need 3 source groups and two target groups!");
			}
			int sel = 0;
			for (IMappingGroup tgt : target) {
				for (IMappingGroup src : source) {
					tgt.addTeamSlot(src.getProceedingSlots().get(sel++));
					sel %= 2;
				}
			}
		}

	}

	public static void main(String[] args) {
		SampleData.clearDB();

		Tournament t = EF2016();

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
	public static Tournament EF2016() {
		int gameTime = 25;
		int pauseBetweenGames = 5;

		Calendar c = Calendar.getInstance();
		c.set(2016, 7, 24);
		Tournament t = new Tournament("Erna Feinauer Turnier 2016",
				(float) 30.0, 200, "Foll subbr!", gameTime, pauseBetweenGames,
				c.getTime());
		t.setRequiredPlayersPerTeam(10);
		t.setType(TournamentType.MultiPlayer);
		t.setState(TournamentState.signupClosed);
		t.setSportKind("Handball");

		c.set(2016, 7, 24);
		t.setExpireDate(c.getTime());

		t.setStartHour(9);
		t.setEndHour(17);

		t.setFields(new String[] { "Sportpark Goldäcker", "PMH",
				"Sportzentrum Leinfelden" });

		t.addPlayTime(new Date(2016, 7, 24, 9, 00), new Date(2016, 7, 24, 18,
				00));
		t.getRoundSettings().add(
				new RoundSetting("Vorrunde", RoundType.GruppenRunde, 3, 0,
						new StartoffTransition()));

		Transition tr = new Transition(2, ScoreTransfer.AllScores,
				Mapping.Custom, 0);
		tr.setCustomMapping(new EFMapping());
		RoundSetting rs = new RoundSetting("Zwischenrunde",
				RoundType.GruppenRunde, 2, 0, tr);
		// rs.setPairwiseMatching(true);
		t.getRoundSettings().add(rs);

		t.getRoundSettings().add(
				new RoundSetting("Finale", RoundType.KORunde, 0, 10,
						new Transition(2, ScoreTransfer.NoScores,
								Mapping.CrossMapper, 0), 20));

		addTeam(t, "HSG Leinfelden-Echterdingen");
		addTeam(t, "SC Korb");
		addTeam(t, "HC Erlangen");
		addTeam(t, "SB BBM Bietigheim"); // 15
		addTeam(t, "HSG St. Leon/Reilingen");

		addTeam(t, "HSG Mannheim");
		addTeam(t, "TV Möglingen");
		addTeam(t, "HSG Freiburg 1");
		addTeam(t, "HSG Freiburg 2");
		addTeam(t, "SF Schwaikheim");

		addTeam(t, "HSG Strohgäu");
		addTeam(t, "Tus Ottenheim");
		addTeam(t, "Haspo Bayreuth");
		addTeam(t, "TSG Ketsch 2");
		addTeam(t, "FSG Donzdorf / Geislingen");

		addSchiri(t, "Benni/Nico");
		addSchiri(t, "Karl/Heinz");
		addSchiri(t, "Kurt/Kowalski");
		t.setReferees(refs);

		return t;
	}

	public static void addTeam(Tournament t, String name) {
		Team team = new Team(name);
		for (int i = 1; i < 11; i++) {
			User u = new User(name + " Spieler " + i, "pw");
			team.addPlayer(u);
			team.getConfirmedPlayers().add(u);
			users.add(u);
		}
		t.addTeam(team);
	}

	public static void addSchiri(Tournament t, String name) {
		User u = new User(name, "pw");
		u.isReferee(true);
		t.addReferee(u);
		users.add(u);
		refs.add(u);
	}

}
