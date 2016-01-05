package bak;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class EntropyEncoder {
	
	public int ivlLow;
	public int ivlCurrRange;
	public int bitsLeft;

	public ArrayList<Integer> outputBytes;
	public int[] ctxTable;

	public int bufferedByte;
	public int numBufferedBytes;
	
	public int[] stdOutputBytes;
	
	public EntropyEncoder(int[] ctxTable,int[] stdOutputBytes){
		this.ivlLow = 0;
		this.ivlCurrRange = 510;
		this.bitsLeft = -12;

        this.outputBytes = new ArrayList<Integer>();
        this.ctxTable = ctxTable.clone();

        this.bufferedByte = 0xff;
        this.numBufferedBytes = 0;

        this.stdOutputBytes = stdOutputBytes;
	}
	
	 
    public void checkAnswer(){
        if(stdOutputBytes == null){
        	return;
        }
           
        if(this.stdOutputBytes.length != this.outputBytes.size()){
        	System.out.println("Different Length:stdOutputBytes len="+this.stdOutputBytes.length+" outputBytes len="+this.outputBytes.size());
        	return;
        }
        
        for(int i=0;i<this.outputBytes.size();i++){
        	if(this.stdOutputBytes[i] != this.outputBytes.get(i)){
        		System.out.println("stdOutputBytes[" + i + "]="+this.stdOutputBytes[i]+" outputBytes[" + i + "]="+this.outputBytes.get(i)); 
        	}
        }

    }
	
	public void outputByte(int db){
		outputBytes.add(db);
	    if(stdOutputBytes == null){
	    	return;
	    }
	    if(outputBytes.size()>stdOutputBytes.length){
	    	System.out.println("Different Length:stdOutputBytes len="+this.stdOutputBytes.length+" outputBytes len="+this.outputBytes.size());
	    	return;
	    }
	    int i = outputBytes.size()-1;
	    if(stdOutputBytes[i]!=db){
	    	//ipdb.set_trace()
	    	System.out.println("stdOutputBytes[" + i + "]="+this.stdOutputBytes[i]+" outputBytes[" + i + "]="+this.outputBytes.get(i));
	    }
	}
	
	public void finish(){
		if ((ivlLow >>> (21 + bitsLeft)) != 0)
	    {
			outputByte(bufferedByte + 1);
	        while (numBufferedBytes > 1)
	        {
	            outputByte(0x00);
	            numBufferedBytes--;
	        }

	        ivlLow -= 1 << (21 + bitsLeft);
	    }
	    else
	    {
	        if (numBufferedBytes > 0)
	        	outputByte(bufferedByte);

	        while (numBufferedBytes > 1)
	        {
	        	outputByte(0xff);
	            numBufferedBytes--;
	        }
	    }
		//outputByte(ivlLow >> 8, 13 + bitsLeft);
	}
	
	public void renormE(){
        if(bitsLeft<0){
        	return;
        }
        System.out.println("pre ivLow=\t"+Integer.toBinaryString(ivlLow));
        
        int leadByte = ivlLow >>> (13+bitsLeft);
        
        System.out.println("leadByte=\t"+Integer.toBinaryString(leadByte));
        
        ivlLow &= (0xffffffff >>> (19-bitsLeft));
        
        System.out.println("after ivlLow=\t"+Integer.toBinaryString(ivlLow));
        bitsLeft -= 8;
        if(leadByte==0xff){
        	numBufferedBytes += 1;
        }else{
	    	if(numBufferedBytes>0){
	    		int carry = leadByte >>> 8;
   	            outputByte(bufferedByte+carry);
   	            int byte2write = (0xff+carry) & 0xff;
   	            while(numBufferedBytes>1){
   	            	numBufferedBytes -= 1;
   	            	outputByte(byte2write);
   	            }
	    	}
	        numBufferedBytes = 1;
	        bufferedByte = leadByte & 0xff;
        }
	}
	       
	 public void encodeBin(int binValue,int ctxIdx){
		//update state
		int mstate = ctxTable[ctxIdx];
		ctxTable[ctxIdx] = Constant.g_nextState[mstate][binValue];
		
		//lookup
		int state = mstate >>> 1;
		int valMps = mstate & 1;
		int qRangeIdx = (ivlCurrRange >>> 6) & 3;
		int ivlLpsRange = Constant.g_lpsTable[state][qRangeIdx];
		
		int range0;
		int low0;
		int numBits;
		if(binValue!=valMps){
			range0 = ivlLpsRange;
		    low0 = ivlLow + ivlCurrRange - ivlLpsRange;
		    if(state >= 63){
		    	numBits = 6;
		    }else{
		    	numBits = 8-(int)(Math.floor(Math.log(ivlLpsRange)/Math.log(2.0)));
		    }
		}else{
			range0 = ivlCurrRange-ivlLpsRange;
		    low0 = ivlLow;
		    numBits = (range0<256) ? 1 : 0;
		}
		
		ivlLow = low0 << numBits;
		ivlCurrRange = range0 << numBits;
		bitsLeft += numBits;
		
		renormE();
	 }
	 
	 public void encodeBinEP(int binValue){
		ivlLow <<= 1;
		if(binValue==1){
			ivlLow += ivlCurrRange;
		}
		bitsLeft += 1;
		renormE();
	 }
	 
	 public void encodeBinsEP(int binValues, int numBins){
			while(numBins > 8){
				numBins -=8;
				ivlLow <<= 8;
				int pattern = binValues >> numBins;
				ivlLow += ivlCurrRange * pattern;
				binValues -= pattern << numBins;
				bitsLeft += 8;
				
				renormE();
			}
			ivlLow <<= numBins;
			ivlLow += ivlCurrRange * binValues;
			bitsLeft += numBins;
			
			renormE();
		 }
	 
	 public void encodeBinTrm(int binValue){
		 if(binValue>0){
			 ivlLow += ivlCurrRange-2;
			 ivlLow <<= 7;
	         ivlCurrRange = 2 << 7;
	         bitsLeft += 7;
		 }else{
			 ivlCurrRange -= 2;
			 
	         if(ivlCurrRange<256){
	        	 ivlLow <<= 1;
 	             ivlCurrRange <<= 1;
 	             bitsLeft += 1;
	         }
		 }
		 renormE();
	 }
	 
	 public static void check_enc0 (int[] context_state,int[] stdOutput,BufferedReader br) throws Exception{
		   EntropyEncoder enc0 = new EntropyEncoder(context_state,stdOutput);

		   String line = "";
		   int lineIdx = 0;
		   while((line = br.readLine()) != null){
			   lineIdx++;
			   //read one sequence and transform it to the form of x
			   String[] tempStrs = line.substring(1, line.length()-2).split(",");
			   int[] x = new int[tempStrs.length];
			   for(int j=0;j<tempStrs.length;j++){
				   x[j] = Integer.parseInt(tempStrs[j].trim());
			   }
			   
			   if(x[0]==1){
				   /*if(lineIdx == 23){
					   lineIdx++;
					   lineIdx--;
				   }*/
				   
				  /* if(x[2] == 151){
					   x[1]++;
					   x[1]--;
				   }*/
				   enc0.encodeBin(x[1],x[2]);
				   /*System.out.print(lineIdx+" ");
				   System.out.print("("+enc0.ivlLow);
				   System.out.print(","+enc0.ivlCurrRange);
				   System.out.print(","+enc0.bufferedByte);
				   System.out.print(","+enc0.numBufferedBytes);
				   System.out.print(","+enc0.bitsLeft);
				   System.out.println(","+enc0.outputBytes.size()+")");*/
			   }else if(x[0]==2){
				   enc0.encodeBinEP(x[1]); 
			   }else if(x[0] == 3){
				   for(int b=x[2]-1;b>=0;b--){
					   enc0.encodeBinEP(((x[1]&(1 << b))>0)? 1 : 0);
				   }
				   ///enc0.encodeBinsEP(x[1],x[2]); 
			   }else if(x[0]==4){
				   enc0.encodeBinTrm(x[1]);
			   }else if(x[0]==5 || x[0]==6){//write 1 bit 1, then output the last partial byte if it is not 0
				   enc0.outputByte(x[1]);
			   }else if(x[0]==0){
				   /*if(enc0.ivlLow != x[1] || enc0.ivlCurrRange!= x[2] || enc0.bufferedByte!=x[3] || enc0.numBufferedBytes!=x[4] || enc0.bitsLeft!=x[5]){
					   System.out.println("Error[" + lineIdx + "]:\t Property\t[STD,\t Output]");
					   System.out.println("low\t\t\t["+x[1]+",\t"+enc0.ivlLow+"]");
					   System.out.println("currRange\t\t["+x[2]+",\t"+enc0.ivlCurrRange+"]");
					   System.out.println("bufferedByte\t\t["+x[3]+",\t"+enc0.bufferedByte+"]");
					   System.out.println("numBufferedBytes\t["+x[4]+",\t"+enc0.numBufferedBytes+"]");
					   System.out.println("bitsLeft\t\t["+x[5]+",\t"+enc0.bitsLeft+"]");
					   return;
				   }*/
			   }
		   }
		   enc0.finish();
		   System.out.println("Done:\nSTD output len="+enc0.stdOutputBytes.length+", test output len="+enc0.outputBytes.size());
		   //print "STD output len = %d, test output len = %d, dd len=%d" %(len(enc0.stdOutputBytes),len(enc0.outputBytes),len(dd));
		   //return enc0.checkAnswer(), enc0, dd;
	 }
	 
	 public static void main(String[] args){
		 BufferedReader br = null;
		 try{
			 br = new BufferedReader(new FileReader(new File("test_seq_hevc.txt")));
			 EntropyEncoder.check_enc0(State.m_contextState_0, State.m_fifo, br);
		 }catch(Exception ex){
			 ex.printStackTrace();
		 }finally{
			 try{
				 br.close();
			 }catch(Exception ex){
				 ex.printStackTrace();
			 }
			 
		 }
	 }
}

