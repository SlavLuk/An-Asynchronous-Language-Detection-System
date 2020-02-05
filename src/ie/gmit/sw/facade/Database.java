package ie.gmit.sw.facade;

import java.util.*;
import ie.gmit.sw.utils.Language;




/**
 * Adapted from John Healy project skeleton
 * @author Slav
 *
 */
public class Database {

	/**
	 * instance variables
	 * 
	 * 
	 */
	private Map<Language, Map<Integer, LanguageEntry>> db = new TreeMap<>();
	private static Database instance;
	private int top;

	
	/**
	 * Private constructor to prevent more than one instance of this object
	 * from being created.
	 * @param max int top rank
	 */
	private Database(int max) {
		this.top = max;
	}

	
	/**
	 * @param top int defines highest top rank
	 * @return {@link Database} itself as a singleton
	 */
	public static Database getInstance(int top) {

		if (instance == null) {
			instance = new Database(top);
		}

		return instance;
	}

	/**
	 * @param s int kmer
	 * @param lang {@link Enum} language
	 */
	public void add(Integer s, Language lang) {

		Map<Integer, LanguageEntry> langDb = getLanguageEntries(lang);

		int frequency = 1;
		/*
		 * calculating occurrence frequency of kmer in dataset for given language
		 * 
		 */
		if (langDb.containsKey(s)) {
			
			frequency += langDb.get(s).getFrequency();
		}
		langDb.put(s, new LanguageEntry(s, frequency));

	}

	/**
	 * @param lang {@link Enum} languages
	 * @return {@link Map} if database has that map otherwise create a new one
	 */
	private Map<Integer, LanguageEntry> getLanguageEntries(Language lang) {
		Map<Integer, LanguageEntry> langDb = null;
		if (db.containsKey(lang)) {
			langDb = db.get(lang);
		} else {
			langDb = new TreeMap<Integer, LanguageEntry>();
			db.put(lang, langDb);
		}
		return langDb;
	}

	/**
	 * set top highest entry by rank
	 * @param max int top entries
	 */
	public void resize(int max) {
		Set<Language> keys = db.keySet();
		for (Language lang : keys) {
			Map<Integer, LanguageEntry> top = getTop(max, lang);

			db.put(lang, top);
		}

	}

	private Map<Integer, LanguageEntry> getTop(int max, Language lang) {
		Map<Integer, LanguageEntry> temp = new LinkedHashMap<Integer, LanguageEntry>();
		Set<LanguageEntry> les = new TreeSet<>(db.get(lang).values());

		int rank = 1;
		/*
		 * set rank for frequency
		 */
		for (LanguageEntry le : les) {

			le.setRank(rank);
			temp.put(le.getKmer(), le);
			if (rank == max)
				break;
			rank++;

		}

		return temp;
	}

	/**
	 * @param query {@link Map} processed request to measure distance in the rank
	 * @return {@link Language} result  bound to response
	 */
	public Language getLanguage(Map<Integer, LanguageEntry> query) {

		resize(top);
		
		TreeSet<OutOfPlaceMetric> oopm = new TreeSet<>();

		Set<Language> langs = db.keySet();

		for (Language lang : langs) {
			OutOfPlaceMetric o = new OutOfPlaceMetric(lang, getOutOfPlaceDistance(query, db.get(lang)));

			oopm.add(o);
		}

		return oopm.first().getLanguage();
	}

	//method calculating distance 
	private int getOutOfPlaceDistance(Map<Integer, LanguageEntry> query, Map<Integer, LanguageEntry> subject) {
		int distance = 0;

		List<LanguageEntry> qent = new ArrayList<>(query.values());

		for (LanguageEntry q : qent) {

			LanguageEntry s = subject.get(q.getKmer());

			int topUp = 0;

			if (s == null) {
				//if processed dataset does not contain enough data we add dummy values
				if (subject.size() < top) {

					topUp = top;
				}

				distance += topUp + 1;

			} else {

				distance += s.getRank() - q.getRank();
			}
		}
		return distance;
	}

}