package ie.gmit.sw.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import ie.gmit.sw.requests.DataSet;

/**
 * 
 * Handle file processing
 * Read file and splits on @ into two datasets
 * @author Slav
 *
 */
public class FileHandler {
	
	
	/**
	 * @param dataSet {@link BlockingQueue} holds processed datasets
	 * @param is {@link InputStream} ref to file to read
	 */
	static public void readFile(BlockingQueue<DataSet> dataSet,InputStream is) {
		 BufferedReader br = null;
			try {
				
				
				br = new BufferedReader(new InputStreamReader(is));
							
				String line = null;
				
				//read file by lines
				while ((line = br.readLine()) != null) {
					//split line into two dataset
					String[] record = line.trim().split("@");

					//if line has no @ sign keep going
					if (record.length != 2) {
						continue;
					}
					//put into out queue 
					dataSet.put(new DataSet(record[1],record[0]));
				
				}
			
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			

		
	}
}
