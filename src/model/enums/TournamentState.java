package model.enums;

/**
 * Gibt an, welchen Status das Turnier hat.
 * 
 * @author Dimitri Wegner
 */
public enum TournamentState {

    /**
     * Turnier erstellt
     */
    created,
    /**
     * Turnier ausgeschrieben
     */
    published,
    /**
     * Anmeldezeitraum beendet
     */
    signupClosed,
    /**
     * Turnier läuft
     */
    running,
    /**
     * Turnier ist beendet
     */
    finished,
    /**
     * Turnier wurde abgebrochen
     */
    canceled;

    /**
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
	public String toString() {
		switch (this) {
		case created:
			return "Erstellt";
		case published:
			return "Ausgeschrieben";
		case signupClosed:
			return "Voranmeldezeitraum beendet";
		case running:
			return "Läuft";
		case finished:
			return "Beendet";
		case canceled:
			return "Abgebrochen";
		}
		return "Fehler: Undefiniert";
	}
}
