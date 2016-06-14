package net.codejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import services.BestMatch;

/**
 * Servlet implementation class AutoReply
 */
@WebServlet("/AutoReply")
public class AutoReply extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	Map<String, String> query, original;
	Properties properties, originalProperties;
	String[] NonKeywords;
	String q1;
    public AutoReply() throws FileNotFoundException 
    {
        super();
        
        query = new HashMap<String, String>();
		original = new HashMap<String, String>();
		properties = new Properties();
		originalProperties = new Properties();
		Scanner scanner = new Scanner( new File("workspace/TejeshAutoReplyWeb/NonKeywords.txt"), "UTF-8" );
		String NonKeywordsString = scanner.useDelimiter("\\A").next();
		scanner.close();
		NonKeywords = NonKeywordsString.split("\\s+");
		try 
		{
			properties.load(new FileInputStream("workspace/TejeshAutoReplyWeb/reply.properties"));
			originalProperties.load(new FileInputStream("workspace/TejeshAutoReplyWeb/query.properties"));
		} 
		catch (FileNotFoundException e1) 
		{
			JOptionPane.showMessageDialog (null, "File not found", "Reply", JOptionPane.INFORMATION_MESSAGE);
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		for (String key : properties.stringPropertyNames())
		{
		   query.put(key, properties.get(key).toString());
		}
		for (String key : originalProperties.stringPropertyNames())
		{
			original.put(key, originalProperties.get(key).toString());
		}
		
		
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//PrintWriter out = response.getWriter();
	    //response.setContentType("text/html");
		String ans = "";
        String q=request.getParameter("mytext");
		try 
		{
			q1 = BestMatch.BestQuery(q,NonKeywords, query);
			//out.println("<html><body>");
			ans = query.get(q1);
			ans = escape(ans);
			ans = "<h3><p align=\"left\">" + ans + "</p></h3>";
			//out.println("<h3>"+ans+"</h3>");
			//out.println("</body></html>");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		request.setAttribute("ans", ans);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}
	
	public static String escape(String s) {
	    StringBuilder builder = new StringBuilder();
	    boolean previousWasASpace = false;
	    for( char c : s.toCharArray() ) {
	        if( c == ' ' ) {
	            if( previousWasASpace ) {
	                builder.append("&nbsp;");
	                previousWasASpace = false;
	                continue;
	            }
	            previousWasASpace = true;
	        } else {
	            previousWasASpace = false;
	        }
	        switch(c) {
	            case '<': builder.append("&lt;"); break;
	            case '>': builder.append("&gt;"); break;
	            case '&': builder.append("&amp;"); break;
	            case '"': builder.append("&quot;"); break;
	            case '\n': builder.append("<br>"); break;
	            // We need Tab support here, because we print StackTraces as HTML
	            case '\t': builder.append("&nbsp; &nbsp; &nbsp;"); break;  
	            default:
	                if( c < 128 ) {
	                    builder.append(c);
	                } else {
	                    builder.append("&#").append((int)c).append(";");
	                }    
	        }
	    }
	    return builder.toString();
	}
}
