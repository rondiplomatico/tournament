/**
 * Created on 27.02.2009 in Project Tournament
 * Location: model.PlaceholderTeam.java
 */
package planning.model;

import java.awt.Color;
import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import model.Team;
import planning.control.ResultManager;

/**
 * Diese Klasse ist das Kernelement eines Turnierplans. Es dient als Platzhalter
 * für ein späteres Team (@see TeamSlot#setTeam(Team, Score))
 * 
 * Für den TeamSlot werden die Begegnungen berechnet und geplant, und die
 * erreichten Punkte und Details der Mannschaft im Slot können abgefragt werden
 * 
 * @author Daniel Wirtz Class TeamSlot
 * 
 * @see TeamSlot#getStats()
 */
@Entity
public class TeamSlot implements Comparable<TeamSlot>, Serializable {

	private static final long serialVersionUID = -7650576583847368060L;

	@SuppressWarnings("unused")
	@Id
    @GeneratedValue
    private long id;

    @ManyToOne(cascade={CascadeType.PERSIST,CascadeType.REFRESH})
	private Team innerTeam = null;
    
	private Score initialScore = null;
	
	private Color color;

	private String preName = null;

    @ManyToOne(cascade=CascadeType.PERSIST)
	private Group containingGroup = null;
    
    // The score associated with the TeamSlot
    public Score Score;

    /** Nach JPA-Spezifikation Standard-Konstruktor ohne Parameter. */
    protected TeamSlot() {
    	Score = new Score();
    }

	/**
	 * Erzeugt einen neuen TeamSlot mit vorläufigem Namen.
	 * 
	 * a)
	 * anzahl 0,1,1+3,1+3+9 => d_n = \sum k=0 n k^3 		(n=0..)
	 * seitenlänge: s_n = (1/2)^n 		(n=1..)
	 * h_0 = sqrt(3/4) = sqrt(1^2 - (1/2)^2)
	 * höhe: h_n = h_0 * (1/2)^n = h_0 * s_n		(n=1..)
	 * flächeninhalt: a_n = .5 s_n * h_n = .5 * h_0 * s_n^2 = .5 * h_0 * (1/2)^2n
	 * 
	 * b) 
	 * \sum k=0 n k^3 * 3 * s_k = \sum_k=0^n k^3 * 3 * (1/2)^k
	 * 
	 * c)
	 * \sum k=0 n k^3 * a_n = \sum k=0 n k^3 * .5 * h_0 * (1/2)^2k
	 * 
	 * @param preliminaryName
	 * @param color
	 */
	public TeamSlot(String preliminaryName, Color color) {
		this();
		preName = preliminaryName;
		initialScore = new Score();
		this.color = color;
	}

	/**
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Gibt den Namen des Platzhaltes zurück.
	 * Ist schon ein Team vermerkt, wird der Teamname ausgegeben.
	 * 
	 * @return Name des TeamSlots
	 */
	public String getName() {
		if (innerTeam != null) return innerTeam.getName();
		return preName;
	}
	
	/**
	 * 
	 * @return Farbe des TeamSlots
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Diese Methode wird (von der Gruppe) aufgerufen, wenn das den Platzhalter
	 * ausfüllende Team feststeht.
	 * 
	 * @param team
	 * @param initialScore 
	 */
	public void setTeam(Team team, Score initialScore) {
		this.innerTeam = team;
		this.initialScore = initialScore;
		new ResultManager(null).updateStats(this);
	}
	
	/**
	 * 
	 * @return Anfangspunkte
	 */
	public Score getInitialScore() {
		return initialScore;
	}

	/**
	 * Gibt das innere Team zurück, falls schon eingetragen.<br>
	 * Null sonst.
	 * 
	 * @return Team
	 */
	public Team getTeam() {
		return innerTeam;
	}

	/**
	 * Legt die den Slot enthaltende Gruppe fest.
	 * 
	 * Designmäßig könnte die übergabe auch im Konstruktor geschehen; allerdings
	 * wäre so ein TeamSlot auf eine Gruppe beschränkt. Daher die Möglichkeit
	 * die Gruppe im Nachhinein zu ändern, um z.B. ein einfaches kopieren von
	 * TeamSlots in die nächste Runde zu ermöglichen, um Hin- und Rückrunden zu
	 * ermöglichen
	 * 
	 * @param group
	 */
	public void setGroup(Group group) {
		this.containingGroup = group;
	}

	/**
	 * 
	 * @return Enthaltende Gruppe
	 */
	public Group getGroup() {
		return this.containingGroup;
	}

	/**
	 * Vergleicht einen TeamSlot mit einem anderen. So können einfach Rankings
	 * erstellt werden.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TeamSlot o) {
		if (Score.points_plus == o.Score.points_plus) {
			/*
			 * Kann auch mit der Tordifferenz keine Entscheidung herbeigeführt
			 * werden, so entscheidet der (Pseudo-) Zufall.
			 */
			int diff = Score.goals_plus-Score.goals_minus;
			int diff_o = o.Score.goals_plus-o.Score.goals_minus;
			if (diff == diff_o) {
				// Ansonsten: Wer hat weniger gegentore
				return Score.goals_plus > o.Score.goals_plus ? -1 : 1;
			} else
				return diff > diff_o ? -1 : 1; 
		} else
			return Score.points_plus > o.Score.points_plus ? -1 : 1;
	}

}
