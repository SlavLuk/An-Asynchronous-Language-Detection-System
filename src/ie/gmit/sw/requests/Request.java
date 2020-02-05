package ie.gmit.sw.requests;

/**
 * 
 * Class to hold user request
 * @author Slav
 *
 */
public class Request {

	private String query;
	private String taskNum;

	/**
	 * @param q {@link String} query from user submitted form
	 * @param t {@link String} task number
	 */
	public Request(String q,String t) {
		query = q;
		taskNum = t;
	}
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
	}
}
