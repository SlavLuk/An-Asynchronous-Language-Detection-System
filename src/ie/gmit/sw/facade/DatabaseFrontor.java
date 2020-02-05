package ie.gmit.sw.facade;

import java.util.Map;

import ie.gmit.sw.utils.Language;

/**
 * This interface hides complexity of database
 *
 */
public interface DatabaseFrontor {

	
	/**
	 * @param kmer int kmer as a hash of string
	 * @param lang {@link Language} language string
	 */
	public void add(Integer kmer, Language lang);
	/**
	 * @param max int top rank
	 */
	public void resize(int max);
	/**
	 * @param query {@link Map} kmer as a key and query as a value
	 * @return
	 */
	public Language getLanguage(Map<Integer, LanguageEntry> query);
}
