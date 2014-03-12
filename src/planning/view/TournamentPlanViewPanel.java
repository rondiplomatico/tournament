package planning.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.User;
import model.enums.TournamentState;
import planning.control.ResultManager;
import planning.model.Group;
import planning.model.Phase;
import planning.model.TournamentPlan;
import planning.model.rounds.Round;

/**
 * 
 * @author Torben Zeine
 * 
 *         Panel das verschiedene Gruppen(Gruppen im Turnier) erhält und in den
 *         Ebenen darstellt.
 * 
 *         ---------------------------Runde------------------------------
 *         ------------------------Phase---------------------------------
 *         -------- -------- Gruppe Gruppe -------- --------
 *         ------------------------Phase--------------------------------
 *         -------- -------- Gruppe Gruppe -------- --------
 * 
 *         --------------------------Runde------------------------------
 *         .............................................
 * 
 */
public class TournamentPlanViewPanel extends JLayeredPane {

	private static final long serialVersionUID = 2226546579337265906L;

	// Höhe eines Gruppenlabels inkl Abstand zum Phasenlabels
	private static int H_GROUPLABEL = 40;
	// Breite des Labels einer Phase, inkl Abstand zu den eigentlichen
	private static int W_PHASELABEL = 40;
	// Minimalhöhe einer Phase
	private static int H_PHASE = 100;
	// Abstand zwischen einzelnen Gruppe
	private static int GROUPSPACE = 40;
	// Farben der Ebenen
	private static Color backFirst = Color.WHITE, backSecound = Color.ORANGE; // Farben
																				// der
																				// Ebenen
	// eingeloggter User
	private User actUser;
	// Panels in Ebenen -> Zwischenspeichern zur späteren Position setzen
	private List<List<Object>> ViewPanels = new ArrayList<List<Object>>();
	// Labels in Ebenen -> Zwischenspeichern zur späteren Position setzen
	private List<List<Object>> ViewLabels = new ArrayList<List<Object>>();
	// Phasen die im Teamview angezeigt werden sollen
	private List<Boolean> teamViewVisiblePhases = new ArrayList<Boolean>();
	// Aktuelles Turnier
	private TournamentPlan actTourn;
	// Fenster in dem sich das Panel befindet
	private TournamentPlanView actFrame;
	// Höhe dieses Panels -> Zwischenspeicher
	private int heigthPanel = 0;
	// Ansicht -> Matchview = true, Teamview = false
	private boolean viewType;

	private ResultManager rm;

	/**
	 * Konstruktor Erstellt das Panel und seine inneren Panels, zeichnet es und
	 * zeigt es an.
	 * 
	 * @param frame
	 *            Fenster in dem das Panel angezeigt wird.
	 * @param t
	 *            Das Turnier aus dem der Spiel erstellt werden soll
	 * @param match
	 *            Soll die Matchansicht angezeigt werden?
	 * @param u
	 *            aktuell eingeloggte User
	 * 
	 * @pre Turnier wurde korrekt erstellt und ist mit Daten gefüllt
	 */
	public TournamentPlanViewPanel(TournamentPlanView frame, TournamentPlan t,
			boolean match, User u) {

		// Objekt zuweisen
		this.viewType = match;
		this.actUser = u;
		this.actTourn = t;
		this.actFrame = frame;
		rm = new ResultManager(frame);
		// Status einer Phase
		boolean phaseStat = true;
		// Iteration über die Runden des Turniers
		for (Round actRound : t.getRounds()) {
			// Rundenlabel erstellen
			createRoundLabel(actRound.getName() + " (" + actRound.getGameTime()
					+ "min pro Spiel)");
			// Iteration über die Phasen
			for (Phase actPhase : actRound.getPhases()) {
				String name = actPhase.getName() + " ("
						+ actRound.getGameTime() + "min pro Spiel)";
				// Phasenlabels erstellen
				// Letzte runde
				if (actPhase.getOutTransition() != null) {
					createPhaseLabel(name, actPhase.getOutTransition()
							.getPauseMinutes());
				} // Sonstige Runden
				else {
					createPhaseLabel(name, actPhase
							.getInTransition().getPauseMinutes());
				}
				// Panels erstellen
				createPanel(match, actPhase, phaseStat);
				// Phasenstatus speichern
				teamViewVisiblePhases.add(phaseStat);
				// Aktuelle Phase ist beendet oder letzte -> nächste nicht
				// anzeigen bzw nicht editierbar
				if (!actPhase.isFinished()) {
					phaseStat = false;
				}
			}
			// Phasenstatus der letzen Phase speichern
			teamViewVisiblePhases.add(phaseStat);
		}
		// Größe errechnen und setzen
		int maxwidth = calcMaxWidth() + (2 * W_PHASELABEL);
		setPreferredSize(new Dimension(maxwidth, heigthPanel));
		setSize(getPreferredSize());
		// Label und Panels positionieren
		setLabelPositions();
		setPanelPositions(maxwidth);
		// Größe erneut sezten
		setPreferredSize(new Dimension(maxwidth, heigthPanel));
		setSize(getPreferredSize());

	}

