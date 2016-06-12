package planning.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import model.User;
import planning.control.ResultManager;
import planning.model.Group;
import planning.model.Match;

/**
 * Erzeugt ein Panel mit Information der Spielbegegnung eines Turniers und
 * ermöglicht je nach Rechten des User die Eingabe von Ergebnissen
 * 
 * @author Torben Zeine
 */
public class MatchPanel extends JPanel {

	private static final long serialVersionUID = -6560712133463513247L;
	private static int PAIRINGLENGTH = 70; // Länge des Angezeigten Textes der
											// Spielpaarung "a - b"
	private static int REFEREELENGTH = 30; // Länge des Namens eines
											// Schiedsrichters
	private static String TRENNZEICHEN_TEAM = " - "; // Trennzeichen zwischen
														// den Begegnungen
	private static SimpleDateFormat TIMEFORMAT = new SimpleDateFormat("kk:mm"); // Angezeigtes
																				// Format
																				// der
																				// Urzeit
																				// des
																				// Matches
	private String[] fieldnames;
	private User actUser; // Aktuell eingeloggter User, null bei Userrechten
	private boolean refereeView = false; // Schiedsrichteransicht -> Nur Felder
											// der Spiele anzeigen, die er auch
											// eingeben darf
	private boolean leaderView = false; // Leiteransicht -> Darf für alle Spiele
										// Ergebnisse eintragen
	private boolean editable = false; // Die Ergebnisse sind je nach Rechten des
										// Users eingebbar
	private ResultManager resManager; // Resultmanager, der ein Match bearbeiten
										// kann
	private Group actGroup; // Aktuelle Gruppe

	/**
	 * Konstruktor Erstellt das Panel und setzt seinen Inhalt
	 * 
	 * @param gruppe
	 *            Gruppe für die das Panel erstellt werden soll
	 * @param frame
	 *            Frame in dem das Panel angezeigt wird
	 * @param fields
	 *            Feldnamen der Felder der Turniers
	 * @param u
	 *            Eingeloggter User, null bei nur Ansichtmodus
	 * @param ref
	 *            Liste der Schiedsrichter des Turniers
	 * @param lead
	 *            Liste der Leiter des Turniers
	 * @param e
	 *            Runde ist noch nicht laufend -> keine Eingaben möglich
	 */
	public MatchPanel(Group gruppe, ResultManager rm, String[] fields,
			User u, List<User> ref, List<User> lead, boolean e) {
		// Setzten des Layouts
		super(new GridBagLayout());
		// Variabeln setzen
		this.actGroup = gruppe;
		this.fieldnames = fields;
		this.actUser = u;
		this.editable = e;
		this.resManager = rm;
		// User ist null -> keine Änderungen möglich
		if (u != null) {
			setUserStatus(u, ref, lead);
		}
		// Gruppe mit Begegnungen
		if (gruppe.getMatches().size() > 0) {
			addComponents();
		}
		// Gruppe hat keine Begegnungen -> Anzeige zum Durchschleifen von Gruppe
		else {
			addComponentsThrough();
		}
		// immer Zeichnen
		setOpaque(true);
		// Größe setzen
		setSize(getPreferredSize());
		// Hintergrundfarbe setzen
		setBackground(Color.WHITE);
	}

	/**
	 * Fügt die Kompontenen in das Panel und erstellt für jede Spielpaarung eine
	 * neue Zeile
	 * 
	 */
	private void addComponents() {
		// Layoutmanager setzen
		GridBagConstraints gbLayout = new GridBagConstraints();
		gbLayout.fill = GridBagConstraints.HORIZONTAL;
		// Rahmen mit Titel setzen
		TitledBorder caption = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(actGroup.getColor(), 2),
				this.actGroup.getLongName());
		caption.setTitleJustification(TitledBorder.CENTER);
		setBorder(caption);
		// Überschirftleiste hinzufügen
		gbLayout.gridx = 0;
		gbLayout.gridy = 0;
		gbLayout.gridwidth = 6;
		gbLayout.ipadx = 5;
		gbLayout.insets = new Insets(0, 20, 0, 0);
		add(new JLabel("Paarung"), gbLayout);
		gbLayout.gridx = 5;
		gbLayout.gridwidth = 1;
		gbLayout.insets = new Insets(0, 0, 0, 0);
		add(new JLabel("Uhrzeit"), gbLayout);

