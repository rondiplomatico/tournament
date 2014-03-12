package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.enums.TournamentState;
import model.enums.TournamentType;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import planning.model.RoundSetting;
import planning.model.TournamentPlan;

/**
 * Stellt ein Turnier dar.
 * 
 * @author Daniel Wirtz, Dimitri Wegner
 */
@Entity
public class Tournament implements Serializable {

	private static final long serialVersionUID = -8714075023243594331L;

	@Id
	@GeneratedValue
	private long id;

	@OneToMany(cascade = CascadeType.ALL)
	private List<RoundSetting> roundSettings;
	/** Mannschaften */
	@OneToMany(cascade = CascadeType.ALL)
	private List<Team> teams;
	/** Schiedsrichter */
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "tournament_referees")
	private List<User> referees;
	/** Leiter */
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "tournament_leaders")
	private List<User> leaders;

	// Startzeitpunkt / Datum fürs Turnier
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	// Anmeldeschluss
	@Temporal(TemporalType.TIMESTAMP)
	private Date expireDate;

	// Turnierstatus
	@Column(name = "tState")
	private TournamentState state;

	// Teilnahmebedingungen
	private String participationCond;
	// Turniername
	private String name;
	/* Austragungsort */
	private String location;
	// Der (endgültige) Turnierplan
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "tournament")
	private TournamentPlan plan = null;

	// Feldnamen
	// Mediumblob in MySQL
	@CollectionOfElements
	@IndexColumn(name = "orderCol")
	private String[] fields;

	// Sportart
	private String sportKind;
	// Max. Teilnehmer
	private int maxPart;
	// Teilnahmegebühren
	private float fee;
	// Gebühren nach Ablauf des Voranmeldezeitraums
	private float lateRegFee;
	// Standard-Spieldauer
	private int stdGameDuration;

	/*
	 * @@@@@@@@@@@@@@@@@@@ Variablen mit Standardwerten @@@@@@@@@@@@@@@@@@@@@
	 */
	/** Endhour */
	private int eH = 18;
	/** Starthour */
	private int sH = 9;
	// Multiplayer als Standard
	private TournamentType type = TournamentType.MultiPlayer;
	// Punkte für Sieg
	private int pfV = 2;
	// Punkte für Unentschieden
	private int pfR = 1;
	// Punkte für Niederlage
	private int pfL = 0;
	// Spieler pro Team
	private int players = 11;

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	/**
	 * Nach JPA-Spezifikation Standard-Konstruktor ohne Parameter. Initialisiert
	 * die Standardwerte
	 */
	protected Tournament() {
		state = TournamentState.created;
		expireDate = new Date();
		teams = new ArrayList<Team>();
		startDate = new Date(0);
		referees = new ArrayList<User>();
		leaders = new ArrayList<User>();
		roundSettings = new ArrayList<RoundSetting>();
		type = TournamentType.MultiPlayer;
	}

	/**
	 * Erzeugt ein Turnier und übernimmt gleich alle notwendigen Einstellungen.
	 * 
	 * Standardmäßig entspricht die Gebühr für die Anmeldung nach Ablauf der
	 * Meldefrist gleich der normalen Anmeldegebühr. Dies kann über @see
	 * {@link Tournament#setLateRegistrationFee(float)} angepasst werden.
	 * 
	 * @see Tournament#setGameDuration(int) Zur Spielzeit
	 * 
	 * @param name
	 *            Name des Turniers
	 * @param fee
	 *            Startgebühr
	 * @param maxParticipants
	 *            Max. Anzahl der Teilnehmer
	 * @param participationConditions
	 *            Teilnahmebedingungen (Anzeigecharakter)
	 * @param standardGameDuration
	 *            Dauer eines Spiels inklusive Pausen
	 * @param startDate
	 *            Das Startdatum
	 */
	public Tournament(String name, float fee, int maxParticipants,
			String participationConditions, int standardGameDuration,
			Date startDate) {
		this();
		this.name = name;
		this.fee = fee;
		this.lateRegFee = fee;
		this.maxPart = maxParticipants;
		this.participationCond = participationConditions;
		this.stdGameDuration = standardGameDuration;
		this.startDate = startDate;
	}

	/**
	 * Gibt die eindeutige Nummer des Turniers wieder
	 * 
	 * @return ID des Turniers
	 */
	public long getId() {
		return id;
	}

	/**
	 * Gibt den Namen des Turniers wierder
	 * 
	 * @return Name des Turniers
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gibt den Austragungsort zurück.
	 * 
	 * @return Austragungsort des Turniers
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Setzt den Austragungsort.
	 * 
	 * @param location
	 *            Austragunsort des Turniers
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Gibt den Turniertyp wieder
	 * 
	 * @return Turniertyp
	 */
	public TournamentType getType() {
		return this.type;
	}

	/**
	 * Setzt den Turniertyp.
	 * 
	 * @see TournamentType
	 * @param type
	 *            Turniertyp
	 */
	public void setType(TournamentType type) {
		this.type = type;
	}

	/**
	 * Teilnahmebeitrag
	 * 
	 * @return Meldegebühr
	 */
	public float getFee() {
		return fee;
	}

	/**
	 * Teilnahmebeitrag für verspätete Anmeldungen.
	 * 
	 * @return Die Nachmeldegebühr
	 */
	public float getLateRegFee() {
		return lateRegFee;
	}

	/**
	 * 
	 * Setzt die mindestanzahl Spieler pro Team.
	 * 
	 * @param value
	 *            mindestanzahl der Spieler pro Team
	 * 
	 * @pre value > 0
	 */
	public void setRequiredPlayersPerTeam(int value) {
		assert (value > 0);
		players = value;
	}

	/**
	 * Gibt das Startdatum des Turniers wieder
	 * 
	 * @return Startdatum des Turniers
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Startdatum ist das Austragungsdatum des Turniers.
	 * 
	 * @param val
	 *            Startdatum des Turniers
	 */
	public void setStartDate(Date val) {
		startDate = val;
	}

	/**
	 * Setzt die Namen der Felder mit "Feld " + lfd. Nummer
	 * 
	 * @param numFields
	 *            Die Anzahl der zu setztenden Spielfelder
	 */
	public void setFields(int numFields) {
		fields = new String[numFields];
		for (int x = 0; x < numFields; x++) {
			fields[x] = "Feld " + (x + 1);
		}
	}

	/**
	 * Setzt die Spielfeldnamen
	 * 
	 * @param fieldNames
	 *            Die Spielfeldnamen
	 */
	public void setFields(String[] fieldNames) {
		fields = fieldNames;
	}

	/**
	 * Gibt die Anzahl der Spielfelder
	 * 
	 * @return Anzahl der Spielfelder
	 */
	public int getNumFields() {
		return fields.length;
	}

	/**
	 * Gibt die Feldnamen des Turniers wieder
	 * 
	 * @return Feldnamen des Turniers
	 */
	public String[] getFieldNames() {
		return fields;
	}

	/**
	 * Gibt die Endstunde des Turniers wieder
	 * 
	 * @return Die Endstunde des Turniers
	 */
	public int getEndHour() {
		return eH;
	}

	/**
	 * Setzt die Endstunde des Turniers
	 * 
	 * @param value
	 *            Endstunde des Turniers
	 */
	public void setEndHour(int value) {
		eH = value;
	}

	/**
	 * Gibt die Startstunde des Turniers wieder
	 * 
	 * @return Startstunde des Turniers
	 */
	public int getStartHour() {
		return sH;
	}

	/**
	 * Setzt die Startstunde des Turniers
	 * 
	 * @param value
	 *            Startstunde
	 */
	public void setStartHour(int value) {
		sH = value;
	}

	/**
	 * Setzt die Nachmeldegebühr
	 * 
	 * @param value
	 *            Nachmeldegebühr
	 */
	public void setLateRegistrationFee(float value) {
		this.lateRegFee = value;
	}

	/**
	 * Setzt die Punkte die für einen Sieg notwendig sind
	 * 
	 * @return Punkte
	 */
	public int pointsForVictory() {
		return pfV;
	}

	/**
	 * Gibt Punkte für ein Unterschieden wieder
	 * 
	 * @return unkte für Unentschieden
	 */
	public int pointsForRemis() {
		return pfR;
	}

	/**
	 * Setzt die Punkte die für eine Niederlage
	 * 
	 * @return Punkte für Niederlage
	 */
	public int pointsForLoss() {
		return pfL;
	}

	/**
	 * Gibt Schiedsrichter-Liste des Turniers zurück.
	 * 
	 * Ist t != null, werden nur schiedsrichter aus dem team zurückgegeben. Ist
	 * darüber hinaus excludeFlag=true, werden alle Schiedsrichter die NICHT in
	 * diesem Team sind zurückgegeben.
	 * 
	 * @return Schiedsrichter-Liste
	 */
	public List<User> getReferees(List<User> avoid) {
		List<User> refs = new ArrayList<User>();
		for (User u : referees) {
			if ((!avoid.contains(u))) {
				refs.add(u);
			}
		}
		return refs;
	}

	public List<User> getReferees(Team t, boolean excludeFlag) {
		if (t != null) {
			List<User> refs = new ArrayList<User>();
			for (User u : referees) {
				if ((!excludeFlag && t.getPlayers().contains(u))
						|| (excludeFlag && !t.getPlayers().contains(u))) {
					refs.add(u);
				}
			}
			return refs;
		} else {
			return referees;
		}
	}

	/**
	 * Returns only referees that are in the specified team
	 * 
	 * @param t
	 * @return
	 */
	public List<User> getReferees(Team t) {
		return getReferees(t, false);
	}

	/**
	 * Returns all referees of the tournament
	 * 
	 * @return
	 */
	public List<User> getReferees() {
		return getReferees((Team)null);
	}

	/**
	 * Fügt einen neuen Schiedsrichter zum Turnier hinzu.
	 * 
	 * @pre u muss global als Referee eingetragen sein
	 * @post u ist als Schiedsrichter in dem Turnier eingetragen.
	 * @param u
	 *            Schiedsrichter
	 */
	public void addReferee(User u) {
		assert (u.isReferee());

		referees.add(u);
	}

	/**
	 * Gibt Leiter-Liste des Turniers zurück.
	 * 
	 * @return Leiter-Liste
	 */
	public List<User> getLeaders() {
		return leaders;
	}

	/**
	 * Setzt die Liste der Turnierleiter.
	 * 
	 * @param leaders
	 *            Liste der Turnierleiter
	 */
	public void setLeaders(List<User> leaders) {
		this.leaders = leaders;
	}

	/**
	 * Gibt die geplante Standard-Begegnungsdauer in Minuten zurück.
	 * 
	 * Dabei wird vorausgesetzt, das diese Spielzeit schon alle Halbzeiten und
	 * Pausen dazwischen enthält.
	 * 
	 * @return Die Zeit die ein Spiel braucht
	 */
	public int getGameDuration() {
		return stdGameDuration;
	}

	/**
	 * Die Standardspieldauer wird vom Planer benutzt, um den Spielzeitplan zu
	 * erstellen. Die Dauer soll alle gewünschten Pausen schon enthalten, also
	 * sowohl evtl. Halbzeitpausen und die Pause nach einem Spiel, zu
	 * Erholungszwecken bzw. Zeit zum Spiel(feld/ort)wechsel.
	 * 
	 * @param value
	 *            Spielzeit inklusive Pausen
	 */
	public void setGameDuration(int value) {
		stdGameDuration = value;
	}

	/**
	 * Fügt eine neue Mannschaft dem Turnier hinzu.
	 * 
	 * @param t
	 *            Mannschaft
	 */
	public void addTeam(Team t) {
		teams.add(t);
	}

	/**
	 * Gibt das Datum zurück, am welchem die Voranmeldung für das Turnier
	 * abläuft.
	 * 
	 * @return expireDate Das Datum an dem die Meldefrist abläuft
	 */
	public Date getExpireDate() {
		return expireDate;
	}

	/**
	 * Setzt das Datum, am welchem die Voranmeldung für das Turnier abläuft.
	 * 
	 * @param expireDate
	 *            Das Datum an dem die Meldefrist abläuft
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * Gib die Liste der Teams wieder aus.
	 * 
	 * @return Liste der Teams
	 */
	public List<Team> getTeams() {
		return teams;
	}

	/**
	 * Gibt die Liste aller aktuell vorangemeldeter Benutzer.
	 * 
	 * @return Liste der Benutzer
	 */
	public List<User> getPlayers() {
		List<User> resultList = new ArrayList<User>();

		for (Team t : getTeams()) {
			resultList.addAll(t.getPlayers());
		}

		return resultList;
	}

	/**
	 * Vergleicht Turnier anhand der Id, ist die noch nicht vergeben, so wird
	 * anhand des Namen verglichen.
	 * 
	 * @param obj
	 *            Das Objekt, mit welchgem verglichen wird.
	 * @return true, falls die Objekte gleich sind, sonst false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tournament) {
			if (id != 0) {
				return this.id == ((Tournament) obj).getId();
			} else {
				return this.name.equals(((Tournament) obj).getName());
			}
		}

		return false;
	}

	/**
	 * Hashcode wird anhand von Id und Namen generiert.
	 * 
	 * @return Hashcode
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 11 * hash + (int) (this.id ^ (this.id >>> 32));
		hash = 11 * hash + (this.name != null ? this.name.hashCode() : 0);
		return hash;
	}

	/**
	 * Gibt Turnierstatus an.
	 * 
	 * @see TournamentState
	 * @return Turnierstatus
	 */
	public TournamentState getState() {
		return state;
	}

	/**
	 * Setzt Turnierstatus.
	 * 
	 * @see TournamentState
	 * @param state
	 */
	public void setState(TournamentState state) {
		this.state = state;
	}

	/**
	 * Gibt den Sporttyp wieder
	 * 
	 * @return the sportKind
	 */
	public String getSportKind() {
		return sportKind;
	}

	/**
	 * Setzt den Sporttyp
	 * 
	 * @param sportKind
	 *            the sportKind to set
	 */
	public void setSportKind(String sportKind) {
		this.sportKind = sportKind;
	}

	/**
	 * 
	 * @return participationCond . The participation conditions.
	 */
	public String getParticipationCond() {
		return participationCond;
	}

	/**
	 * Setzt die Liste der Schiedsrichter
	 * 
	 * @param referees
	 *            the referees to set
	 */
	public void setReferees(List<User> referees) {
		this.referees = referees;
	}

	/**
	 * Gibt den Turnierplan wieder
	 * 
	 * @return the plan
	 */
	public TournamentPlan getTournamentPlan() {
		return plan;
	}

	/**
	 * Setzt den Turnierplan
	 * 
	 * @param plan
	 *            the plan to set
	 */
	public void setTournamentPlan(TournamentPlan plan) {
		this.plan = plan;
	}

	/**
	 * Gibt die Rundeneinstellungen wieder
	 * 
	 * @return the roundSettings
	 */
	public List<RoundSetting> getRoundSettings() {
		return roundSettings;
	}

	/**
	 * @return max Participating Teams
	 */
	public int getMaxParticipatingTeams() {
		return maxPart;
	}

	/**
	 * Gibt die mindestanzahl an Spielern pro Team aus.
	 * 
	 * Ist es ein Einzelspieler-Turnier, so wird 1 zurückgegeben.
	 * 
	 * @return mindestanzahl Spieler
	 */
	public int getRequiredPlayersPerTeam() {
		return type == TournamentType.SinglePlayer ? 1 : players;
	}
}
