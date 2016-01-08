package residueCoding;

public class TComTrQuant {
	/** Pattern decision for context derivation process of significant_coeff_flag
	 * \param sigCoeffGroupFlag pointer to prior coded significant coeff group
	 * \param uiCGPosX column of current coefficient group
	 * \param uiCGPosY row of current coefficient group
	 * \param widthInGroups width of the block
	 * \param heightInGroups height of the block
	 * \returns pattern for current coefficient group
	 */
	
	public static int MLS_CG_LOG2_WIDTH = 2;
	public static int MLS_CG_LOG2_HEIGHT = 2;
	
	public static int NEIGHBOURHOOD_00_CONTEXT_1_THRESHOLD_4x4 = 3;
	public static int NEIGHBOURHOOD_00_CONTEXT_2_THRESHOLD_4x4 = 1;
	
	public static int ctxIndMap4x4[] =
		{
		  0, 1, 4, 5,
		  2, 3, 4, 5,
		  6, 6, 8, 8,
		  7, 7, 8, 8
		};
	
	/**
	 * patternSigCtx是根据右边和下边的sigCoeffGroupFlag来计算的
	 * 
	 * @param sigCoeffGroupFlag
	 * @param uiCGPosX
	 * @param uiCGPosY
	 * @param widthInGroups
	 * @param heightInGroups
	 * @return
	 */
	public static int calcPatternSigCtx( boolean[] sigCoeffGroupFlag, int uiCGPosX, int uiCGPosY, int widthInGroups, int heightInGroups )
	{
	  if ((widthInGroups <= 1) && (heightInGroups <= 1))
	  {
	    return 0;
	  }

	  boolean rightAvailable = uiCGPosX < (widthInGroups  - 1);
	  boolean belowAvailable = uiCGPosY < (heightInGroups - 1);

	  int sigRight = 0;
	  int sigLower = 0;

	  if (rightAvailable)
	  {
	    sigRight = ((sigCoeffGroupFlag[ (uiCGPosY * widthInGroups) + uiCGPosX + 1 ] != false) ? 1 : 0);
	  }
	  if (belowAvailable)
	  {
	    sigLower = ((sigCoeffGroupFlag[ (uiCGPosY + 1) * widthInGroups + uiCGPosX ] != false) ? 1 : 0);
	  }

	  return sigRight + (sigLower << 1);
	}
	
	/**
	 *  Context derivation process of coeff_abs_significant_flag
	 *  patternSigCtx是根据右边和下边的sigCoeffGroupFlag来计算的
	 * 
	 * \param uiSigCoeffGroupFlag significance map of L1
	 * \param uiCGPosX column of current scan position
	 * \param uiCGPosY row of current scan position
	 * \param widthInGroups width of the block
	 * \param heightInGroups height of the block
	 * \returns ctxInc for current scan position
	 */
	public static int getSigCoeffGroupCtxInc  (boolean[]  uiSigCoeffGroupFlag,
	                                           int   uiCGPosX,
	                                           int   uiCGPosY,
	                                           int   widthInGroups,
	                                           int   heightInGroups)
	{
	  int sigRight = 0;
	  int sigLower = 0;

	  if (uiCGPosX < (widthInGroups  - 1))
	  {
	    sigRight = ((uiSigCoeffGroupFlag[ (uiCGPosY * widthInGroups) + uiCGPosX + 1 ] != false) ? 1 : 0);
	  }
	  if (uiCGPosY < (heightInGroups - 1))
	  {
	    sigLower = ((uiSigCoeffGroupFlag[ (uiCGPosY + 1) * widthInGroups + uiCGPosX ] != false) ? 1 : 0);
	  }

	  return ((sigRight + sigLower) != 0) ? 1 : 0;
	}
	
