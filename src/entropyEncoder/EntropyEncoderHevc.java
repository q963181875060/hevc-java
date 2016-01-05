package entropyEncoder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class EntropyEncoderHevc {
	
	public int ivlLow;
	public int ivlCurrRange;
	public int bitsLeft;

	public ArrayList<Integer> outputBytes;
	public int[] ctxTable;

	public int bufferedByte;
	public int numBufferedBytes;
	
	public int[] stdOutputBytes;
	
	public EntropyEncoderHevc(int[] ctxTable,int[] stdOutputBytes){
		this.ivlLow = 0;
		this.ivlCurrRange = 510;
		this.bitsLeft = 23;

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
		if ((ivlLow >>> (32 - bitsLeft)) != 0)
	    {
			outputByte(bufferedByte + 1);
	        while (numBufferedBytes > 1)
	        {
	            outputByte(0x00);
	            numBufferedBytes--;
	        }

	        ivlLow -= 1 << (32 - bitsLeft);
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
		
			
	}
	
	public void bottomPartFinishAndWriteSingleOneAndAlignZero(){
		int bits = ivlLow >>> 8;
		int numOfBits = 24 - bitsLeft;
		
		bits = (bits << 1) + 1;
		numOfBits++;
		
		int numOfOutputBytes = numOfBits/8;
		int numOfRemainingBits = numOfBits%8;
		
		int bitsWithoutRemaining = bits >>> numOfRemainingBits; 
		for(int i=numOfOutputBytes;i>0; i--){
			outputByte(bitsWithoutRemaining >>> (8 * (i-1)));
		}
		int lastByte = (bits << (8 - numOfRemainingBits)) & (0x0ff);
		if(lastByte != 0) outputByte(lastByte);
	}
	
	public void renormE(){
        if(bitsLeft >= 12){
        	return;
        }
		
        int leadByte = ivlLow >>> (24-bitsLeft);
        bitsLeft += 8;
        ivlLow &= (0xffffffff >>> bitsLeft);
        
        if(leadByte==0xff){
        	numBufferedBytes += 1;
        }else{
	    	if(numBufferedBytes>0){
	    		int carry = leadByte >>> 8;
	    		int byte2write = bufferedByte+carry; 
	    		bufferedByte = leadByte & 0xff;
   	            outputByte(byte2write);
   	            byte2write = ( 0xff + carry ) & 0xff;
   	            
   	            while(numBufferedBytes>1){
   	            	outputByte(byte2write);
   	            	numBufferedBytes -= 1;
   	            }
	    	}else{
	    		numBufferedBytes = 1;
		        bufferedByte = leadByte & 0xff;
	    	}
        }
	}
	       
	 public void encodeBin(int binValue,int ctxIdx){
		int mstate = ctxTable[ctxIdx];
		int state = mstate >>> 1;
		int valMps = mstate & 1;
		int qRangeIdx = (ivlCurrRange >>> 6) & 3;
		
		int ivlLpsRange = Constant.g_lpsTable[state][qRangeIdx];
		ivlCurrRange -= ivlLpsRange;
		
		if(binValue!=valMps){
			int numBits = Constant.enormTable[ivlLpsRange >>> 3];
			ivlLow = (ivlLow + ivlCurrRange) << numBits;
			ivlCurrRange = ivlLpsRange << numBits;
			ctxTable[ctxIdx] = Constant.nextStateLPS[mstate];
			bitsLeft -= numBits;
		    renormE();
		}else{
			ctxTable[ctxIdx] = Constant.nextStateMPS[mstate];
			if(ivlCurrRange < 256){
				ivlLow <<= 1;
				ivlCurrRange <<= 1;
				bitsLeft--;
				renormE();
			}
		}
	 }
	 
	 public void encodeAlignedBinsEP(int binValues, int numBins )
	 {
	   int binsRemaining = numBins;

	   while (binsRemaining > 0)
	   {
	     int binsToCode = Math.min(binsRemaining, 8); //code bytes if able to take advantage of the system's byte-write function
	     int binMask    = (1 << binsToCode) - 1;

	     int newBins = (binValues >>> (binsRemaining - binsToCode)) & binMask;
	     ivlLow = (ivlLow << binsToCode) + (newBins << 8); //range is known to be 256

	     binsRemaining -= binsToCode;
	     bitsLeft    -= binsToCode;

	     renormE();
	   }
	 }
	 
	public void encodeBinEP(int binValue) {
		if (ivlCurrRange == 256) {
			encodeAlignedBinsEP(binValue, 1);
			return;
		}

		ivlLow <<= 1;
		if (binValue == 1) {
			ivlLow += ivlCurrRange;
		}
		bitsLeft--;
		renormE();
	}
	 
	public void encodeBinsEP(int binValues, int numBins) {
		if(ivlCurrRange == 256){
			encodeAlignedBinsEP(binValues, numBins);
			return;
		}
		while (numBins > 8) {
			numBins -= 8;
			int pattern = binValues >> numBins;
			ivlLow <<= 8;
			ivlLow += ivlCurrRange * pattern;
			binValues -= pattern << numBins;
			bitsLeft -= 8;

			renormE();
		}
		ivlLow <<= numBins;
		ivlLow += ivlCurrRange * binValues;
		bitsLeft -= numBins;

		renormE();
	}

	public void encodeBinTrm(int binValue) {
		ivlCurrRange -= 2;
		if (binValue > 0) {
			ivlLow += ivlCurrRange;
			ivlLow <<= 7;
			ivlCurrRange = 2 << 7;
			bitsLeft -= 7;
		} else if (ivlCurrRange >= 256) {
			return;
		} else {
			ivlLow <<= 1;
			ivlCurrRange <<= 1;
			bitsLeft--;
		}
		renormE();
	}
	 
	 public static void check_enc0 (int[] context_state,int[] stdOutput,BufferedReader br) throws Exception{
		   EntropyEncoderHevc enc0 = new EntropyEncoderHevc(context_state,stdOutput);

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
				   if(enc0.ivlLow == 155264 && enc0.ivlCurrRange==416){
					   enc0.ivlLow++;
					   enc0.ivlLow--;
					}
				   /*for(int b=x[2]-1;b>=0;b--){
					   enc0.encodeBinEP(((x[1]&(1 << b))>0)? 1 : 0);
				   }*/
				   enc0.encodeBinsEP(x[1],x[2]); 
			   }else if(x[0]==4){
				   enc0.encodeBinTrm(x[1]);
			   }else if(x[0]==5){
				   enc0.finish();
			   }else if(x[0]==6){//m_pcTComBitIf->write( m_uiLow >> 8, 24 - m_bitsLeft ), write 1 bit 1, then output the last partial byte if it is not 0
				   enc0.bottomPartFinishAndWriteSingleOneAndAlignZero();
			   }else if(x[0]==0){
				   if(enc0.ivlLow != x[1] || enc0.ivlCurrRange!= x[2] || enc0.bufferedByte!=x[3] || enc0.numBufferedBytes!=x[4] || enc0.bitsLeft!=x[5]){
					   System.out.println("Error[" + lineIdx + "]:\t Property\t[STD,\t Output]");
					   System.out.println("low\t\t\t["+x[1]+",\t"+enc0.ivlLow+"]");
					   System.out.println("currRange\t\t["+x[2]+",\t"+enc0.ivlCurrRange+"]");
					   System.out.println("bufferedByte\t\t["+x[3]+",\t"+enc0.bufferedByte+"]");
					   System.out.println("numBufferedBytes\t["+x[4]+",\t"+enc0.numBufferedBytes+"]");
					   System.out.println("bitsLeft\t\t["+x[5]+",\t"+enc0.bitsLeft+"]");
					   return;
				   }
			   }
		   }
		   //enc0.finish();
		   System.out.println("Done:\nSTD output len="+enc0.stdOutputBytes.length+", test output len="+enc0.outputBytes.size());
		   //print "STD output len = %d, test output len = %d, dd len=%d" %(len(enc0.stdOutputBytes),len(enc0.outputBytes),len(dd));
		   //return enc0.checkAnswer(), enc0, dd;
	 }
	 
	 public static void main(String[] args){
		 BufferedReader br = null;
		 try{
			 br = new BufferedReader(new FileReader(new File("64-64-output-sequence.txt")));
			 EntropyEncoderHevc.check_enc0(State.m_contextState_0, State.m_fifo, br);
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



/*package entropyEncoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class EntropyEncoderHevc {
	
	public int ivlLow;
	public int ivlCurrRange;
	public int bitsLeft;

	public ArrayList<Integer> outputBytes;
	public int[] ctxTable;

	public int bufferedByte;
	public int numBufferedBytes;
	
	public int[] stdOutputBytes;
	
	public BufferedWriter bw;
	
	public EntropyEncoderHevc(int[] ctxTable,int[] stdOutputBytes, BufferedWriter bw){
		this.ivlLow = 0;
		this.ivlCurrRange = 510;
		this.bitsLeft = 23;

        this.outputBytes = new ArrayList<Integer>();
        this.ctxTable = ctxTable.clone();

        this.bufferedByte = 0xff;
        this.numBufferedBytes = 0;

        this.stdOutputBytes = stdOutputBytes;
        this.bw = bw;
        
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
	    if(i == 2136){
    		i = 2136;
    	}
	    if(stdOutputBytes[i]!=db){
	    	//ipdb.set_trace()
	    	
	    	System.out.println("stdOutputBytes[" + i + "]="+this.stdOutputBytes[i]+" outputBytes[" + i + "]="+this.outputBytes.get(i));
	    }
	}
	
	public void finish(){
		if ((ivlLow >>> (32 - bitsLeft)) != 0)
	    {
			outputByte(bufferedByte + 1);
	        while (numBufferedBytes > 1)
	        {
	            outputByte(0x00);
	            numBufferedBytes--;
	        }

	        ivlLow -= 1 << (32 - bitsLeft);
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
        if(bitsLeft >= 12){
        	return;
        }
		
        int leadByte = ivlLow >>> (24-bitsLeft);
        bitsLeft += 8;
        ivlLow &= (0xffffffff >>> bitsLeft);
        
        if(leadByte==0xff){
        	numBufferedBytes += 1;
        }else{
	    	if(numBufferedBytes>0){
	    		int carry = leadByte >>> 8;
	    		int byte2write = bufferedByte+carry; 
	    		bufferedByte = leadByte & 0xff;
   	            outputByte(byte2write);
   	            byte2write = ( 0xff + carry ) & 0xff;
   	            
   	            while(numBufferedBytes>1){
   	            	outputByte(byte2write);
   	            	numBufferedBytes -= 1;
   	            }
	    	}else{
	    		numBufferedBytes = 1;
		        bufferedByte = leadByte & 0xff;
	    	}
        }
	}
	       
	 public void encodeBin(int binValue,int ctxIdx) throws Exception{
		 bw.write("(1,"+binValue+","+ctxIdx+"),\n");
		 if(ctxIdx == 151){
			 ctxIdx = 151;
		 }
		int mstate = ctxTable[ctxIdx];
		int state = mstate >>> 1;
		int valMps = mstate & 1;
		int qRangeIdx = (ivlCurrRange >>> 6) & 3;
		
		int ivlLpsRange = Constant.g_lpsTable[state][qRangeIdx];
		ivlCurrRange -= ivlLpsRange;
		
		if(binValue!=valMps){
			int numBits = Constant.enormTable[ivlLpsRange >>> 3];
			ivlLow = (ivlLow + ivlCurrRange) << numBits;
			ivlCurrRange = ivlLpsRange << numBits;
			ctxTable[ctxIdx] = Constant.nextStateLPS[mstate];
			bitsLeft -= numBits;
		    renormE();
		}else{
			ctxTable[ctxIdx] = Constant.nextStateMPS[mstate];
			if(ivlCurrRange < 256){
				ivlLow <<= 1;
				ivlCurrRange <<= 1;
				bitsLeft--;
				renormE();
			}
		}
	 }
	 
	 public void encodeAlignedBinsEP(int binValues, int numBins )
	 {
	   int binsRemaining = numBins;

	   while (binsRemaining > 0)
	   {
	     int binsToCode = Math.min(binsRemaining, 8); //code bytes if able to take advantage of the system's byte-write function
	     int binMask    = (1 << binsToCode) - 1;

	     int newBins = (binValues >>> (binsRemaining - binsToCode)) & binMask;
	     ivlLow = (ivlLow << binsToCode) + (newBins << 8); //range is known to be 256

	     binsRemaining -= binsToCode;
	     bitsLeft    -= binsToCode;

	     renormE();
	   }
	 }
	 
	public void encodeBinEP(int binValue) throws Exception{
		bw.write("(2,"+binValue+"),\n");
		if (ivlCurrRange == 256) {
			return;
		}

		ivlLow <<= 1;
		if (binValue == 1) {
			ivlLow += ivlCurrRange;
		}
		bitsLeft--;
		renormE();
	}
	 
	public void encodeBinsEP(int binValues, int numBins) throws Exception{
		bw.write("(3,"+binValues+","+numBins+"),\n");
		if(binValues == 65535){
			binValues = 65535;
		}
		if(ivlCurrRange == 256){
			encodeAlignedBinsEP(binValues, numBins);
			return;
		}
		while (numBins > 8) {
			numBins -= 8;
			int pattern = binValues >> numBins;
			ivlLow <<= 8;
			ivlLow += ivlCurrRange * pattern;
			binValues -= pattern << numBins;
			bitsLeft -= 8;

			renormE();
		}
		ivlLow <<= numBins;
		ivlLow += ivlCurrRange * binValues;
		bitsLeft -= numBins;

		renormE();
	}

	public void encodeBinTrm(int binValue) throws Exception{
		bw.write("(4,"+binValue+"),\n");
		ivlCurrRange -= 2;
		if (binValue > 0) {
			ivlLow += ivlCurrRange;
			ivlLow <<= 7;
			ivlCurrRange = 2 << 7;
			bitsLeft -= 7;
		} else if (ivlCurrRange >= 256) {
			return;
		} else {
			ivlLow <<= 1;
			ivlCurrRange <<= 1;
			bitsLeft--;
		}
		renormE();
	}
	 
	 public static void check_enc0 (int[] context_state,int[] stdOutput,BufferedReader br) throws Exception{
		   EntropyEncoderHevc enc0 = new EntropyEncoderHevc(context_state,stdOutput);

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
				   if(lineIdx == 23){
					   lineIdx++;
					   lineIdx--;
				   }
				   
				   if(x[2] == 151){
					   x[1]++;
					   x[1]--;
				   }
				   enc0.encodeBin(x[1],x[2]);
				   System.out.print(lineIdx+" ");
				   System.out.print("("+enc0.ivlLow);
				   System.out.print(","+enc0.ivlCurrRange);
				   System.out.print(","+enc0.bufferedByte);
				   System.out.print(","+enc0.numBufferedBytes);
				   System.out.print(","+enc0.bitsLeft);
				   System.out.println(","+enc0.outputBytes.size()+")");
			   }else if(x[0]==2){
				   enc0.encodeBinEP(x[1]); 
			   }else if(x[0] == 3){
				   if(enc0.ivlLow == 155264 && enc0.ivlCurrRange==416){
					   enc0.ivlLow++;
					   enc0.ivlLow--;
					}
				   for(int b=x[2]-1;b>=0;b--){
					   enc0.encodeBinEP(((x[1]&(1 << b))>0)? 1 : 0);
				   }
				   enc0.encodeBinsEP(x[1],x[2]); 
			   }else if(x[0]==4){
				   enc0.encodeBinTrm(x[1]);
			   }else if(x[0]==5 || x[0]==6){//write 1 bit 1, then output the last partial byte if it is not 0
				   enc0.outputByte(x[1]);
			   }else if(x[0]==0){
				   if(enc0.ivlLow != x[1] || enc0.ivlCurrRange!= x[2] || enc0.bufferedByte!=x[3] || enc0.numBufferedBytes!=x[4] || enc0.bitsLeft!=x[5]){
					   System.out.println("Error[" + lineIdx + "]:\t Property\t[STD,\t Output]");
					   System.out.println("low\t\t\t["+x[1]+",\t"+enc0.ivlLow+"]");
					   System.out.println("currRange\t\t["+x[2]+",\t"+enc0.ivlCurrRange+"]");
					   System.out.println("bufferedByte\t\t["+x[3]+",\t"+enc0.bufferedByte+"]");
					   System.out.println("numBufferedBytes\t["+x[4]+",\t"+enc0.numBufferedBytes+"]");
					   System.out.println("bitsLeft\t\t["+x[5]+",\t"+enc0.bitsLeft+"]");
					   return;
				   }
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

			 EntropyEncoderHevc.check_enc0(State.m_contextState_0, State.m_fifo, br);
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

*/