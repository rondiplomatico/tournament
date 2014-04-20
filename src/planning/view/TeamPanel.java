package planning.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ListIterator;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.TitledBorder;

import planning.model.Group;
import planning.model.TeamSlot;
import planning.model.TeamStats;

/**
 * Erzeugt ein Panel mit Information der Mannschaften und in einer Gruppe
 *
 * @author Torben Zeine
 */
public class TeamPanel extends JPanel{

	private static final long serialVersionUID = -7457379484355027880L;
	
	private int victoryTeams = 0; //Anzahl der Teams die grün markiert werden
    private Group actGroup;//Aktuelle Gruppe
    /**
     *
     * Konstruktor um das Panel in einem eigenen Fenster anzuzeigen
     * ohne Mouselistener
     *
     * @param g Gruppe für die eine Rangliste angezeigt werden soll
     * @param victeams Anzahl der Teams die weiter kommen
     * @param frame Fenster in dem sich das Panel befindet
     */
    public TeamPanel(Group g,int victeams,JFrame frame){
        //layout setzen
        super(new GridBagLayout());
        //Objekte speichern
        actGroup = g;
        this.victoryTeams = victeams;
        //Kompontenten hinzufügen
        addComponents(true);
        //standardattibute des panels setzen
        setAttribs();
    }
     /**
     *
     * Konstruktor um das Panel in einem Panel anzuzeigen
     *
     * @param g Gruppe für die eine Rangliste angezeigt werden soll
     * @param victeams Anzahl der Teams die weiter kommen
     * @param window JFrame der das Panel enthält
     * @param view Vollbild
     */
    public TeamPanel(final Group g,int victeams, final JFrame window,boolean view){
        //layout setzen
        super(new GridBagLayout());
        actGroup = g;
        this.victoryTeams = victeams;
        //Komponenten hinzufügen -> QUICKVIEW
        addComponents(false);
        //standardattibute des panels setzen
        setAttribs();
        //Mouseclicklistener zur ermöglichen des rechtsklicks
        addMouseListener(new MouseListener(){ 
            @Override
            public void mouseClicked(MouseEvent e){
                //Rechte Maustaste gedrückt
                if(e.getButton() == MouseEvent.BUTTON3){
                        //Dialog erstellen
                        final JDialog tmp = new JDialog(window,actGroup.getLongName());
                        //Modular -> hinteres Fenster nicht auswählbar
                        tmp.setModal(true);
                        //Neues Teampanel in Dialog mit FULLVIEW
                        tmp.getContentPane().add(new TeamPanel(actGroup,victoryTeams,window));
                        tmp.addWindowListener(new WindowAdapter(){
                        @Override
                            public void windowClosing(WindowEvent e){
                                    //Dialog schliessen
                                    tmp.dispose();
                            }
                        });
                        //Größe nicht änderbar
                        tmp.setResizable(false); 
                        tmp.pack();
                        //Am Hauptfenster zentrieren
                        tmp.setLocation(window.getLocation().x + ((window.getSize().width/2) - (tmp.getSize().width/2)),
                                window.getLocation().y + ((window.getSize().height/2) - (tmp.getSize().height/2)));
                        //Anzeigen
                        tmp.setVisible(true);
                    }
                }
            @Override
            public void mouseEntered(MouseEvent e){}
            @Override
            public void mouseExited(MouseEvent e){}
            @Override
            public void mousePressed(MouseEvent e){}
            @Override
            public void mouseReleased(MouseEvent e){}
        });
    }
    /**
     * Setzt die standard Attribute des Panels
     */
    private void setAttribs(){
        setOpaque(true);
        setSize(getPreferredSize());
        setBackground(Color.WHITE);
    }

    /**
     * Fügt die Komponenten hinzu und zeigt sie an
     *
     * @param fullview Fullscreen-Modus Ja/Nein
     */
    private void addComponents(boolean fullview){
        //Layout manager
        GridBagConstraints gbLayout = new GridBagConstraints();
        gbLayout.fill = GridBagConstraints.HORIZONTAL;
        //Rahmen erstellen und setzen
        TitledBorder caption = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,2),actGroup.getLongName());
        caption.setTitleJustification(TitledBorder.CENTER); 
        setBorder(caption);
        //Titelleiste aus Teamstat Objekt auslesen
        //Quickview
        gbLayout.gridy = 0;
        gbLayout.ipadx = 20;
        gbLayout.insets = new Insets(0,20, 0, 0);
        for(int i = 0;i<TeamStats.QUICKVIEW;i++){
            gbLayout.gridx = i;
            JLabel tmp = new JLabel(TeamStats.getTitle(i));
            tmp.setHorizontalAlignment(TeamStats.getAlignment(i));
            add(tmp,gbLayout);
            gbLayout.insets = new Insets(0, 0, 0, 0);
        }
        //FULLVIEW
        if(fullview){
            for(int i = TeamStats.QUICKVIEW;i<TeamStats.ENTRIES;i++){
                gbLayout.gridx = i;
                JLabel tmp = new JLabel(TeamStats.getTitle(i));
                tmp.setHorizontalAlignment(TeamStats.getAlignment(i));
                add(tmp,gbLayout);
            }
        }
        //Trennung zweischen Überschrift und Datenansicht
        gbLayout.gridy = 1;
        gbLayout.gridx = 0;
        gbLayout.gridwidth = TeamStats.ENTRIES;
        gbLayout.ipady = 5;
        add(new JSeparator(),gbLayout);
        //Iterator für die einzelnen Slots
        ListIterator<TeamSlot> lSlots = actGroup.getSlots().listIterator();
        //Zählervariable
        int a = 0;
        //Iteration über die Slots einer Gruppe
        while(lSlots.hasNext()){
            //Aktueller Stat
            TeamStats actStats = new TeamStats(lSlots.next()); 
            //Standard Farbe&Stil der Schrift der Reihe
            Font lineFont = new Font("Verdana", Font.PLAIN, 12);
            Color lineColor = Color.RED;
            //Siegerteam
            if(a<this.victoryTeams){
                //Reihe als Sieger markieren
                lineFont = new Font("Verdana", Font.PLAIN, 14);
                lineColor= Color.GREEN;
            }
            gbLayout.ipadx = 20;
            gbLayout.gridy = a+2;
            gbLayout.gridwidth = 1;
            gbLayout.insets = new Insets(0, 20, 0, 0);
            //QUICKVIEW Spalten hinzufügen
            for(int i = 0;i<TeamStats.QUICKVIEW;i++){
                JLabel tmp = new JLabel(actStats.getContent(i));
                tmp.setHorizontalAlignment(TeamStats.getAlignment(i));
                tmp.setForeground(lineColor);
                tmp.setFont(lineFont);
                gbLayout.gridx = i;
                add(tmp,gbLayout);
                gbLayout.insets = new Insets(0, 0, 0, 0);
            }
            //FULLVIEW Spalten hinzufügen
            if(fullview){
                for(int i = TeamStats.QUICKVIEW;i<TeamStats.ENTRIES;i++){
                    JLabel tmp = new JLabel(actStats.getContent(i));
                    tmp.setHorizontalAlignment(TeamStats.getAlignment(i));
                    tmp.setForeground(lineColor);
                    tmp.setFont(lineFont);
                    gbLayout.gridx = i;
                    add(tmp,gbLayout);
                }
            }
            //Interne Laufvariable erhöhen -> Siegerteams
            a++;
        }
    }
}
