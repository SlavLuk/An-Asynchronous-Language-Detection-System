package ie.gmit.sw.threads;

import java.util.concurrent.*;

/**
 * This class is responsible for setting up a thread pool that will handle
 * the processing of dataset stored in BlockingQueue after reading data from a file.
 * 
 */
public class ThreadPoolManager {
 
	/*
	 * Own instance of the ThreadPoolManager class.
	 */
	private static ThreadPoolManager instance;
	
	/*
	 * The pool of worker threads.
	 */
	private ExecutorService pool;

	/*
	 * Private constructor to prevent more than one instance of this object
	 * from being created.
	 */
	private ThreadPoolManager() {

	}

	/**
	 * Get the single instance of the {@link ie.gmit.sw.threads.ThreadPoolManager} class.
	 * @return the instance of the class.
	 */
	public static ThreadPoolManager getInstance() {

		if (instance == null) {
			instance = new ThreadPoolManager();
		}

		return instance;
	}
	
	/**
	 * Setup the thread pool.
	 * @param threadPoolSize the number of threads in the thread pool.
	 * @param data {@link BlockingQueue} to process
	 * @throws InterruptedException
	 */

	/**
	 * @param threadPoolSize int pool size
	 * @param data data to process
	 * @throws InterruptedException if thread gets interrupted
	 */
	public void init(int threadPoolSize, Runnable data) throws InterruptedException {
		// Create a thread pool with X number of threads.
		pool = Executors.newFixedThreadPool(threadPoolSize);

		// Populate the thread pool with workers.
		for (int i = 0; i < threadPoolSize; i++) {
	
			pool.execute(data);
		}
		
		// pool orderly shutdown
		pool.shutdown();
		try {
		    if (!pool.awaitTermination(800, TimeUnit.MILLISECONDS)) {
		    	pool.shutdownNow();
		    } 
		} catch (InterruptedException e) {
			pool.shutdownNow();
		}
	}


}