		gbLayout.gridx = 6;
		add(new JLabel("Halle"), gbLayout);

		gbLayout.gridx = 7;
		add(new JLabel("Schiedsrichter"), gbLayout);
		gbLayout.gridx = 8;
		gbLayout.gridwidth = 3;
		gbLayout.insets = new Insets(0, 0, 0, 20);
		add(new JLabel("Ergebnis"), gbLayout);
		// Seperator zur eigentlichlichen Datenleiste
		gbLayout.gridy = 1;
		gbLayout.gridx = 0;
		gbLayout.gridwidth = 12;
		gbLayout.ipady = 5;
		gbLayout.insets = new Insets(0, 0, 0, 0);
		add(new JSeparator(), gbLayout);
		// laufvariable
		int i = 0;
		// Iteration über die Spielpaarungen(Teamslot)
		for (Match actMatch : actGroup.getMatches()) {
			// Label der Begegnungen Team1 - Team2
			gbLayout.gridx = 0;
			gbLayout.gridy = i + 2;
			gbLayout.gridwidth = 2;
			gbLayout.insets = new Insets(0, 20, 0, 0);
			JLabel homeTeamLabel = new JLabel(actMatch.getHomeTeam().getName());
			homeTeamLabel.setForeground(actMatch.getHomeTeam().getColor());
			homeTeamLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			add(homeTeamLabel, gbLayout);
			gbLayout.ipadx = 0;
			gbLayout.gridx = 2;
			gbLayout.gridwidth = 1;
			gbLayout.insets = new Insets(0, 0, 0, 0);
			add(new JLabel(TRENNZEICHEN_TEAM), gbLayout);
			gbLayout.gridx = 3;
			gbLayout.ipadx = 20;
			gbLayout.gridwidth = 2;
			JLabel guestTeamLabel = new JLabel(cutString(actMatch
					.getGuestTeam().getName(), PAIRINGLENGTH / 2));
			guestTeamLabel.setForeground(actMatch.getGuestTeam().getColor());
			add(guestTeamLabel, gbLayout);
			// Zeitpunkt der Begegnung
			gbLayout.insets = new Insets(0, 0, 0, 0);
			gbLayout.gridx = 5;
			gbLayout.gridwidth = 1;
			add(new JLabel(TIMEFORMAT.format(actMatch.getStartTime())),
					gbLayout);
			// Speilfeld der Begegnung
			gbLayout.gridx = 6;
			add(new JLabel(fieldnames[actMatch.getField()]), gbLayout);
			// Schiedsrichter der Begegnung
			gbLayout.gridx = 7;
			String refname = actMatch.getReferee() != null ? actMatch
					.getReferee().getName() : "TBA";
			JLabel refereeName = new JLabel(cutString(refname, REFEREELENGTH));
			add(refereeName, gbLayout);
			// Eingabefelder für die Eingabe der Toore der Heim- und
			// Gastmannschaft
			JSpinner insertGoalsHome = new JSpinner(new SpinnerNumberModel(
					actMatch.getGoals(actMatch.getHomeTeam()), -1, 1000, 1));
			JSpinner insertGoalsGuest = new JSpinner(new SpinnerNumberModel(
					actMatch.getGoals(actMatch.getGuestTeam()), -1, 1000, 1));
			JLabel seperator = new JLabel(" : ");
			// Ergebnisse laden
			// Felder grundsätzlich ausblenden
			insertGoalsHome.setVisible(false);
			insertGoalsGuest.setVisible(false);
			seperator.setVisible(false);
			// Anzeigemodus prüfen
			// läuft die Runde schon?
			// Team sind schon gefüllt
			boolean qualified = true;
			if (actMatch.getGuestTeam().getTeam() != null
					&& actMatch.getHomeTeam().getTeam() != null) {
				if (actMatch.getGuestTeam().getTeam().isDisqualified()
						|| actMatch.getHomeTeam().getTeam().isDisqualified()) {
					// Eines der Teams ist disqualifiziert -> änderungen nicht
					// mehr möglich
					qualified = false;
				}
			}
			if (editable && qualified) {
				// Runde läuft
				// Schiedsrichteransicht
				if (refereeView) {
					if (actMatch.getReferee().equals(actUser)) {
						// Aktueller Schiedsrichter leitet die Begegnung
						// Felder anzeigen
						insertGoalsHome.setVisible(true);
						insertGoalsGuest.setVisible(true);
						seperator.setVisible(true);
					}
				}
				// Leiteransicht
				if (leaderView) {
					// alle Felder anzeigen
					insertGoalsHome.setVisible(true);
					insertGoalsGuest.setVisible(true);
					seperator.setVisible(true);
				}
				// normale UserAnsicht
				else {
					// Ist schon ein Ergbniss eingeben worden
					if (actMatch.isFinished()) {
						// Felder anzeigen, jedoch nicht bearbeitbar
						insertGoalsHome.setVisible(true);
						insertGoalsGuest.setVisible(true);
						seperator.setVisible(true);
						insertGoalsHome.setEnabled(false);
						insertGoalsGuest.setEnabled(false);
					}
				}
			}
			// Listener zur Behandlung von Fehleingaben, Speicherung der
			// eingaben
			ResultUpdateListener l = new ResultUpdateListener(
					insertGoalsHome, insertGoalsGuest, resManager, actMatch);
			insertGoalsHome.addChangeListener(l);
			insertGoalsHome.addFocusListener(l);
			insertGoalsGuest.addChangeListener(l);
			insertGoalsGuest.addFocusListener(l);
			// Elemente hinzufügen
			gbLayout.gridx = 8;
			gbLayout.ipadx = 0;
			gbLayout.gridwidth = 1;
			// Heimeingabefeld hinzufügen
			add(insertGoalsHome, gbLayout);
			// Trennung hinzufügen
			gbLayout.gridx = 9;
			add(seperator, gbLayout);
			// Feld für die Toore Gastmannschaft hinzufügen
			gbLayout.gridx = 10;
			gbLayout.insets = new Insets(0, 0, 0, 20);
			add(insertGoalsGuest, gbLayout);
			gbLayout.insets = new Insets(0, 0, 0, 0);
			i++; // Iteration
		}
	}

	/**
	 * Erzeugt den Inhalt des Fensters, wenn die Gruppe keine Matches enthält.
	 */
	public void addComponentsThrough() {
		// Überschrift ereugen und setzen
		TitledBorder caption = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(actGroup.getColor()),
				this.actGroup.getLongName());
		caption.setTitleJustification(TitledBorder.CENTER);
		setBorder(caption);
		// Nur anzeigen, wenn auch wirklich ein Team in der Gruppe
		if (actGroup.getSlots().size() > 0) {
			// Wartendes Team anzeigen und in Panel hinzufügen
			JLabel waintingLabel = new JLabel("<html>Wartendes Team:<br>"
					+ actGroup.getSlots().get(0).getName() + "</html>");
			waintingLabel.setForeground(actGroup.getSlots().get(0).getColor());
			add(waintingLabel);
		} else {
			// Kein Team vorhanden in der Gruppe
			add(new JLabel("Kein Team vorhanden"));
		}
	}

	/**
	 * Kürzt oder verlängert einen String auf auf PAIRINGLENGTH
	 * 
	 * @param String
	 *            der beschnitten werden soll
	 * @return String der optimiert oder mit "..." wenn gekürzt wurde
	 */
	private String cutString(String old, int length) {
		if (old.length() > length) {
			return old.substring(0, length) + "...";
		} else {
			for (int i = old.length(); i < length; i++) {
				old += " ";
			}
			return old;
		}
	}

	/**
	 * Ermittelt den Status eines Users und setzt entsprechend die Variabelne
	 * 
	 * @param User
	 *            eingeloggten User
	 * @param ref
	 *            List<User> Liste aller Schiedsrichter eines Turniers
	 * @param lead
	 *            List<User> aller Leiter eines Turniers
	 */
	private void setUserStatus(User u, List<User> ref, List<User> lead) {
		if (ref.contains(u)) {
			this.refereeView = true;
		}
		if (lead.contains(u)) {
			this.leaderView = true;
		}
		if (u.isManager()) {
			this.leaderView = true;
		}
	}

	/**
	 * Gibt die Größe des Match zurück
	 * 
	 * @return Dimension Größe des Panels
	 */
	public Dimension getDimension() {
		return getPreferredSize().getSize();
	}
}
