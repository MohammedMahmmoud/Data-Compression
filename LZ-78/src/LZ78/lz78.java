package LZ78;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class lz78 {
	
	public static Map<Integer, Pair<Integer,String>> compress(File toCompress) 
	{
		Map<Integer, Pair<Integer,String>> compressedMap = new HashMap();
		char[] words = new char[1000];	
		int counter=0;
		Scanner scan;
		try {
			scan = new Scanner(toCompress);
			while(scan.hasNext())
			{
				String word=scan.nextLine();
				for(int i=0;i<word.length();i++)
				{
					words[counter]=word.charAt(i);
					counter++;
				}
				if(scan.hasNext())
				{
					words[counter]='\n';
					counter++;
				}
			}
			
			if(counter>0)
			{
				Pair strat=null;
				compressedMap.put(0, strat);
				String T="";	int count=1, index=0;;
				for(int i=0;i<counter;i++)
				{
					boolean isExist=false;		
					for(int j=1;j<compressedMap.size();j++)
					{						
						String text=T+words[i];
						if(text.equals(compressedMap.get(j).getSecond()))
							isExist=true;
						
					}
					if(isExist)
						if(i+1==counter)
						{
							String text=T+words[i];
							Pair end=new Pair(0,text);
							compressedMap.put(count,end);
						}
						else
							T+=words[i];
					
					else 
					{
						String text=T+words[i];
						for(int j=1;j<compressedMap.size();j++)
						{
							if(T.equals(compressedMap.get(j).getSecond()))
								index=j;
						}
						Pair nextChar=new Pair(index,text);
						compressedMap.put(count, nextChar);
						count++;	
						T=new StringBuilder(words[i]).toString();
					}
				}
				
				return compressedMap;
			}
		}catch(Exception e)
		{
			
		}
		return compressedMap;
		
	}
	
	public static String decompress(Map<Integer,Pair<Integer,String>> todecompress)
	{
		String word = "";
		for(int n=1;n<todecompress.size();n++)
		{
			Pair<Integer, String> one=todecompress.get(n);
			int toTake=todecompress.get(n).getFirst();
			if(toTake==0)
				word+=todecompress.get(n).getSecond();
			else 
				word+=todecompress.get(toTake).getSecond()+todecompress.get(n).getSecond();
				
		}
		return word;
	}

	public static void main(String[] args) {
		try {
			File suorceFile=new File("Suorce.txt");
			Map<Integer,Pair<Integer,String>> compressed =compress(suorceFile);
			for(int i=1;i<compressed.size();i++)
				System.out.println(compressed.get(i).getFirst()+".."+compressed.get(i).getSecond());
			System.out.println(decompress(compressed));
			
		}catch(Exception e)
		{
			
		}

	}

}
