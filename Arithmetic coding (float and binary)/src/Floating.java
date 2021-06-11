import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Floating {
	protected Map<Character,Double[]> probabilities=new HashMap<Character,Double[]>();
	protected char[]Source;
	protected int sourceLength;
	
	Floating(File toCompress)
	{
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
			Source=words;
			sourceLength=counter;
			if(counter>0) 
				entropy(words,counter);
		}catch(Exception e) {	}
				
				
		/*for(Map.Entry x:probabilities.entrySet())
		{
			Character z=(Character) x.getKey();
			System.out.println(z+" : "+probabilities.get(z)[0]+", "+probabilities.get(z)[1]+", "+probabilities.get(z)[2]);
		}*/
		//System.out.println(compressedCode);
	}
	
	public Double compress()
	{
		Double compressedCode = null;
		Double low=0.0,high=1.0;
		for(int i=0;i<sourceLength;i++)
		{
			Double lower=low+(high-low)*probabilities.get(Source[i])[0];
			Double higher=low+(high-low)*probabilities.get(Source[i])[1];
			low=lower;	high=higher;
		}
		compressedCode=(low+high)/2;		
					

		return compressedCode;
	}
	
	public String Decompress(Double code)
	{
		String compressed=null;
		
		Double low=0.0,high=1.0,toGetCharCode=code;
		for(int i=0;i<sourceLength;i++)
		{
			for(char x:probabilities.keySet())
			{
				if(toGetCharCode>probabilities.get(x)[0]&&toGetCharCode<probabilities.get(x)[1]) {
					if(compressed==null)
						compressed=new String(String.valueOf(x));
					else
						compressed+=x;
					Double lower=low+(high-low)*probabilities.get(x)[0];
					Double higher=low+(high-low)*probabilities.get(x)[1];
					low=lower;	high=higher;
					toGetCharCode=(code-low)/(high-low);
				}
			}
				
			
		}
		
		return compressed;
	}
	
	public void entropy(char[] words,int size)
	{
		Double[] frq=new Double[3];
		int index=0;
		for(int i=0;i<size;i++)
		{
			frq=new Double[3];
			if(probabilities.containsKey(words[i])) {
				frq[2]=probabilities.get(words[i])[2]+1;
				probabilities.put(words[i],frq);
			}
			else
			{
				frq[2]=1.0;
				probabilities.put(words[i],frq);
			}
		}
		for(char x: probabilities.keySet())
		{	
			frq=new Double[3];
			frq=probabilities.get(x);
			frq[2]/=size;			
			if(index==0)
			{
				frq[0]=0.0;	frq[1]=frq[2];
			}
			else 
			{
				char before = 0;
				for(Map.Entry z:probabilities.entrySet())
				{
					if(z.getKey().equals(x))
						break;
					before=(char) z.getKey();
				}
				frq[0] =probabilities.get(before)[1];
				if((index==probabilities.size())) 
					frq[1]=1.0;
				else 
					frq[1]=probabilities.get(x)[2]+frq[0];
				
			}
			index++;
			probabilities.put(x,frq);
		}
	}
	
	
}
