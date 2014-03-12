package model.enums;

/**
 * Ein Turnier kann entweder ein Einzelspieler-Turnier sein, d.h.
 *  Teams in dem Turnier bestehen aus genau einem Benutzer
 * oder ein Mehrspieler-Turnier, d.h.
 *  für das Turnier gemeldete Benutzer werden in Teams zusammengefasst und
 *  diese nehmen an dem Turnier teil.
 */
public enum TournamentType {
    /**
     * Einzelspielermodus
     */
    SinglePlayer,
    /**
     * Mehrspielermodus
     */
    MultiPlayer;
}
