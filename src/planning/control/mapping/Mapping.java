package planning.control.mapping;

/**
 * 
 * Enthält alle IGroupMapper-Implementationen inkluvise Beschreibungen für die
 * GUI.
 * 
 * @author Daniel Wirtz
 * 
 */
public enum Mapping {
	/**
	 * Über Kreuz
	 */
	CrossMapper,
	/**
	 * Zufälliges Mapping
	 */
	RandomMapper,
	/**
	 * Direktes Kopieren, soweit möglich
	 */
	StraightMapper,

	/**
	 * Combines two (or more) groups together
	 */
	CombineMapper,

	/**
	 * Maps the proceedants of two groups crosswise over a 2-rank distance
	 */
	TwoToPairwise,

	/**
	 * Works the same as the cross mapper, but changes mapping/distribution
	 * direction every two source groups
	 */
	AlternatingCrossMapper,

	EqualMapping,

	/**
	 * A custom mapping instance.
	 */
	Custom;

	/**
	 * Gibt eine kurze Beschreibung des Mapping-Typs aus.
	 * 
	 * @return Beschreibung
	 */
	public String getDescription() {
		String res = "<html>";
		switch (this) {
		case CrossMapper:
			res += "Die Mannschaften werden aus den Gruppen über Kreuz weitergeleitet.<br>"
					+ "Beispiel vorige Runde eine Gruppe, nächste Runde 3 Gruppen:<br>"
					+ "<ul><li>Vorige Gruppe A 1. Team -> Nächste Gruppe A 1. Team</li>"
					+ "<li>Vorige Gruppe A 2. Team -> nächste Gruppe B 1. Team</li>"
					+ "<li>Vorige Gruppe A 3. Team -> nächste Gruppe C 1. Team</li>"
					+ "<li>Vorige Gruppe A 4. Team -> nächste Gruppe A 2. Team</li>"
					+ "<li>Vorige Gruppe A 5. Team -> nächste Gruppe B 2. Team</li>"
					+ "<li>etc...</li>" + "</ul>";
			break;

		case RandomMapper:
			res += "Die Mannschaften werden <b>zufällig</b> in die nächsten Gruppen verteilt.<br>"
					+ "Dabei werden die Mannschaften möglichst gleichmäßig auf die Zielgruppen verteilt.";
			break;

		case StraightMapper:
			res += "Die Mannschaften werden aus den Gruppen direkt weitergeleitet.<br><br>"
					+ "<b>Sonderfälle</b>:<br>"
					+ "<i>Mehr Quellgruppen als Zielgruppen</i><br>"
					+ "Die restlichen Gruppen werden über Kreuz gleichmäßig verteilt.<br>"
					+ "<br>"
					+ "<i>Mehr Zielgruppen als Quellgruppen</i><br>"
					+ "Es wird weiterleitung über Kreuz für alle Gruppen durchgeführt<br>"
					+ "(Direktes Mapping macht dann keinen Sinn)";
			break;
		case CombineMapper:
			res += "Kombiniert je zwei Gruppen in eine Zielgruppe";
			break;

		case TwoToPairwise:
			res += "Kombiniert zwei Gruppen über Kreuz, jedoch mit 2 Ranglistenplätzen Abstand. Also 1-3, 2-4 etc";
			break;
		case AlternatingCrossMapper:
			res += "Arbeitet wie der CrossMapper.<br>"
					+ "Als Erweiterung wird die Durchlaufrichtung innerhalb jeder weiteren Quellgruppe umgedreht.";
			break;
		case Custom:
			res += "A custom mapping.<br>";
			break;
		case EqualMapping:
			res += "Foo!";
			break;
		default:
			res += "No description for " + this.name();
		}

		return res + "</html>";
	}

	/**
	 * Für bessere Darstellung
	 * 
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		switch (this) {
		case CrossMapper:
			return "Über Kreuz";
		case StraightMapper:
			return "Direkt";
		case RandomMapper:
			return "Zufällig";
		case CombineMapper:
			return "Kombination";
		case TwoToPairwise:
			return "2 zu paarweise";
		case EqualMapping:
			return "Gleichm��ig aufteilen";
		case Custom:
			return "Custom";
		default:
			return this.name();
		}
	}

	/**
	 * Erzeugt eine Instanz des beschriebenen Mapping-Verfahrens.
	 * 
	 * @return IGroupMapper-Instanz
	 */
	public IGroupMapper getInstance() {
		switch (this) {
		case CrossMapper:
			return new CrossMapper();

		case RandomMapper:
			return new RandomMapper();

		case CombineMapper:
			return new CombineMapper();

		case StraightMapper:
			return new StraightMapper();

		case TwoToPairwise:
			return new TwoToPairwiseMapper();

		case AlternatingCrossMapper:
			return new AlternatingCrossMapper();
		case EqualMapping:
			return new HalfMapper();
		}
		return null;
	}
}
