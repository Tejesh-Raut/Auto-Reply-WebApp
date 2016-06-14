package services;

import java.util.ArrayList;

public class KeepKeywords 
{
	public static String[] SeparateWords(String query) throws Exception
	{
		String s = query;
		s = s.replaceAll("[^\\w\\s\\d' -]"," ");//removes all characters except ', -, alphabets, spaces, digits
		s = s.replaceAll("'s ", " ");// remove all 's
		s = s.replaceAll("-", " ");// replace - with spaces
		String[] words = s.split("\\s+");
		return words;
	}
	
	public static String Keywords(String[] allwords, String[] NonKeywords) throws Exception
	{
		ArrayList<String> Keywords = new ArrayList<String>();
		int c=0;
		for (int i=0; i<allwords.length; i++)
		{
			c=0;
			for (int j=0; j<NonKeywords.length; j++)
			{
				if(allwords[i].equalsIgnoreCase(NonKeywords[j]))
				{
					c = 1;//denotes that given word is not a keyword
				}
			}
			if(c==0)
			{
				Keywords.add(allwords[i]);
			}
		}


		String listString = "";
		
		for (String s : Keywords)
		{
		    listString += s + " ";
		}
		return listString;
	}
}