	/**
	 * Erstellt ein Panel für die Darstellung einer Gruppe und setzt seinen
	 * Status
	 * 
	 * @param ansicht
	 *            Matchansicht anzeigen
	 * @param Listiterator
	 *            Gruppe die angezeigt werden soll
	 * @param stat
	 *            Aktivieren bzw anzeigen der Gruppe
	 * @param Transition
	 *            Übergang zwischen Phasen
	 */
	private void createPanel(boolean ansicht, Phase p, boolean stat) {
		// Arraylist der Gruppen einer Phase
		List<Object> tmp = new ArrayList<Object>();
		// Iteration über die Gruppen einer Phase
		for (Group actGroup : p.getGroups()) {
			// Matchansicht
			if (ansicht) {
				// Turnier ist abgeschlosen -> keine Änderung möglich (null als
				// user übergeben)
				User u = actTourn.getTournament().getState() == TournamentState.finished ? null
						: actUser;
				tmp.add(new MatchPanel(actGroup, rm, actTourn.getTournament()
						.getFieldNames(), u, actTourn.getTournament()
						.getReferees(), actTourn.getTournament().getLeaders(),
						stat));
				// Teamansicht
			} else {
				TeamPanel teamP;
				// Transition ist nicht leer -> Normale Runde
				if (p.getOutTransition() != null) {
					teamP = new TeamPanel(actGroup, p.getOutTransition()
							.getNumProceedantsPerGroup(), actFrame, true);
				} // Finale Phase -> nur der erste wird als weiter kommend
					// markiert
				else {
					teamP = new TeamPanel(actGroup, 1, actFrame, true);
				}
				// Soll die Phase angezeigt werden
				if (!stat) {
					teamP.setVisible(false);
				}
				// Panel hinzufügen
				tmp.add(teamP);
			}
		}
		// Gruppen in Liste einfügen
		ViewPanels.add(tmp);
	}

	/**
	 * 
	 * Positioniert die Panels in der Liste ViewPanels auf dem Panel
	 * 
	 * @param Breite
	 *            des Panels
	 */
	private void setPanelPositions(int width) {
		int y = 0, grouplabels = 0;
		// Iteration über die Ebenen
		for (int i = 0; i < ViewLabels.size(); i++) {
			// Gruppenlabel-> nur Abstand erhöhen
			if ((Boolean) ViewLabels.get(i).get(0)) {
				y += H_GROUPLABEL;
				grouplabels++;
			} // Phase anzeigen
			else {
				// Abstand
				y += GROUPSPACE;
				int panelNum = i - grouplabels;
				// Mittig positionieren
				int x = (width / 2)
						- ((Integer) ViewPanels.get(panelNum).get(
								ViewPanels.get(panelNum).size() - 1) / 2);
				// Iteration über die Gruppen der Phasen
				for (int j = 0; j < ViewPanels.get(panelNum).size() - 1; j++) {
					// Position der Panels setzen
					((JPanel) ViewPanels.get(panelNum).get(j))
							.setLocation(x, y);
					// Panel hinuzufügen
					add((JPanel) ViewPanels.get(panelNum).get(j));
					// in der Vordergrund verschieben
					moveToFront((JPanel) ViewPanels.get(panelNum).get(j));
					// Neue Horizontale Position setzen
					x += ((JPanel) ViewPanels.get(panelNum).get(j)).getSize().width
							+ GROUPSPACE;
				}
				// Minimale höhe der Phase unterschritten?
				int hei = calcPhaseHeight(ViewPanels.get(panelNum));
				if (hei < H_PHASE) {
					hei = H_PHASE;
				}
				y += hei;
			}
		}
	}

