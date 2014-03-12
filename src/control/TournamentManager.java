package control;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Team;
import model.Tournament;
import model.User;
import model.enums.TournamentState;
import model.enums.TournamentType;
import planning.control.GroupManager;
import planning.model.Group;
import planning.model.Phase;
import planning.model.TeamSlot;
import planning.model.rounds.IFinalRound;
import planning.model.rounds.Round;

/**
 * Singleton TournamentManager zur Verwaltung von Turnieren.
 * 
 * @author Dimitri Wegner
 */
public final class TournamentManager {

    /**
     * Liste aller Turniere
     */
    private List<Tournament> tournamentList = null;
    /**
     * Instanz von TournamentManager
     */
    private static final TournamentManager INSTANCE = new TournamentManager();

    /**
     * Erstellt eine Instanz von TournamentManager.
     *
     * Es werden alle Turniere aus der Datenbank zur weiteren Verwendung
     * geladen.
     */
    private TournamentManager() {
    }

    /**
     * Singleton Methode
     * Gibt das Tournamentmanager Objekt zurück
     *
     * @return Tournamentmanger Das aktuelle Tournamentmanager Objekt
     */
    public static TournamentManager getInstance() {
        return INSTANCE;
    }

    /**
     * Findet die aktuelle Turnierphase und die Gruppe, in der das Team spielt.
     * Dann werden alle Spiele dieses Teams in der Gruppe als verloren markiert.
     *
     * Spiele gegen ebenfalls disqualifizierte Teams werden unentschieden markiert.
     *
     * TODO: Disqualifizieren: Testen!
     *
     * @param currentTournament Das Turnier für das dass Team disqualifiziert werden soll
     * @param team
     *            Zu disqualifizierndes Team
     *
     * @pre {@link Tournament#getState()} == {@value TournamentState#running}
     * @pre {@link Team#isDisqualified() != true}
     */
    public void disqualify(Tournament currentTournament, Team team) {
        assert (currentTournament.getState().equals(TournamentState.running)) : "Turnierstatus ist nicht " + TournamentState.running + "!";
        assert (!team.isDisqualified()) : "Team ist schon disqualifiziert!";

        team.disqualify();
        for (Round r : currentTournament.getTournamentPlan().getRounds()) {
            // Die erste Runde die nicht beendet ist ist die aktuelle
            if (!r.isFinished()) {
                for (Phase p : r.getPhases()) {
                    // Die erste Phase die nicht fertig ist ist die aktuelle
                    if (!p.isFinished()) {
                        for (Group g : p.getGroups()) {
                            for (TeamSlot ts : g.getSlots()) {
                                if (team.equals(ts.getTeam())) {
                                    ts.getInitialScore().setGoalsMinus(0);
                                    ts.getInitialScore().setGoalsPlus(0);
                                    ts.getInitialScore().setPointsMinus(0);
                                    ts.getInitialScore().setPointsPlus(0);
                                    new GroupManager().markAllMatchesLost(ts);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Liefert eine Liste mit allen vorhandenen Turnieren zurück.
     * Ist die Liste der Turnier noch nicht aus dem Persistierungskontext
     * ausgelesen, wird dies gemacht.
     *
     * Diese Methode soll stets verwendet werden, um auf die Variable
     * tournamentList zuzugreifen.
     *
     * @return Liste aller Turniere
     */
    @SuppressWarnings("unchecked")
    public List<Tournament> getAllTournaments() {
        if (tournamentList == null) {
            EntityManager em = MainApplication.getEntityManager();

            // Alle Turniere auslesen
            Query q = em.createQuery("SELECT t FROM model.Tournament t");
            tournamentList = q.getResultList();
        }

        return tournamentList;
    }

    /**
     * Gibt das finale Ranking eines beendeten Turniers mit Finalrunde aus.
     *
     * @pre {@link Tournament#getState()} == {@link TournamentState#finished}
     * @pre {@link planning.model.TournamentPlan#hasFinalRound()} == true
     *
     * @param t Turnier
     * @return Finales Ranking eines Turniers
     */
    public List<TeamSlot> getFinalRanking(Tournament t) {
        assert (t.getState() == TournamentState.finished) : "Turnier ist nicht beendet!";
        assert (t.getTournamentPlan().hasFinalRound()) : "Turnier hat keine Finalrunde.";

        List<Round> rounds = t.getTournamentPlan().getRounds();
        Round last = rounds.get(rounds.size() - 1);
        // Nicht geprüfter Cast wegen DBC
        return ((IFinalRound) last).getFinalRanking();
    }

    /**
     * Gibt eine Liste der Turniere zurück, die nur Turniere enthält, die der
     * Benutzer u sehen darf. Diese Rechte sind hier hardcoded einprogrammiert.
     *
     * u ist ein ... - Sportler ist, sieht er alle publizierten und beendeten
     * Turniere sowie Turniere, in welchen er angemeldet ist. - Schiedsrichter
     * ist, sieht alle Turnier die auch ein Sportler sieht, sowie alle Turniere,
     * in welchen er Schiedsrichter ist. - Leiter ist, sieht er alle Turniere
     * die ein Sportler sieht, sowie alle Turniere, in welchen er Leiter ist. -
     * Verwalter ist, sieht er alle Turniere.
     *
     * @param u User für den die Turnierliste erstellt werden soll
     * @return Turnierliste
     */
    public List<Tournament> getTournamentsByUser(User u) {
        List<Tournament> resultList = new LinkedList<Tournament>();

        // Für den Manager wird nicht gefiltert.
        if (u.isManager()) {
            resultList = getAllTournaments();
        } else {

            // Turniere filtern
            for (Tournament t : getAllTournaments()) {
                if (t.getState() == TournamentState.published ||
                    t.getState() == TournamentState.finished ||
                    t.getPlayers().contains(u) ||
                    t.getReferees().contains(u) ||
                    t.getLeaders().contains(u)) {

                    resultList.add(t);
                }
            }
        }

        return resultList;
    }

    /**
     * Gibt die Liste aller Turniere zurück, wobei für jedes Turnier in der
     * Liste gilt: - u ist in dem Turnier vorangemeldet. - u ist Schiedsrichter
     * in dem Turnier. - u ist Leiter des Turniers.
     *
     * @param u
     *            Benutzer
     * @return Liste der Turniere mit der Beteiligung von u.
     */
    public List<Tournament> getUsersTournaments(User u) {
        List<Tournament> resultList = new LinkedList<Tournament>();

        for (Tournament t : getAllTournaments()) {
            if (t.getReferees().contains(u) ||
                t.getLeaders().contains(u) ||
                t.getPlayers().contains(u)) {
                
                resultList.add(t);
            }
        }

        return resultList;
    }

    /**
     * Gibt die Liste aller veröffentlichen Turniere zurück.
     *
     * @return Turnierliste
     */
    public List<Tournament> getPublishedTournaments() {
        List<Tournament> resultList = new ArrayList<Tournament>();

        for (Tournament t : getAllTournaments()) {
            if (t.getState() == TournamentState.published) {
                resultList.add(t);
            }
        }

        return resultList;
    }

    /**
     * Gibt die Liste alle aktuell laufender Turniere zurück.
     *
     * @return Turnierliste
     */
    public List<Tournament> getRunningTournaments() {
        List<Tournament> resultList = new ArrayList<Tournament>();

        for (Tournament t : getAllTournaments()) {
            if (t.getState() == TournamentState.running) {
                resultList.add(t);
            }
        }

        return resultList;
    }

    /**
     * Gibt Liste alle aktuell laufender Turniere.
     *
     * @return Turnierliste
     */
    public List<Tournament> getFinishedTournaments() {
        List<Tournament> resultList = new ArrayList<Tournament>();

        for (Tournament t : getAllTournaments()) {
            if (t.getState() == TournamentState.finished) {
                resultList.add(t);
            }
        }

        return resultList;
    }

    /**
     * Erstellt ein neues Turnier aus übergebenen Daten. Es werden Turniere mit
     * gleichen Namen zugelassen. Nach dem Erstellen wird das neue Turnier in
     * die Liste aller Turniere hinzugefügt.
     *
     * @param t
     *            Turnier, gefüllt mit eingegebenen Daten
     */
    public void createTournament(Tournament t) {
        // Turnier in der Datenbank speichern
        EntityManager em = MainApplication.getEntityManager();
        em.getTransaction().begin();
        em.persist(t);
        em.getTransaction().commit();

        // ... und anschließen in die Liste hinzufügen.
        getAllTournaments().add(t);
    }

    /**
     * Turnier wird veröffentlicht.
     *
     * @pre t hat den Status erstellt
     * @post t hat den Status published
     * @param t Turnier
     */
    public void publishTournament(Tournament t) {
        assert (t.getState() == TournamentState.created);

        t.setState(TournamentState.published);
    }

    /**
     * Meldezeitraum für ein Turnier beenden.
     *
     * @pre t hat den Status published
     * @post t hat den Status signupClosed
     * @param t
     *            Turnier
     */
    public void finishSignUpTournament(Tournament t) {
        assert (t.getState() == TournamentState.published);

        t.setState(TournamentState.signupClosed);
    }

    /**
     * Turnier wird beendet.
     *
     * @pre t hat den Status running
     * @post t hat den Status finished
     * @param t
     *            Turnier
     */
    public void finishTournament(Tournament t) {
        assert (t.getState() == TournamentState.running);

        t.setState(TournamentState.finished);
    }

    /**
     *
     * Startet das Turnier.
     *
     * @pre Tournament.state == TournamentState.signupClosed
     * @param t
     */
    public void startTournament(Tournament t) {
        assert (t.getState() == TournamentState.signupClosed);
        t.setState(TournamentState.running);
    }

    /**
     * Turnier abbrechen.
     *
     * @pre t hat nicht den Status finished
     * @pre t hat nicht den Status canceled
     * @post t hat den Status finished
     * @param t
     *            Turnier, welches abgebrochen werden soll.
     */
    public void cancelTournament(Tournament t) {
        assert (t.getState() != TournamentState.finished);
        assert (t.getState() != TournamentState.canceled);
        t.setState(TournamentState.canceled);
    }

    /**
     * Meldet den Benutzer u von dem Turnier t ab. Ist die Mannschaft, welche
     * den Spieler enthält leer (Team.isEmpty), wird diese entfernt.
     *
     * @pre u muss in t vorangemeldet sein.
     * @pre u darf nicht bestaetigt sein
     * @post u ist von dem Turnier t abgemeldet
     * @see Team#isEmpty()
     * @param t
     *            Turnier
     * @param u
     *            Benutzer
     */
    public void signoff(Tournament t, User u) {
        List<Team> teams = t.getTeams();

        for (Team tm : teams) {
            if (tm.getPlayers().contains(u)) {
                assert (!tm.getConfirmedPlayers().contains(u));
                tm.getPlayers().remove(u);

                if (tm.isEmpty()) {
                    teams.remove(tm);
                }
                return;
            }
        }

        assert (false);
    }

    /**
     * Benutzer u wird in dem Turnier t vorangemeldet. Diese Methode soll
     * verwendet werden, falls das Turnier ein Einspieler-Turnier ist.
     *
     * @pre t ist ein Einzelspieler-Turnier. t ist im Voranmeldungsmodus. u darf
     *      nicht in t vorangemeldet sein.
     * @post u ist in t vorangemeldet.
     *
     * @param t
     *            Turnier
     * @param u
     *            Benutzer
     */
    public void signup(Tournament t, User u) {
        assert (t.getType() == TournamentType.SinglePlayer);
        assert (t.getState() == TournamentState.published);

        // Prüfen, ob der Spieler schon vorangemeldet ist.
        for (Team team : t.getTeams()) {
            assert (!team.getPlayers().contains(u));
        }

        // durchführen
        Team team = new Team(u.getName());
        team.addPlayer(u);
        t.addTeam(team);
    }

    /**
     * Benutzer u wird mit der Mannschaft team in dem Turnier t vorangemeldet.
     * Diese Methode soll verwendet werden, falls das Turnier ein Mehrspieler-
     * Turnier ist.
     *
     * @pre t ist ein Mehrspieler-Turnier sein. t ist im Voranmeldungsmodus. u
     *      darf nicht in dem Team sein.
     * @post u ist mit der Mannschafter team in t vorangemeldet.
     *
     * @param t
     *            Turnier
     * @param team
     *            Mannschaft
     * @param u
     *            Benutzer
     */
    public void signup(Tournament t, Team team, User u) {
        assert (t.getType() == TournamentType.MultiPlayer);
        assert (t.getState() == TournamentState.published);
        assert (!team.getPlayers().contains(u));

        team.addPlayer(u);

        // Mannschaft ex. noch nicht.
        if (!t.getTeams().contains(team)) {
            t.addTeam(team);
        }
    }

    /**
     * Benutzer u wird in dem Turnier t nachgemeldet (also auch bestätigt).
     * Diese Methode soll verwendet werden, falls das Turnier ein
     * Einspieler-Turnier ist.
     *
     * @pre t ist ein Einzelspieler-Turnier. t ist im Nachmeldemodus. u darf
     *      nicht in t vorangemeldet sein.
     * @post u ist in t vorangemeldet.
     *
     * @param t
     *            Turnier
     * @param u
     *            Benutzer
     */
    public void lateSignup(Tournament t, User u) {
        assert (t.getType() == TournamentType.SinglePlayer);
        assert (t.getState() == TournamentState.signupClosed);

        // Prüfen, ob der Spieler schon vorangemeldet ist.
        for (Team team : t.getTeams()) {
            assert (!team.getPlayers().contains(u));
        }

        // durchführen
        Team team = new Team(u.getName());
        team.addPlayer(u);
        t.addTeam(team);
        confirmPlayer(team, u);

    }

    /**
     * Benutzer u wird mit der Mannschaft team in dem Turnier t nachgemeldet.
     * Diese Methode soll verwendet werden, falls das Turnier ein Mehrspieler-
     * Turnier ist.
     *
     * @pre t ist ein Mehrspieler-Turnier sein. t ist im Nachmeldemodus. u darf
     *      nicht in dem Team sein.
     * @post u ist mit der Mannschafter team in t vorangemeldet.
     *
     * @param t
     *            Turnier
     * @param team
     *            Mannschaft
     * @param u
     *            Benutzer
     */
    public void lateSignup(Tournament t, Team team, User u) {
        assert (t.getType() == TournamentType.MultiPlayer);
        assert (t.getState() == TournamentState.signupClosed);
        assert (!team.getPlayers().contains(u));

        team.addPlayer(u);

        // Mannschaft ex. noch nicht.
        if (!t.getTeams().contains(team)) {
            t.addTeam(team);
        }

        confirmPlayer(team, u);
    }

    /** Speichert alle Turniere und somit auch Änderungen in diesen. */
    public void saveAllTournaments() {
        EntityManager em = MainApplication.getEntityManager();

        em.getTransaction().begin();
        for (Tournament t : getAllTournaments()) {
            em.persist(t);
        }
        em.getTransaction().commit();
    }

    /**
     * Bestätigt die Voranmeldung von dem Benutzer u in der Mannschaft t.
     *
     * @pre u ist in der Mannschaft t. Voranmeldung von u ist unbestätigt.
     * @post Voranmeldung von u ist bestätigt.
     * @param t
     *            Mannschaft
     * @param u
     *            Benutzer
     */
    public void confirmPlayer(Team t, User u) {
        confirmPlayer(t, u, true);
    }

    /**
     * Bestätigt die Voranmeldung (oder setzt die Bestätigung zurück) von dem
     * Benutzer u in der Mannschaft t.
     *
     * @pre u ist in der Mannschaft t. ist confirm == true, so ist u
     *      unbestätigt. ist confirm == false, so ist u bestätigt.
     * @post u ist bestätigt, falls confirm == true, sonst ist u unbestätigt
     * @param t
     *            Mannschaft
     * @param u
     *            Benutzer
     * @param confirm
     *            true = bestätigen, false = Besätigung rückgängig
     */
    public void confirmPlayer(Team t, User u, boolean confirm) {
        assert (t.getPlayers().contains(u));

        List<User> confirmed = t.getConfirmedPlayers();

        if (confirm) {
            assert (!confirmed.contains(u));
            confirmed.add(u);
        } else {
            assert (confirmed.contains(u));
            confirmed.remove(u);
        }
    }

    /**
     * Setzt user als Schiedsrichter vom Turnier t.
     *
     * @pre Keine
     * @post Genau alle Benutzer in Schiedsrichter sind Leiter von t.
     * @param t Turnier für das die Schiedsrichter gesetzt werden sollen
     * @param referees Die Liste der zu setzenden Schiedsrichter
     */
    public void setReferees(Tournament t, List<User> referees) {
        t.setReferees(referees);
    }

    /**
     * Setzt user als Turnierleiter vom Turnier t.
     *
     * @pre Keine
     * @post Genau alle Benutzer in leader sind Leiter von t.
     * @param t
     *            Turnier
     * @param leaders
     *            Liste der Leiter
     */
    public void setLeaders(Tournament t, List<User> leaders) {
        t.setLeaders(leaders);
    }
}
