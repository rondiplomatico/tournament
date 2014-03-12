package planning.control.score;

/**
 * 
 * Enthält alle ScoreTransfer-Strategien mit Beschreibungen
 * 
 * @author Daniel Wirtz
 * 
 */
public enum ScoreTransfer {
	/**
	 * Keine Punkte
	 */
	NoScores, 
	/**
	 * Alle Punkte
	 */
	AllScores, 
	/**
	 * Punkte gegen ebenfalls weiterkommende Teams
	 */
	Special1;

	/**
	 * Beschreibung für die GUI-Anzeige
	 * 
	 * @return Beschreibung
	 */
	public String getDescription() {
		String res = "<html>";
		switch (this) {
		case NoScores:
			res += "Es werden <b>keine</b> Punkte und Tore in die nächste Runde übernommen"; break;
		case AllScores:
			res += "Alle Punkte und Tore werden komplett mit in die nächste Runde übernommen."; break;
		case Special1:
			res += "Es werden die Punkte gegen alle Teams übernommen, die zusammen mit diesem Team in die nächste Runde weitergekommen sind"; break;
		}
		return res + "</html>";
	}

	/**
	 * Bessere Anzeige im Debug / GUI
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		switch (this) {
		case NoScores:
			return "Keine";
		case AllScores:
			return "Alle";
		case Special1:
			return "Weiterkommende";
		}
		return null;
	}

	/**
	 * Erzeugt eine Instanz der Beschriebenen Transfer-Strategie
	 * (Factory)
	 * 
	 * @return IScoreCalculator-Instanz
	 */
	public IScoreCalculator getInstance() {
		switch (this) {
		case NoScores:
			return new NoScoreTransfer();
		case AllScores:
			return new AllScoreTransfer();
		case Special1:
			return new ScoresAgainstOtherProceedingTeams();
		}
		return null;
	}

	/**
	 * Versucht einer IScoreCalculator-Instanz das indentifizierende Enum
	 * zuzuweisen. Bei Fehlschlag wird einfach ScoreTransfer.NoScore
	 * zurückgegeben.
	 * 
	 * @param calc
	 * @return
	 */
	/*public static ScoreTransfer parse(IScoreCalculator calc) {
		if (calc instanceof AllScoreTransfer) return ScoreTransfer.AllScores;
		if (calc instanceof NoScoreTransfer) return ScoreTransfer.AllScores;
		if (calc instanceof ScoresAgainstOtherProceedingTeams) return ScoreTransfer.Special1;
		return ScoreTransfer.NoScores;
	}*/

}
