package services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class BestMatch 
{
	public static String BestQuery(String q , String[] NonKeywords, Map<String, String> query) throws Exception
	{
		String s,s1, syn;
		s1="";
		String[] ss,qs, syns;
		String[] allwords = KeepKeywords.SeparateWords(q);
		String Keywords = KeepKeywords.Keywords(allwords, NonKeywords);
		qs = Keywords.split("\\s+");// words from the query string
		int c=0;
		int cmax = 0;
		for (Map.Entry<String,String> entry : query.entrySet()) 
		{
			c=0;
			s = entry.getKey();// a query from map(database)
			ss = s.split("\\s+");//array of words of the query from map(database)
			for (int i=0; i<qs.length; i++)
			{
				for (int j=0; j<ss.length; j++)
				{
					syn = synonym(ss[j]);
					syns = syn.split("\\s+");//array of words of synonyms
					for(int k=0; k<syns.length;k++)
					{
						if(qs[i].equalsIgnoreCase(syns[k]))
						{
							c++;
						}
					}
					
				}
			}
			if(cmax<c)
			{
				cmax = c;
				s1 = s;
			}
		    //System.out.println(entry.getKey()+entry.getValue());
		}
		return s1;
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
