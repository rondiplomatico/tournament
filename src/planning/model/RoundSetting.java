/**
 * Created on 10.03.2009 in Project Tournament
 * Location: planning.model.TournamentPlanRound.java
 */
package planning.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import planning.model.rounds.RoundType;

/**
 * @author Daniel Wirtz
 * 
 */
@Entity
public class RoundSetting implements Serializable {

	private static final long serialVersionUID = 6707806489971745840L;

	@SuppressWarnings("unused")
	@Id
	@GeneratedValue
	private int id;

	private RoundType type;
	private String name;
	private String hint;

	// Aus IGroupRound
	private int numGroups = 1;

	// IMultiphaseRound
	private int pauseBetweenPhases = 0;

	private int gameTime = Integer.MIN_VALUE;
	protected boolean pairwiseMatching = false;
	protected String[] fields = null;

	// Übergang aus voriger Runde
	@OneToOne(cascade = CascadeType.ALL)
	private Transition inTransition;

	/**
	 * JPA-Benötigt
	 */
	protected RoundSetting() {
		inTransition = new Transition();
	}

	/**
	 * Verwendung in der GUI
	 * 
	 * @param name
	 * @param type
	 */
	public RoundSetting(String name, RoundType type) {
		this();
		this.name = name;
		this.type = type;
	}

	/**
	 * Verwendung in Testing.java
	 * 
	 * @param name
	 * @param type
	 * @param numGroups
	 * @param phasePause
	 * @param inTransition
	 */
	public RoundSetting(String name, RoundType type, int numGroups, int phasePause, Transition inTransition) {
		this(name, type, numGroups, phasePause, inTransition, Integer.MIN_VALUE);
	}

	/**
	 * Verwendung in Testing.java
	 * 
	 * @param name
	 * @param type
	 * @param numGroups
	 * @param phasePause
	 * @param inTransition
	 * @param gameTime
	 */
	public RoundSetting(String name, RoundType type, int numGroups, int phasePause, Transition inTransition,
			int gameTime) {
		this(name, type);
		this.numGroups = numGroups;
		this.pauseBetweenPhases = phasePause;
		this.inTransition = inTransition;
		this.gameTime = gameTime;
	}

	/**
	 * @return the type
	 */
	public RoundType getType() {
		return type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String value) {
		hint = value;
	}

	/**
	 * @return the numGroups
	 */
	public int getNumGroups() {
		return numGroups;
	}

	public int getGameTime() {
		return gameTime;
	}

	public boolean getPairwiseMatching() {
		return pairwiseMatching;
	}

	public void setPairwiseMatching(boolean value) {
		pairwiseMatching = value;
	}

	public void setUsableFields(String[] fields) {
		this.fields = fields;
	}

	public String[] getUsableFields() {
		return fields;
	}

	/**
	 * @return the pauseBetweenPhases
	 */
	public int getPauseBetweenPhases() {
		return pauseBetweenPhases;
	}

	/**
	 * @return the inTransition
	 */
	public Transition getInTransition() {
		return inTransition;
	}

	/**
	 * 
	 * @param value
	 */
	public void setInTransition(Transition value) {
		this.inTransition = value;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(RoundType type) {
		this.type = type;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param numGroups the numGroups to set
	 */
	public void setNumGroups(int numGroups) {
		this.numGroups = numGroups;
	}

	/**
	 * @param pauseBetweenPhases the pauseBetweenPhases to set
	 */
	public void setPauseBetweenPhases(int pauseBetweenPhases) {
		this.pauseBetweenPhases = pauseBetweenPhases;
	}

	/**
	 * Darstellung in Rundenliste (GUI)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name + " (" + type + ")";
	}
}
