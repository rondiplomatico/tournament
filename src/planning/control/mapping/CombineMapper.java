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
	
	int numComb = 2;

	public CombineMapper() {
		this(2);
	}

	public CombineMapper(int num) {
		numComb = num;
	}

	@Override
	public void map(List<IMappingGroup> source, List<IMappingGroup> target)
			throws PlanningException {
		if (source.size() % numComb != 0
				&& source.size() / numComb != target.size()) {
			throw new PlanningException("Cannot combine " + source.size()
					+ " groups by " + numComb + " into " + target.size()
					+ " groups!");
		}
		for (int k = 0; k < target.size(); k++) {
			for (int i = 0; i < numComb; i++) {
				for (TeamSlot ts : source.get(k * numComb + i)
						.getProceedingSlots()) {
					target.get(k).addTeamSlot(ts);
				}
			}
		}
	}

}
