/**
 * 
 */
package planning.control;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import model.Tournament;

/**
 * @author CreaByte
 * 
 */
public class TimeManager {

	public class NoPlayTimeException extends Exception {

		private static final long serialVersionUID = -1771533973439807997L;

		public NoPlayTimeException(String msg) {
			super(msg);
		}
	}

	private Tournament t;
	private GregorianCalendar cal;
	private int ptidx = 0;

	public TimeManager(Tournament t) {
		this.t = t;
		cal = new GregorianCalendar();
		// Set start to either the first play time or the tournament start time
		cal.setTime(t.getPlayTimes().size() > 0 ? t.getPlayTimes().get(0)
				.getBegin() : t.getStartDate());
	}

	/**
	 * Gibt die Zeit nach ablauf der angegebenen Minuten zurück. Entspricht ggf.
	 * NICHT dem von getCurrentTime() zurückgegebenen Wert, welcher eventuelle
	 * Pausen in den PlayTimes berücksichtigt.
	 * 
	 * @param minutes
	 * @return
	 * @throws NoPlayTimeException
	 */
	public Date consumeTime(int minutes) throws NoPlayTimeException {
		cal.add(GregorianCalendar.MINUTE, minutes);
		Date res = cal.getTime();
		if (t.getPlayTimes().size() > 0) {
			// Check if current time is later than end time of current playtime
			if (t.getPlayTimes().get(ptidx).getEnd().before(res)) {
				// If so, advance to the next playtime
				ptidx++;
				// See if there actually IS a new playtime (all being used up!)
				if (t.getPlayTimes().size() > ptidx) {
					cal.setTime(t.getPlayTimes().get(ptidx).getBegin());
				} else {
					throw new NoPlayTimeException(
							"No more time to consume is left. Last time: "
									+ res.toString());
				}
			}
		} else {
			// Alternative: Simply use start- and end times for each day where
			// games can be played.
			if (cal.get(Calendar.HOUR_OF_DAY) > t.getEndHour()) {
				// Einen Tag später weitermachen, zur StartHour()
				cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + 1);
				cal.set(Calendar.HOUR_OF_DAY, t.getStartHour());
				cal.set(Calendar.MINUTE, 0);
			}
		}
		return res;
	}

	public Date getCurrentTime() {
		return cal.getTime();
	}
}
