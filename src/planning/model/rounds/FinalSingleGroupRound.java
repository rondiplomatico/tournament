/**
 * Created on 27.02.2009 in Project Tournament
 * Location: plans.rounds.FinalSingleGroupRound.java
 */
package planning.model.rounds;

import java.util.List;

import javax.persistence.Entity;

import model.Tournament;
import planning.control.PlanningException;
import planning.control.PlanningManager;
import planning.model.Phase;
import planning.model.TeamSlot;

/**
 * Repräsentiert eine finale Runde, in der es nur noch eine Gruppe gibt deren
 * Ranking den Turnierausgang festlegt.
 * 
 * @author Daniel Wirtz Class FinalSingleGroupRound
 * 
 */
@Entity
public class FinalSingleGroupRound extends Round implements IFinalRound {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3083455588618532905L;

    /** Nach JPA-Spezifikation Standard-Konstruktor ohne Parameter */
    protected FinalSingleGroupRound() {
        super();
    }
    
    /**
     * Nur hier weil der JPA-Standardkonstruktor implementiert werden muss.
     * @param t
     * @param name
     */
    public FinalSingleGroupRound(Tournament t, String name) {
    	super(t,name);
    }

	/**
	 * Die einfache Finalgruppenrunde hat genau das Ranking gemäß des Gruppenrankings.
	 * 
	 * @see planning.model.rounds.IFinalRound#getFinalRanking()
	 */
	@Override
	public List<TeamSlot> getFinalRanking() {
		return phases.get(0).getGroups().get(0).getSlots();
	}

	/**
	 * Erstellt die Runde.
	 * 
	 * Es gibt eine Phase mit einer Gruppe.
	 * @throws PlanningException 
	 * 
	 * @see planning.model.rounds.Round#build(planning.control.PlanningManager, planning.model.rounds.IGroupRound)
	 */
	@Override
	public void build(PlanningManager pm, IGroupRound round) throws PlanningException {
		phases.clear();
		Phase p = new Phase(this, 1, getName());
		phases.add(p);
		p.setInTransition(inTransition);
		pm.buildPhase(p, round.getGroups());
	}

}
