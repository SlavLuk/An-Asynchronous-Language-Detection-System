package ie.gmit.sw.threads;

import java.util.concurrent.BlockingQueue;

import javax.servlet.AsyncContext;

import ie.gmit.sw.facade.DataBaseFacade;
import ie.gmit.sw.facade.DatabaseFrontor;
import ie.gmit.sw.parsers.ParserText;
import ie.gmit.sw.requests.Request;
import ie.gmit.sw.utils.Language;

import java.util.*;

/**
 * Worker threads are responsible for processing requests. Once processed
 * they are moved from the in queue to a map and sent back with response to the client
 * 
 */
public class Worker implements Runnable{
	
	
	/**
	 * Instance variables
	 */
	private BlockingQueue<Request> inQueue;
	private Map<String, Language> outQueue;
	private ParserText parserText = new ParserText();
	private DatabaseFrontor db ;
	private int top;
	private AsyncContext ac;
	private int kmerSize;
	
	

	/**
	 * Constructor for {@link Worker} class
	 * @param db {@link DataBaseFacade} database
	 * @param bq {@link BlockingQueue} holding user's requests
	 * @param map {@link Map} outQueue map holding results
	 * @param top int top shingles
	 * @param ac {@link AsyncContext} context ref 
	 * @param kmerSize int kmer size
	 */
	public Worker(DatabaseFrontor db,BlockingQueue<Request> bq,Map<String, Language> map,int top,AsyncContext ac,int kmerSize) {
	
		this.db = db;
		this.inQueue = bq;
		this.outQueue = map;
		this.top = top;
		this.ac = ac;
		this.kmerSize = kmerSize;
		
	}
	
	/**
	 * Everything within this method will be executed in a separate thread.
	 */
	@Override
	public void run() {
		
	 		
			while(inQueue.size()>0) {
				
				
				try {
				
				// Blocking call to take the next request from the in queue.
						Request req = inQueue.take();
						
						//Process user's request
						Language lang = db.getLanguage(parserText.getTop(top, parserText.parse(req.getQuery(), kmerSize)));
	
						
						// Add the result to an out queue when processed.
						outQueue.put(req.getTaskNum(), lang);
			
					
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
		//called when thread finished
			ac.complete();
		
		}

}