	/** Context derivation process of coeff_abs_significant_flag
	 *  不用到其他地方的信息
	 * 
	 * 
	 * \param patternSigCtx pattern for current coefficient group
	 * \param codingParameters coding parameters for the TU (includes the scan)
	 * \param scanPosition current position in scan order
	 * \param log2BlockWidth log2 width of the block
	 * \param log2BlockHeight log2 height of the block
	 * \param chanType channel type (CHANNEL_TYPE_LUMA/CHROMA)
	 * \returns ctxInc for current scan position
	 */
	public static int getSigCtxInc    (       int                        patternSigCtx,
										CodingParameters 			codingParameters,
	                                    int                        scanPosition,
	                                    int                        log2BlockWidth,
	                                    int                        log2BlockHeight,
	                                    int                			chanType)
	{
	 /* if (codingParameters.firstSignificanceMapContext == ContextTables.significanceMapContextSetStart[chanType][3])
	  {
	    //single context mode
	    return ContextTables.significanceMapContextSetStart[chanType][3];
	  }*/

	   int rasterPosition = codingParameters.scan[scanPosition];
	   int posY           = rasterPosition >> log2BlockWidth;//顺序扫描的位置
	   int posX           = rasterPosition - (posY << log2BlockWidth);//顺序扫描的位置

	  if ((posX + posY) == 0)
	  {
	    return 0; //special case for the DC context variable
	  }

	  int offset = Integer.MAX_VALUE;

	  /*if ((log2BlockWidth == 2) && (log2BlockHeight == 2)) //4x4
	  {
	    offset = ctxIndMap4x4[ (4 * posY) + posX ];
	  }
	  else*/
	  {
	    int cnt = 0;

	    switch (patternSigCtx)
	    {
	      //------------------

	      case 0: //neither neighbouring group is significant
	        {
	           int posXinSubset     = posX & ((1 << MLS_CG_LOG2_WIDTH)  - 1);
	           int posYinSubset     = posY & ((1 << MLS_CG_LOG2_HEIGHT) - 1);
	           int posTotalInSubset = posXinSubset + posYinSubset;

	          //first N coefficients in scan order use 2; the next few use 1; the rest use 0.
	           int context1Threshold = NEIGHBOURHOOD_00_CONTEXT_1_THRESHOLD_4x4;
	           int context2Threshold = NEIGHBOURHOOD_00_CONTEXT_2_THRESHOLD_4x4;

	          cnt = (posTotalInSubset >= context1Threshold) ? 0 : ((posTotalInSubset >= context2Threshold) ? 1 : 2);
	        }
	        break;

	      //------------------

	      case 1: //right group is significant, below is not
	        {
	           int posYinSubset = posY & ((1 << MLS_CG_LOG2_HEIGHT) - 1);
	           int groupHeight  = 1 << MLS_CG_LOG2_HEIGHT;

	          cnt = (posYinSubset >= (groupHeight >> 1)) ? 0 : ((posYinSubset >= (groupHeight >> 2)) ? 1 : 2); //top quarter uses 2; second-from-top quarter uses 1; bottom half uses 0
	        }
	        break;

	      //------------------

	      case 2: //below group is significant, right is not
	        {
	           int posXinSubset = posX & ((1 << MLS_CG_LOG2_WIDTH)  - 1);
	           int groupWidth   = 1 << MLS_CG_LOG2_WIDTH;

	          cnt = (posXinSubset >= (groupWidth >> 1)) ? 0 : ((posXinSubset >= (groupWidth >> 2)) ? 1 : 2); //left quarter uses 2; second-from-left quarter uses 1; right half uses 0
	        }
	        break;

	      //------------------

	      case 3: //both neighbouring groups are significant
	        {
	          cnt = 2;
	        }
	        break;

	      //------------------

	      default:
	        System.out.println("ERROR: Invalid patternSigCtx "+(int)(patternSigCtx)+" in getSigCtxInc\n");
	        System.exit(1);
	        break;
	    }

	    //------------------------------------------------

	     boolean notFirstGroup = ((posX >> MLS_CG_LOG2_WIDTH) + (posY >> MLS_CG_LOG2_HEIGHT)) > 0;

	    offset = (notFirstGroup ? ContextTables.notFirstGroupNeighbourhoodContextOffset[chanType] : 0) + cnt;
	  }

	  return codingParameters.firstSignificanceMapContext + offset;
	}
}
