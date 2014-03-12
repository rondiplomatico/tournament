package tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Stellt eine Methode zur Verfügung, welche ermöglicht eine Zeichenkette
 * mit SHA1-Algorthmus zu verschlüsseln.
 *
 * Quelle:
 * http://www.anyexample.com/programming/java/java_simple_class_to_compute_sha_1_hash.xml
 *
 * @author Dimitri Wegner
 */
public class SimpleSHA1 {

    /**
     * Konvertiert ein byte-Array in einen String mit hexadezimal Werten.
     *
     * @param data Zu konvertierendes Array
     * @return String
     */
    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();

    }

    /**
     * Verschlüsselt einen String mit SHA1-Verschlüsselungsverfahren.
     *
     * @param text der zu verschlüsselnde Text
     * @return Einen verschlüsselten String
     * @throws java.security.NoSuchAlgorithmException falls der Algorithmus nicht
     *  unterstützt wird.
     * @throws java.io.UnsupportedEncodingException falls auswähltes Encoding nicht
     *  unterstützt wird.
     */
    public static String SHA1(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
}
