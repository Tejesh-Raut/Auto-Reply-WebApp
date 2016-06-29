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
		String s, sf1, sf2, sf3, sf4, sf5, q1;
		sf1="";
		sf2="";
		sf3="";
		sf4="";
		sf5="";
		String[] ss,q1s;
		ArrayList<String> s1 = new ArrayList<String>();
		String[] allwords = KeepKeywords.SeparateWords(q);
		String Keywords = KeepKeywords.Keywords(allwords, NonKeywords);
		double d, dmin1, dmin2, dmin3, dmin4, dmin5, numw, ld, score;
		d = 1000;
		dmin1 = 1000;
		dmin2 = 1000;
		dmin3 = 1000;
		dmin4 = 1000;
		dmin5 = 1000;
		numw = 0.0;
		for (Map.Entry<String,String> entry : query.entrySet())// loop through each query in the database
		{
			d = 1000;
			s = entry.getKey();// a query from map(database)
			ss = s.split("\\s+");//array of words of the query from map(database)
			//Try all ways of asking q
			ArrayList<String> qways = new ArrayList<String>();
			qways = getAllWays(Keywords);
			for(int i=0; i<qways.size(); i++)// loop through each way of writing the query asked
			{
				q1 = qways.get(i);
				q1s = q1.split("\\s+");
				numw=0;
				for(int j=0; j<q1s.length; j++)
				{
					for(int k=0; k<ss.length; k++)
					{
						if(q1s[j].equalsIgnoreCase(ss[k]))
						{
							numw++;
						}
					}
				}
				//numw represents the number of words matching between q1 and s
				ld = StringUtils.getLevenshteinDistance(s, q1);
				if(numw ==0)
				{
					score = 1000;
				}
				else
				{
					score = ld/numw;
				}
				System.out.println(s);
				System.out.println(q1);
				System.out.println("Levenshtein Distance "+ld);
				System.out.println("Number of words matching are "+ numw);
				System.out.println(score);
				System.out.println("********************************************************************");
				if(d > (score ))
				{
					d = score;
				}
			}
			if(dmin1 >= d)
			{
				sf5 = sf4;
				sf4 = sf3;
				sf3 = sf2;
				sf2 = sf1;
				sf1 = s;
				dmin5 = dmin4;
				dmin4 = dmin3;
				dmin3 = dmin2;
				dmin2 = dmin1;
				dmin1 = d;
			}
			else
			{
				if(dmin2 >= d)
				{
					sf5 = sf4;
					sf4 = sf3;
					sf3 = sf2;
					sf2 = s;
					dmin5 = dmin4;
					dmin4 = dmin3;
					dmin3 = dmin2;
					dmin2 = d;
				}
				else
				{
					if(dmin3 >= d)
					{
						sf5 = sf4;
						sf4 = sf3;
						sf3 = s;
						dmin5 = dmin4;
						dmin4 = dmin3;
						dmin3 = d;
					}
					else
					{
						if(dmin4 >= d)
						{
							sf5 = sf4;
							sf4 = s;
							dmin5 = dmin4;
							dmin4 = d;
						}
						else
						{
							if(dmin5 >= d)
							{
								sf5 = s;
								dmin5 = d;
							}
						}
					}
				}
			}
		}
		s1.add(sf1);
		s1.add(sf2);
		s1.add(sf3);
		s1.add(sf4);
		s1.add(sf5);
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