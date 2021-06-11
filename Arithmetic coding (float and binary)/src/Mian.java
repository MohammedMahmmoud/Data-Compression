import java.io.File;

public class Mian {

	public static void main(String[] args) {
		File sourceFile=new File("Suorce.txt");
		Floating Fcompressor=new Floating(sourceFile);
		Double floatingCompressed=Fcompressor.compress();
		System.out.println(Fcompressor.Decompress(floatingCompressed));
		
		Binary Bcompressor=new Binary(sourceFile);
		String binaryCompressed=Bcompressor.Compress();
		//System.out.println(binaryCompressed);
		System.out.println(Bcompressor.Decompress(binaryCompressed));
		

	}

}
