package residueCoding;
import entropyEncoder.EntropyEncoderHevc;
import entropyEncoder.State;


public class TEncSbac{
	
	//32*32��TU�Ŀ黮�֣�������
	public static int[] g_uiGroupIdx = {0,1,2,3,4,4,5,5,6,6,6,6,7,7,7,7,8,8,8,8,8,8,8,8,9,9,9,9,9,9,9,9};
	
	//32*32��TU�Ŀ黮�֣������򣩵���ʼλ��
	public static int[] g_uiMinInGroup = {0,1,2,3,4,6,8,12,16,24};
	
	public static int MLS_CG_LOG2_HEIGHT = 2;
	public static int MLS_CG_LOG2_WIDTH = 2;
	public static int MLS_CG_SIZE = 4;
	
	public static EntropyEncoderHevc  m_pcBinIf;// = new EntropyEncoderHevc(State.m_contextState_0, State.m_fifo);
	
	CodingParameters codingParameters;
	
	
	/**
	 * �����ر����˳��
	 * 1�����һ������Ԫ��λ��
	 * 2��4*4��groupFlag
	 * 3��4*4��group������Ԫ�ص�sigFlag
	 * 4��4*4��ǰ8��Ԫ�ص�greaterThan1Flag
	 * 5��һ��greaterThan2Flag
	 * 6�����еķ���λһ��
	 * 7��remainsAbsһ��
	 * 
	 * @param pcCoef
	 * @param compID
	 * @throws Exception
	 */
	public void codeCoeffNxN(int[] pcCoef, int compID) throws Exception{
		
		
	  int uiWidth = (int)Math.pow(pcCoef.length, 0.5);
	  int uiHeight = (int)Math.pow(pcCoef.length, 0.5);
	  int uiLog2BlockWidth = (int)(Math.log(uiWidth)/Math.log(2));
	  int uiLog2BlockHeight = (int)(Math.log(uiHeight)/Math.log(2));
	  
	  codingParameters	= new CodingParameters(uiWidth);
	  
	  // Find position of last coefficient
	  int scanPosLast = -1;//scanPosLast�ǶԽ�ɨ���λ��
	  int posLast;//posLast��˳��ɨ���λ��
	  
	  //uiNumSig���ܹ��ķ���Ԫ�صĸ���
	  int uiNumSig = countNonZeroCoeffs(pcCoef);
	  
	  boolean[] uiSigCoeffGroupFlag = new boolean[64];//uiSigCoeffGroupFlag�е�ֵ��˳��ɨ���

	  do
	  {
	    posLast = codingParameters.scan[++scanPosLast];//posLast��˳��ɨ���λ��

	    if( pcCoef[ posLast ] != 0 )
	    {
	      // get L1 sig map
	      int uiPosY   = posLast >> uiLog2BlockWidth;//uiPosY�Ǵ�ֱ�����λ��
	      int uiPosX   = posLast - ( uiPosY << uiLog2BlockWidth );//uiPosX��ˮƽ�����λ��

	      int uiBlkIdx = (codingParameters.widthInGroups * (uiPosY >> MLS_CG_LOG2_HEIGHT)) + (uiPosX >> MLS_CG_LOG2_WIDTH);//uiBlkIdx��4*4��С�Ŀ���32*32��TU����˳��ɨ���λ��
	      uiSigCoeffGroupFlag[ uiBlkIdx ] = true;//uiSigCoeffGroupFlag�е�ֵ��˳��ɨ���

	      uiNumSig--;
	    }
	  } while ( uiNumSig > 0 );//uiNumSig���ܹ��ķ���Ԫ�صĸ���

	  //�������һ������Ԫ�ص�λ��
	  int posLastY = posLast >> uiLog2BlockWidth;
	  int posLastX = posLast - ( posLastY << uiLog2BlockWidth );
	  //����������262ҳ
	  codeLastSignificantXY(posLastX, posLastY, uiWidth, uiHeight, compID, codingParameters.scanType);

	  //===== code significance flag =====
	  int baseCoeffGroupCtxIdx = (compID == 0)? 42: 44;
	  int baseCtxIdx = 46 + ((compID == 0)? 0: 28);

	  int  iLastScanSet  = scanPosLast >> MLS_CG_SIZE;//iLastScanSet�ǶԽ�ɨ�跽ʽ�����һ������Ԫ�����ڵ�4*4���λ��

	  int c1                  = 1;
	  int uiGoRiceParam       = 0;
	  int  iScanPosSig         = scanPosLast;

	  for( int iSubSet = iLastScanSet; iSubSet >= 0; iSubSet-- )
	  {
	    int numNonZero = 0;
	    int  iSubPos   = iSubSet << MLS_CG_SIZE;//iSubpos��subset�ĵ�һ�����ص��λ��,�Խ�ɨ��
	     uiGoRiceParam  = 0;
	   // boolean updateGolombRiceStatistics = bUseGolombRiceParameterAdaptation; //leave the statistics at 0 when not using the adaptation system
	    int coeffSigns = 0;

	    int absCoeff[] =  new int[1 << MLS_CG_SIZE];

	    int lastNZPosInCG  = -1;
	    int firstNZPosInCG = 1 << MLS_CG_SIZE;

	    boolean escapeDataPresentInGroup = false;

	    if( iScanPosSig == scanPosLast )
	    {
	      absCoeff[ 0 ] = (int)(Math.abs( pcCoef[ posLast ] ));
	      coeffSigns    = ( pcCoef[ posLast ] < 0 )? 1 : 0;
	      numNonZero    = 1;
	      lastNZPosInCG  = iScanPosSig;
	      firstNZPosInCG = iScanPosSig;
	      iScanPosSig--;
	    }

	    // encode significant_coeffgroup_flag
	    int iCGBlkPos = codingParameters.scanCG[ iSubSet ];//iCGBlkPos��4*4˳��ɨ���λ��
	    int iCGPosY   = iCGBlkPos / codingParameters.widthInGroups;//4*4��˳��ɨ��Ĵ�ֱλ��
	    int iCGPosX   = iCGBlkPos - (iCGPosY * codingParameters.widthInGroups);//4*4��˳��ɨ���ˮƽλ��

	    if( iSubSet == iLastScanSet || iSubSet == 0)
	    {
	      uiSigCoeffGroupFlag[ iCGBlkPos ] = true;
	    }
	    else
	    {
	      boolean uiSigCoeffGroup   = (uiSigCoeffGroupFlag[ iCGBlkPos ] != false);
	      int uiCtxSig  = TComTrQuant.getSigCoeffGroupCtxInc( uiSigCoeffGroupFlag, iCGPosX, iCGPosY, codingParameters.widthInGroups, codingParameters.heightInGroups );
	      m_pcBinIf.encodeBin( uiSigCoeffGroup?1:0,  baseCoeffGroupCtxIdx +  uiCtxSig);
	    }

	    // encode significant_coeff_flag
	    if( uiSigCoeffGroupFlag[ iCGBlkPos ])
	    {
	      int patternSigCtx = TComTrQuant.calcPatternSigCtx(uiSigCoeffGroupFlag, iCGPosX, iCGPosY, codingParameters.widthInGroups, codingParameters.heightInGroups);

	      int uiBlkPos, uiSig, uiCtxSig;
	      for( ; iScanPosSig >= iSubPos; iScanPosSig-- )
	      {
	        uiBlkPos  = codingParameters.scan[ iScanPosSig ];//uiBlkPos��˳��ɨ������ص��λ��
	        uiSig     = (pcCoef[ uiBlkPos ] != 0)? 1 : 0;//uiSig�Ǵ������Ƿ����
	        if( iScanPosSig > iSubPos || iSubSet == 0 || numNonZero  != 0)
	        {
	          uiCtxSig  = TComTrQuant.getSigCtxInc( patternSigCtx, codingParameters, iScanPosSig, uiLog2BlockWidth, uiLog2BlockHeight, compID );
	          m_pcBinIf.encodeBin( uiSig, baseCtxIdx+ uiCtxSig);
	        }
	        if( uiSig == 1)
	        {
	          absCoeff[ numNonZero ] = (int)(Math.abs( pcCoef[ uiBlkPos ] ));//absCoeff����в�ľ���ֵ���Խ�˳��洢
	          coeffSigns = 2 * coeffSigns + (( pcCoef[ uiBlkPos ] < 0 ) ? 1 : 0);//coeffSigns����һ��4*4�������з���в�ķ��ţ���λ��
	          numNonZero++;
	          if( lastNZPosInCG == -1 )
	          {
	            lastNZPosInCG = iScanPosSig;
	          }
	          firstNZPosInCG = iScanPosSig;
	        }
	      }
	    }
	    else
	    {
	      iScanPosSig = iSubPos - 1;
	    }

	    if( numNonZero > 0 )
	    {
	      boolean signHidden = ( lastNZPosInCG - firstNZPosInCG >= ContextTables.SBH_THRESHOLD );

	      int uiCtxSet = getContextSetIndex(compID, iSubSet, (c1 == 0));
	      c1 = 1;

	      int baseCtxMod = 150 + (ContextTables.NUM_ONE_FLAG_CTX_PER_SET * uiCtxSet);

	      int numC1Flag = Math.min(numNonZero, ContextTables.C1FLAG_NUMBER);
	      int firstC2FlagIdx = -1;
	      for( int idx = 0; idx < numC1Flag; idx++ )
	      {
	        int uiSymbol = absCoeff[ idx ] > 1 ? 1:0;
			m_pcBinIf.encodeBin( uiSymbol, baseCtxMod + c1);
	        if( uiSymbol == 1)
	        {
	          c1 = 0;//������ھ���ֵ����1�Ĳвc1=0

	          if (firstC2FlagIdx == -1)
	          {
	            firstC2FlagIdx = idx;
	          }
	          else //if a greater-than-one has been encountered already this group
	          {
	            escapeDataPresentInGroup = true;
	          }
	        }
	        else if( (c1 < 3) && (c1 > 0) )
	        {
	          c1++;
	        }
	      }

	      if (c1 == 0)//������ھ���ֵ����1�Ĳвc1=0
	      {
	        baseCtxMod = 174 + (ContextTables.NUM_ABS_FLAG_CTX_PER_SET * uiCtxSet);
	        if ( firstC2FlagIdx != -1)
	        {
	          int symbol = absCoeff[ firstC2FlagIdx ] > 2 ? 1 : 0;
	          m_pcBinIf.encodeBin( symbol,  baseCtxMod);
	          if (symbol != 0)
	          {
	            escapeDataPresentInGroup = true;
	          }
	        }
	      }

	      escapeDataPresentInGroup = escapeDataPresentInGroup || (numNonZero > ContextTables.C1FLAG_NUMBER); 
	      m_pcBinIf.encodeBinsEP( coeffSigns, numNonZero );

	      int iFirstCoeff2 = 1;
	      if (escapeDataPresentInGroup)
	      {
	        for ( int idx = 0; idx < numNonZero; idx++ )
	        {
	          int baseLevel  = (idx < ContextTables.C1FLAG_NUMBER)? (2 + iFirstCoeff2 ) : 1;

	          if( absCoeff[ idx ] >= baseLevel)
	          {
	            int escapeCodeValue = absCoeff[idx] - baseLevel;

	            xWriteCoefRemainExGolomb( escapeCodeValue, uiGoRiceParam, false, 15 );

	            if (absCoeff[idx] > (3 << uiGoRiceParam))
	            {
	              uiGoRiceParam = Math.min((uiGoRiceParam + 1), 4);
	            }
	          }

	          if(absCoeff[ idx ] >= 2)
	          {
	            iFirstCoeff2 = 0;
	          }
	        }
	      }
	    }
	  }
	  return;
	}

