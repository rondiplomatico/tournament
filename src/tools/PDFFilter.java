/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Filter für den JFileChooser.
 * Damit kann man sich nur PDF Dateien anzeigen lassen
 * @author Paul Lubos
 */
public class PDFFilter extends FileFilter {

    /**
     * Methode die angibt ob die übergebene datei gezeigt wird oder nicht
     * @param f Die datei die geprüft wird
     * @return True wenn die datei gezeit werden soll,false wenn nicht.
     */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        if (f.isFile()) {
            String extension = getExtension(f);

            if (extension != null && extension.equalsIgnoreCase(".pdf")) {

                return true;
            }
        }
        return false;
    }

    /**
     * Die Filterbeschreibung
     * @return Die Beschreibung des Filters
     */
    public String getDescription() {
        return "PDF-Typ";
    }

    /**
     * Returns the extension of a file.
     */
    private static String getExtension(File f) {
        String extension = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            extension = s.substring(i).toLowerCase();
        }
        return extension;
    }
}
