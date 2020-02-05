package ie.gmit.sw.parsers;

import java.util.Map;

public interface Parsable {

	public Map<Integer, Integer> parse(String text, int kmerSize);
}
