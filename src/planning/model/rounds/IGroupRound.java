/**
 * Created on 27.02.2009 in Project Tournament
 * Location: rounds.IContinuableRound.java
 */
package planning.model.rounds;

import java.util.List;

import planning.model.Group;
import planning.model.Transition;

/**
 * 
 * Stellt eine Gruppenrunde dar. Diese Eigenschaft wird benötigt, falls man per
 * Mapper verschiedene Gruppenrunden hintereinanderschalten möchte.
 * 
 * 27.02.2009
 * 
 * @author Daniel Wirtz Class IContinuableRound
 * 
 */
public interface IGroupRound {

	/**
	 * @return Die Gruppen der Runde
	 */
	public List<Group> getGroups();
	
	/**
	 * Setzt die Anzahl der Gruppen
	 * (Gültig VOR Aufruf von {@link Round#build(planning.control.PlanningManager, IGroupRound)})
	 * @param value
	 */
	public void setNumGroups(int value);
	
	/**
	 * @return Gruppenanzahl
	 */
	public int getNumGroups();
	
	/**
	 * Gibt die OutTransition dieser Runde an, falls gesetzt.
	 * Sonst kann null zurückgegeben werden.
	 * @return Übergang aus Runde heraus
	 */
	public Transition getOutTransition();
	
	/**
	 * Für jede Runde, nach der noch eine weitere Folgen soll,
	 * muss die OutTransition gesetzt werden, anhand derer sie feststellen
	 * kann welche/wie viele TeamSlots benötigt werden und wie die Punkte berechnet werden.
	 * 
	 * @param value
	 */
	public void setOutTransition(Transition value);

}
