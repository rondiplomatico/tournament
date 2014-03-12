package planning.control.mapping;

import java.util.List;

import planning.model.TeamSlot;

/**
 * 
 * Verteilt die Slots für die weiterkommenden Teams gleichmäßig auf alle neuen
 * Gruppen.
 * 
 * 28.02.2009
 * 
 * @author Daniel Wirtz
 * 
 */
public class CrossMapper implements IGroupMapper {

	private static final long serialVersionUID = 7066693550950808648L;

	/**
	 * @see planning.control.mapping.IGroupMapper#map(java.util.List,
	 *      java.util.List)
	 */
	@Override
	public void map(List<IMappingGroup> source, List<IMappingGroup> target) {
		int idx = 0;
		for (IMappingGroup g : source) {
			for (TeamSlot ts : g.getProceedingSlots()) {
				target.get(idx++).addTeamSlot(ts);
				idx %= target.size();
			}
		}
	}
}
