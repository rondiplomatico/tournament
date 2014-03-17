package planning.control.mapping;

import java.io.Serializable;
import java.util.List;

import planning.control.PlanningException;

/**
 * 
 * Stellt das Interface der Strategy-Methode map dar. Diese ermöglicht die
 * benutzerdefinierte Zuordnung der Gruppen einer vorigen Phase in die nächste.
 * 
 * Fügt man einen weiteren Mapper hinzu, so muss @see Mapping angepasst werden.
 * 
 * @author Daniel Wirtz
 * 
 */
public interface IGroupMapper extends Serializable {

	/**
	 * Abstrakte Funktion, die es erlaubt die weiterkommenden Slots aus einer
	 * Gruppe in die Gruppen der nächsten Phase einzusortieren.
	 * 
	 * Benutzt werden dabei die Methoden
	 * {@link IMappingGroup#addTeamSlot(planning.model.TeamSlot)} zum Hinzufügen
	 * eines Slots und {@link IMappingGroup#getProceedingSlots()} zum holen der
	 * Slots aus den Gruppen der alten Phase.
	 * 
	 * Die Anzahl der Gruppen darf in diesem Algorithmus nicht verändert werden.
	 * 
	 * @see IMappingGroup#addTeamSlot(planning.model.TeamSlot)
	 * @see IMappingGroup#getProceedingSlots()
	 * 
	 * @param source
	 * @param target
	 * 
	 */
	public void map(List<IMappingGroup> source, List<IMappingGroup> target) throws PlanningException;
}
