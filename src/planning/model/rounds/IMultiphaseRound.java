/**
 * Created on 04.03.2009 in Project Tournament
 * Location: plans.rounds.IMultiphaseRound.java
 */
package planning.model.rounds;

/**
 * 
 * Interface um festzustellen, ob die Runde in mehrere Phasen unterteilt ist.
 * 
 * Es kann eine Pausenzeit zwischen den Runden festgelegt werden.
 * 
 * @author Daniel Wirtz
 *
 */
public interface IMultiphaseRound {
	/**
	 * Legt fest wie lange zwischen zwei Abschnitten der Runde gewartet werden soll.
	 * @param value
	 */
	public void setPauseMinutes(int value);
	
	/**
	 * 
	 * @return Pausenzeit zwischen den Phasen [Minuten]
	 */
	public int getPauseMinutes();
}
