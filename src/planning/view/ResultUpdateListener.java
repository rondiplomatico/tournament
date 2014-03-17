package planning.view;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import planning.control.ResultManager;
import planning.model.Match;

/**
 * Focuslistener für die Spinner der Ergebnisseingabe
 * 
 * @author Torben Zeine
 */
public class ResultUpdateListener implements ChangeListener, FocusListener {
	// Objekte die zum Speichen und aktualisieren benötigt werden

	private JSpinner home, guest;
	private ResultManager resMan;
	private Match match;

	/**
	 * Konstruktor Erstellt den Focuslistener und speichert die Daten
	 * 
	 * @param home
	 *            Hometeam
	 * @param guest
	 *            Guestteam
	 * @param resman
	 *            der die Ergebnisse einer Mannschaft entgegen nimmt
	 * @param m
	 *            Spielbegegnung
	 * @param f
	 *            Fenster in dem sich das Panel befindet
	 */
	public ResultUpdateListener(JSpinner home, JSpinner guest,
			ResultManager resman, Match m) {
		// Objekte speichern
		this.home = home;
		this.guest = guest;
		this.resMan = resman;
		this.match = m;
	}

	/**
	 * Prüft ob der Wert der Felder sich im Gegensatz zu den gespeicherten
	 * verändert hat
	 * 
	 * @return boolean Ergebnisse wurde geändert
	 */
	private boolean changed() {
		return (Integer) home.getValue() != match.getGoals(match.getHomeTeam())
				|| (Integer) guest.getValue() != match.getGoals(match
						.getGuestTeam());
	}

	private void updateResult() {
		if ((Integer) home.getValue() > -1 && (Integer) guest.getValue() > -1) {
			// Werte haben sich geändert
			if (changed()) {
				// Speichern
				resMan.setResult(match, (Integer) home.getValue(),
						(Integer) guest.getValue());
			}
		}
	}

	/**
	 * 
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		updateResult();
	}

	@Override
	public void focusGained(FocusEvent e) {
		// chill
	}

	@Override
	public void focusLost(FocusEvent e) {
		updateResult();
	}
}
