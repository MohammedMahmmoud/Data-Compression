import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Huffman {

	private static Map<Character,String> compressed=new HashMap<Character,String>();

	public static Map<Character, Double> entropy(char[] words,int size)			//calculate probability of each character
	{
		Map<Character,Double> findChar=new HashMap<Character,Double>();
		
		for(int i=0;i<size;i++)
		{
			if(findChar.containsKey(words[i]))
				findChar.put(words[i],findChar.get(words[i])+1);			//increasing the counter of the char
			else
				findChar.put(words[i],(double) 1);							//count the first char "+1"	
		}
		for(char x: findChar.keySet())
		{
			findChar.put(x,findChar.get(x)/size);							//probability =count/size
		}
		return findChar;
	}
	
	public static void toSort(double []fraq,StringBuilder[]Chars,ArrayList<Pair<String,Double>> huff, int size,int i)
	{
		fraq[i+1]+=fraq[i];								//concatenate the probabilities together
		Chars[i+1].append(Chars[i]).toString();			//concatenate characters together
		Pair<String,Double> pair=new Pair<String, Double>(Chars[i+1].toString(),fraq[i+1]);
		huff.add(pair);									//storing the concatenated char and it's probability 
		for(int j=i+1;j<size;j++)
		{
			if(fraq[i+1]>fraq[j])						//check if the new concatenated probability is in the right place in the array if not resorting
			{
				int ShiftIndex=i+1;
				for(int k=j;k<size;k++)					//shifting to the right place
				{
					if(fraq[ShiftIndex]>fraq[k])
					{
						double temp=(double)fraq[k];	StringBuilder Ctemp=Chars[k];
						fraq[k]=fraq[ShiftIndex];	fraq[ShiftIndex]=temp;
						Chars[k]=Chars[ShiftIndex];	Chars[ShiftIndex]=Ctemp;
						ShiftIndex=k;
					}
					
				}
				break;
			}
				
		}
		
	}
	
	public static void compress(File toCompress,File compressFile)
	{
		char[] words = new char[1000];	
		ArrayList<Pair<String,Double>>huff = new ArrayList();
		int counter=0;
		Scanner scan;
		try {
			scan = new Scanner(toCompress);
			while(scan.hasNext())									//storing the characters of the file in "words" array
			{
				String word=scan.nextLine();
				for(int i=0;i<word.length();i++)
				{
					words[counter]=word.charAt(i);
					counter++;
				}
				if(scan.hasNext())									//in case multiple lines
				{
					words[counter]='\n';
					counter++;
				}
			}
			
			if(counter>0)								//comprising in case there are characters in the array
			{
				Map<Character, Double> prob=entropy(words, counter);		//here is each char and it's probability
				double[] probs = new double[prob.size()];		StringBuilder[] characters= new StringBuilder[prob.size()];		//containing the concatenate chars and probabilities
				
				double min=2.0;
				for(int i=0;i<prob.size();i++)				//sorting the probabilities
				{
					min=2.0;
					for(double x : prob.values())
					{	
						if(x<min&&i==0)
							min=x;
						else if(x<min && x>probs[i-1])
							min=x;
					}
					probs[i]=min;						//store the sorted probabilities
				}
				for(char x: prob.keySet())				//storing the characters in the same index as it's probability 
				{
					for(int i=0;i<prob.size();i++)
						if(prob.get(x)==probs[i])
							characters[i]=new StringBuilder().append(x);
				}
				
				
				
				for(int i=0;i<prob.size()-2;i++)		//concatenating tell having 2 bigger probabilities (last two)
					toSort(probs, characters,huff,prob.size(), i);
				
				char one=0,two=0;
				if(huff.size()>=1)
				{
					one=huff.get(huff.size()-1).first.charAt(0);
					if(huff.size()>=2)
						two=huff.get(huff.size()-2).first.charAt(0);
				}
				String[] charCode= {"0","1"};
				for(int i=huff.size()-1;i>=0;i--)
				{			
					if(huff.get(i).first.length()==1) {
						if(huff.get(i).first.charAt(0)==one)
							compressed.put(huff.get(i).first.charAt(0),charCode[0]);
						else
							compressed.put(huff.get(i).first.charAt(0),charCode[1]);
					}
					else if(huff.get(i).first.length()==2)
					{
						char firstLetter=huff.get(i).first.charAt(0);
						char lastLetter=huff.get(i).first.charAt(huff.get(i).first.length()-1);
						
						if(huff.get(i).first.charAt(0)==one)
						{
							String code1=charCode[0],code2=charCode[0];
							charCode[0]+="0";
							code2+="1";
							code1+="0";
							compressed.put(firstLetter,code1);
							compressed.put(lastLetter,code2);
						}
						else
						{
							String code1=charCode[1],code2=charCode[1];
							charCode[1]+="0";
							code2+="1";
							code1+="0";
							compressed.put(firstLetter,code1);
							compressed.put(lastLetter,code2);
						}
					}
					else
					{	
						for(int j=i-1;j>=0;j--) {
							String fullWord = "";	char[] ch = huff.get(i).first.toCharArray(); 
							char c=huff.get(i).first.charAt(huff.get(i).first.length()-1);
							fullWord=String.copyValueOf(ch,0,huff.get(i).first.length()-1);
							if(huff.get(j).first.charAt(0)==huff.get(i).first.charAt(0))
							{
								if(huff.get(i).first.charAt(0)==one)
								{
									String code=charCode[0];
									charCode[0]+="0";
									code+="1";
									//System.out.println(c+"-->>"+code);
									compressed.put(c,code);
								}
								else
								{
									String code=charCode[1];
									charCode[1]+="0";
									code+="1";
									compressed.put(c,code);
								}
								break;
							}
						}
					}
				}
				
				for(char x:compressed.keySet())
					System.out.println(x+" : "+compressed.get(x));
				
				compressFile.createNewFile();
				
				FileWriter myWriter = new FileWriter(compressFile);
				for(int i=0;i<counter;i++)
					for(char x:compressed.keySet())
						if(words[i]==x)
							myWriter.write(compressed.get(x));
				myWriter.close();
			}
		}catch(Exception e)
		{
			
		}
	}
	
	public static void Decompress(File compressedFile)
	{
		Scanner scan = null;
		try {
			scan = new Scanner(compressedFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String word="";
		while(scan.hasNext())									
		{
			word=scan.nextLine();
		}
		for(int i=0;i<word.length();i++)
		{
			String getChar="";
			getChar+=word.charAt(i);
			boolean charPrinted=false;
			while(!charPrinted)
			{
				for(char x:compressed.keySet())
					if(getChar.equals(compressed.get(x)))
					{
						System.out.print(x);
						charPrinted=true;
					}
				if(!charPrinted)
				{
					i++;
					getChar+=word.charAt(i);
				}
			}
			
		}
			
	}
	
	
	public static void main(String args[])
	{
		File suorceFile=new File("Suorce.txt");
		File compressed=new File("compressed.txt");
		System.out.println("compress code for each letter :");
		compress(suorceFile,compressed);
		System.out.println("uncompressed");
		Decompress(compressed);
	}
}
