package planning.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.swing.SwingConstants;

/**
 * @author torben Zeine
 * 
 *         Klasse die die Statusdaten einer Mannschaft enthält und Information
 *         zu Ihrer Anzeige
 * 
 */
@Entity
public class TeamStats implements Serializable {

	private static final long serialVersionUID = -8685011514472893666L;

	/*
	 * Orientierung der Inhalte der Felder in einem Panel
	 */
	private static int[] FIELDALIGNMENTS = { SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER };

	/*
	 * Länge in Zeichen auf die angezeigten Felder vergrößert werden
	 */
	private static int[] FIELDLENGTH = { 40, 10, 10, 15, 15 };

	/*
	 * Überschriften zu den Daten der Mannschaft
	 */
	private static String[] HEADS = { "Name", "Punkte", "Tore", "G/U/V", "Gespielt/Total" };

	/**
	 * Position der Felder
	 */
	public static int NAME = 0;

	/**
	 * Punkte
	 */
	public static int POINTS = 1;

	/**
	 * Tore
	 */
	public static int GOALS = 2;
	/**
	 * Stats (Gewonnen/Unentsch/Verloren)
	 */
	public static int STAT = 3;
	/**
	 * Games (Gespielt/Total)
	 */
	public static int GAMES = 4;
	
	/**
	 * Anzahl der Felder(vom ersten an), die im Quickview angezeigt werden
	 */
	public static int QUICKVIEW = 2;
	
	/**
	 * Anzahl der Felder
	 */
	public static int ENTRIES = 5;

	@SuppressWarnings("unused")
	@Id
	@GeneratedValue
	private long id;

	private Score scores;
	private String[] fields = new String[HEADS.length]; // Daten der Mannschaft

	/**
	 * 
	 */
	public TeamStats() {
		scores = new Score();
	}

	/**
	 * Gibt die Bezeichnung eines Datenfeldes zurück
	 * 
	 * @param x
	 *            anzeigende Überschriftentitel -> Konstranten
	 * @return Übeschrift
	 */
	public static String getTitle(int x) {
		return TeamStats.HEADS[x];
	}

	/**
	 * Gibt die Ausrichtung eines Datenfeldes zurück
	 * 
	 * @param x
	 *            anzeigende Überschriftentitel -> Konstranten
	 * @return Alignment -> Swing Konstants
	 */
	public static int getAlignment(int x) {
		return TeamStats.FIELDALIGNMENTS[x];
	}

	/**
	 * Gibt die Daten eines Datenfeldes zurück
	 * 
	 * @param x
	 *            stelle der gewünschten Daten -> Konstranten
	 * @return Daten
	 */
	public String getContent(int x) {
		if (x == GOALS) {
			return scores.getGoals() + ":" + scores.getCounterGoals();
		} else if (x == POINTS) {
			return scores.getPointsPlus() + ":" + scores.getPointsMinus();
		}
		return this.fields[x];
	}

	/**
	 * Setzt die Daten einer Mannschaft und verlängert ihn auf die vorgegebene
	 * Länge je nach ihrem alignment
	 * 
	 * @param a
	 *            Typ der zu setzenden Daten
	 * @param value
	 *            der gesetzt werden soll
	 */
	public void setField(int a, String value) {
		if (FIELDALIGNMENTS[a] == SwingConstants.CENTER) { // Zentrieren
			String tmp = "";
			for (int i = 0; i < ((FIELDLENGTH[a] / 2) - (value.length() / 2)); i++) {
				tmp += " ";
			}
			tmp += value;
			for (int i = tmp.length(); i < FIELDLENGTH[a]; i++) {
				tmp += " ";
			}
			this.fields[a] = tmp;
		} else if (FIELDALIGNMENTS[a] == SwingConstants.LEFT) { // Links
			for (int i = value.length(); i < FIELDLENGTH[a]; i++) {
				value += " ";
			}
			this.fields[a] = value;
		} else {// Rechts
			String tmp = "";
			for (int i = 0; i < FIELDLENGTH[a] - value.length(); i++) {
				tmp += " ";
			}
			tmp += value;
			this.fields[a] = tmp;
		}
	}

	/**
	 * 
	 * @return Scores
	 */
	public Score getScores() {
		return scores;
	}
}
