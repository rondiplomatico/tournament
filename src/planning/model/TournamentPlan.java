/**
 * Created on 10.03.2009 in Project Tournament
 * Location: planning.model.TournamentPlan.java
 */
package planning.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;

import model.Tournament;

import org.hibernate.annotations.IndexColumn;

import planning.model.rounds.IFinalRound;
import planning.model.rounds.Round;

/**
 * @author Daniel Wirtz
 */
@Entity
public class TournamentPlan implements Serializable {

	private static final long serialVersionUID = -5544505912447607139L;

	@SuppressWarnings("unused")
	@Id
	@GeneratedValue
	private int id;

	@IndexColumn(name = "roundNr")
	@OneToMany(cascade = CascadeType.ALL)
	private List<Round> rounds;

	// Es wird nur ein Plan pro Turnier persistiert
	@OneToOne
	private Tournament tournament;

	/**
	 * JPA-Erforderlicher Konstruktor
	 */
	protected TournamentPlan() {
		rounds = new ArrayList<Round>(5);
	}

	/**
	 * Erstellt einen neuen Plan
	 * 
	 * @param t
	 *            Turnier
	 */
	public TournamentPlan(Tournament t) {
		this();
		this.tournament = t;
	}

	/**
	 * Makes sure the groups are all sorted correctly after being loaded from
	 * the database
	 */
	@PostLoad
	private void postLoad() {
		for (Round r : rounds) {
			for (Phase p : r.getPhases()) {
				for (Group g : p.getGroups()) {
					Collections.sort(g.getSlots());
				}
			}
		}
	}

	/**
	 * 
	 * @return Runden des Plans
	 */
	public List<Round> getRounds() {
		return rounds;
	}

	/**
	 * Gibt an ob alle Begegnungen eines Turnierplans beendet wurden (also
	 * Ergebnisse eingetragen)
	 * 
	 * @return true, falls alle Runden abgeschlossen sind. False sonst
	 */
	public boolean allMatchesFinished() {
		for (Round r : rounds) {
			if (!r.isFinished())
				return false;
		}
		return true;
	}

	/**
	 * Stellt fest ob das Turnier eine IFinalRound enthält.
	 * 
	 * @see IFinalRound
	 * 
	 * @return true, falls eine Finalrunde existiert
	 */
	public boolean hasFinalRound() {
		for (Round r : rounds) {
			if (r instanceof IFinalRound) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Fügt eine Runde in ein Turnier ein.
	 * 
	 * Spezialfall: Es darf keine Runde mehr eingefügt werden, falls die letzte
	 * Eingefügte Runde eine KO-Runde ist, weil es nach dieser Runde das Turnier
	 * beendet ist
	 * 
	 * @param r
	 *            Runde
	 * 
	 * @pre Es wurde noch keine IFinalRound hinzugefügt.
	 */
	public void addRound(Round r) {
		assert (!hasFinalRound());
		r.plan = this;
		rounds.add(r);
	}

	/**
	 * @return the tournament
	 */
	public Tournament getTournament() {
		return tournament;
	}

	/**
	 * Returns a tournaments match list, sorted by scheduled time
	 * 
	 * @return
	 */
	public List<Match> getMatchList() {
		List<Match> res = new ArrayList<Match>();
		for (Round r : rounds) {
			res.addAll(r.getMatches());
		}
		Collections.sort(res);
		return res;
	}

	/**
	 * Schöne Darstellung
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Turnierplan für '" + tournament.getName() + "'";
	}

}
