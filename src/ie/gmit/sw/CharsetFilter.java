package ie.gmit.sw;
import javax.servlet.*;
import java.io.IOException;


/**
 * This is the filter to set up charset UTF-8 encoding on request/response before getting them
 * processed by servlet.This filter is called before servlet.
 * 
 * 
 */
public class CharsetFilter implements Filter {

    /**
     * String stores Init Parameter from web.xml
     * 
     */
    private String encoding;

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {
    	
        encoding = config.getInitParameter("requestEncoding");
        if (encoding == null) encoding = "UTF-8";
    }

  
    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        // Respect the client-specified character encoding
        // (see HTTP specification section 3.4.1)
        if (null == request.getCharacterEncoding()) {
            request.setCharacterEncoding(encoding);
        }

        // Set the default response content type and encoding
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        
        
        	 next.doFilter(request, response);
       
       
    }

    public void destroy() {
    }
}