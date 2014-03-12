package planning.control.mapping;

import java.util.List;

import planning.model.TeamSlot;

/**
 * Extrahiert die mapping-relevanten Methoden einer Gruppe,
 * um mißbrauch in den Mapper-Implementationen zu verhindern.
 * 
 * 
 * @author CreaByte
 *
 */
public interface IMappingGroup {

	/**
	 * 
	 * @return Liste der weiterkommenden TeamSlots
	 */
	public abstract List<TeamSlot> getProceedingSlots();

	/**
	 * Fügt der Gruppe einen TeamSlot hinzu. Wird für die
	 * Turnierplan-Erstellungsphase benötigt.
	 * 
	 * @param ts Hinzuzufügender TeamSlot
	 */
	public abstract void addTeamSlot(TeamSlot ts);

}