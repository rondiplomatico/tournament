/**
 * 
 */
package planning.control.mapping;

import java.util.List;

import planning.control.PlanningException;

/**
 * @author CreaByte
 * 
 */
public class TwoToPairwiseMapper implements IGroupMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3689459510625251223L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see planning.control.mapping.IGroupMapper#map(java.util.List,
	 * java.util.List)
	 */
	@Override
	public void map(List<IMappingGroup> source, List<IMappingGroup> target)
			throws PlanningException {
		if (source.size() != 2) {
			throw new PlanningException("Mapping must be from two groups");
		}
		if (target.size() != source.get(0).getProceedingSlots().size()) {
			throw new PlanningException(
					"Must have equal number of target groups and proceeding teams.");
		}
		for (int k = 0; k < source.get(0).getProceedingSlots().size(); k++) {
			// Step 1: Map first group's proceedants to each a separate group
			target.get(k)
					.addTeamSlot(source.get(0).getProceedingSlots().get(k));
			// Step 2: Take the 1st against the 3rd, 2nd against the 4th etc by
			// shifting the proceeding slots by 2 and running modulo
			int idx = (k + 2) % source.get(0).getProceedingSlots().size();
			target.get(k).addTeamSlot(
					source.get(1).getProceedingSlots().get(idx));
		}

	}

}
