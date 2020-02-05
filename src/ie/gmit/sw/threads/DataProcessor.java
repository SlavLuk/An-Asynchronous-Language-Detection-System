package ie.gmit.sw.threads;

import java.util.concurrent.BlockingQueue;

import ie.gmit.sw.facade.DataBaseFacade;
import ie.gmit.sw.facade.Database;
import ie.gmit.sw.facade.DatabaseFrontor;
import ie.gmit.sw.requests.DataSet;
import ie.gmit.sw.utils.Language;
/**
 * This runnable will process dataset on the separate threads.
 * 
 */
public class DataProcessor implements Runnable {

	
	/**
	 * Instance variables
	 */
	private DatabaseFrontor db;
	private BlockingQueue<DataSet> in;
	private int kmerSize;
	

	
	/**
	 * @param db {@link DataProcessor} facade pattern used to hide complexity of {@link Database}
	 * @param in {@link BlockingQueue} holding all dataset of languages
	 * @param kmerSize int size for shingles
	 */
	public DataProcessor(DatabaseFrontor db, BlockingQueue<DataSet> in, int kmerSize) {
		this.db = db;
		this.in = in;
		this.kmerSize = kmerSize;
		
	}

	
	/**
	 * Everything within this method will be executed in a separate thread.
	 */
	@Override
	public void run() {

		while (in.size()>0) {

			DataSet data;
			
			try {
				
				//blocking operation runs when data is available
				data = in.take();
				//parse words into kmer(shingles)
				parse(data.getText(), data.getLang(), kmerSize);
			
				
			} catch (InterruptedException e) {
				
			}

		}
		
	}
	
	
	/**
	 * @param text {@link String} text written in different languages
	 * @param lang {@link String} language associated with text
	 * @param k int kmerSize
	 */
	private void parse(String text, String lang, int k) {

		Language language = Language.valueOf(lang);
		
		//regexp used to remove numeric,punctuations items
		String nText = text.replaceAll("\\p{P}", "").toLowerCase().replaceAll("\\d", "").replaceAll(" ", "_");

		for (int i = 0; i <= nText.length() - k; i++) {

			String s = nText.substring(i, i + k);
			//convert string into hashcode int
			int kmer = s.hashCode();
				
			
			synchronized (db) {
				
				db.add(kmer, language);
			}
				
			

		}

	}

}
