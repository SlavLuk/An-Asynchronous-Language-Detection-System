package ie.gmit.sw;

import java.io.*;

import java.util.Map;
import java.util.concurrent.*;

import javax.servlet.*;
import javax.servlet.http.*;

import ie.gmit.sw.facade.DataBaseFacade;
import ie.gmit.sw.facade.DatabaseFrontor;
import ie.gmit.sw.requests.DataSet;
import ie.gmit.sw.requests.Request;
import ie.gmit.sw.threads.DataProcessor;
import ie.gmit.sw.threads.ThreadPoolManager;
import ie.gmit.sw.threads.Worker;
import ie.gmit.sw.utils.FileHandler;
import ie.gmit.sw.utils.Language;


/*
 * To compile this servlet, open a command prompt in the web application directory and execute the following commands:
 *
 * Linux/Mac																	Windows
 * ---------																	---------	
 * cd WEB-INF/classes/															cd WEB-INF\classes\
 * javac -cp .:$TOMCAT_HOME/lib/servlet-api.jar ie/gmit/sw/*.java				javac -cp .:%TOMCAT_HOME%/lib/servlet-api.jar ie/gmit/sw/*.java
 * cd ../../																	cd ..\..\
 * jar -cf ngrams.war *															jar -cf ngrams.war *
 * 
 * Drag and drop the file ngrams.war into the webapps directory of Tomcat to deploy the application. It will then be 
 * accessible from http://localhost:8080.
 *
 * NOTE: the text file containing the 253 different languages needs to be placed in /data/wili-2018-Edited.txt. This means 
 * that you must have a "data" directory in the root of your file system that contains a file called "wili-2018-Edited.txt".
 * Do NOT submit the wili-2018 text file with your assignment!
 * 
*/
/**
 * This servlet handling request to endpoint http://localhost:8080/ngrams
 * @author Slav Lukyanov G00339839
 * @version 1.0
 * 
 * 
 */
public class ServiceHandler extends HttpServlet {
	
	// These variables are shared by all HTTP requests for the servlet
	private static final long serialVersionUID = 1L;
	private String languageDataSet = null; 
	private int kmerSize;
	private int threadPoolSize;
	private int top;
	private static long jobNumber = 1; 
	//store incoming requests
	private static BlockingQueue<Request> inQueue = new LinkedBlockingQueue<>();
	//store processed data from file
	private static BlockingQueue<DataSet> dataSet = new LinkedBlockingQueue<>();
	//store outgoing responses result
	private static Map<String, Language> outQueue = new ConcurrentHashMap<>();
	private static DatabaseFrontor db;
	
	

	// The init() method is only ever called once during the life cycle of a servlet
	public void init() throws ServletException {

		// Get a handle on the application context
		ServletContext ctx = getServletContext();

		// Reads the value from the <context-param> in web.xml
		languageDataSet = ctx.getInitParameter("LANGUAGE_DATA_SET");

		kmerSize = Integer.parseInt(ctx.getInitParameter("KMER_SIZE"));

		threadPoolSize = Integer.parseInt(ctx.getInitParameter("THREAD_POOL_SIZE"));

		top = Integer.parseInt(ctx.getInitParameter("TOP"));

		// You can start to build the subject database at this point.
		InputStream is = ctx.getResourceAsStream(languageDataSet);
		//read file
		FileHandler.readFile(dataSet,is);
		//initialise database
		db = new DataBaseFacade(top);
		 
	   	    
	    Runnable dataProcess = new DataProcessor(db, dataSet, kmerSize);

		try {
			// Start the thread pool.
			ThreadPoolManager poolManager = ThreadPoolManager.getInstance();
			poolManager.init(threadPoolSize, dataProcess);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			

	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// Initialise some request varuables with the submitted form info. These are
		// local to this method and thread safe...
		// Change options to whatever you think adds value to your assignment...

		String option = req.getParameter("cmbOptions");
		String query = req.getParameter("query");
		String taskNumber = req.getParameter("frmTaskNumber");
	

		resp.setContentType("text/html;charset=UTF-8"); // Output the MIME type
		PrintWriter out = resp.getWriter(); // Write out text. We can write out binary too and change the
											// MIME type...

		out.print("<html><head><title>Advanced Object Oriented Software Development Assignment</title>");
		out.print("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
		out.print("</head>");
		out.print("<body>");
		
		

		if(query!=null) {
			
			if (taskNumber == null) {

				taskNumber = new String("T " + jobNumber);
				jobNumber++;
				
				// Add job to in-queue

				try {

					inQueue.put(new Request(query, taskNumber));

				} catch (InterruptedException e) {

					e.printStackTrace();
				}

			} else {
				// Check out-queue for finished job

				if (outQueue.containsKey(taskNumber)) {
					
					out.print("<H1>Language detected : " + outQueue.get(taskNumber) + "</H1></ br>");
					
					outQueue.remove(taskNumber);
				}
		}
	

		}else {
			//if query blank
			query = "";
			taskNumber= "T 0";
		}

		// obtain async context
		final AsyncContext ac = req.startAsync();

		// pull  a thread from container thread pool
		ac.start(new Worker(db, inQueue, outQueue, top, ac, kmerSize));

		out.print("<H1>Processing request for Job#: " + taskNumber + "</H1>");
		
		out.print("<div id=\"r\"></div>");

		out.print("<font color=\"#993333\"><b>");
		out.print("Language Dataset is located at " + languageDataSet + " and is <b><u>" + query.length()
				+ "</u></b> bytes in size");
		out.print("<br>Option(s): " + option);
		out.print("<br>Query Text : " + query);
		out.print("</font><p/>");

		out.print(
				"<br>This servlet should only be responsible for handling client request and returning responses. Everything else should be handled by different objects. ");
		out.print(
				"Note that any variables declared inside this doGet() method are thread safe. Anything defined at a class level is shared between HTTP requests.");
		out.print("</b></font>");

		out.print("<P> Next Steps:");
		out.print("<OL>");
		out.print(
				"<LI>Generate a big random number to use a a job number, or just increment a static long variable declared at a class level, e.g. jobNumber.");
		out.print("<LI>Create some type of an object from the request variables and jobNumber.");
		out.print("<LI>Add the message request object to a LinkedList or BlockingQueue (the IN-queue)");
		out.print(
				"<LI>Return the jobNumber to the client web browser with a wait interval using <meta http-equiv=\"refresh\" content=\"10\">. The content=\"10\" will wait for 10s.");
		out.print("<LI>Have some process check the LinkedList or BlockingQueue for message requests.");
		out.print(
				"<LI>Poll a message request from the front of the queue and pass the task to the language detection service.");
		out.print("<LI>Add the jobNumber as a key in a Map (the OUT-queue) and an initial value of null.");
		out.print(
				"<LI>Return the result of the language detection system to the client next time a request for the jobNumber is received and the task has been complete (value is not null).");
		out.print("</OL>");

		out.print("<form method=\"POST\" name=\"frmRequestDetails\">");
		out.print("<input name=\"cmbOptions\" type=\"hidden\" value=\"" + option + "\">");
		out.print("<input name=\"query\" type=\"hidden\" value=\"" + query + "\">");
		out.print("<input name=\"frmTaskNumber\" type=\"hidden\" value=\"" + taskNumber + "\">");

		out.print("</form>");
		out.print("</body>");
		out.print("</html>");

		out.print("<script>");
		out.print("var wait=setTimeout(\"document.frmRequestDetails.submit();\", 10000);");
		out.print("</script>");

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		doGet(req, resp);
	}
	
	
}