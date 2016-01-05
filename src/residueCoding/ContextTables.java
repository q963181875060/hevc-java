package residueCoding;

public class ContextTables {
	//------------下面是辅助求ctxIdx的数据----------
	public static int MAX_NUM_CHANNEL_TYPE = 2;
	public static int CONTEXT_NUMBER_OF_TYPES = 4;
	public static int significanceMapContextSetStart         [][] = { {0,  9, 21, 27}, {0,  9, 12, 15} };
	public static int significanceMapContextSetSize          [][] = { {9, 12,  6,  1}, {9,  3,  3,  1} };
	public static int notFirstGroupNeighbourhoodContextOffset[]  = {  3, 0};
	
	public static int NUM_ONE_FLAG_CTX_PER_SET = 4;      ///< number of context models for greater than 1 flag in a set
	public static int NUM_ABS_FLAG_CTX_PER_SET = 1;      ///< number of context models for greater than 2 flag in a set
	public static int SBH_THRESHOLD =            4; ///< value of the fixed SBH controlling threshold
	
	public static int C1FLAG_NUMBER =                                    8; // maximum number of largerThan1 flag coded in one chunk:  16 in HM5
	public static int C2FLAG_NUMBER =                                    1; // maximum number of largerThan2 flag coded in one chunk:  16 in HM5
	
	public static int  COEF_REMAIN_BIN_REDUCTION =                        3; ///< indicates the level at which the VLC transitions from Golomb-Rice to TU+EG(k)
	//------------上面是辅助求ctxIdx的数据----------
}
