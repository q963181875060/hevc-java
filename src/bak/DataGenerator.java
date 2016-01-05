package bak;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Generate the input for FPGA entropy encoder
 * @author OwenLiu
 *
 */
public class DataGenerator {
	
	public static void main(String[] args){
		DataGenerator.test();
	}
	
	public static String generateStateStr(int ctxIdx, int binValue, int etpy){
		int state = 0;
		switch(etpy){
		case 0:
			state = 1;
			break;
		case 1:
			state = 2;
			break;
		case 2:
			state = 4;
			break;
		}
		state = (state << 1) + binValue;
		state = (state << 8) + ctxIdx;
		return state+"";
	}
	
	public static void test(){
/*		DFEVar ctxIdx = input.slice(0, 8).cast(dfeUInt(8));
		DFEVar binValue = input.slice(8, 1).cast(dfeUInt(1));
		DFEVar etpy = input.slice(9, 7).cast(dfeUInt(7));

		DFEVar etpyIsBin = etpy.slice(TP_ENC_BIN_____BIT,1).cast(dfeBool());
		DFEVar etpyIsEP_ = etpy.slice(TP_ENC_BIN_EP__BIT,1).cast(dfeBool());
		DFEVar etpyIsTRM = etpy.slice(TP_ENC_BIN_TRM_BIT,1).cast(dfeBool());*/
		try{
			File sequenceFile = new File("seqs00000000.py");
			File dataFile = new File("data-ivlLow_ivlCurrRange_bitLeft.txt");
			
			ArrayList<String> result = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(sequenceFile));
			String line = br.readLine().trim();//the first line "test_seq", ignore
			
			while(!(line = br.readLine().trim()).contains("]")){
				String[] strs = line.split(",");
				strs[0] = strs[0].substring(1);
				strs[strs.length-1] = strs[strs.length-1].substring(0, strs[strs.length-1].length()-1);
				
				int[] state = new int[strs.length];
				for(int i=0;i<strs.length;i++){
					state[i] = Integer.parseInt(strs[i]);
				}
				
				int etpy = Integer.parseInt(strs[0]);
				int ctxIdx = 0;
				int binValue = 0;
				
				switch(state[0]){
				case 1:
					etpy = 0;
					ctxIdx = state[2];
					binValue = state[1];
					result.add(DataGenerator.generateStateStr(ctxIdx, binValue, etpy));
					break;
				case 2:
					etpy = 1;
					ctxIdx = 0;
					binValue = state[1];
					result.add(DataGenerator.generateStateStr(ctxIdx, binValue, etpy));
					break;
				case 3:
					etpy = 1;
					ctxIdx = 0;
					for(int i=state[2]-1; i>=0; i--){
						binValue = ((state[1] & (1 << i)) > 0)?1:0;
						result.add(DataGenerator.generateStateStr(ctxIdx, binValue, etpy));
					}
					break;
				case 4:
					etpy = 1;
					ctxIdx = 0;
					binValue = state[1];
					result.add(DataGenerator.generateStateStr(ctxIdx, binValue, etpy));
					break;
				}
				
			}
			if(dataFile.exists()){
				dataFile.delete();
			}
			dataFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile));
			bw.write(result.size()+"\n");
			for(String str: result){
				bw.write(str+"\n");
			}
			
			br.close();
			bw.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		
	}
}
