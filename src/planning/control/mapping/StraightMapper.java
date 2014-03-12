package planning.control.mapping;

import java.util.ArrayList;
import java.util.List;

import planning.model.TeamSlot;

/**
 * Fügt alle Gruppenslots direkt in die gleiche Zielgruppe ein. Sonderfälle:
 * Gibt es mehr Quellgruppen als Zielgruppen, werden die restlichen Gruppen per
 * CrossMapper auf die Zielgruppen verteilt.
 * 
 * Sind Mehr Zielgruppen als Quellgruppen vorhanden, macht StraightMapping
 * keinen Sinn und es wird CrossMapping benutzt.
 * 
 * @author Daniel Wirtz
 * 
 */
public class StraightMapper implements IGroupMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5195149044132086801L;

	/**
	 * 
	 * @see planning.control.mapping.IGroupMapper#map(java.util.List,
	 *      java.util.List)
	 */
	@Override
	public void map(List<IMappingGroup> source, List<IMappingGroup> target) {
		if (source.size() >= target.size()) {
			int idx = 0;
			for (IMappingGroup g : target) {
				IMappingGroup sourceGroup = source.get(idx++);
				for (TeamSlot ts : sourceGroup.getProceedingSlots()) {
					g.addTeamSlot(ts);
				}
			}
			// Sind noch Quellgruppen übrig
			if (idx < target.size()) {
				List<IMappingGroup> leftovers = new ArrayList<IMappingGroup>(target.size()
						- idx);
				// Die restlichen sammeln und Crossmappen
				for (; idx < target.size(); idx++) {
					leftovers.add(source.get(idx));
				}
				new CrossMapper().map(leftovers, target);
			}
		} else {
			new CrossMapper().map(source, target);
		}

	}

}
