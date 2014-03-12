/**
 * Created on 26.02.2009 in Project Tournament
 * Location: plans.GroupRound.java
 */
package planning.model.rounds;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import model.Tournament;
import planning.control.PlanningManager;
import planning.model.Group;
import planning.model.Phase;
import planning.model.Transition;

/**
 * Stellt eine einfache Gruppenrunde dar.
 * 
 * @author Daniel Wirtz
 * 
 */
@Entity
public class StandardGroupRound extends Round implements IGroupRound {

	private static final long serialVersionUID = -6789922562888521936L;
	
    /**
     *
     */
    protected int numGroups;
	
    /**
     *
     */
    @OneToOne(cascade=CascadeType.ALL)
	protected Transition outTransition=null;

	/** Nach JPA-Spezifikation Standard-Konstruktor ohne Parameter */
	protected StandardGroupRound() {
	}

	/**
	 * Bei einer Gruppenrunde gibt es nur eine Phase.
	 * @param t Turnier
	 * @param name Name
	 * 
	 */
	public StandardGroupRound(Tournament t, String name) {
		super(t, name);
	}

	/**
	 * 
	 * @see planning.model.rounds.IGroupRound#setNumGroups(int)
	 */
	@Override
	public void setNumGroups(int value) {
		numGroups = value;
	}
	
	/**
	 * 
	 * @see planning.model.rounds.IGroupRound#getNumGroups()
	 */
	public int getNumGroups() {
		return numGroups;
	}

	/**
	 * Erstellt die GruppenRunde. Sie enthält nur eine Phase.
	 * 
	 * @see planning.model.rounds.Round#build(PlanningManager, IGroupRound)
	 */
	@Override
	public void build(PlanningManager pm, IGroupRound round) {
		phases.clear();
		Phase p = new Phase(this, numGroups, getName());
		phases.add(p);
		p.setInTransition(inTransition);
		p.setOutTransition(outTransition);
		pm.buildPhase(p, round.getGroups());
	}

	/**
	 * Weiterleiten auf die einzige Phase
	 * 
     * @param trans
     * @see planning.model.rounds.IGroupRound#setOutTransition(planning.model.Transition)
	 */
	@Override
	public void setOutTransition(Transition trans) {
		outTransition = trans;
	}

	/**
	 * Weiterleiten auf die einzige Phase
	 * 
	 * @see planning.model.rounds.IGroupRound#getOutTransition()
	 */
	@Override
	public Transition getOutTransition() {
		return outTransition;
	}

	/**
	 * 
	 * Weiterleiten auf die einzige Phase
	 * 
	 * @see planning.model.rounds.IGroupRound#getGroups()
	 */
	@Override
	public List<Group> getGroups() {
		return phases.get(0).getGroups();
	}
}
