package planning.view;

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
public class SpinChangeListener implements ChangeListener {
    //Objekte die zum Speichen und aktualisieren benötigt werden

    private TournamentPlanView frame;
    private JSpinner home,  guest;
    private ResultManager resMan;
    private Match match;

    /**
     * Konstruktor
     * Erstellt den Focuslistener und speichert die Daten
     *
     * @param home Hometeam
     * @param guest Guestteam
     * @param resman der die Ergebnisse einer Mannschaft entgegen nimmt
     * @param m Spielbegegnung
     * @param f Fenster in dem sich das Panel befindet
     */
    public SpinChangeListener(JSpinner home, JSpinner guest, ResultManager resman, Match m, TournamentPlanView f) {
        //Objekte speichern
        this.home = home;
        this.guest = guest;
        this.resMan = resman;
        this.match = m;
        this.frame = f;
    }
    /**
     * Prüft ob der Wert der Felder sich im Gegensatz zu den gespeicherten verändert hat
     * @return boolean Ergebnisse wurde geändert
     */
    private boolean changed(){
        if((Integer) home.getValue() == match.getGoals(match.getHomeTeam())){
            return false;
        }
        else if((Integer) home.getValue() == match.getGoals(match.getGuestTeam())){
            return false;
        }
        else{
            return true;
        }
        
    }

    /**
     * 
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
         if ((Integer) home.getValue() > -1 && (Integer) guest.getValue() > -1) {
            //Werte haben sich geändert
            if(changed()){
                //Speichern
                resMan.setResult(match, (Integer) home.getValue(), (Integer) guest.getValue());
                //Frameinhalt neu laden
                frame.reloadMatchPanel();
            }
        }
    }
}
