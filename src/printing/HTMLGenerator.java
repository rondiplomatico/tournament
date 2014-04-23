package printing;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import planning.model.Match;
import planning.model.TournamentPlan;
import planning.model.rounds.Round;

public class HTMLGenerator {

	public void genAndOpen(TournamentPlan t) {
		String plan = "<html>"
				+ "<meta charset=\"utf-8\" />"
				+ "<head><link type=\"text/css\" rel=\"stylesheet\" href=\"cup.css\"/></head>"
				+ "<body>";
		plan += generate(t);
		plan += "</body></html>";
		FileWriter f;
		File file = new File("plan.html");
		try {
			f = new FileWriter(file);
			f.write(plan);
			f.close();
			Desktop.getDesktop().browse(file.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String generate(TournamentPlan t) {
		String res = "";
		for (Round r : t.getRounds()) {
			res += "<h1>Runde '" + r.getName() + "':</h1>\n";
			res += getMatchTable(r.getMatches());
		}
		return res;
	}

	private String getMatchTable(List<Match> matches) {
		String res = "<table>"
				+ "<th>Zeit</th><th>Heim</th><th>Gast</th><th>Schiri</th><th>Gruppe</th><th>Ergebnis</th>\n";
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
		res += "<td>" + m.getScoreString() + "</td>";
		return res + "</tr>\n";
	}

}
