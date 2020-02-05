package ie.gmit.sw.facade;

/**
 * This class stores processed text as kmer and its frequency and rank
 * @author Slav
 *
 */
public class LanguageEntry implements Comparable<LanguageEntry> {
	/**
	 * instance variables
	 */
	private int kmer;
	private int frequency;
	private int rank;

	public LanguageEntry(int kmer, int frequency) {
		super();
		this.kmer = kmer;
		this.frequency = frequency;
	}
/*
 * getters/setters
 */
	public int getKmer() {
		return kmer;
	}

	public void setKmer(int kmer) {
		this.kmer = kmer;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
/*
 * (non-Javadoc)
 * @see java.lang.Comparable#compareTo(java.lang.Object)
 *  compare one language entry (k-mer) to another by their frequency in descending order
 */
	@Override
	public int compareTo(LanguageEntry next) {
		return - Integer.compare(frequency, next.getFrequency());
	}
	
	@Override
	public String toString() {
		return "[" + kmer + "|" + frequency + "|" + rank + "]";
	}
}