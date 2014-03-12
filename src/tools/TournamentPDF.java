package tools;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import model.Team;
import model.Tournament;
import model.User;
import model.enums.TournamentState;
import model.enums.TournamentType;
import planning.control.PlanningManager;
import planning.model.Match;
import planning.model.TeamSlot;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import control.TournamentManager;

/**
 *  * Klasse zum Erstellen von Pdf-Dateien
 *  (Urkunde, Ausschreibung, Ergebnissliste
 *
 * @author Osama, Sandra
 */
public class TournamentPDF {

    final private static String IMG_PUBLISH = "/resources/pdf/publish.jpg";
    final private static String IMG_DOCUMENT = "/resources/pdf/document.jpg";


    /**
     * Erzeugt ein PDF-Datei von Ergibnis-liste eines Turniers
     *
     * Dafür muss ein parameter Tournament übergeben werden mit ein filename,
     * unterdem es der Datei gespeichert werden soll
     *
     *
     * @param t Turnier für das die Urkunde erstellt werden soll
     * @param path Pfad an dem die Datei gespeichert werden soll
     * @throws FileNotFoundException Datei kann nicht geschrieben werden
     * @throws DocumentException Fehler bei der Rrstellung der PDF
     */
    public static void createResultListPDF(Tournament t, String path)
            throws FileNotFoundException, DocumentException {

        PlanningManager pm = new PlanningManager();
        List<Match> matchList = pm.getSchedule(t.getTournamentPlan());

        // document erzeugen
        Document document = new Document(PageSize.A4, 33, 33, 20, 20);
        // document speichern unter filename in Ordner Tournament
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();

        document.add(new Cell("Ergebnisliste"));

        SimpleDateFormat formatter = new SimpleDateFormat();
        Font bold = FontFactory.getFont(BaseFont.TIMES_BOLD, 11);

        //tabelle erzeugen
        PdfPTable table = new PdfPTable(3);
        // je match , die daten herauslesen und in der tabelle einfügen
        for (Match m : matchList) {

            table.addCell(formatter.format(m.getStartTime()));

            TeamSlot home = m.getHomeTeam();
            TeamSlot guest = m.getGuestTeam();

            Phrase ph1 = new Phrase(home.getName());
            Phrase ph2 = new Phrase(guest.getName());

            if (m.hasWinner() && m.getWinner() == home) {
                ph1.setFont(bold);
            } else {
                ph2.setFont(bold);
            }

            Phrase teamPhrase = new Phrase();
            teamPhrase.add(ph1 + " : " + ph2);

            table.addCell(teamPhrase);
            table.addCell(m.getGoals(home) + " : " + m.getGoals(guest));
        }

        document.add(table);
        document.close();
    }

    /**
     * Erzeugt eine Zelle ohne seitenränder mit inhalt txt und mit die höhe 40
     * am Ende wird die Zelle hinzugefügt in der übergebene tabelle
     *
     * @param t
     *            Name der Tabelle an die, die Zelle hinzugefügt werden soll
     * @param txt
     *            Inhalt der Zelle
     *
     */
    private static void setCell(PdfPTable t, String txt) {
        Font font = FontFactory.getFont(BaseFont.TIMES_BOLD, 13);
        PdfPCell cell = new PdfPCell(new Paragraph(txt, font));
        cell.setBorder(0);
        cell.setFixedHeight(40);
        t.addCell(cell);
    }

    /**
     * Erzeugt ein PDF-Datei als Ausschreibung eines Turniers
     *
     * Dafür muss ein parameter Tournament übergeben werden mit ein filename,
     * unterdem es der Datei gespeichert werden soll
     *
     *
     * @param tournament
     *           Turnier für das die Ausschreibung erstellt werden soll
     * @param filename
     *            Pfad der Datei wo gespeichert werden soll
     * @param gez
     *            Unterschrift
     * @throws DocumentException PDF konnte nicht korretkt erstellt werden
     * @throws FileNotFoundException Datei konnt nicht geschrieben werden
     * @throws MalformedURLException Pfad ist Fehlerhaft
     * @throws BadElementException Fehlerhaftes Element
     * @throws IOException DAtei konnte nicht korrekt gespeichert werden
     */
    public static void createPublishPDF(Tournament tournament, String filename, String gez)
            throws DocumentException, FileNotFoundException,
            BadElementException, MalformedURLException, IOException {

        // document erzeugen
        Document document = new Document(PageSize.A4, 33, 33, 20, 20);
        // document speichern unter filename in Ordner Tournament
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();

        // bild abrufen und hinzufügen
        URL url = TournamentPDF.class.getResource(IMG_PUBLISH);
        Image image = Image.getInstance(url.toString());
        image.setAlignment(Image.BOTTOM | Image.UNDERLYING);
        image.scaleAbsoluteWidth(500);
        document.add(image);

        SimpleDateFormat s = new SimpleDateFormat("dd.MM.yyyy");
        Font fontHead1 = FontFactory.getFont(BaseFont.TIMES_BOLD, 30);
        Font fontHead2 = FontFactory.getFont(BaseFont.TIMES_BOLD, 15);
        Font fontHead3 = FontFactory.getFont(BaseFont.TIMES_BOLD, 10);

        String txt = tournament.getType() == TournamentType.SinglePlayer ? "Einzelspieler-Turnier" : "Mehrspieler-Turnier";

        String perTeam = "";
        if (tournament.getType() == TournamentType.MultiPlayer) {
            perTeam = String.valueOf(tournament.getRequiredPlayersPerTeam()) + " Spieler pro Mannschaft";
        }

        String head = tournament.getName();
        String date = s.format(tournament.getStartDate());

        Paragraph buttom = new Paragraph("Ausschreibung " + head, fontHead1);
        Paragraph para1 = new Paragraph(txt, fontHead2);
        Paragraph para2 = new Paragraph(perTeam, fontHead2);
        Paragraph para3 = new Paragraph("am " + date, fontHead3);

        buttom.setIndentationLeft(10);
        para1.setIndentationLeft(10);
        para2.setIndentationLeft(10);
        para3.setIndentationLeft(10);

        document.add(buttom);
        document.add(para1);
        document.add(para2);
        document.add(para3);

        // tabelle erzeugen
        PdfPTable table = new PdfPTable(2);

        // BODY

        setCell(table, "Sportart");
        setCell(table, (tournament.getSportKind()));

        setCell(table, "Ort");
        setCell(table, tournament.getLocation());

        setCell(table, "Anmeldung");
        setCell(table, "Anmeldung geht über das Tournament-System");

        setCell(table, "Meldeschluss");
        setCell(table, s.format(tournament.getExpireDate()));

        setCell(table, "Anmelde Gebühr");
        setCell(table, String.valueOf(tournament.getFee()) + " EURO");

        setCell(table, "Nachmelde Gebühr");
        setCell(table, String.valueOf(tournament.getLateRegFee()) + " EURO");

        setCell(table, "Max Teilnehmeranzahl");
        setCell(table, String.valueOf(tournament.getMaxParticipatingTeams()));

        setCell(table, "Standard Dauer eines Spiels");
        setCell(table, String.valueOf(tournament.getGameDuration()) + " min");

        setCell(table, "Teilnahmebedingungen");
        setCell(table, tournament.getParticipationCond());

        setCell(table, "Preise");
        setCell(table, "Urkunden und Pokale");

        table.setSpacingBefore(100);
        document.add(table);

        // Ende
        Font fontEnd = FontFactory.getFont(BaseFont.TIMES_BOLD, 12);
        Paragraph paraEnd = new Paragraph("gez " + gez, fontEnd);
        paraEnd.setIndentationLeft(55);
        paraEnd.setAlignment(Element.ALIGN_BOTTOM);
        document.add(paraEnd);

        document.close();
    }

