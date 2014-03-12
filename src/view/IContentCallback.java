

package view;

/**
 *
 * @author Dimitri Wegner
 */
public interface IContentCallback {
    /**
     *
     * Setzt den Status ob der Prozess abgeschlossen wurde
     * @param success  Status
     */
    void processFinished(boolean success);
}
