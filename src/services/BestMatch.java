package services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

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
		return s1;
	}
	
	public static ArrayList<String> getAllWays (String q) throws FileNotFoundException
	{
		ArrayList<String> qways = new ArrayList<String>();
		String[] qs, syns;
		String syn;
		qs = q.split("\\s+");
		if(qs.length == 1)
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
			for(int i=1; i<qs.length; i++)
			{
				q1 = q1 + " " + qs[i];
			}
			//q1 = string formed after removing first word
			for(int i=0; i<syns.length;i++)
			{
				ArrayList<String> qways1 = new ArrayList<String>();
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
		File file = new File("workspace/TejeshAutoReplyWeb/Synonyms.txt");
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