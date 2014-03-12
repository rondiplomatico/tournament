/**
 * 
 */
package planning.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author CreaByte
 * 
 */
@Entity
public class PlayTime {

	@Id
	@GeneratedValue
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date b, e;

	public PlayTime() {
		GregorianCalendar c = new GregorianCalendar();
		b = c.getTime();
		c.add(Calendar.MINUTE, 30);
		e = c.getTime();
	}

	public PlayTime(Date begin, Date end) {
		b = begin;
		e = end;
	}

	public Date getBegin() {
		return b;
	}

	public Date getEnd() {
		return e;
	}

}