	/**
	 * Erstellt eine Label für den Namen einer Runde und fügt ihn in ViewLabels
	 * Der erste Wert von Viewlabels wird mit "true" markiert
	 * 
	 * @param String
	 *            Name der Runde
	 */
	private void createRoundLabel(String text) {
		JLabel roundname = new JLabel(text);
		// Schriftart setzen, Horizontale Orientierung
		roundname.setFont(new Font("Arial", Font.BOLD, 18));
		roundname.setHorizontalAlignment(SwingConstants.CENTER);
		List<Object> Tmp = new ArrayList<Object>();
		Tmp.add(new Boolean(true));
		Tmp.add(roundname);
		ViewLabels.add(Tmp);
	}

	/**
	 * Erstellt eine Label für den Namen einer Phase und fügt ihn in ViewLabels
	 * Der erste Wert von Viewlabels wird mit "false" markiert
	 * 
	 * @param Namen
	 *            der Phase
	 * @param Zeit
	 *            die zwischen den Runden bzw Phasen pausiert wird
	 */
	private void createPhaseLabel(String text, int pauseTime) {
		// Labels erzeugen
		JLabel phasename_left = new JLabel(text);
		JLabel phasename_right = new JLabel(text);
		JLabel pause = new JLabel(pauseTime + " Minuten Pause");
		// Schriften setzen
		phasename_left.setFont(new Font("Arial", Font.BOLD, 16));
		phasename_right.setFont(new Font("Arial", Font.BOLD, 16));
		// Horizontale Orientierung
		phasename_left.setHorizontalAlignment(SwingConstants.CENTER);
		phasename_right.setHorizontalAlignment(SwingConstants.CENTER);
		// Vertikales Label
		phasename_left.setUI(new VerticalLabelUI(false));
		phasename_right.setUI(new VerticalLabelUI(false));
		// In Liste einfügen
		List<Object> Tmp = new ArrayList<Object>();
		Tmp.add(new Boolean(false));
		Tmp.add(phasename_left);
		Tmp.add(phasename_right);
		Tmp.add(pause);
		ViewLabels.add(Tmp);
	}