    /**
     * Erzeugt ein PDF-Datei als eine Urkunde für den Sieger den übergegebenes
     * turniers
     *
     * Dafür muss ein parameter Tournament übergeben werden mit ein filename,
     * unterdem es der Datei gespeichert werden soll
     *
     *
     *
     * @pre Tournament ist beendet. Tournament hat eine Final-Runde.
     * @param tournament
     *            Turnier für das die Urkunden erstellt werden sollen
     * @param filename
     *            Pfad der Datei wo gespeichert werden soll
     * @throws DocumentException PDF konnte nicht korretkt erstellt werden
     * @throws FileNotFoundException Datei konnt nicht geschrieben werden
     * @throws MalformedURLException Pfad ist Fehlerhaft
     * @throws BadElementException Fehlerhaftes Element
     * @throws IOException DAtei konnte nicht korrekt gespeichert werden
     *
     *
     */
    public static void createDocumentPDF(Tournament tournament, String filename)
            throws DocumentException, FileNotFoundException,
            BadElementException, MalformedURLException, IOException {
        assert (tournament.getState() == TournamentState.finished);
        assert (tournament.getTournamentPlan().hasFinalRound());

        // PDF erstellen

        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, new FileOutputStream(filename));
        doc.open();

        URL url = TournamentPDF.class.getResource(IMG_DOCUMENT);
        Image image = Image.getInstance(url.toString());
        image.setAlignment(Image.MIDDLE | Image.UNDERLYING);
        image.scaleAbsoluteHeight(800);
        image.scaleAbsoluteWidth(600);

        Font font = FontFactory.getFont(BaseFont.TIMES_ROMAN, 80);
        font.setColor(Color.DARK_GRAY);
        Paragraph headPara = new Paragraph("URKUNDE", font);
        headPara.setSpacingBefore(100);
        headPara.setIndentationLeft(75);

        Font helveticaFont = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.WINANSI, 25);

        String tournamentName = tournament.getName();

        List<TeamSlot> rankingList = TournamentManager.getInstance().getFinalRanking(tournament);

        // für die ersten "DOCUMENTS_COUNT"-Manschaften eine urkunde erstellen
        for (int i = 0; i < rankingList.size(); i++) {
            Team current = rankingList.get(i).getTeam();
            int rank = i + 1;

            // für alle spieler des teams eine Urkunde erstellen
            for (User player : current.getPlayers()) {
                String teaminfo = current.getPlayers().size() > 1 ? " mit der Mannschaft \n\r" + current.getName() + "\n\r" : "";
                doc.add(image);
                doc.add(headPara);

                String name = player.getName();
                String text = "Beim " + tournamentName + "-Turnier \n\r" + "erreichte\n\r " + "" + name + " \n\r" +
                        teaminfo + "den " + rank + " Platz";

                Paragraph winnerPara = new Paragraph(text, helveticaFont);
                winnerPara.setIndentationLeft(140);
                winnerPara.setSpacingBefore(30);

                doc.add(winnerPara);

                doc.newPage();
            }
        }

        doc.close();
    }

    /**
     * Öffnet die erstellte PDF mit dem Standard PDF-Viewer.
     *
     * @param filename Pfad zur PDF Datei
     * @throws IOException Datei konnte nicht korrekt geöffnet werden
     *            
     */
    public static void showDocument(String filename) throws IOException {
        Desktop.getDesktop().open(new File(filename));
    }
}
