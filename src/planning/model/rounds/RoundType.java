/**
 * Created on 06.03.2009 in Project Tournament
 * Location: planning.model.rounds.RoundTypes.java
 */
package planning.model.rounds;

import model.Tournament;

/**
 * 
 * Führt eine Liste der Rundentypen
 * 
 * @author Daniel Wirtz
 * 
 */
public enum RoundType {
	/**
	 * Finale Runde mit einer Gruppe
	 */
	EinzelgruppenFinalrunde, 
	/**
	 * KO-Runde
	 */
	KORunde, 
	/**
	 * Einfache Gruppenrunde
	 */
	GruppenRunde, 
	/**
	 * Liga-Gruppenrunde mit mehreren Gruppen
	 */
	LigaGruppenRunde, 
	/**
	 * Liga-Runde mit genau einer Gruppe.
	 * Ist final.
	 */
	Liga;

	/**
	 * Prüft ob die Implementierung eines Rundentyps das Interface IGroupRound implementiert.
	 * @param type
	 * @return boolean
	 */
	public static boolean implementsIGroupRound(RoundType type) {
		return type == LigaGruppenRunde || type == GruppenRunde;
	}

	/**
	 * Prüft ob die Implementierung eines Rundentyps das Interface IMultiphaseRound implementiert.
	 * @param type
	 * @return boolean
	 */
	public static boolean implementsIMultiPhaseRound(RoundType type) {
		return type == KORunde || type == LigaGruppenRunde || type == Liga;
	}

	/**
	 * Prüft ob die Implementierung eines Rundentyps das Interface IFinalRound implementiert.
	 * @param type
	 * @return boolean
	 */
	public static boolean implementsIFinalRound(RoundType type) {
		return type == KORunde || type == EinzelgruppenFinalrunde
				|| type == Liga;
	}

	/**
	 * 
	 * @return Beschreibung des Rundentyps
	 */
	public String getDescription() {
		String desc = "<html>";
		switch (this) {
		case EinzelgruppenFinalrunde:
			desc += "Eine finale Runde bestehend aus nur <b>einer</b> Gruppe.<br>";
			break;
		case KORunde:
			desc += "Turnierübliche <b>Finalrunde</b>.<br>Berechnet anhand der weiterkommenden Teilnehmer automatisch die Anzahl der Finalphasen und erzeugt <ul><li>Finale</li><li>Halbfinale</li><li>Viertelfinale</li></ul> etc.";
			break;
		case GruppenRunde:
			desc += "Einfache Gruppenrunde mit <b>frei einstellbarer</b> Anzahl an Gruppen.<br>"
					+ "<br>"
					+ "In jeder Gruppe wird <b>Jeder gegen Jeden</b> gespielt.";
			break;
		case LigaGruppenRunde:
			desc += "Eine Runde bestehend aus <b>Hin- und Rückrunde</b> für jede eingerichtete Gruppe.";
			break;
		case Liga:
			desc += "Eine <b>finale</b> Runde die aus einer Gruppe besteht.<br><br>Gespielt werden Hin- und Rückrunde.";
			break;
		}
		return desc + "</html>";
	}

	/**
	 * Bessere Darstellung
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		switch (this) {
		case EinzelgruppenFinalrunde:
			return "Finale Einzelgruppenrunde";
		case KORunde:
			return "Finalrunde";
		case GruppenRunde:
			return "Gruppenrunde";
		case LigaGruppenRunde:
			return "Hin- und Rückspiel Gruppenrunde";
		case Liga:
			return "Liga-Runde";
		}
		return null;
	}

	/**
	 * Erzeugt eine neue Instanz des Rundentyps. (Factory)
	 * @param t
	 * @param name
	 * @return Rundentyp-Instanz
	 */
	public Round getNewInstance(Tournament t, String name) {
		switch (this) {
		case EinzelgruppenFinalrunde:
			return new FinalSingleGroupRound(t, name);
		case GruppenRunde:
			return new StandardGroupRound(t, name);
		case KORunde:
			return new KORound(t, name);
		case LigaGruppenRunde:
			return new FirstSecondLegRound(t, name);
		case Liga:
			return new LeagueRound(t, name);
		}
		return null;
	}

	/*
	 * public static RoundType parse(Round r) { if (r instanceof
	 * FinalSingleGroupRound) return RoundType.EinzelgruppenFinalrunde; if (r
	 * instanceof StandardGroupRound) return RoundType.GruppenRunde; if (r
	 * instanceof KORound) return RoundType.KORunde; if (r instanceof
	 * FirstSecondLegRound) return RoundType.LigaRunde; return
	 * RoundType.GruppenRunde; }
	 */

}
