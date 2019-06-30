/**
 * Created on 05.03.2009 in Project Tournament
 * Location: planning.control.PlanningManager.java
 */
package planning.control;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.swing.JOptionPane;

import model.Team;
import model.Tournament;
import model.User;
import planning.control.TimeManager.NoPlayTimeException;
import planning.control.mapping.IMappingGroup;
import planning.model.Group;
import planning.model.Match;
import planning.model.Phase;
import planning.model.RoundSetting;
import planning.model.Score;
import planning.model.StartoffTransition;
import planning.model.TeamSlot;
import planning.model.TournamentPlan;
import planning.model.Transition;
import planning.model.rounds.IGroupRound;
import planning.model.rounds.IMultiphaseRound;
import planning.model.rounds.Round;
import planning.model.rounds.RoundType;
import control.MainApplication;
import control.ProgressLogger;
import control.TournamentManager;

/**
 * 05.03.2009
 * 
 * @author Daniel Wirtz
 * 
 */
public class PlanningManager {

	private char curGroup = 'A';

	/**
	 * Akzeptiert einen Turnierplan und startet das Turnier
	 * 
	 * @param t Turnier
	 * @throws @throws NoPlayTimeException
	 */
	public boolean acceptPlanAndStart(Tournament t) {
		// Plan generieren
		TournamentPlan tp = null;
		try {
			tp = generateTournamentPlan(t, true);
		} catch (PlanningException pe) {
			JOptionPane.showMessageDialog(null, "Tournament cannot be planned: " + pe.getMessage());
			return false;
		}

		// Zeitablauf erstellen
		try {
			schedule(tp);
		} catch (NoPlayTimeException e) {
			JOptionPane.showMessageDialog(null,
					"Cannot schedule this tournament plan: Too little time for all games (See PlayTimes)");
			return false;
		}

		// Plan ins Turnier eintragen
		t.setTournamentPlan(tp);

		// Turnier starten
		TournamentManager.getInstance().startTournament(t);

		// Rundeneinstellungen rausschmeissen (Platz sparen)
		/*
		 * EntityManager em = MainApplication.getEntityManager();
		 * em.getTransaction().begin(); int numSettings = t.getRoundSettings().size();
		 * for (int dummy = 0; dummy < numSettings; dummy++) { RoundSetting rs =
		 * t.getRoundSettings().get(0); // Die Transitions sind aber evtl. im
		 * Turnierplan vorhanden, also // vorher annullieren rs.setInTransition(null);
		 * // Aus Liste nehmen t.getRoundSettings().remove(rs); // Aus DB löschen if
		 * (em.contains(rs)) em.remove(rs); } em.getTransaction().commit();
		 */
		return true;
	}

	/**
	 * Fügt eine weitere Rundeneinstellung zum Turnier hinzu.
	 * 
	 * @see RoundType
	 * 
	 * @param t    Turnier
	 * @param name gewünschter Name
	 * @param type Rundentyp
	 * @return Rundeneinstellung für die neue Runde
	 */
	public RoundSetting addRound(Tournament t, String name, RoundType type) {
		RoundSetting rs = new RoundSetting(name, type);
		// Ist noch keine Runde vorhanden, kommt automatisch eine
		// StartoffTransition in die Runde.
		if (t.getRoundSettings().size() == 0) {
			rs.setInTransition(new StartoffTransition());
		}
		t.getRoundSettings().add(rs);
		return rs;
	}

	/**
	 * Entfernt eine Runde aus einem Turnierplan. Muss per Manager gemacht werden,
	 * da auch der Datenbankeintrag entfernt werden muss. (Hinzufügen ist
	 * automatisch)
	 * 
	 * @param t  Turnier
	 * @param rs Rundeneinstellung
	 */
	public void removeRound(Tournament t, RoundSetting rs) {
		// Aus Liste nehmen
		t.getRoundSettings().remove(rs);
		// Aus DB löschen
		EntityManager em = MainApplication.getEntityManager();
		em.getTransaction().begin();
		if (em.contains(rs))
			em.remove(rs);
		em.getTransaction().commit();
	}

