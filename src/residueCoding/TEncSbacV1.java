package residueCoding;
import java.util.HashSet;
import java.util.Stack;

/**
 * Definition for undirected graph. class UndirectedGraphNode { int label;
 * List<UndirectedGraphNode> neighbors; UndirectedGraphNode(int x) { label = x;
 * neighbors = new ArrayList<UndirectedGraphNode>(); } };
 */
public class TEncSbacV1 {
	/**
	 * �����ر����˳�� 1�����һ������Ԫ��λ�� 2��4*4��groupFlag 3��4*4��group������Ԫ�ص�sigFlag
	 * 4��4*4��ǰ8��Ԫ�ص�greaterThan1Flag 5��һ��greaterThan2Flag 6�����еķ���λһ��
	 * 7��remainsAbsһ��
	 * 
	 * @param pcCoef
	 * @param compID
	 * @throws Exception
	 */
	public static void main(String[] args) {
		int[] pcCoef = new int[] { 1, 0, 0, 0, 123, -4, 0, 0, 0, 0, 0, 0, 7, 0, 0, 60 };
		try {
			TEncSbacV1 s = new TEncSbacV1();
			s.codeCoeff4x4(pcCoef, 0);
			if(s.coded_sub_block_flag_buffer[1] != (s.hasNonZero(pcCoef) == true ? 1: 0)){
				System.out.println(String.format("coded_sub_block_flag Diff: STD=%d, Output=%d", (s.hasNonZero(pcCoef) == true ? 1: 0), s.coded_sub_block_flag_buffer[1]));
			}
			for(int i=0;i<pcCoef.length;i++){
				if((pcCoef[i] != 0 ? 1 : 0) != s.sig_coeff_flag_buffer[3*i+1]){
					System.out.println(String.format("%dth sig_coeff_fla Diff: STD=%d, Output=%d",i, (pcCoef[i] != 0 ? 1 : 0), s.sig_coeff_flag_buffer[3*i+1]) );
				}
			}
			System.out.println("Done!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean hasNonZero(int[] pcCoef){
		for(int i=0;i<pcCoef.length;i++){
			if(pcCoef[i] != 0){
				return true;
			}
		}
		return false;
	}

	//buffer
	int[] coded_sub_block_flag_buffer = 			new int[3];// the size should be narrowed down later, 3 ints are a unit:(function, val,param)
	int[] sig_coeff_flag_buffer = 					new int[3*16];
	int[] coeff_abs_level_greater1_flag_buffer = 	new int[3*8];
	int[] escapeDataPresentInGroup_buffer = 		new int[3];// ��CPU���ж�remaining��offset������������
	int[] coeff_abs_level_greater2_flag_buffer = 	new int[3];
	
	//32*32
	int[] scan = new int[]{0,32,1,64,33,2,96,65,34,3,97,66,35,98,67,99,128,160,129,192,161,130,224,193,162,131,225,194,163,226,195,227,4,36,5,68,37,6,100,69,38,7,101,70,39,102,71,103,256,288,257,320,289,258,352,321,290,259,353,322,291,354,323,355,132,164,133,196,165,134,228,197,166,135,229,198,167,230,199,231,8,40,9,72,41,10,104,73,42,11,105,74,43,106,75,107,384,416,385,448,417,386,480,449,418,387,481,450,419,482,451,483,260,292,261,324,293,262,356,325,294,263,357,326,295,358,327,359,136,168,137,200,169,138,232,201,170,139,233,202,171,234,203,235,12,44,13,76,45,14,108,77,46,15,109,78,47,110,79,111,512,544,513,576,545,514,608,577,546,515,609,578,547,610,579,611,388,420,389,452,421,390,484,453,422,391,485,454,423,486,455,487,264,296,265,328,297,266,360,329,298,267,361,330,299,362,331,363,140,172,141,204,173,142,236,205,174,143,237,206,175,238,207,239,16,48,17,80,49,18,112,81,50,19,113,82,51,114,83,115,640,672,641,704,673,642,736,705,674,643,737,706,675,738,707,739,516,548,517,580,549,518,612,581,550,519,613,582,551,614,583,615,392,424,393,456,425,394,488,457,426,395,489,458,427,490,459,491,268,300,269,332,301,270,364,333,302,271,365,334,303,366,335,367,144,176,145,208,177,146,240,209,178,147,241,210,179,242,211,243,20,52,21,84,53,22,116,85,54,23,117,86,55,118,87,119,768,800,769,832,801,770,864,833,802,771,865,834,803,866,835,867,644,676,645,708,677,646,740,709,678,647,741,710,679,742,711,743,520,552,521,584,553,522,616,585,554,523,617,586,555,618,587,619,396,428,397,460,429,398,492,461,430,399,493,462,431,494,463,495,272,304,273,336,305,274,368,337,306,275,369,338,307,370,339,371,148,180,149,212,181,150,244,213,182,151,245,214,183,246,215,247,24,56,25,88,57,26,120,89,58,27,121,90,59,122,91,123,896,928,897,960,929,898,992,961,930,899,993,962,931,994,963,995,772,804,773,836,805,774,868,837,806,775,869,838,807,870,839,871,648,680,649,712,681,650,744,713,682,651,745,714,683,746,715,747,524,556,525,588,557,526,620,589,558,527,621,590,559,622,591,623,400,432,401,464,433,402,496,465,434,403,497,466,435,498,467,499,276,308,277,340,309,278,372,341,310,279,373,342,311,374,343,375,152,184,153,216,185,154,248,217,186,155,249,218,187,250,219,251,28,60,29,92,61,30,124,93,62,31,125,94,63,126,95,127,900,932,901,964,933,902,996,965,934,903,997,966,935,998,967,999,776,808,777,840,809,778,872,841,810,779,873,842,811,874,843,875,652,684,653,716,685,654,748,717,686,655,749,718,687,750,719,751,528,560,529,592,561,530,624,593,562,531,625,594,563,626,595,627,404,436,405,468,437,406,500,469,438,407,501,470,439,502,471,503,280,312,281,344,313,282,376,345,314,283,377,346,315,378,347,379,156,188,157,220,189,158,252,221,190,159,253,222,191,254,223,255,904,936,905,968,937,906,1000,969,938,907,1001,970,939,1002,971,1003,780,812,781,844,813,782,876,845,814,783,877,846,815,878,847,879,656,688,657,720,689,658,752,721,690,659,753,722,691,754,723,755,532,564,533,596,565,534,628,597,566,535,629,598,567,630,599,631,408,440,409,472,441,410,504,473,442,411,505,474,443,506,475,507,284,316,285,348,317,286,380,349,318,287,381,350,319,382,351,383,908,940,909,972,941,910,1004,973,942,911,1005,974,943,1006,975,1007,784,816,785,848,817,786,880,849,818,787,881,850,819,882,851,883,660,692,661,724,693,662,756,725,694,663,757,726,695,758,727,759,536,568,537,600,569,538,632,601,570,539,633,602,571,634,603,635,412,444,413,476,445,414,508,477,446,415,509,478,447,510,479,511,912,944,913,976,945,914,1008,977,946,915,1009,978,947,1010,979,1011,788,820,789,852,821,790,884,853,822,791,885,854,823,886,855,887,664,696,665,728,697,666,760,729,698,667,761,730,699,762,731,763,540,572,541,604,573,542,636,605,574,543,637,606,575,638,607,639,916,948,917,980,949,918,1012,981,950,919,1013,982,951,1014,983,1015,792,824,793,856,825,794,888,857,826,795,889,858,827,890,859,891,668,700,669,732,701,670,764,733,702,671,765,734,703,766,735,767,920,952,921,984,953,922,1016,985,954,923,1017,986,955,1018,987,1019,796,828,797,860,829,798,892,861,830,799,893,862,831,894,863,895,924,956,925,988,957,926,1020,989,958,927,1021,990,959,1022,991,1023};
	int[] notFirstGroupNeighbourhoodContextOffset  = {  3, 0};
	
	public void codeCoeff4x4(int[] pcCoef, int comp_id) throws Exception {

		final int DO_NOT_OUTPUT 			= 0;
		final int ENCODE_BIN				= 0;
		final int ENCODE_BIN_EP				= 1;
		final int ENCODE_BINS_EP			= 2;
	
		
		// scalar
		int width							= 32;
		int height							= 32;
		int scan_first_pos 					= 1023;//�Խ�ɨ������һ��Ԫ�ص�λ�ã������Ͽ�ʼ
		int sig_right 						= 1;// �ұߵ�4*4���Ƿ���ڷ���Ԫ
		int sig_low 						= 1;// �±ߵ�4*4���Ƿ���ڷ���Ԫ

		// counter
		int input_counter 					= 0;

		// loop variables
		int num_non_zero 								= 0;
		int c1 											= 1;
		int firstC2FlagIdx								= -1;
		int escapeDataPresentInGroup					= 0;
		int hasOutput_coeff_abs_level_greater2_flag		= 0;

		for (int i = 0; i < 4 * 4; i++) {
			int input = pcCoef[i];			
			int num_non_zero_1 = input == 0 ? num_non_zero : num_non_zero + 1;
			
			//����coded_sub_block_flag
			boolean is_input_end = input_counter == 15;
			int base_coeff_group_ctx_idx = comp_id == 0 ? 42 : 44;
			int ui_ctx_sig = (sig_right | sig_low) > 0 ? 1 : 0;
			int coded_sub_block_flag_ctx = base_coeff_group_ctx_idx + ui_ctx_sig; 
			coded_sub_block_flag_buffer[0] = is_input_end ? ENCODE_BIN : DO_NOT_OUTPUT;
			coded_sub_block_flag_buffer[1] = is_input_end ? (num_non_zero_1 > 0 ? 1: 0): DO_NOT_OUTPUT;
			coded_sub_block_flag_buffer[2] = is_input_end ? coded_sub_block_flag_ctx: DO_NOT_OUTPUT;

			//����sig_coeff_flag
			int ui_sig = input != 0 ? 1 : 0;
			int base_ctx_idx = 46 + ((comp_id == 0)? 0: 28);
			int pattern_sig_ctx = sig_right + (sig_low << 1);
			int scan_current_pos = scan_first_pos - input_counter;//��ǰ�ĶԽ�ɨ���λ��
			int rasterPosition = scan[scan_current_pos];//��ǰ��˳��ɨ���λ��
			int pos_y = rasterPosition >> width;
			int pos_x = rasterPosition - (pos_y << width);
			boolean is_special_case_for_dc = (pos_y + pos_x) == 0;
			boolean is_case_0 = pattern_sig_ctx == 0;
			boolean is_case_1 = pattern_sig_ctx == 1;
			boolean is_case_2 = pattern_sig_ctx == 2;
			boolean is_case_3 = pattern_sig_ctx == 3;
			int posXinSubset = (is_case_0 | is_case_2) ? pos_x & ((1 << 2)  - 1) : 0;
			int posYinSubset = (is_case_0 | is_case_1) ? pos_y & ((1 << 2)  - 1) : 0;
			int posTotalInSubset = posXinSubset + posYinSubset;
			int groupHeight  = 1 << 2;
			int groupWidth   = 1 << 2;
			int cnt = is_case_0 ? ((posTotalInSubset >= 3) ? 0 : ((posTotalInSubset >= 1) ? 1 : 2)) : 
					  is_case_1 ? ((posYinSubset >= (groupHeight >> 1)) ? 0 : ((posYinSubset >= (groupHeight >> 2)) ? 1 : 2)) :
					  is_case_2 ? ((posXinSubset >= (groupWidth >> 1)) ? 0 : ((posXinSubset >= (groupWidth >> 2)) ? 1 : 2)) :
					  is_case_3 ? (2) : 0;
			boolean notFirstGroup = ((pos_x >> 2) + (pos_y >> 2)) > 0;
			int offset = (notFirstGroup ? notFirstGroupNeighbourhoodContextOffset[comp_id] : 0) + cnt;
			int firstSignificanceMapContext = width == 32 ? 21 : 12;
			int uiCtxSig1 = is_special_case_for_dc ? 0 : (firstSignificanceMapContext + offset);
			sig_coeff_flag_buffer[input_counter * 3 + 0] = ENCODE_BIN;
			sig_coeff_flag_buffer[input_counter * 3 + 1] = ui_sig;
			sig_coeff_flag_buffer[input_counter * 3 + 2] = base_ctx_idx + uiCtxSig1;
			
			//����coeff_abs_level_greater1_flag
			int absCoeff = (int)Math.abs(input);
			boolean isOutputCoeff_abs_level_greater1_flag = (absCoeff != 0 & num_non_zero < 8);
			int uiSymbol = absCoeff > 1 ? 1 : 0;
			int notFirstSubsetOffset = (comp_id == 0 & (scan_first_pos > 15)) ? 2 : 0;
			int foundAGreaterThan1Offset = (c1 == 0) ? 1 : 0;
			int uiCtxSet = (comp_id==0?0:4) + notFirstSubsetOffset + foundAGreaterThan1Offset;
			int baseCtxMod = 150 + (4 * uiCtxSet);
			coeff_abs_level_greater1_flag_buffer[num_non_zero * 3 + 0] = isOutputCoeff_abs_level_greater1_flag ? ENCODE_BIN : DO_NOT_OUTPUT;
			coeff_abs_level_greater1_flag_buffer[num_non_zero * 3 + 1] = isOutputCoeff_abs_level_greater1_flag ? uiSymbol : DO_NOT_OUTPUT;
			coeff_abs_level_greater1_flag_buffer[num_non_zero * 3 + 2] = isOutputCoeff_abs_level_greater1_flag ? baseCtxMod + c1 : DO_NOT_OUTPUT;
			boolean isBranch1 = (uiSymbol == 1) & isOutputCoeff_abs_level_greater1_flag;
			boolean isBranch2 = isBranch1 & firstC2FlagIdx == -1;
			boolean isBranch3 = isBranch1 & firstC2FlagIdx != -1;
			boolean isBranch4 = isOutputCoeff_abs_level_greater1_flag & (c1 < 3) & (c1 > 0);
			int c1_1 = isBranch1 ? 0 : (isBranch4 ? c1+1: c1);
			boolean firstC2Flag = isBranch2 ? true : false;
			int escapeDataPresentInGroup_1 = isBranch3 ? 1 : escapeDataPresentInGroup;
			
			//����coeff_abs_level_greater2_flag
			int symbol = absCoeff > 2 ? 1 : 0;
			boolean isBranch5 = (firstC2Flag & hasOutput_coeff_abs_level_greater2_flag == 0);
			boolean isBranch6 = isBranch5 & (symbol != 0);
			int hasOutput_coeff_abs_level_greater2_flag_1 = (hasOutput_coeff_abs_level_greater2_flag == 1) ? 1 : isBranch5 ? 1 : 0;
			int escapeDataPresentInGroup_2 = isBranch6 ? 1 : escapeDataPresentInGroup_1;
			int baseCtxMod_1 = 174 + (1 * uiCtxSet);
			coeff_abs_level_greater2_flag_buffer[0] = isBranch5 ? ENCODE_BIN : DO_NOT_OUTPUT;
			coeff_abs_level_greater2_flag_buffer[1] = isBranch5 ? symbol : DO_NOT_OUTPUT;
			coeff_abs_level_greater2_flag_buffer[2] = isBranch5 ? baseCtxMod_1 : DO_NOT_OUTPUT;
			
			//���escapeDataPresentInGroup
			int escapeDataPresentInGroup_3 = is_input_end ? ((escapeDataPresentInGroup_2 == 1 | (num_non_zero_1 > 8)) ? 1 : 0) : escapeDataPresentInGroup_2;
			
			//ѭ�����õı���
			num_non_zero = num_non_zero_1;
			c1 = c1_1;
			hasOutput_coeff_abs_level_greater2_flag = hasOutput_coeff_abs_level_greater2_flag_1;
			escapeDataPresentInGroup = escapeDataPresentInGroup_3;
			input_counter++;
		}

	}
}