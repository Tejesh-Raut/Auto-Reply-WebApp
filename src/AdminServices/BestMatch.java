package AdminServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

import services.KeepKeywords;

/**
 * @author Tejesh_Raut
 *
 */
public class BestMatch 
{
	public static ArrayList<String> BestQuery(String q , String[] NonKeywords, Map<String, String> query) throws Exception
	{
		String s, s2;
		s2="";
		ArrayList<String> s1 = new ArrayList<String>();
		String[] allwords = KeepKeywords.SeparateWords(q);
		String Keywords = KeepKeywords.Keywords(allwords, NonKeywords);
		int d, dmin;
		d = 1000;
		dmin = 1000;
		for (Map.Entry<String,String> entry : query.entrySet())// loop through each query in the database
		{
			d = 1000;
			s = entry.getKey();// a query from map(database)
			//Try all ways of asking q
			ArrayList<String> qways = new ArrayList<String>();
			qways = getAllWays(Keywords);
			for(int i=0; i<qways.size(); i++)
			{
				if(d > StringUtils.getLevenshteinDistance(q, qways.get(i)))
				{
					d = StringUtils.getLevenshteinDistance(q, qways.get(i));
				}
			}
			if(dmin > d)
			{
				s2 = s;
				dmin = d;
			}
		}
		s1.add(s2);
		s1.add("Please ask your query in other words");
		return s1;
	}
	
	public static ArrayList<String> getAllWays (String q) throws FileNotFoundException
	{
		ArrayList<String> qways = new ArrayList<String>();
		ArrayList<String> qways1 = new ArrayList<String>();
		String[] qs, syns;
		String syn;
		qs = q.split("\\s+");
		if(qs.length == 1)//query is of only 1 word
		{
			syn = synonym(qs[0]);
			syns = syn.split("\\s+");
			for(int i=0; i<syns.length;i++)
			{
				qways.add(syns[i]);
			}
			return(qways);
		}
		else
		{
			syn = synonym(qs[0]);
			syns = syn.split("\\s+");
			String q1;
			q1="";
			for(int i=0; i<q.length(); i++)
			{
				if(q.charAt(i) == ' ')
				{
					for(int j=(i+1); j<q.length(); j++)
					{
						q1 = q1 + q.charAt(j);
					}
					break;
				}
				
			}
			//q1 = string formed after removing first word
			for(int i=0; i<syns.length;i++)
			{
				qways1 = getAllWays(q1);
				for(int j=0; j<qways1.size(); j++)
				{
					qways.add(qs[0] + " "+ qways1.get(j));
				}
			}
			
			return(qways);
		}
	}
	
	public static String synonym(String s) throws FileNotFoundException
	{
		String syns = s + " ";
		File file = new File("Synonyms.txt");
		Scanner in = null;
		in = new Scanner(file);
		while(in.hasNext())
		{
			String line = in.nextLine();
			if(line.contains(s))
			{
				syns = line;
			}
		}
		in.close();
		return syns;
	}
}