	/**
	 * Wird im Moment nicht benutzt. Ich glaube nämlich nicht das man extra einen
	 * Manager benötigt, um elementare Werte einer Modelklasse direkt zu ändern.
	 * (Ich denke das ist im Rahmen dieses Projekts zu "generisch", es wird an
	 * keiner anderen Stelle benutzt werden) Falls das doch gewünscht ist, werden
	 * wir natürlich gerne den Code umschreiben.
	 * 
	 * Das gleiche gilt für {@link #setPhaseBreakTime(IMultiphaseRound, int)}
	 * 
	 * @param r
	 * @param value
	 */
	/*
	 * public void setNumGroups(IGroupRound r, int value) { r.setNumGroups(value); }
	 */

	/**
	 * @see PlanningManager#setNumGroups(IGroupRound, int)
	 * @param r
	 * @param minutes
	 */
	/*
	 * public void setPhaseBreakTime(IMultiphaseRound r, int minutes) {
	 * r.setPauseMinutes(minutes); }
	 */

	/**
	 * 
	 * Erzeugt den Turnierplan für ein gegebenes Turnier.<br>
	 * <br>
	 * Verwendet dazu alle im Turnier vermerkten Rundeneinstellungen.
	 * 
	 * Mannschaften ja nach onlyConfirmed:<br>
	 * <b>true</b><br>
	 * Alle Mannschaften, in denen mindestens so viele Spieler bestätigt wurden wie
	 * die im Turnier eingestellte Mindestspieleranzahl pro Team.<br>
	 * <br>
	 * <b>false</b><br>
	 * Alle vorangemeldeten Mannschaften werden verwendet
	 * 
	 * @pre Es gibt mindestens eine Rundeneinstellung
	 * @pre Es gibt mindestens ein bestätigtes Team
	 * 
	 * @post Der Vollständige Turnierplan mit allen Begegnungen ist erstellt. (OHNE
	 *       Zeitplanung.)
	 * 
	 * @param t             Das Turnier
	 * @param onlyConfirmed Nur schon bestätigte Teams werden bei der Generierung
	 *                      berücksichtigt.
	 * @return Generierten Turnierplan
	 * @throws PlanningException
	 */
	public TournamentPlan generateTournamentPlan(Tournament t, boolean onlyConfirmed) throws PlanningException {
		assert (t.getRoundSettings().size() > 0);

		TournamentPlan plan = new TournamentPlan(t);
		curGroup = 'A';

		// Runden gemäß Einstellungen einbauen
		Round prev = null;
		for (RoundSetting rs : t.getRoundSettings()) {
			Round r = rs.getType().getNewInstance(t, rs.getName());
			if (r instanceof IGroupRound) {
				((IGroupRound) r).setNumGroups(rs.getNumGroups());
			}
			if (r instanceof IMultiphaseRound) {
				((IMultiphaseRound) r).setPauseMinutes(rs.getPauseBetweenPhases());
			}
			r.setInTransition(rs.getInTransition());
			r.setPairwiseMatching(rs.getPairwiseMatching());
			r.setFields(rs.getUsableFields());

			r.setHint(rs.getHint());
			// Use game duration from round, if set, otherwise the default from
			// the tournament
			r.setGameTime(rs.getGameTime() + t.getGamePause());

			// Evtl. voriger Runde die OutTransition setzen.
			if (prev != null && prev instanceof IGroupRound) {
				((IGroupRound) prev).setOutTransition(rs.getInTransition());
			}
			plan.addRound(r);

			prev = r;
		}
		/*
		 * Ist die tatsächliche Transition der ersten Runde keine Startoff-Transition
		 * (z.B. durch Löschen von runden entstanden), so muss hier eine
		 * StartoffTransition gesetzt werden.
		 */
		if (!(t.getRoundSettings().get(0).getInTransition() instanceof StartoffTransition)) {
			t.getRoundSettings().get(0).setInTransition(new StartoffTransition());
		}

		/*
		 * Die erste Runde mit den Teams initialisieren, also Slots für jedes Team
		 * erstellen, die schon mit Teams gefüllt sind
		 */
		final List<Group> initialGroup = new ArrayList<Group>(1);
		// Eine Gruppe für alle Teams
		Group init = new Group(null, "Init", "I") {

			private static final long serialVersionUID = 7467615611197665192L;

			@Override
			public int getActualNumProceedants() {
				return this.getSlots().size();
			}

		};
		for (Team team : t.getTeams()) {
			// Nur hinzufügen, falls genug Spieler bestätigt
			if (!onlyConfirmed || team.getConfirmedPlayers().size() >= t.getRequiredPlayersPerTeam()) {

				// Slot erzeugen, Team rein
				TeamSlot ts = new TeamSlot(null, Color.BLACK);
				// Teams einsetzen, mit keinen Punkten zu Beginn.
				init.addTeamSlot(ts);
				ts.setTeam(team, new Score());
			} else {
				String msg = team.getPlayers().size() == 1
						? team.getName() + " wurde nicht bestätigt und nimmt nicht am Turnier teil."
						: "Team '" + team.getName()
								+ "' wird wegen zu wenig bestätigten Spielern nicht teilnehmen. Benötigt: "
								+ t.getRequiredPlayersPerTeam() + ", bestätigt:" + team.getConfirmedPlayers().size();
				ProgressLogger.getInstance().log(msg);
			}
		}

		assert (init.getSlots().size() > 0) : "Keine teilnahmeberechtigten Teams im Turnier";

		initialGroup.add(init);
		plan.getRounds().get(0).build(this, new IGroupRound() {

			@Override
			public List<Group> getGroups() {
				return initialGroup;
			}

			@Override
			public Transition getOutTransition() {
				return new StartoffTransition();
			}

			@Override
			public void setOutTransition(Transition value) {
			}

			@Override
			public void setNumGroups(int value) {
			}

			@Override
			public int getNumGroups() {
				return 1;
			}

		});

		/*
		 * Die restlichen Runden mit den Slots generieren
		 */
		for (int idx = 1; idx < plan.getRounds().size(); idx++) {
			plan.getRounds().get(idx).build(this, ((IGroupRound) plan.getRounds().get(idx - 1)));
		}

		return plan;
	}

