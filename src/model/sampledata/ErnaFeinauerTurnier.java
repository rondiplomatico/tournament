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
import planning.control.PlanningException;
import planning.control.mapping.IGroupMapper;
import planning.control.mapping.IMappingGroup;
import planning.control.mapping.Mapping;
import planning.control.score.ScoreTransfer;
import planning.model.RoundSetting;
import planning.model.StartoffTransition;
import planning.model.Transition;
import planning.model.rounds.RoundType;

public class ErnaFeinauerTurnier {

	static List<User> users = new ArrayList<User>(), refs = new ArrayList<User>();

	private static class EFMapping implements IGroupMapper {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1829874400050571129L;

		@Override
		public void map(List<IMappingGroup> source, List<IMappingGroup> target) throws PlanningException {
			if (source.size() != 3 || target.size() != 2) {
				throw new PlanningException("Need 3 source groups and two target groups!");
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
//		SampleData.createDB();
//		System.exit(0);

		SampleData.clearDB();

		Tournament t = EF2019();

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
	public static Tournament EF2019() {
		int gameTime = 20;
		int pauseBetweenGames = 5;

		Calendar c = Calendar.getInstance();
		c.set(2019, 7, 21);
		Tournament t = new Tournament("Erna Feinauer Turnier 2019", (float) 30.0, 200, "42. Auflage", gameTime,
				pauseBetweenGames, c.getTime());
		t.setRequiredPlayersPerTeam(10);
		t.setType(TournamentType.MultiPlayer);
		t.setState(TournamentState.signupClosed);
		t.setSportKind("Handball");

		c.set(2019, 7, 21);
		t.setExpireDate(c.getTime());

		t.setStartHour(9);
		t.setEndHour(19);

		t.setFields(new String[] { "Goldäcker", "PMH", "Sportzentrum" });

		t.addPlayTime(new Date(2019, 7, 21, 9, 00), new Date(2019, 7, 21, 19, 00));
		t.getRoundSettings().add(new RoundSetting("Vorrunde", RoundType.GruppenRunde, 3, 0, new StartoffTransition()));

		Transition tr = new Transition(2, ScoreTransfer.AllScores, Mapping.CombineMapper, 15);
		RoundSetting rs = new RoundSetting("Zwischenrunde", RoundType.GruppenRunde, 1, 0, tr);
		rs.setPairwiseMatching(true);
		rs.setUsableFields(new String[] { "Goldäcker" });
		t.getRoundSettings().add(rs);

		RoundSetting rsf = new RoundSetting("Finale", RoundType.KORunde, 0, 10,
				new Transition(4, ScoreTransfer.AllScores, Mapping.EqualMapping, 10), 15);
		rsf.setUsableFields(new String[] { "Goldäcker" });
		t.getRoundSettings().add(rsf);

		addTeam(t, "HSG Leinfelden-Echterdingen");
		addTeam(t, "HSG Freiburg 1");
		addTeam(t, "TV Nellingen 1");

		addTeam(t, "Kurpfalz Bären 1");
		addTeam(t, "TSV Wolfschlugen");
		addTeam(t, "TV Möglingen");

		addTeam(t, "TSV Haunstetten 2");
		addTeam(t, "Kurpfalz Bären 2");
		addTeam(t, "TSV Haunstetten 1");

		addTeam(t, "SV Allensbach");
		addTeam(t, "TSV Heiningen");
		addTeam(t, "SG Nussloch");

		addTeam(t, "HSG Freiburg 2");
		addTeam(t, "HB Ludwigsburg");
		addTeam(t, "SF Schweikheim");

		addTeam(t, "ESV Regensburg");
		addTeam(t, "TG Nürtingen 2");
		addTeam(t, "FA Göppingen");

		addSchiri(t, "S1");
		addSchiri(t, "S2");
		addSchiri(t, "S3");
		addSchiri(t, "S4");
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