	/** Coding of coeff_abs_level_minus3
	 * \param symbol                  value of coeff_abs_level_minus3
	 * \param rParam                  reference to Rice parameter
	 * \param useLimitedPrefixLength
	 * \param maxLog2TrDynamicRange 
	 */
	public void xWriteCoefRemainExGolomb( int symbol, int rParam, boolean useLimitedPrefixLength, int maxLog2TrDynamicRange ) throws Exception
	{
	  int codeNumber  = (int)symbol;
	  int length;

	  if (codeNumber < (ContextTables.COEF_REMAIN_BIN_REDUCTION << rParam))
	  {
	    length = codeNumber>>rParam;
	    m_pcBinIf.encodeBinsEP( (1<<(length+1))-2 , length+1);
	    m_pcBinIf.encodeBinsEP((codeNumber%(1<<rParam)),rParam);
	  }
	  else if (useLimitedPrefixLength)
	  {
	    /*int maximumPrefixLength = (32 - (ContextTables.COEF_REMAIN_BIN_REDUCTION + maxLog2TrDynamicRange));

	    int prefixLength = 0;
	    int suffixLength = Integer.MAX_VALUE;
	    int codeValue    = (symbol >> rParam) - ContextTables.COEF_REMAIN_BIN_REDUCTION;

	    if (codeValue >= ((1 << maximumPrefixLength) - 1))
	    {
	      prefixLength = maximumPrefixLength;
	      suffixLength = maxLog2TrDynamicRange - rParam;
	    }
	    else
	    {
	      while (codeValue > ((2 << prefixLength) - 2))
	      {
	        prefixLength++;
	      }

	      suffixLength = prefixLength + 1; //+1 for the separator bit
	    }

	    int suffix = codeValue - ((1 << prefixLength) - 1);

	    int totalPrefixLength = prefixLength + ContextTables.COEF_REMAIN_BIN_REDUCTION;
	    int prefix            = (1 << totalPrefixLength) - 1;
	    int rParamBitMask     = (1 << rParam) - 1;

	    m_pcBinIf.encodeBinsEP(  prefix,                                        totalPrefixLength      ); //prefix
	    m_pcBinIf.encodeBinsEP(((suffix << rParam) | (symbol & rParamBitMask)), (suffixLength + rParam)); //separator, suffix, and rParam bits*/
	  }
	  else
	  {
	    length = rParam;
	    codeNumber  = codeNumber - ( ContextTables.COEF_REMAIN_BIN_REDUCTION << rParam);

	    while (codeNumber >= (1<<length))
	    {
	      codeNumber -=  (1<<(length++));
	    }

	    m_pcBinIf.encodeBinsEP((1<<(ContextTables.COEF_REMAIN_BIN_REDUCTION+length+1-rParam))-2,ContextTables.COEF_REMAIN_BIN_REDUCTION+length+1-rParam);
	    m_pcBinIf.encodeBinsEP(codeNumber,length);
	  }
	}
	

