package tools;

import java.io.File;
import java.io.FileInputStream;

public class HevcGetHeader {

	public static void main(String[] args){
		try{
			
			File file = new File("64-64.bin");
			int amount = 0x50;
			FileInputStream fis = new FileInputStream(file);
			System.out.print("{");
			for(int i=0;i<amount;i++){
				if(i == amount - 1){
					System.out.print((int)fis.read()+"}");
				}else{
					System.out.print((int)fis.read()+" ,");
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
}
