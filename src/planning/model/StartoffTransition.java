/**
 * Created on 04.03.2009 in Project Tournament
 * Location: plans.transitions.InsertTransition.java
 */
package planning.model;

import java.util.List;

import javax.persistence.Entity;

import planning.control.mapping.IMappingGroup;
import planning.control.mapping.Mapping;
import planning.control.score.ScoreTransfer;

/**
 * 04.03.2009
 * 
 * Nur für den Einstiegspunkt in eine Turnierplanung. KOPIERT die Slots aus der
 * einzigen Source-Gruppe gleichverteilt in die Zielgruppen.
 * 
 * @author Daniel Wirtz
 * 
 */
@Entity
public class StartoffTransition extends Transition {

	private static final long serialVersionUID = -4952586352731048113L;

	/**
	 * Übergang für den Anfang eines Turnierplans
	 */
	public StartoffTransition() {
		super(Integer.MAX_VALUE, ScoreTransfer.NoScores, Mapping.CrossMapper, 0);
	}

	/**
	 * Bei der Startoff-Transition müssen die Slots einfach kopiert werden.
	 * @see planning.model.Transition#map(List, List)
	 */
	@Override
	public void map(List<IMappingGroup> source, List<IMappingGroup> target) {
		int idx = 0; // Sicherer Cast an dieser Stelle
		for (TeamSlot ts : ((Group) source.get(0)).getSlots()) {
			target.get(idx++).addTeamSlot(ts);
			idx %= target.size();
		}
	}
}
