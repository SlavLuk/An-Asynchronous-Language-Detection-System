package ie.gmit.sw.facade;

import ie.gmit.sw.utils.Language;

/**
 * This class used in database to calculate Absolute Distance in ranks
 * @author Slav
 *
 */
public class OutOfPlaceMetric implements Comparable<OutOfPlaceMetric> {
	/**
	 * instance variables
	 */
	private Language lang;
	private int distance;
	

	/**
	 * @param lang {@link Language} language
	 * @param distance int distance
	 */
	public OutOfPlaceMetric(Language lang, int distance) {
		super();
		this.lang = lang;
		this.distance = distance;
		
	}


	
	public Language getLanguage() {
		return lang;
	}

	public int getAbsoluteDistance() {
		return Math.abs(distance);
	}

	@Override
	public int compareTo(OutOfPlaceMetric o) {
		return Integer.compare(this.getAbsoluteDistance(), o.getAbsoluteDistance());

	}

	@Override
	public String toString() {
		return "[lang=" + lang + ", distance=" + getAbsoluteDistance() + "]";
	}
}
