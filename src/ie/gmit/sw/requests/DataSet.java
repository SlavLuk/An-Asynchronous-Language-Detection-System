package ie.gmit.sw.requests;

/**
 * 
 * Class to hold split dataset data
 * @author Slav
 *
 */
public class DataSet  {
	/**
	 * Instance variables
	 */
	private String text;
	private String lang;
	
	public DataSet() {
		
	}
	
/**
 * @param lang {@link String} language
 * @param text {@link String} text associated with language
 */
public DataSet(String lang,String text) {
		this.lang = lang;
		this.text = text;
	}

//getter and setter
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
	
}