	/**
	 * 
	 * Ruft den internen Mapper der Phase auf, um die in dieser Phase enthaltenen
	 * Gruppen mit neu generieten Slots aus einer vorigen Phase zu füllen.<br>
	 * <br>
	 * <b>WICHTIG</b>:<br>
	 * Man könnte sich fragen, warum die neuen Slots von den in der VORIGEN Phase
	 * liegenden Gruppen erzeugt werden; dies liegt daran, das nur die dortigen
	 * Gruppen wirklich wissen wie viele Mannschaften darin enthalten sind!! Sollte
	 * also eine Gruppe weniger Slots enthalten, als die Anzahl der weiterkommenden
	 * Teams aus der Gruppe, so wird natürlich nur eine Liste mit so viele Slots
	 * erzeugt wie auch tatsächlich Teams vorhanden sind. Sonst könnte man Slots
	 * im Turnier erhalten, in denen nie Teams vorkommen.<br>
	 * 
	 * @param p            Phase
	 * @param sourceGroups
	 * @throws PlanningException
	 */
	public void buildPhase(Phase p, List<Group> sourceGroups) throws PlanningException {
		GroupManager gm = new GroupManager();

		// Gruppen erstellen und hinzufügen
		for (int i = 0; i < p.getNumGroups(); i++) {
			Group g = new Group(p, "Gruppe " + curGroup, curGroup + "");
			p.getGroups().add(g);
			curGroup++;
		}

		/**
		 * Füllt die Gruppen mit dem Mapper.
		 * 
		 * Um Missbrauch zu verhindern, wurden die fürs Mapping relevanten Methoden in
		 * ein Interface IMappingGroup ausgelagert. Leider kann Java im Bezug auf
		 * Generics in Listen aber keine Vererbung detektieren, daher muss hier jeweils
		 * eine eigene Liste von IMappingGroup-Elementen erstellt werden, obwohl alle
		 * Group-Elemente ja eigentlich IMappingGroup implementieren.
		 * 
		 * @see IMappingGroup
		 */
		p.getInTransition().map(new ArrayList<IMappingGroup>(sourceGroups),
				new ArrayList<IMappingGroup>(p.getGroups()));

		// Matches ausrechnen und weiterkommende Slots erstellen
		for (Group g : p.getGroups()) {
			gm.calculateMatches(g);

			// Nur falls es eine nächste Phase gibt
			if (p.getOutTransition() != null) {
				// Slots für die weiterkommenden erzeugen
				gm.generateTransitionSlots(g);
			}

		}
	}

