package view.tools;

/**
 * Hilfsklasse für die Aktivierung / Deaktivierung sämtlicher Aktionen
 * (und somit Aktivierung / Deaktivierung sämtlicher Buttons und MenuItems)
 * je nach Turnierstatus und Aktion
 *
 * Die Klasse stellt 2-dim Arrays zur Verfügung, die abhängig vom Turnierstatus
 * und von der Aktion einen boolischen Wert liefert, der den Zustand der
 * Aktion beschreibt.
 *
 * @author Maike Dudek
 */
public class ToolBarStates {

    /**
     * Aufzählungstypen für die Aktionen eines Sportlers
     */
    public enum AthleteActions{
        /**
         * Anmeldung
         */
        signUp,
        /**
         * Abmeldung
         */
        signOff,
        /**
         * Turnierplan anzeigen
         */
        showTournamentPlan;
    }

    /**
     * Tabelle für die Aktivierung(true) / Deaktivierung(false)
     * der Aktionen eines Sportlers
     * Zeilen: Turniersati
     * (Reihenfolge von oben nach unten: created, published, signUpClosed,
     * running, finished, canceled)
     * Spalten: Sportler-Aktionen
     * (Reihenfolge von links nach rechts: signUp, signOff, show TournamentPlan)
     */
    public final static boolean[][] athleteStates = new boolean[][]{

        {false, false, false},
        {true, true, false},
        {false, false, false},
        {false, false, true},
        {false, false, true},
        {false, false, false}
    };

    /**
     * Aufzählungstypen für die Aktionen eines Schiedsrichters
     */
    public enum RefereeActions{
        /**
         * Ergebnisse eingeben
         */
        enterResults;
    }

    /**
     * Tabelle für die Aktivierung(true) / Deaktivierung(false)
     * der Aktionen eines Schiedsrichters
     * Zeilen: Turniersati
     * (Reihenfolge von oben nach unten: created, published, signUpClosed,
     * running, finished, canceled)
     * Spalten: Schiedsrichter-Aktionen
     * (Reihenfolge von links nach rechts: enterResults)
     */
    public final static boolean[][] refereeStates = new boolean[][]{

        {false},
        {false},
        {false},
        {true},
        {false},
        {false}
    };

    /**
     * Aufzählungstypen für die Aktionen eines Turnierleiters
     */
    public enum LeaderActions{
        /**
         * Anmeldungen bestötigen
         */
        confirmSignUp,
        /**
         * Nachmelden
         */
        lateSignUp,
        /**
         * Schiedsrichter anzeigen
         */
        chooseReferee,
        /**
         * Turnierplan erzeugen
         */
        createTournamentPlan,
        /**
         * Team disqualifizieren
         */
        disqualify,
        /**
         * Turnier abgeschlossen
         */
        finishTournament,
        /**
         * Ergebnissliste erstellen
         */
        createResultlist,
        /**
         * Dokumente erstellen
         */
        createDocuments;
    }

    /**
     * Tabelle für die Aktivierung(true) / Deaktivierung(false)
     * der Aktionen eines Turnierleiters
     * Zeilen: Turniersati
     * (Reihenfolge von oben nach unten: created, published, signUpClosed,
     * running, finished, canceled)
     * Spalten: Turnierleiter-Aktionen
     * (Reihenfolge von links nach rechts: confirmSignUp, lateSignUp, chooseReferee,
     * createTournamentPlan, disqualify, finishTournament, createResultlist,
     * createDocuments)
     */
    public final static boolean[][] leaderStates = new boolean[][]{

        {false, false, false, false, false, false, false, false},
        {false, false, false, false, false, false, false, false},
        {true, true, true, true, false, false, false, false},
        {false, false, false, false, true, true, false, false},
        {false, false, false, false, false, false, true, true},
        {false, false, false, false, false, false, false, false}
    };

     /**
     * Aufzählungstypen für die Aktionen eines Turnierverantwortlichen
     */
    public enum ManagerActions{
        /**
         * Turnier ausschreiben
         */
        publishTournament,
        /**
         * Leiter auwählen
         */
        chooseLeader,
        /**
         * Anmeldezeitraum beenden
         */
        finishSignUpTime,
        /**
         * Turnier absagen
         */
        cancelTournament;
    }

    /**
     * Tabelle für die Aktivierung(true) / Deaktivierung(false)
     * der Aktionen eines Turnierverantwortlichen
     * Zeilen: Turniersati
     * (Reihenfolge von oben nach unten: created, published, signUpClosed,
     * running, finished, canceled)
     * Spalten: Turnierverantwortlicher-Aktionen
     * (Reihenfolge von links nach rechts: publishTournament, chooseLeader,
     * finishSignUpTime, cancelTournament) 
     */
    public final static boolean[][] managerStates = new boolean[][]{

        {true, true, false, false},
        {true, true, true, true},
        {false, true, false, true},
        {false, true, false, true},
        {false, false, false, false},
        {false, false, false, false}
    };
}
