package planning.control.mapping;

import java.util.List;

import planning.model.TeamSlot;

/**
 * 
 * Verteilt die Slots für die weiterkommenden Teams gleichmäßig auf alle neuen
 * Gruppen.
 * 
 * 29.08.2017
 * 
 * @author Daniel Wirtz
 * 
 */
public class HalfMapper implements IGroupMapper {

	private static final long serialVersionUID = 2677103429168154257L;

	/**
	 * @see planning.control.mapping.IGroupMapper#map(java.util.List,
	 *      java.util.List)
	 */
	@Override
	public void map(List<IMappingGroup> source, List<IMappingGroup> target) {
		int sidx = 0;
		for (IMappingGroup g : source) {
			for (int i = 0; i < g.getProceedingSlots().size(); i++) {
				int pos = sidx % 2 == 0 ? g.getProceedingSlots().size() - 1 - i
						: i;
				TeamSlot ts = g.getProceedingSlots().get(pos);
				target.get(pos / target.size()).addTeamSlot(ts);
			}
			sidx++;
		}
	}
}
