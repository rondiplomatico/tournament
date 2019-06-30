/**
 * 
 */
package planning.control.mapping;

import java.util.List;

import planning.control.PlanningException;
import planning.model.TeamSlot;

/**
 * @author CreaByte
 * 
 */
public class CombineMapper implements IGroupMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1887512588298153157L;

	@Override
	public void map(List<IMappingGroup> source, List<IMappingGroup> target) throws PlanningException {
		if (target.size() > 1) {
			throw new PlanningException("Cannot combine into more than one group.");
		}
		for (int k = 0; k < source.size(); k++) {
			for (TeamSlot ts : source.get(k).getProceedingSlots()) {
				target.get(0).addTeamSlot(ts);
			}
		}
	}

}
