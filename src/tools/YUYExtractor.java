package tools;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class YUYExtractor{
	
	public static void main(String[] args){
		try {
			int rowNum = 480;
			int colNum = 832;
			
			
			int toRowNum = 480;
			int toColNum = 832;
			
			byte[][] bs = new byte[rowNum][colNum];
			byte[][] crs = new byte[rowNum/2][colNum/2];
			byte[][] cbs = new byte[rowNum/2][colNum/2];
			File inputFile = new File("BasketballDrill_832x480_50.yuv");
			File outputFile = new File(toColNum+"-"+toRowNum+"-basketball.yuv");
			if(!inputFile.exists()){
				return;
			}
			if(outputFile.exists()){
				outputFile.delete();
			}
			outputFile.createNewFile();
			
			FileInputStream fi = new FileInputStream(inputFile);
			for(int i=0;i<rowNum;i++){
				for(int j=0;j<colNum;j++){
					//for(int t=0;t<3;t++){
						bs[i][j] = (byte)fi.read();
					//}
				}
			}
			for(int i=0;i<rowNum/2;i++){
				for(int j=0;j<colNum/2;j++){
					//for(int t=0;t<3;t++){
						crs[i][j] = (byte)fi.read();
					//}
				}
			}
			
			for(int i=0;i<rowNum/2;i++){
				for(int j=0;j<colNum/2;j++){
					//for(int t=0;t<3;t++){
						cbs[i][j] = (byte)fi.read();
					//}
				}
			}
			
			FileOutputStream fo = new FileOutputStream(outputFile);
			for(int i=0;i<toRowNum;i++){
				for(int j=0;j<toColNum;j++){
					//for(int t=0;t<3;t++){
						fo.write(bs[i][j]);
					//}
				}
			}
			
			for(int i=0;i<toRowNum/2;i++){
				for(int j=0;j<toColNum/2;j++){
					//for(int t=0;t<3;t++){
					fo.write(crs[i][j]);
					//}
				}
			}
			
			for(int i=0;i<toRowNum/2;i++){
				for(int j=0;j<toColNum/2;j++){
					//for(int t=0;t<3;t++){
					fo.write(cbs[i][j]);
					//}
				}
			}
			
			
			fi.close();
			fo.flush();
			fo.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}