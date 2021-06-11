import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class az77 {
	
	public static Integer[] comparison(char[] arr,int size,int position,int lengt)
	{
		int back=0,take=0;
		Integer x[]={back,0};
		if(position==0)
			return x;
		else
			for(int i=position-1;i>=0;i--)
			{
				if(arr[position]==arr[i])
				{
					int isLarge=0;
					for(int j=1;j<size-1;j++)
					{						
						if(arr[position+j]==arr[i+j]) {	
							
							isLarge++;}
						else
							break;
					}
					if(isLarge>=take)
					{
						take=isLarge+1;
						back=position-i;
					}
				}
			}
			
			x= new Integer[]{back,take};
			
		return x;
	}

	public static File compress(File toCompress) 
	{
		char[] words = new char[1000];		int counter=0;
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
				File compressed=new File("Compressed.txt");
				try {
					int length=0;	ArrayList<Integer[]> arr=new ArrayList<>();
					compressed.createNewFile();
					FileWriter write= new FileWriter("Compressed.txt");
					for(int i=0;i<counter;i++) {
						/*arr.add(comparison(words, counter,i,length));
						System.out.println("at "+i+"\t'"+arr.get(i)[0]+"' , '"+arr.get(i)[1]+"'");
						i+=arr.get(i)[1];*/
						
						/*if(i<counter)
							write.write(comparison(words, counter,i,length)[0]+" "+comparison(words, counter,i,length)[1]+" "+words[i]+'\n');
						else
							write.write(comparison(words, counter,i,length)[0]+" "+comparison(words, counter,i,length)[1]+" "+'\0');*/
						int courr=i;
						if(comparison(words, counter,i,length)[1]!=0);
							i+=comparison(words, counter,i,length)[1];
							
						if(courr<counter)
							write.write(comparison(words, counter,courr,length)[0]+" "+comparison(words, counter,courr,length)[1]+" "+words[i]+'\n');
						else
							write.write(comparison(words, counter,courr,length)[0]+" "+comparison(words, counter,courr,length)[1]+" "+'\0');
							
						/*if(comparison(words, counter,i,length)[1]>comparison(words, counter,i,length)[0])
							i+=comparison(words, counter,i,length)[1]-comparison(words, counter,i,length)[0];
						else
							i+=comparison(words, counter,i,length)[0]-comparison(words, counter,i,length)[1];*/
					}
					write.close();
					return compressed;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return toCompress;
		
	}
	
	
	public static String decompress(File toDecompress) throws FileNotFoundException
	{
		String decompressedText = "";
		Scanner scan=new Scanner(toDecompress);
		while(scan.hasNext())
		{
			int position=scan.nextInt(), length=scan.nextInt();
			char nextChar =scan.next().charAt(0);
			
			for(int i=0;i<length;i++)
			{
				//System.out.println(i);
				decompressedText+=decompressedText.charAt(decompressedText.length()-position);

			}
			if(nextChar!='\0')
				decompressedText+=nextChar;
			scan.nextLine();
		}
		scan.close();
		
		return decompressedText;
	}
	
	public static void main(String[] args) {	
		try {
			File suorceFile=new File("Suorce.txt");
			File compressd=compress(suorceFile);
			System.out.println(decompress(compressd));
			
		}catch(Exception e)
		{
			
		}

	}

}