	/**
	 * Gibt die Liste aller Spiele für einen bestimmten Schiedsrichter aus.
	 * 
	 * @param tp Turnierplan
	 * @param u  Schiedsrichter
	 * @return Spielplan
	 */
	public List<Match> getScheduleForReferee(TournamentPlan tp, User u) {
		List<Match> res = new ArrayList<Match>();
		for (Match m : getSchedule(tp)) {
			if (m.getReferee().equals(u))
				res.add(m);
		}
		return res;
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ SCHEDULING @@@@@@@@@@@@@@@@@@@@@@@@@@

	/**
	 * Plant den Zeitverlauf für das Turnier.
	 * 
	 * Bei erneutem Aufruf wird der alte Plan überschrieben.
	 * 
	 * @param t             Turnier
	 * @param startDateTime Startzeitpunkt
	 * @throws NoPlayTimeException
	 * 
	 * @pre Es gibt mindestens ein Spielfeld
	 * @pre Es gibt mindestens einen Schiedsrichter
	 * 
	 * @post Ein vollständiger Zeitplan fürs gesamte Turnier wurde erstellt.
	 */
	public void schedule(TournamentPlan t) throws NoPlayTimeException {
		assert (t.getTournament().getNumFields() > 0) : "schedule: Keine Spielfelder!";
		assert (t.getTournament().getReferees().size() > 0) : "schedule: Keine Schiedsrichter in Turnier "
				+ t.getTournament();
		TimeManager tm = new TimeManager(t.getTournament());

		/*
		 * Bestimmen der Anzahl der wirklich benutzbaren Felder. Hängt ab von der
		 * maximalen Anzahl an gleichzeitig durchführbaren Spielen ohne doppelten
		 * Teameinsatz und der Anzahl der Schiedsrichter
		 */
		String[] fields = t.getTournament().getFieldNames();
		int totalUseFields = fields.length;
		if (totalUseFields > t.getTournament().getReferees().size()) {
			totalUseFields = t.getTournament().getReferees().size();
			ProgressLogger.getInstance().log("Weniger Schiedsrichter als Spielfelder!");
		}
		assert (totalUseFields > 0);

		// Alle Runden durchgehen
		for (Round r : t.getRounds()) {
			assert (r.getPhases().size() > 0) : "Keine Phasen in Runde " + r;

			// Alle Phasen durchgehen
			for (Phase p : r.getPhases()) {
				schedule(t, p, tm, r.getFields() == null ? fields : r.getFields());
			}
		}

		// Schiedsrichter für die erste Runde festlegen; der rest wird
		// "on the fly" berechnet
		RefereeManager rm = new RefereeManager();
		// rm.assignRefereesLinear(t.getRounds().get(0).getPhases().get(0));
		rm.assignReferees(t.getRounds().get(0).getPhases().get(0));
	}

	/**
	 * Erzeugt einen Standard-Spielzeitplan ab dem gegebenen Zeitpunkt.
	 * 
	 * Wählt nacheinander aus jeder Gruppe eine Begegnung und platziert sie auf
	 * einem Spielfeld. Dadurch werden alle Gruppenbegegnungen gleichmäßig
	 * verteilt. Allerdings wird an dieser Stelle erwartet, das die Begegnungen
	 * innerhalb einer Gruppe schon möglichst gerecht verteilt sind, das nicht z.B.
	 * Match 1 Team1 vs. Team2 ist und dann Match 2 Team1 vs. Team3 usw.
	 * 
	 * @throws NoPlayTimeException
	 * 
	 * 
	 */
	private void schedule(TournamentPlan t, Phase p, TimeManager tm, String[] fields) throws NoPlayTimeException {
		// Hält den aktuellen Index des grade verplanten Matches pro Gruppe
		int[] gmidx = new int[p.getGroups().size()];
		// Zeigt an, ob alle Matches einer Gruppe verplant wurden.
		boolean[] finished = new boolean[p.getGroups().size()];
		// Bei erster Gruppe anfangen
		int groupIdx = 0;
		// Erstes Feld nehmen
		int fieldIdx = 0;

		Group curGr = null;

		// Init
		int maxSimultaneousMatches = 0;
		for (int x = 0; x < p.getGroups().size(); x++) {
			gmidx[x] = 0;
			finished[x] = false;
			// Schön das Java automatisch bei int-Division nach unten rundet.
			maxSimultaneousMatches += p.getGroups().get(x).getSlots().size() / 2;
		}

		// TODO: No re-mapping of field indices to actual fields implemented yet.
		int totalUseFields = fields.length;
		if (totalUseFields > maxSimultaneousMatches) {
			totalUseFields = maxSimultaneousMatches;
			ProgressLogger.getInstance().log(p.getName() + ": Es werden nicht alle Spielfelder genutzt.");
		}

		// Wartezeit zwischen den Phasen berücksichtigen
		tm.consumeTime(p.getInTransition().getPauseMinutes());

		// Evtl. vorher eingefügte Matches rausnehmen
		p.getSchedule().clear();

		// Matches verplanen bis alle Gruppenmatches durch sind
		while (!allTrue(finished)) {
			// Aktuelle Gruppe wählen
			curGr = p.getGroups().get(groupIdx);

			// Falls es noch ein übriges Match gibt..
			if (curGr.getMatches().size() > gmidx[groupIdx]) {
				// Match holen
				Match target = curGr.getMatches().get(gmidx[groupIdx]);

				target.setScheduleData(tm.getCurrentTime(), fieldIdx);
				p.getSchedule().add(target);

				// Feldnummer erhöhen
				fieldIdx++;
				fieldIdx %= totalUseFields;

				/*
				 * Ist der fieldIdx wieder auf 0, so sind alle Felder mit der Zeit belegt, und
				 * es muss der nächste Zeitslot angefangen werden.
				 */
				if (fieldIdx == 0) {
					tm.consumeTime(p.getRound().getGameTimeInclPause());
				}

				// Merken, das das Match aus der Gruppe schon verplant
				// wurde.
				gmidx[groupIdx]++;

				// Wurden alle verteilt (kann auch KEINS gewesen sein), Gruppe
				// als fertig bearbeitet markieren.
			} else
				finished[groupIdx] = true;

			// Zur nächsten Gruppe springen.
			groupIdx++;
			groupIdx %= p.getGroups().size();
		}

		// Abschliessend nochmal in Reihenfolge bringen
		// (Zur Sicherheit, falls der Algorithmus weiter oben mal so verändert
		// wird dass der Plan nicht schon in Reihenfolge erzeugt wird, oder die
		// Java-Collections mal doch nicht reihenfolgenerhaltend sind)
		Collections.sort(p.getSchedule());
		// Auch innerhalb der Gruppen die richtige Reihenfolge festlegen
		for (Group g : p.getGroups()) {
			Collections.sort(g.getMatches());
		}

		// Noch eine Spieldauer draufrechnen, da bisher mit Startzeiten
		// gerechnet wurde
		// p.setScheduledEndDateTime(tm.getCurrentTime());
	}

	/**
	 * Gibt true zurück, gdw. alle Einträge true sind.
	 * 
	 * @param barr
	 * @return
	 */
	private boolean allTrue(boolean[] barr) {
		for (boolean b : barr)
			if (!b)
				return false;
		return true;
	}

	/**
	 * Stellt den Zeitplan eines Turniers aus allen Phasen zusammen und gibt ihn
	 * zurück.
	 * 
	 * @param t
	 * @return Liste der Begegnungen in zeitlicher Reihenfolge
	 */
	public List<Match> getSchedule(TournamentPlan t) {
		List<Match> res = new ArrayList<Match>();
		for (Round r : t.getRounds()) {
			for (Phase p : r.getPhases()) {
				res.addAll(p.getSchedule());
			}
		}
		return res;
	}

	/**
	 * Debug-Funktion
	 * 
	 * @param t Turnierplan
	 */
	public void printSchedule(TournamentPlan t) {
		System.out.println(t.toString());
		for (Round r : t.getRounds()) {
			System.out.println("Runde: " + r.toString());
			for (Phase p : r.getPhases()) {
				System.out.println("Phase: " + p);
				for (Match m : p.getSchedule()) {
					System.out.println(m);
				}
			}
		}
	}

}
