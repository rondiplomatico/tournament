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
public class AlternatingCrossMapper implements IGroupMapper {

	private static final long serialVersionUID = 7066693550950808648L;

	/**
	 * @see planning.control.mapping.IGroupMapper#map(java.util.List,
	 *      java.util.List)
	 */
	@Override
	public void map(List<IMappingGroup> source, List<IMappingGroup> target) {
		int idx = 0;
		boolean reverse = false;
		for (IMappingGroup g : source) {
			for (int x = 0; x < g.getProceedingSlots().size(); x++) {
				TeamSlot ts = reverse ? g.getProceedingSlots().get(
						g.getProceedingSlots().size() - x - 1) : g
						.getProceedingSlots().get(x);
				target.get(idx++).addTeamSlot(ts);
				idx %= target.size();
			}
			reverse = !reverse;
		}
	}
}
