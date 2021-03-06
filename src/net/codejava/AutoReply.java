package net.codejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import AdminServices.Special;
import services.BestMatch;

/**
 * @author Tejesh_Raut
 *
 */
@WebServlet("/AutoReply")
public class AutoReply extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Map<String, String> query, original;
	Properties properties, originalProperties;
	String[] NonKeywords;
	ArrayList<String> q1;
	int i;
    public AutoReply() throws FileNotFoundException 
    {
        super();
        
        query = new HashMap<String, String>();
		original = new HashMap<String, String>();
        i =1;
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String ans = " ";
        String q=request.getParameter("mytext");
        String submit=request.getParameter("ask");
        String notsatisfied=request.getParameter("notsatisfied");
        if(submit!=null)
        {
        	i=1;
        	try 
    		{
    			q1 = BestMatch.BestQuery(q,NonKeywords, query);
    			if(q1.get(0)=="*")//special query
				{
					ans = Special.Reply(q1.get(1), q1.get(2), q1.get(3));
        			ans = "<h3><p align=\"left\">" + ans + "</p></h3>";
				}
    			else
    			{
    				ans = query.get(q1.get(0));
        			ans = escape(ans);
        			ans = "<h3><p align=\"left\">" + ans + "</p></h3>";
    			}
    		} 
    		catch (Exception e) 
    		{
    			e.printStackTrace();
    		}
    		request.setAttribute("ans", ans);
    		request.getRequestDispatcher("index.jsp").forward(request, response);
    		return;
        }
        else
        {
        	while((notsatisfied !=null)&&(i<q1.size()))
        	{
        		ans = query.get(q1.get(i));
        		ans = escape(ans);
        		ans = "<h3><p align=\"left\">" + ans + "</p></h3>";
        		request.setAttribute("ans", ans);
        		request.getRequestDispatcher("index.jsp").forward(request, response);
        		i++;
        		return;
        	}
        	if(i>=q1.size())
			{
        		ans = "These were the best matching results. <br> For better results ask your query in another way.";
        		ans = "<h3><p align=\"left\">" + ans + "</p></h3>";
        		request.setAttribute("ans", ans);
        		request.getRequestDispatcher("index.jsp").forward(request, response);
        		return;
			}
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}
	
	public static String escape(String s) //copied from stackoverflow
	{
	    StringBuilder builder = new StringBuilder();
	    boolean previousWasASpace = false;
	    for( char c : s.toCharArray() ) 
	    {
	        if( c == ' ' ) 
	        {
	            if( previousWasASpace ) 
	            {
	                builder.append("&nbsp;");
	                previousWasASpace = false;
	                continue;
	            }
	            previousWasASpace = true;
	        } 
	        else 
	        {
	            previousWasASpace = false;
	        }
	        switch(c) 
	        {
	            case '<': builder.append("&lt;"); break;
	            case '>': builder.append("&gt;"); break;
	            case '&': builder.append("&amp;"); break;
	            case '"': builder.append("&quot;"); break;
	            case '\n': builder.append("<br>"); break;
	            case '\t': builder.append("&nbsp; &nbsp; &nbsp;"); break;  
	            default:
	                if( c < 128 ) 
	                {
	                    builder.append(c);
	                } 
	                else 
	                {
	                    builder.append("&#").append((int)c).append(";");
	                }    
	        }
	    }
	    String converted = builder.toString();
        String str = "(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:\'\".,<>?«»“”‘’]))";
        Pattern patt = Pattern.compile(str);
        Matcher matcher = patt.matcher(converted);
        converted = matcher.replaceAll("<a href=\"$1\">$1</a>");
        return converted;
	}
}
