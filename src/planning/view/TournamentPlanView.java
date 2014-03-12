package planning.view;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.filechooser.FileFilter;

import model.User;
import planning.model.TournamentPlan;

/**
 *
 * @author Torben Zeine
 *
 * Fenster in dem der Spielplan eines Turniers in verschriedenen Modi angezeigt wird und
 * exportiert werden kann
 */
public class TournamentPlanView extends JFrame {
    
	private static final long serialVersionUID = -6475623976759307061L;
	
	//Komponentendefinition
	private JScrollPane viewPane;
    private TournamentPlanViewPanel tournViewPane;
    private TournamentPlan actTurnier; //Turnier für das der Spielplan angezeigt werden soll
    private TournamentPlanView tView = this; //Frameobjekt für Listener
    private static Dimension SIZE = new Dimension(1100, 700); //Größe des Frames, Standard
    private User actUser; //Aktuell eingelogter User
    private Point DragStartPoint, //Startpunkt des verschiebens durch Mausdragging
             ViewStartpoint; //Startpunkt des Viewports
    private JRadioButton teamButton; //Teamansicht
    private JRadioButton matchButton; //Begegnungsansicht

    /**
     * Konstruktor
     * Erstellt das JFrame mit Inhalt und zeigt zuerst die Matchansicht an
     *
     * @param t Turnier für dass der Turnierplan erstellt werden soll
     * @param u Aktuell eingeloggter User
     */
    public TournamentPlanView(TournamentPlan t, User u) {
        //Name an Fenster setzen
        super("Turnierplan: " + t.getTournament().getName());
        //Setzen Variablen
        this.actTurnier = t;
        this.actUser = u;
        //Inhalt des Frames erstellen -> Fensteransicht, Matchansicht
        getContentPane().add(createComponents(false, true));
        //Fenster schliessen
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        //Ändernungen der Größe des Fensters sind nicht möglich -> Manuell gezeichnet
        setResizable(false);
        //Festgelegte Größe setzen
        setPreferredSize(SIZE);
        setSize(getPreferredSize());
        //In der Mitte des Bildschirms positionieren
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);
        //Frame anzeigen
        setVisible(true);
    }

    /**
     * Erstellt den Inhalt des Frames aus 2 Paneln und positioniert die Komponenten
     *
     * @param fullscreen Vollbildmodus
     * @param matchansicht Ansicht -> Matchansicht = true, Teamansicht = false
     * @return JPanel Inhalt des Frames
     */
    private JPanel createComponents(boolean fullscreen, boolean matchansicht) {
        //Laout setzen
        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();
        //Größe des Panels
        Dimension dim = new Dimension(SIZE.width - 20, SIZE.height - 80);
        //Vollbildgröße setzen
        if (fullscreen) {
            dim = new Dimension(getSize().width - 20, getSize().height - 70);
        }
        //Aus 2 einzelnen Panels zusammenbauen
        content.add(createScrollPane(dim, matchansicht, fullscreen), layout);
        layout.gridy = 1;
        content.add(createButtonbar(fullscreen, matchansicht), layout);
        content.updateUI();
        return content;
    }

    /**
     * Erstellt die Buttonleiste an der unteren Seite des Frames
     *
     * @param fullscreen Vollbildmodus
     * @param matchansicht Ansicht -> Matchansicht = true, Teamansicht = false
     * @return JPanel Die Buttonleiste
     */
    private JPanel createButtonbar(boolean fullscreen, boolean matchansicht) {
        //Komponenten erzeugen und Laut setzen
        JPanel tmp = new JPanel(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();
        final JCheckBox fullscreenCheck = new JCheckBox("Vollbild");
        ButtonGroup viewGroup = new ButtonGroup();
        JButton closeButton = new JButton("Schliessen");
        teamButton = new JRadioButton("Ranglistenansicht");
        matchButton = new JRadioButton("Begegnungsansicht");
        JButton exportButton = new JButton("Export");
        //ButtonGroup -> Nur ein radiobutton auswählbar
        viewGroup.add(matchButton);
        viewGroup.add(teamButton);
        //Vollbildmodus?
        fullscreenCheck.setSelected(fullscreen);
        //Gewählte Ansicht
        if (matchansicht) {
            matchButton.setSelected(true);
        } else {
            teamButton.setSelected(true);
        }
        //Kurzbefehle
        teamButton.setMnemonic(KeyEvent.VK_R);
        matchButton.setMnemonic(KeyEvent.VK_B);
        //Teamsansicht erstellen
        teamButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Teamansicht erstellen und anzeigen
                tournViewPane = new TournamentPlanViewPanel(tView, actTurnier, false, actUser);
                viewPane.getViewport().setView(new JScrollPane(tournViewPane).getViewport().getView());
                viewPane.getViewport().setViewPosition(new Point((viewPane.getViewport().getViewSize().width / 2) - (viewPane.getSize().width / 2), 0));
            }
        });
        //Matchansicht erstellen
        matchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Matchansicht erstellen und anzeigen
                tournViewPane = new TournamentPlanViewPanel(tView, actTurnier, true, actUser);
                viewPane.getViewport().setView(new JScrollPane(tournViewPane).getViewport().getView());
                viewPane.getViewport().setViewPosition(new Point((viewPane.getViewport().getViewSize().width / 2) - (viewPane.getSize().width / 2), 0));
            }
        });

        //Schliessen Button klicken
        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Fenster schliessen
                dispose();
            }
        });
        //FullscrennCheckbox geklickt
        fullscreenCheck.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Graphicdevice des aktuell verwendeten Rechners
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                //Fullscreenmodus beenden
                if (!fullscreenCheck.isSelected()) {
                    //Ansicht speichern
                    boolean ansicht = matchButton.isSelected();
                    //kein Fullscreenwindow
                    gd.setFullScreenWindow(null);
                    //Komponenten entfernen und verkleinert neu hinzufügen
                    getContentPane().removeAll();
                    getContentPane().add(createComponents(false, ansicht));
                } //Fullscreenmodus aktivieren
                else {
                    //Ansicht speichern
                    boolean ansicht = matchButton.isSelected();
                    //Fenster auf Vollbild anzeigen
                    gd.setFullScreenWindow(tView);
                    //tView.enableInputMethods(true);
                    //Komponenten entfernen und vergrößert neu hinzufügen
                    getContentPane().removeAll();
                    getContentPane().add(createComponents(true, ansicht));
                }
            }
        });
        //Export Button gedrückt
        exportButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Filechooser anzeigen
                JFileChooser fc = new JFileChooser("Turnierplan speichern");
                fc.setMultiSelectionEnabled(false);
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                //Filefilter, sodass nur JPG Dateien gespeichert werden können
                fc.addChoosableFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        //verzeichnisse und jpg Dateien dürfen ausgewählt werden
                        return f.isDirectory() || f.getName().toLowerCase().endsWith(".jpg");
                    }

                    @Override
                    public String getDescription() {
                        return "JPG Bild Datei";
                    }
                });
                //Dialog anzeigen
                int ret = fc.showSaveDialog(tView);
                //Speichern bestätigt
                if (ret == JFileChooser.APPROVE_OPTION) {
                    //An Dateinamen .jpg anhängen, falls dies nicht schon geschehen ist
                    String path = fc.getSelectedFile().getPath();
                    if (!path.toLowerCase().endsWith(".jpg")) {
                        path = path + ".jpg";
                        //Datei erstellen
                        export(path);
                    }
                }
            }
        });
        //Komponenten in Panel hinzufügen
        tmp.add(matchButton, layout);
        layout.gridx = 1;
        tmp.add(teamButton, layout);
        //Abstand je nach aktueller Größe des Fensters setzen
        layout.insets = new Insets(0, getSize().width - 600, 0, 10);
        if (getSize().width == 0) {
            layout.insets = new Insets(0, 500, 0, 10);
        }
        layout.gridx = 2;
        tmp.add(fullscreenCheck, layout);
        layout.insets = new Insets(0, 0, 0, 10);
        layout.gridx = 3;
        tmp.add(exportButton, layout);
        layout.gridx = 4;
        tmp.add(closeButton, layout);
        tmp.updateUI();
        return tmp;
    }

    /**
     * Export den angezeigten Turnierplan in eine jpg Bilddatei und überschreibt sie falls schon vorhanden
     *
     * @param f Den Dateipfand inkl Dateiname
     */
    public void export(String f) {
        try {
            Component pComp = tournViewPane;
            BufferedImage bim = new BufferedImage(pComp.getSize().width, pComp.getSize().height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bim.createGraphics();
            pComp.paint(g2d);
            FileOutputStream fout = new FileOutputStream(f);
            ImageIO.write(bim, "jpg", fout);
            fout.close();
            Desktop.getDesktop().open(new File(f));

        } //Exceptions treten nicht auf
        catch (IOException ex) {
        }
    }

    /**
     * Erstellt das Scrollpanel in dem der Turnierplan angezeigt wird
     *
     * @param Dimension Größe des Panels
     * @param match Ansicht -> Matchansicht = true, Teamansicht = false
     * @param fullscreen Vollbildansicht
     * @return JPanel Das Panel inkl der Komponenten
     */
    private JPanel createScrollPane(Dimension d, boolean match, boolean fullscreen) {
        //Variabel initialisieren
        JPanel scroll = new JPanel();
        tournViewPane = new TournamentPlanViewPanel(this, actTurnier, match, actUser);
        viewPane = new JScrollPane(tournViewPane);
        //Eigenschaften des Scrollpanels
        //Scrollbalken, nur wenn sie gebraucht werden
        viewPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        viewPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //Größe setzen
        viewPane.setPreferredSize(d);
        //Scollgeschwindigkeit
        viewPane.getVerticalScrollBar().setUnitIncrement(15);
        viewPane.getVerticalScrollBar().setBlockIncrement(15);
        viewPane.getHorizontalScrollBar().setUnitIncrement(15);
        viewPane.getHorizontalScrollBar().setBlockIncrement(15);
        viewPane.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
        //Verschieben durch Maus
        viewPane.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //Startpunkt setzen
                DragStartPoint = e.getPoint();
                //Aktuell angezeigter Bereich
                ViewStartpoint = viewPane.getViewport().getViewPosition();
                //Handcursor
                viewPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //Normaler Cursor nach dem loslassen
                viewPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        //Auf Mausbewegungen reagieren
        viewPane.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                //Viewport verschiebung berechnen
                Point tP = e.getPoint();
                int tX = ViewStartpoint.x + (DragStartPoint.x - tP.x);
                int tY = ViewStartpoint.y + (DragStartPoint.y - tP.y);
                //Viewport verschieben
                if (tX > (0-viewPane.getViewport().getSize().width/2) && tY > (0-viewPane.getViewport().getSize().height/2) && tX < (tournViewPane.getSize().width-(viewPane.getViewport().getSize().width/2)) && tY < (tournViewPane.getSize().height-(viewPane.getViewport().getSize().height/2))) {
                    viewPane.getViewport().setViewPosition(new Point(tX, tY));
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        //Im Vollbildmodus keine Rand zum Frame
        if (!fullscreen) {
            viewPane.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        }
        //Komponente hinzufügen
        scroll.add(viewPane);
        scroll.updateUI();
        return scroll;
    }

    /**
     * Setzt den aktuellen eingeloggten User neu
     *
     * @param u Eingloggten User
     */
    public void setUser(User u) {
        this.actUser = u;

    }

    /**
     * Aktualisiert Matchpanel nach Eingabe von Ergebnisse
     */
    public void reloadMatchPanel() {
        //Aktuelle Viewposition speichtern
        Point aktview = viewPane.getViewport().getViewPosition();
        //Aktualisieren der Ansicht
        tournViewPane = new TournamentPlanViewPanel(tView, actTurnier, true, actUser);
        viewPane.getViewport().setView(new JScrollPane(tournViewPane).getViewport().getView());
        //Viewposition wiederherstellen
        viewPane.getViewport().setViewPosition(aktview);
    }
}