	/** Encode (X,Y) position of the last significant coefficient
	 * \param uiPosX     X component of last coefficient
	 * \param uiPosY     Y component of last coefficient
	 * \param width      Block width
	 * \param height     Block height
	 * \param component  chroma component ID
	 * \param uiScanIdx  scan type (zig-zag, hor, ver)
	 * This method encodes the X and Y component within a block of the last significant coefficient.
	 */
	public void codeLastSignificantXY( int uiPosX, int uiPosY, int width, int height, int component, int scanType) throws Exception
	{
	  int uiCtxLast;
	  int uiGroupIdxX    = g_uiGroupIdx[ uiPosX ];//ÿ����uiPosX��uiPosY����Ӧһ����������������10*10������
	  int uiGroupIdxY    = g_uiGroupIdx[ uiPosY ];
	
	  //ctxIdx
	  int pCtxX = (component == 0) ? 90 : 105;
	  int pCtxY = (component == 0) ? 120 : 135;
	
	  int blkSizeOffsetX, blkSizeOffsetY, shiftX, shiftY;
	
	  int convertedWidth  = (int)((Math.log(width)/Math.log(2))-2);
	  int convertedHeight = (int)((Math.log(height)/Math.log(2))-2);

	  blkSizeOffsetX = (isChroma(component)) ? 0               : ((convertedWidth  * 3) + ((convertedWidth  + 1) >> 2));
	  blkSizeOffsetY = (isChroma(component)) ? 0               : ((convertedHeight * 3) + ((convertedHeight + 1) >> 2));
	  shiftX  = (isChroma(component)) ? convertedWidth  : ((convertedWidth  + 3) >> 2);
	  shiftY  = (isChroma(component)) ? convertedHeight : ((convertedHeight + 3) >> 2);
	  //------------------
	
	  // posX
	
	  for( uiCtxLast = 0; uiCtxLast < uiGroupIdxX; uiCtxLast++ ){
	    m_pcBinIf.encodeBin( 1, pCtxX + blkSizeOffsetX + (uiCtxLast >>shiftX) );
	  }
	  if( uiGroupIdxX < g_uiGroupIdx[ width - 1 ]){
	    m_pcBinIf.encodeBin( 0, pCtxX + blkSizeOffsetX + (uiCtxLast >>shiftX) );
	  }
	
	  // posY
	
	  for( uiCtxLast = 0; uiCtxLast < uiGroupIdxY; uiCtxLast++ )
	  {
	    m_pcBinIf.encodeBin( 1, pCtxY+ blkSizeOffsetY + (uiCtxLast >>shiftY) );
	  }
	  if( uiGroupIdxY < g_uiGroupIdx[ height - 1 ])
	  {
	    m_pcBinIf.encodeBin( 0, pCtxY+ blkSizeOffsetY + (uiCtxLast >>shiftY) );
	  }
	
	  // EP-coded part
	
	  if ( uiGroupIdxX > 3 )
	  {
	    int uiCount = ( uiGroupIdxX - 2 ) >> 1;
	    uiPosX       = uiPosX - g_uiMinInGroup[ uiGroupIdxX ];
	    for (int i = uiCount - 1 ; i >= 0; i-- )
	    {
	      m_pcBinIf.encodeBinEP( ( uiPosX >> i ) & 1 );
	    }
	  }
	  if ( uiGroupIdxY > 3 )
	  {
	    int uiCount = ( uiGroupIdxY - 2 ) >> 1;
	    uiPosY       = uiPosY - g_uiMinInGroup[ uiGroupIdxY ];
	    for ( int i = uiCount - 1 ; i >= 0; i-- )
	    {
	      m_pcBinIf.encodeBinEP( ( uiPosY >> i ) & 1 );
	    }
	  }
	}

	public int countNonZeroCoeffs( int[] pcCoef){
	  int count = 0;
	  for ( int i = 0; i < pcCoef.length; i++ ){
		  if(pcCoef[i] != 0) count++;
	  }
	  return count;
	}
	
	public boolean isChroma(int component){
		return component != 0;
	}
	
	public boolean isLuma(int component){
		return component == 0;
	}
	
	public int getContextSetIndex (int  component,
	            int         subsetIndex,
	            boolean         foundACoefficientGreaterThan1)
	{
	int notFirstSubsetOffset     = (isLuma(component) && (subsetIndex > 0)) ? 2 : 0;
	int foundAGreaterThan1Offset = foundACoefficientGreaterThan1            ? 1 : 0;
	
	return (component==0?0:4) + notFirstSubsetOffset + foundAGreaterThan1Offset;
	}
	
}
