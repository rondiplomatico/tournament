package planning.control.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import planning.model.TeamSlot;

/**
 * Verteilt die Teams zufällig aber gleichmäßig auf die Zielgruppen.
 * 
 * @author Daniel Wirtz Class RandomMapper
 * 
 */
public class RandomMapper implements IGroupMapper {

	private static final long serialVersionUID = -5569410555752285031L;

	/**
	 * @see planning.control.mapping.IGroupMapper#map(java.util.List,
	 *      java.util.List)
	 */
	@Override
	public void map(List<IMappingGroup> source, List<IMappingGroup> target) {
		List<TeamSlot> all = new ArrayList<TeamSlot>();
		// Alle Slots sammeln
		for (IMappingGroup g : source) {
			all.addAll(g.getProceedingSlots());
		}

		// Kräftig schütteln
		Collections.shuffle(all);

		// Gleichmäßig auf Gruppen verteilen
		int groupIdx = 0;
		for (TeamSlot ts : all) {
			target.get(groupIdx++).addTeamSlot(ts);
			groupIdx %= target.size();
		}
	}

}
