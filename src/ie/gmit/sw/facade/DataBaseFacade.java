package ie.gmit.sw.facade;

import java.util.Map;

import ie.gmit.sw.utils.Language;


/**
 * This class represent Facade pattern to hide complexity
 * @author Slav
 *
 */
public class DataBaseFacade implements DatabaseFrontor{
/*
 * instance variable
 */
	private Database dataBase ;
	
	
	/**
	 * default constructor
	 */
	public DataBaseFacade() {
		
		
	}
	
	/**
	 * @param top constructor to initialise {@link Database} class and set top
	 */
	public DataBaseFacade(int top) {
		
		 dataBase = Database.getInstance(top);
		
	}
	/**
	 * @param s int kmer
	 * @param lang {@link Language} language
	 */
	public void add(Integer s, Language lang) {
		//delegate method to database
		dataBase.add(s, lang);
	}
	
	/**
	 * @param max same as {@link Database} resize
	 */
	public void resize(int max) {
		//call method to database
		dataBase.resize(max);
	}
	
	/**
	 * @param query same as {@link Database} getLanguage
	 * @return language
	 */
	public Language getLanguage(Map<Integer, LanguageEntry> query) {
		//call method to database
		return dataBase.getLanguage(query);
	}
	
}
