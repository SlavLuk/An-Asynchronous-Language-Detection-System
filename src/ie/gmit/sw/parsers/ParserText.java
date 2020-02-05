package ie.gmit.sw.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import ie.gmit.sw.facade.LanguageEntry;

public class ParserText implements Parsable{

	private Map<Integer, Integer> langDb = new TreeMap<>();

	@Override
	public Map<Integer, Integer> parse(String text, int kmerSize) {

		List<Integer> temp = new ArrayList<>();

		String spaces = text.replaceAll("\\p{P}", "").toLowerCase().replaceAll("\\d", "").replaceAll(" ", "_");

		for (int i = 0; i <= spaces.length() - kmerSize; i++) {

			CharSequence ch = spaces.substring(i, i + kmerSize);

			int kmer = ch.hashCode();
			

			temp.add(kmer);
		}

		Set<Integer> d = new HashSet<>(temp);

		for (Integer j : d) {

			int f = Collections.frequency(temp, j);

			langDb.put(j, f);

		}

		return sortMapReversed(langDb);
	}

	private Map<Integer, Integer> sortMapReversed(Map<Integer, Integer> db) {

		// LinkedHashMap preserve the ordering of elements in which they are inserted
		Map<Integer, Integer> reverseSortedMap = new LinkedHashMap<>();

		// Use Comparator.reverseOrder() for reverse ordering
		db.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

		return reverseSortedMap;

	}

	// D
	public Map<Integer, LanguageEntry> getTop(int max, Map<Integer, Integer> db) {

		Map<Integer, LanguageEntry> qMap = new LinkedHashMap<>();

		int rank = 1;

		for (Integer j : db.keySet()) {

			LanguageEntry le = new LanguageEntry(j, db.get(j));

			le.setRank(rank);

			qMap.put(j, le);

			if (rank == max)
				break;
			rank++;

		}

		return qMap;
	}

}
