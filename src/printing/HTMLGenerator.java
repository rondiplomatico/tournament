package printing;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import planning.model.Group;
import planning.model.Match;
import planning.model.Phase;
import planning.model.TeamSlot;
import planning.model.TournamentPlan;
import planning.model.rounds.Round;

public class HTMLGenerator {

	public void writeAndOpen(String src, String filename) {
		FileWriter f;
		File file = new File(filename);
		try {
			f = new FileWriter(file);
			f.write(src);
			f.close();
			Desktop.getDesktop().browse(file.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getPlan(TournamentPlan t) {
		return getHead() + getPlanHTML(t) + getFoot();
	}

	public String getGroups(TournamentPlan t) {
		return getHead() + getGroupsHTML(t) + getFoot();
	}

	public String getRanking(TournamentPlan t) {
		return getHead() + getRankingHTML(t) + getFoot();
	}

	private String getHead() {
		return "<!DOCTYPE html><html>"
				+ "<head><meta charset=\"utf-8\" />"
				+ "<link type=\"text/css\" rel=\"stylesheet\" href=\"cup3.css\"/></head>"
				+ "<body>";
	}

	private String getFoot() {
		return "<br><br><br></body></html>";
	}

	private String getGroupsHTML(TournamentPlan t) {
		String res = "";
		for (Round r : t.getRounds()) {
			res += "<h1>" + r.getName() + "</h1>\n";
			for (Phase p : r.getPhases()) {
				if (r.getPhases().size() > 1) {
					res += "<h2>" + p.getName() + "</h2>\n";
				}
				res += "<table><tr>\n";
				int maxsize = 0;
				for (Group g : p.getGroups()) {
					String field = t.getTournament().getFieldNames()[g
							.getMatches().get(0).getField()];
					res += "<th>" + g.getLongName() + "  (Halle: " + field
							+ ")</th>\n";
					maxsize = Math.max(g.getSlots().size(), maxsize);
				}
				res += "</tr>\n";
				for (int pos = 0; pos < maxsize; pos++) {
					res += "<tr>\n";
					for (Group g : p.getGroups()) {
						String name = g.getSlots().size() > pos ? g.getSlots()
								.get(pos).getName() : "";
						res += "<td>" + name + "</td>\n";
					}
					res += "</tr>\n";
				}

				res += "</table>";
			}
		}
		return res;
	}

	private String getPlanHTML(TournamentPlan t) {
		String res = "";
		for (Round r : t.getRounds()) {
			String label = r.getHint();
			if (label == null) {
				label = r.getGameTime() + " Minuten pro Spiel";
				if (r.getTournament().getGamePause() > 0) {
					label += "(je " + r.getTournament().getGamePause()
							+ " Min. Pause)";
				}
			}
			res += "<h1>" + r.getName() + ": " + label + "</h1>\n";
			res += getMatchTable(r.getMatches());
		}
		return res;
	}

	private String getMatchTable(List<Match> matches) {
		String res = "<table><tr>"
				+ "<th>Zeit</th><th>Heim</th><th>Gast</th><th>Schiedsrichter</th><th>Gruppe</th><th>Ergebnis</th></tr>\n";
		for (Match m : matches) {
			res += getMatchHTML(m);
		}
		return res + "</table>";
	}

	private String getMatchHTML(Match m) {
		String res = "<tr>";
		DateFormat fmt = new SimpleDateFormat("dd.MM. H:mm");
		res += "<td>" + fmt.format(m.getStartTime()) + "</td>";
		res += "<td>" + m.getHomeTeam() + "</td>";
		res += "<td>" + m.getGuestTeam() + "</td>";
		res += "<td>" + (m.getReferee() != null ? m.getReferee() : "TBA")
				+ "</td>";
		res += "<td>" + m.getGroup().getLongName() + "</td>";
		String hlp = m.getScoreString();
		res += "<td>" + hlp.replace("-", "&nbsp;&nbsp;&nbsp;") + "</td>";
		return res + "</tr>\n";
	}

	private String getRankingHTML(TournamentPlan t) {
		Round r = t.getRounds().get(2);
		List<TeamSlot> all = new ArrayList<TeamSlot>();

		List<TeamSlot> hlp = new ArrayList<TeamSlot>(r.getPhases().get(0)
				.getGroups().get(0).getSlots());
		hlp.remove(0);
		hlp.remove(0);
		all.addAll(hlp);
		hlp = new ArrayList<TeamSlot>(r.getPhases().get(0).getGroups().get(1)
				.getSlots());
		hlp.remove(0);
		hlp.remove(0);
		all.addAll(hlp);
		Collections.sort(all);

		// Finalrunde
		List<Match> m = t.getRounds().get(3).getMatches();
		all.add(0, m.get(2).getLoser()); // vierter
		all.add(0, m.get(2).getWinner()); // dritter
		all.add(0, m.get(3).getLoser()); // zweiter
		all.add(0, m.get(3).getWinner()); // gewinner

		String res = "<h1>Finale Platzierung "
				+ t.getTournament().getName()
				+ "</h1><table><tr>"
				+ "<th>Platz</th><th>Mannschaft</th><th>Punkte</th><th>Tore</th></tr>\n";
		for (int k = 0; k < all.size(); k++) {
			TeamSlot s = all.get(k);
			res += "<tr><td>" + (k + 1) + "</td><td>" + s.getName()
					+ "</td><td>" + s.Score.getPointsStr() + "</td><td>"
					+ s.Score.getGoalStr() + "</td></tr>";
		}
		return res + "</table>";
	}

}
