import java.io.File;

public class Binary extends Floating{
	private String compressedCode;
	private int k=0;
	
	Binary(File toCompress) {
		super(toCompress);
		setK();
		//Compress();
		//System.out.println(compressedCode);
	}
	
	String Compress()
	{
		Double low=0.0,high=1.0;
		for(int i=0;i<sourceLength;i++)
		{
			Double lower=low+(high-low)*probabilities.get(Source[i])[0];
			Double higher=low+(high-low)*probabilities.get(Source[i])[1];
			if(lower<.5 && higher>.5) {
				low=lower;	high=higher;
			}
			else
			{
				while(true)
				{
					if(lower<.5 && higher>.5) {
						low=lower;	high=higher;
						break;
					}
					else if(lower>=.5 && higher>=.5)
					{
						Double[] low_high=E2(lower,higher);
						lower=low_high[0];		higher=low_high[1];
					}
					else if(lower<=.5 && higher<=.5)
					{
						Double[] low_high=E1(lower,higher);
						lower=low_high[0];		higher=low_high[1];
					}
					
				}
			}
		}
		compressedCode+='1';
		for(int i=1;i<k;i++)
		{
			compressedCode+='0';
		}
		return compressedCode;
	}
	
	public String Decompress(String code)
	{
		String compressed="";
		int b=1;
		for(int i=0;i<k;i++)
			b*=2;
		
		Double low=0.0,high=1.0;
		int length=code.length();
		for(int i=0;i<length-k;)
		{
			String t="";
			for(int j=i;j<k+i;j++)
				t+=code.charAt(j);
			int num=Integer.parseInt(t, 2);
			double freq=(double)num/b;
			freq=(freq-low)/(high-low);
			
			
			for(char x:probabilities.keySet())
			{
				if(freq>=probabilities.get(x)[0]&&freq<=probabilities.get(x)[1])
				{
					compressed+=x;
					Double lower=low+(high-low)*probabilities.get(x)[0];
					Double higher=low+(high-low)*probabilities.get(x)[1];
					
					if(lower<.5 && higher>.5) {
						low=lower;	high=higher;
					}
					else
					{
						while(true)
						{

							if(lower<.5 && higher>.5) {
								low=lower;	high=higher;
								break;
							}
							else if(lower>=.5 && higher>=.5)
							{
								Double[] low_high=E2(lower,higher);
								lower=low_high[0];		higher=low_high[1];
								i++;
							}
							else if(lower<=.5 && higher<=.5)
							{
								Double[] low_high=E1(lower,higher);
								lower=low_high[0];		higher=low_high[1];
								i++;
							}
							
						}
					}
				}
			}
		}
		return compressed;
	}
	
	void setK()
	{
		Double minProb=1.0;
		for(char x:probabilities.keySet())
		{
			if(probabilities.get(x)[2]<minProb)
				minProb=probabilities.get(x)[2];
		}
		
		Double minFrq=1.0;
		while(minFrq>minProb)
		{
			k++;
			minFrq/=2;
		}
	}
	
	Double[] E1(Double low,Double high)
	{
		low*=2;		high*=2;
		if(compressedCode==null)
			compressedCode=new String(String.valueOf('0'));
		else compressedCode+='0';
		Double[] toReturn= {low,high};
		return toReturn;
	}
	
	Double[] E2(Double low,Double high)
	{
		low=(low-0.5)*2;	high=(high-0.5)*2;
		Double[] toReturn= {low,high};
		if(compressedCode==null)
			compressedCode=new String(String.valueOf('1'));
		else compressedCode+='1';
		return toReturn;
	}
	

}
