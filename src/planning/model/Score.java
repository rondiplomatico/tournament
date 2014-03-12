/**
 * Created on 27.02.2009 in Project Tournament
 * Location: plans.Score.java
 */
package planning.model;

import java.io.Serializable;

/**
 * Enthält die Punkte und Tore einer Mannschaft.
 *
 * @author Daniel Wirtz
 * 
 */
public class Score implements Serializable {

	private static final long serialVersionUID = 4910543484759209071L;

	private int goals_plus, goals_minus;
	private int points_plus, points_minus;

	/**
	 * Erzeugt eine Score mit 0-werten für alle Felder.
	 */
	public Score() {
		points_plus = points_minus = goals_plus = goals_minus = 0;
	}

	/**
	 * Bequemlichkeits-Konstruktor
	 * @param points_plus
	 * @param points_minus
	 * @param goals_plus
	 * @param goals_minus
	 */
	public Score(int points_plus, int points_minus, int goals_plus, int goals_minus) {
		this.points_plus = points_plus;
		this.goals_plus = goals_plus;
		this.points_minus = points_minus;
		this.goals_minus = goals_minus;
	}

	/**
	 * 
	 * @return Tore
	 */
	public int getGoals() {
		return goals_plus;
	}

	/**
	 * 
	 * @return Pluspunkte
	 */
	public int getPointsPlus() {
		return points_plus;
	}
	
	/**
	 * 
	 * @return Minuspunkte
	 */
	public int getPointsMinus() {
		return points_minus;
	}
	
	/**
	 * 
	 * @return Gegentore
	 */
	public int getCounterGoals() {
		return goals_minus;
	}

	/**
	 * @param goals_plus the goals_plus to set
	 */
	public void setGoalsPlus(int goals_plus) {
		this.goals_plus = goals_plus;
	}

	/**
	 * @param goals_minus the goals_minus to set
	 */
	public void setGoalsMinus(int goals_minus) {
		this.goals_minus = goals_minus;
	}

	/**
	 * @param points_plus the points_plus to set
	 */
	public void setPointsPlus(int points_plus) {
		this.points_plus = points_plus;
	}

	/**
	 * @param points_minus the points_minus to set
	 */
	public void setPointsMinus(int points_minus) {
		this.points_minus = points_minus;
	}

}
