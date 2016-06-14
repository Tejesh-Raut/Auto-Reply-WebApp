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
		PrintWriter out = response.getWriter();
	    //response.setContentType("text/html");
		String q=request.getParameter("mytext");
		try 
		{
			String q1 = BestMatch.BestQuery(q,NonKeywords, query);
			out.println("<html><body>");
			out.println("<h1>Welcome, "+query.get(q1)+"</h1>");
			out.println("</body></html>");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	/*
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	*/
}