	/**
	 * Setzt die Position der Labels aus ViewLabels Panel und erzeugt
	 * Hintergründe für die Gruppen und zeigt sie im Panel an
	 */
	private void setLabelPositions() {
		int x = 0, y = 0, gBeg = 0, changelabel = 0, colorBack = 0, groupLabels = 0;
		// Phasen sollen angezeigt werden
		boolean view = true;
		// Linien zwischen Phasen
		JLabel line = new JLabel();
		// Pausen zwischen Abschnitten
		JLabel pause = new JLabel();
		// Iteration über die Ebenen
		for (int i = 0; i < ViewLabels.size(); i++) {
			// Anzeigen, nur bei Teamasicht
			if (view) {
				// Gruppenlabel
				if ((Boolean) ViewLabels.get(i).get(0)) {
					// Nicht bei erstem Durchlauf ausführen
					if (i > 0) {
						// Bei jeder Iteration ausser der ersten ausführen
						// Hintergrund setzen
						JLabel background = new JLabel();
						background
								.setBounds(0, gBeg, getSize().width, y - gBeg);
						// Immer Zeichnen
						background.setOpaque(true);
						// Hintergrundfarbe
						if (colorBack % 2 == 0) {
							background.setBackground(backFirst);
						} else {
							background.setBackground(backSecound);
						}
						// Hinzufügen und in den Hintergrund
						add(background);
						moveToBack(background);
						// Pausenposition setzen
						pause.setLocation(pause.getLocation().x,
								pause.getLocation().y + H_GROUPLABEL);
						// Keine Linie zwischen Runden
						line.setVisible(false);
						gBeg = y;
					}
					// Größe,Position der Gruppenlabels setzen und hinzufügen
					((JLabel) ViewLabels.get(i).get(1)).setSize(
							getSize().width, H_GROUPLABEL);
					((JLabel) ViewLabels.get(i).get(1)).setLocation(x, y);
					add((JLabel) ViewLabels.get(i).get(1));
					// In den Fordergrund
					moveToFront((JLabel) ViewLabels.get(i).get(1));
					// Anzahl Gruppenlabels erhöhen, Farbe je Runde verändern
					groupLabels++;
					colorBack++;
					// Nur eine Phase -> GruppenLabel = Phasenlabel
					if (changelabel == 1) {
						((JLabel) ViewLabels.get(i - 1).get(1))
								.setVisible(false);
						((JLabel) ViewLabels.get(i - 1).get(2))
								.setVisible(false);
						((JLabel) ViewLabels.get(i - 2).get(1))
								.setText(((JLabel) ViewLabels.get(i - 1).get(1))
										.getText());
					}
					y += H_GROUPLABEL;
					changelabel = 0;
				}
				// Phasenlabel
				else {
					// Höhe berechnen und setzen
					int heigth = calcPhaseHeight(ViewPanels
							.get(i - groupLabels));
					if (heigth < H_PHASE) {
						heigth = H_PHASE;
					}
					// Position und Größe der Phasenlabels links und rechts
					// setzen
					((JLabel) ViewLabels.get(i).get(1)).setSize(W_PHASELABEL,
							heigth);// Links
					((JLabel) ViewLabels.get(i).get(1)).setLocation(x, y);
					((JLabel) ViewLabels.get(i).get(2)).setSize(W_PHASELABEL,
							heigth);// Rechts
					((JLabel) ViewLabels.get(i).get(2)).setLocation(
							getSize().width - W_PHASELABEL, y);
					// Labels hinzufügen und in den Vordergrund
					add((JLabel) ViewLabels.get(i).get(1));
					add((JLabel) ViewLabels.get(i).get(2));
					moveToFront((JLabel) ViewLabels.get(i).get(1));
					moveToFront((JLabel) ViewLabels.get(i).get(2));
					y += heigth + GROUPSPACE;
					// Anzahl Phasen in Gruppe
					changelabel++;
					// Linie zwischen Phasen erstellen, positionieren und
					// hinzufügen
					line = new JLabel();
					line.setOpaque(true);
					line.setBorder(BorderFactory.createLineBorder(Color.black));
					line.setBounds(W_PHASELABEL, y, getSize().width
							- (2 * W_PHASELABEL), 1);
					add(line);
					moveToFront(line);
					// Pause zwischen Abschnitten hinzufügen
					pause = (JLabel) ViewLabels.get(i).get(3);
					pause.setBounds(W_PHASELABEL, y, getSize().width
							- (2 * W_PHASELABEL), 20);
					pause.setHorizontalAlignment(SwingConstants.CENTER);
					add(pause);
				}
				// Teamansicht und letzte Phase
				if (!viewType && i > 0) {
					if (view) {
						// Ebene wurde noch angezeigt
						if (!(Boolean) teamViewVisiblePhases.get(i)) {
							// Nächsten Ebenen nicht anzeigen
							view = false;
						}
					}
				}
			}
		}
		// Letzter Hintergrund setzen und anzeigen
		JLabel background = new JLabel();
		background.setBounds(0, gBeg, getSize().width, y - gBeg);
		background.setOpaque(true); // Immer Zeichnen
		if (colorBack % 2 == 0) { // Hintergrundfarbe
			background.setBackground(backFirst);
		} else {
			background.setBackground(backSecound);
		}
		add(background);
		moveToBack(background);
		// Letzte Linie und Pause nicht anzeigen
		line.setVisible(false);
		pause.setVisible(false);
		heigthPanel = y;
	}

	/**
	 * Berechnet die maximale Breite des Panels in Pixel für die Darstellung der
	 * Gruppen
	 * 
	 * @return int Breite des Panels in Pixel ink Abstand zwischen den Gruppen
	 */
	private int calcMaxWidth() {
		int x = 0;
		for (int i = 0; i < ViewPanels.size(); i++) {
			int tmp = GROUPSPACE;
			for (int j = 0; j < ViewPanels.get(i).size(); j++) {
				tmp += ((JPanel) ViewPanels.get(i).get(j)).getSize().width
						+ GROUPSPACE;
			}
			if (tmp > x) {
				x = tmp;
			}
			ViewPanels.get(i).add(tmp);
		}
		return x;
	}

	/**
	 * Errechnet die Höhe einer Phase in einer Runde
	 * 
	 * @param ArrayList
	 *            der Gruppen in einer Runde
	 * @return int Höhe der Phase in Pixel
	 */
	private int calcPhaseHeight(List<Object> a) {
		int x = 0;
		for (int i = 0; i < a.size() - 1; i++) {
			int tmp = ((JPanel) a.get(i)).getSize().height + GROUPSPACE;
			if (tmp > x) {
				x = tmp;
			}
		}
		return x;
	}
}
