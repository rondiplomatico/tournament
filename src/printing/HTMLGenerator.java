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

import planning.model.Match;
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
	
	public String getRanking(TournamentPlan t) {
		return getHead() + getRankingHTML(t) + getFoot();
	}

	private String getHead() {
		return "<html>"
				+ "<meta charset=\"utf-8\" />"
				+ "<head><link type=\"text/css\" rel=\"stylesheet\" href=\"cup.css\"/></head>"
				+ "<body>";
	}

	private String getFoot() {
		return "</body></html>";
	}

	private String getPlanHTML(TournamentPlan t) {
		String res = "";
		for (Round r : t.getRounds()) {
			res += "<h1>Runde '" + r.getName() + "':</h1>\n";
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
		
		List<TeamSlot> hlp = new ArrayList<TeamSlot>(r.getPhases().get(0).getGroups().get(0).getSlots());
		hlp.remove(0); hlp.remove(0);
		all.addAll(hlp);
		hlp = new ArrayList<TeamSlot>(r.getPhases().get(0).getGroups().get(1).getSlots());
		hlp.remove(0); hlp.remove(0);
		all.addAll(hlp);
		Collections.sort(all);
		
		// Finalrunde
		List<Match> m = t.getRounds().get(3).getMatches();
		all.add(0, m.get(2).getLoser()); // vierter
		all.add(0, m.get(2).getWinner()); // dritter
		all.add(0, m.get(3).getLoser()); // zweiter
		all.add(0, m.get(3).getWinner()); // gewinner

		String res = "<h1>Finale Platzierung Schwabencup</h1><table><tr>"
				+ "<th>Platz</th><th>Mannschaft</th><th>Punkte</th><th>Tore</th></tr>\n";
		for (int k = 0; k < all.size(); k++) {
			TeamSlot s = all.get(k);
			res += "<tr><td>" + (k + 1) + "</td><td>" + s.getName() + "</td><td>"
					+ s.Score.getPointsStr() + "</td><td>"
					+ s.Score.getGoalStr() + "</td></tr>";
		}
		return res + "</table>";
	}

}
