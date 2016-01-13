package residueCoding;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import entropyEncoder.EntropyEncoderHevc;
import entropyEncoder.State;

/**
 * Definition for undirected graph. class UndirectedGraphNode { int label;
 * List<UndirectedGraphNode> neighbors; UndirectedGraphNode(int x) { label = x;
 * neighbors = new ArrayList<UndirectedGraphNode>(); } };
 */
public class TEncSbacV1 {
	public static int[] scan = new int[]{0,32,1,64,33,2,96,65,34,3,97,66,35,98,67,99,128,160,129,192,161,130,224,193,162,131,225,194,163,226,195,227,4,36,5,68,37,6,100,69,38,7,101,70,39,102,71,103,256,288,257,320,289,258,352,321,290,259,353,322,291,354,323,355,132,164,133,196,165,134,228,197,166,135,229,198,167,230,199,231,8,40,9,72,41,10,104,73,42,11,105,74,43,106,75,107,384,416,385,448,417,386,480,449,418,387,481,450,419,482,451,483,260,292,261,324,293,262,356,325,294,263,357,326,295,358,327,359,136,168,137,200,169,138,232,201,170,139,233,202,171,234,203,235,12,44,13,76,45,14,108,77,46,15,109,78,47,110,79,111,512,544,513,576,545,514,608,577,546,515,609,578,547,610,579,611,388,420,389,452,421,390,484,453,422,391,485,454,423,486,455,487,264,296,265,328,297,266,360,329,298,267,361,330,299,362,331,363,140,172,141,204,173,142,236,205,174,143,237,206,175,238,207,239,16,48,17,80,49,18,112,81,50,19,113,82,51,114,83,115,640,672,641,704,673,642,736,705,674,643,737,706,675,738,707,739,516,548,517,580,549,518,612,581,550,519,613,582,551,614,583,615,392,424,393,456,425,394,488,457,426,395,489,458,427,490,459,491,268,300,269,332,301,270,364,333,302,271,365,334,303,366,335,367,144,176,145,208,177,146,240,209,178,147,241,210,179,242,211,243,20,52,21,84,53,22,116,85,54,23,117,86,55,118,87,119,768,800,769,832,801,770,864,833,802,771,865,834,803,866,835,867,644,676,645,708,677,646,740,709,678,647,741,710,679,742,711,743,520,552,521,584,553,522,616,585,554,523,617,586,555,618,587,619,396,428,397,460,429,398,492,461,430,399,493,462,431,494,463,495,272,304,273,336,305,274,368,337,306,275,369,338,307,370,339,371,148,180,149,212,181,150,244,213,182,151,245,214,183,246,215,247,24,56,25,88,57,26,120,89,58,27,121,90,59,122,91,123,896,928,897,960,929,898,992,961,930,899,993,962,931,994,963,995,772,804,773,836,805,774,868,837,806,775,869,838,807,870,839,871,648,680,649,712,681,650,744,713,682,651,745,714,683,746,715,747,524,556,525,588,557,526,620,589,558,527,621,590,559,622,591,623,400,432,401,464,433,402,496,465,434,403,497,466,435,498,467,499,276,308,277,340,309,278,372,341,310,279,373,342,311,374,343,375,152,184,153,216,185,154,248,217,186,155,249,218,187,250,219,251,28,60,29,92,61,30,124,93,62,31,125,94,63,126,95,127,900,932,901,964,933,902,996,965,934,903,997,966,935,998,967,999,776,808,777,840,809,778,872,841,810,779,873,842,811,874,843,875,652,684,653,716,685,654,748,717,686,655,749,718,687,750,719,751,528,560,529,592,561,530,624,593,562,531,625,594,563,626,595,627,404,436,405,468,437,406,500,469,438,407,501,470,439,502,471,503,280,312,281,344,313,282,376,345,314,283,377,346,315,378,347,379,156,188,157,220,189,158,252,221,190,159,253,222,191,254,223,255,904,936,905,968,937,906,1000,969,938,907,1001,970,939,1002,971,1003,780,812,781,844,813,782,876,845,814,783,877,846,815,878,847,879,656,688,657,720,689,658,752,721,690,659,753,722,691,754,723,755,532,564,533,596,565,534,628,597,566,535,629,598,567,630,599,631,408,440,409,472,441,410,504,473,442,411,505,474,443,506,475,507,284,316,285,348,317,286,380,349,318,287,381,350,319,382,351,383,908,940,909,972,941,910,1004,973,942,911,1005,974,943,1006,975,1007,784,816,785,848,817,786,880,849,818,787,881,850,819,882,851,883,660,692,661,724,693,662,756,725,694,663,757,726,695,758,727,759,536,568,537,600,569,538,632,601,570,539,633,602,571,634,603,635,412,444,413,476,445,414,508,477,446,415,509,478,447,510,479,511,912,944,913,976,945,914,1008,977,946,915,1009,978,947,1010,979,1011,788,820,789,852,821,790,884,853,822,791,885,854,823,886,855,887,664,696,665,728,697,666,760,729,698,667,761,730,699,762,731,763,540,572,541,604,573,542,636,605,574,543,637,606,575,638,607,639,916,948,917,980,949,918,1012,981,950,919,1013,982,951,1014,983,1015,792,824,793,856,825,794,888,857,826,795,889,858,827,890,859,891,668,700,669,732,701,670,764,733,702,671,765,734,703,766,735,767,920,952,921,984,953,922,1016,985,954,923,1017,986,955,1018,987,1019,796,828,797,860,829,798,892,861,830,799,893,862,831,894,863,895,924,956,925,988,957,926,1020,989,958,927,1021,990,959,1022,991,1023};
	/**
	 * 调用熵编码的顺序：
	 * 1）最后一个非零元的位置
	 * 2）4*4的groupFlag
	 * 3）4*4的group内所有元素的sigFlag
	 * 4）4*4内前8个元素的greaterThan1Flag
	 * 5）一个greaterThan2Flag
	 * 6）所有的符号位一起
	 * 7）remainsAbs一起
	 * 
	 * 特殊情况：
	 * 1）lastScanPos所在的4*4块，以及第一个4*4块的groupFlag不编码，默认为1
	 * 2）lastScanPos的sigFlag不编码，默认为1
	 * 3）如果存在greaterThan1Flag，编码greaterThan2Flag，否则不存在
	 * 4）当coeff_abs_level_greater1_flag不为零的个数大于1 || coeff_abs_level_greater2_flag==1 || numNonZero>8时，不为0的remainsAbs为有效编码
	 * @param pcCoef
	 * @param compID
	 * @throws Exception
	 */
	public static void main(String[] args) {
		int[] pcCoef_32 = new int[] {-25,-29,-25,-33,-29,-25,-35,-33,-29,-26,-35,-33,-29,-35,-33,-35,-35,-33,-35,-28,-33,-35,-24,-29,-33,-35,-24,-29,-33,-25,-30,-26,-28,-31,-29,-33,-31,-32,-34,-33,-33,-34,-34,-33,-34,-33,-33,-32,-20,-19,-21,-21,-20,-22,-23,-22,-21,-24,-24,-23,-23,-26,-24,-27,-34,-32,-33,-30,-32,-32,-27,-29,-31,-31,-27,-29,-30,-27,-29,-28,-35,-34,-37,-33,-35,-39,-31,-32,-36,-40,-31,-32,-37,-30,-32,-29,-26,-28,-27,-29,-30,-29,-27,-30,-31,-30,-28,-31,-31,-30,-32,-31,-24,-24,-25,-25,-24,-26,-27,-25,-25,-27,-27,-26,-26,-27,-26,-27,-30,-29,-29,-29,-29,-28,-28,-28,-28,-28,-28,-28,-27,-29,-28,-29,-41,-37,-42,-32,-37,-42,-29,-32,-38,-42,-29,-32,-38,-28,-32,-28,-25,-24,-26,-24,-25,-28,-27,-25,-26,-29,-28,-27,-28,-29,-29,-30,-29,-31,-29,-31,-30,-28,-31,-31,-29,-27,-30,-30,-28,-30,-29,-30,-28,-27,-28,-27,-28,-29,-27,-27,-29,-30,-27,-28,-30,-26,-28,-26,-27,-27,-26,-28,-26,-26,-30,-28,-26,-26,-30,-28,-26,-30,-28,-30,-41,-37,-40,-32,-37,-39,-28,-32,-36,-38,-28,-32,-35,-29,-32,-29,-30,-34,-31,-36,-35,-32,-35,-37,-35,-33,-36,-37,-36,-36,-37,-36,-29,-29,-30,-29,-29,-30,-31,-30,-30,-30,-31,-31,-31,-32,-32,-33,-27,-27,-26,-28,-26,-25,-29,-27,-25,-25,-29,-26,-24,-29,-26,-28,-31,-30,-31,-29,-31,-31,-26,-29,-31,-32,-26,-29,-32,-26,-29,-26,-26,-26,-26,-28,-26,-26,-30,-28,-26,-27,-30,-28,-27,-30,-28,-30,-37,-35,-36,-32,-34,-35,-29,-32,-34,-34,-30,-32,-33,-31,-33,-31,-33,-29,-33,-25,-29,-34,-23,-26,-30,-34,-24,-26,-30,-24,-27,-25,-33,-35,-33,-36,-35,-33,-36,-36,-34,-33,-35,-35,-34,-34,-34,-33,-31,-32,-31,-33,-33,-31,-33,-34,-34,-32,-34,-35,-34,-35,-36,-35,-24,-24,-24,-25,-23,-24,-28,-25,-23,-24,-28,-24,-23,-28,-24,-28,-32,-32,-31,-29,-31,-31,-26,-29,-31,-31,-26,-29,-30,-26,-29,-26,-28,-27,-28,-28,-28,-29,-29,-28,-29,-30,-29,-28,-29,-28,-29,-28,-34,-34,-34,-33,-33,-33,-32,-33,-33,-33,-33,-33,-33,-34,-33,-34,-24,-26,-24,-29,-26,-25,-31,-29,-27,-25,-31,-29,-27,-31,-29,-30,-33,-30,-33,-28,-31,-33,-26,-28,-31,-32,-27,-29,-31,-28,-30,-29,-33,-33,-33,-33,-33,-34,-32,-32,-32,-34,-32,-31,-32,-31,-30,-30,-32,-35,-32,-36,-35,-32,-36,-37,-36,-32,-36,-37,-36,-36,-37,-36,-24,-23,-24,-24,-23,-24,-28,-24,-23,-24,-28,-25,-24,-28,-25,-28,-30,-30,-29,-28,-29,-28,-26,-28,-28,-28,-26,-27,-27,-27,-27,-27,-31,-30,-32,-29,-31,-33,-28,-29,-32,-34,-27,-29,-32,-27,-30,-27,-32,-32,-32,-33,-33,-32,-35,-33,-33,-32,-35,-33,-33,-35,-33,-35,-26,-27,-26,-28,-27,-27,-29,-28,-27,-28,-29,-27,-27,-28,-27,-27,-32,-31,-32,-31,-32,-31,-30,-31,-32,-31,-31,-32,-32,-32,-33,-32,-34,-31,-34,-30,-31,-34,-29,-29,-31,-34,-29,-29,-31,-28,-28,-28,-32,-36,-32,-37,-36,-32,-36,-37,-35,-32,-36,-37,-35,-36,-36,-36,-25,-24,-25,-26,-25,-26,-28,-26,-26,-27,-29,-27,-27,-29,-28,-29,-27,-26,-26,-26,-25,-25,-27,-26,-24,-24,-27,-25,-24,-27,-25,-27,-35,-33,-35,-30,-33,-36,-26,-30,-34,-36,-26,-30,-34,-26,-30,-26,-29,-27,-29,-27,-28,-30,-26,-26,-28,-31,-25,-26,-28,-24,-25,-24,-30,-32,-30,-33,-32,-30,-33,-34,-32,-30,-34,-34,-32,-34,-34,-34,-34,-31,-34,-28,-31,-34,-28,-29,-31,-34,-28,-29,-31,-29,-30,-29,-32,-34,-31,-36,-34,-31,-35,-35,-33,-31,-35,-34,-32,-34,-33,-33,-27,-28,-28,-29,-29,-29,-30,-30,-30,-29,-30,-31,-31,-30,-31,-31,-24,-23,-23,-24,-22,-23,-27,-24,-22,-23,-27,-24,-22,-27,-24,-27,-31,-28,-31,-25,-28,-32,-23,-25,-28,-32,-22,-24,-28,-22,-24,-22,-30,-32,-30,-34,-32,-30,-34,-34,-32,-30,-34,-34,-32,-34,-33,-33,-34,-32,-34,-30,-32,-33,-30,-31,-33,-33,-31,-32,-33,-32,-33,-32,-30,-31,-30,-32,-30,-30,-33,-31,-29,-29,-32,-30,-29,-31,-29,-31,-30,-31,-30,-32,-32,-30,-31,-32,-32,-30,-31,-33,-33,-31,-33,-31,-32,-28,-32,-24,-28,-31,-22,-24,-28,-31,-22,-25,-28,-22,-25,-23,-31,-32,-31,-33,-32,-32,-32,-32,-32,-32,-32,-31,-31,-31,-31,-30,-33,-34,-33,-34,-34,-33,-33,-35,-35,-33,-34,-36,-35,-35,-36,-36,-29,-28,-29,-29,-28,-29,-30,-28,-27,-29,-30,-28,-27,-30,-27,-30,-31,-28,-30,-25,-28,-29,-24,-26,-28,-29,-24,-26,-27,-25,-27,-26,-32,-31,-33,-30,-31,-33,-29,-29,-31,-33,-28,-28,-30,-27,-28,-26,-33,-36,-33,-37,-36,-33,-36,-38,-36,-33,-37,-38,-36,-37,-38,-37,-28,-27,-27,-27,-27,-26,-27,-27,-27,-26,-28,-28,-27,-29,-28,-29,-34,-30,-34,-27,-30,-34,-25,-27,-30,-34,-25,-26,-30,-24,-26,-24,-25,-27,-25,-29,-27,-25,-30,-29,-27,-25,-31,-29,-27,-31,-29,-31};
		int lastScanPos_32 = 1023;
		int width = 32;
		
		int[] pcCoef_16 = new int[] {-19,-20,-19,-20,-20,-19,-20,-20,-20,-19,-20,-20,-20,-20,-20,-20};
		
		int[] pcCoef_32_cpu  = new int[]{-31,-31,-31,-30,-29,-29,-28,-27,-26,-25,-24,-24,-23,-22,-22,-22,-22,-22,-22,-23,-24,-24,-25,-26,-27,-28,-29,-29,-30,-31,-31,-31,-29,-29,-29,-29,-28,-28,-27,-27,-27,-26,-26,-25,-25,-25,-24,-24,-24,-24,-25,-25,-25,-26,-26,-27,-27,-27,-28,-28,-29,-29,-29,-29,-27,-27,-27,-27,-27,-27,-27,-27,-27,-28,-28,-28,-28,-28,-28,-28,-28,-28,-28,-28,-28,-28,-28,-27,-27,-27,-27,-27,-27,-27,-26,-26,-25,-25,-25,-25,-26,-26,-27,-28,-29,-29,-30,-31,-31,-31,-32,-32,-32,-32,-31,-31,-31,-30,-29,-29,-28,-27,-26,-26,-25,-25,-24,-24,-24,-24,-25,-25,-26,-27,-28,-29,-30,-31,-32,-32,-33,-34,-34,-34,-34,-34,-34,-33,-32,-32,-31,-30,-29,-28,-27,-26,-25,-24,-24,-23,-26,-26,-27,-27,-28,-28,-29,-30,-31,-31,-32,-33,-33,-34,-34,-34,-34,-34,-34,-33,-33,-32,-31,-31,-30,-29,-28,-28,-27,-26,-26,-25,-30,-30,-30,-30,-30,-31,-31,-31,-31,-32,-32,-32,-32,-32,-32,-32,-32,-32,-32,-32,-32,-32,-32,-31,-31,-31,-31,-30,-30,-30,-29,-29,-34,-34,-34,-34,-33,-33,-33,-32,-32,-32,-31,-31,-30,-30,-30,-30,-30,-30,-30,-30,-31,-31,-32,-32,-32,-33,-33,-33,-34,-34,-33,-33,-37,-37,-37,-36,-36,-35,-34,-33,-32,-32,-31,-30,-29,-29,-28,-28,-28,-28,-29,-29,-30,-31,-32,-32,-33,-34,-35,-36,-36,-36,-36,-35,-38,-38,-38,-37,-36,-36,-35,-34,-33,-32,-31,-30,-30,-29,-29,-28,-28,-29,-29,-30,-30,-31,-32,-33,-34,-35,-36,-36,-37,-37,-37,-36,-36,-36,-36,-36,-35,-35,-34,-34,-33,-33,-32,-32,-31,-31,-31,-31,-31,-31,-31,-31,-32,-32,-33,-33,-34,-34,-35,-35,-36,-35,-35,-34,-33,-33,-33,-33,-33,-33,-33,-33,-33,-33,-34,-34,-34,-34,-34,-34,-34,-34,-34,-34,-34,-34,-33,-33,-33,-33,-33,-33,-33,-32,-31,-30,-30,-30,-30,-30,-31,-31,-32,-33,-33,-34,-35,-35,-36,-36,-36,-36,-36,-36,-36,-36,-35,-35,-34,-33,-33,-32,-31,-31,-30,-29,-28,-27,-27,-28,-28,-29,-29,-30,-31,-32,-33,-34,-35,-36,-36,-37,-37,-37,-37,-37,-37,-36,-36,-35,-34,-33,-32,-31,-30,-29,-29,-27,-25,-24,-27,-27,-28,-28,-29,-29,-30,-31,-32,-33,-34,-34,-35,-35,-36,-36,-36,-36,-35,-35,-34,-34,-33,-32,-31,-30,-29,-29,-28,-26,-25,-24,-29,-29,-29,-29,-29,-30,-30,-30,-31,-31,-31,-32,-32,-32,-32,-32,-32,-32,-32,-32,-32,-31,-31,-31,-30,-30,-30,-29,-29,-28,-26,-25,-31,-31,-31,-31,-31,-30,-30,-30,-29,-29,-29,-28,-28,-28,-28,-28,-28,-28,-28,-28,-28,-29,-29,-29,-30,-30,-30,-31,-31,-30,-28,-27,-33,-33,-32,-32,-31,-31,-30,-29,-28,-27,-26,-26,-25,-25,-24,-24,-24,-24,-25,-25,-26,-26,-27,-28,-29,-30,-31,-31,-32,-31,-30,-29,-33,-32,-32,-31,-31,-30,-29,-28,-27,-26,-25,-24,-24,-23,-23,-23,-23,-23,-23,-24,-24,-25,-26,-27,-28,-29,-30,-31,-31,-31,-30,-28,-30,-30,-30,-30,-29,-29,-28,-27,-27,-26,-25,-25,-24,-24,-24,-24,-24,-24,-24,-24,-25,-25,-26,-27,-27,-28,-29,-29,-30,-29,-27,-26,-27,-27,-27,-27,-27,-27,-27,-27,-27,-27,-26,-26,-26,-26,-26,-26,-26,-26,-26,-26,-26,-26,-27,-27,-27,-27,-27,-27,-27,-26,-24,-23,-24,-24,-24,-24,-25,-25,-26,-26,-27,-27,-28,-28,-29,-29,-29,-29,-29,-29,-29,-29,-28,-28,-27,-27,-26,-26,-25,-25,-24,-23,-22,-21,-22,-22,-22,-23,-24,-24,-25,-26,-27,-28,-29,-30,-30,-31,-31,-32,-32,-31,-31,-30,-30,-29,-28,-27,-26,-25,-24,-24,-23,-21,-20,-19,-23,-23,-23,-24,-24,-25,-26,-27,-28,-28,-29,-30,-31,-31,-31,-32,-32,-31,-31,-31,-30,-29,-28,-28,-27,-26,-25,-24,-24,-22,-21,-20,-26,-26,-26,-26,-27,-27,-27,-28,-28,-28,-29,-29,-30,-30,-30,-30,-30,-30,-30,-30,-29,-29,-28,-28,-28,-27,-27,-27,-26,-25,-24,-24,-30,-30,-30,-30,-30,-29,-29,-29,-29,-28,-28,-28,-28,-28,-28,-28,-28,-28,-28,-28,-28,-28,-28,-29,-29,-29,-29,-30,-30,-29,-29,-28,-34,-34,-33,-33,-32,-32,-31,-30,-29,-29,-28,-27,-27,-26,-26,-26,-26,-26,-26,-27,-27,-28,-29,-29,-30,-31,-32,-32,-33,-33,-33,-33,-36,-36,-35,-35,-34,-33,-32,-31,-30,-29,-28,-28,-27,-26,-26,-26,-26,-26,-26,-27,-28,-28,-29,-30,-31,-32,-33,-34,-35,-35,-35,-35,-35,-35,-35,-35,-34,-34,-33,-32,-31,-31,-30,-29,-29,-29,-28,-28,-28,-28,-29,-29,-29,-30,-31,-31,-32,-33,-34,-34,-35,-35,-35,-35,-33,-33,-33,-33,-33,-33,-33,-33,-33,-32,-32,-32,-32,-32,-32,-32,-32,-32,-32,-32,-32,-32,-32,-33,-33,-33,-33,-33,-33,-33,-33,-33,-33,-33,-33,-32,-33,-33,-33,-34,-33,-34,-34,-35,-35,-36,-37,-37,-38,-38,-37,-37,-37,-36,-35,-34,-34,-33,-31,-31,-29,-29,-29,-29,-32,-32,-32,-32,-33,-33,-34,-34,-34,-35,-36,-37,-38,-39,-40,-41,-42,-42,-42,-41,-40,-39,-37,-35,-34,-32,-29,-28,-26,-25,-25,-25};
		int[] pcCoef_16_cpu  = new int[]{-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-20,-19,-19,-19,-19,-19,-19,-19,-19,-19,-19,-19,-19,-19,-19,-19,-19};
		try {
			TEncSbacV1_test cpu = new TEncSbacV1_test();
			TEncSbacV1_test.m_pcBinIf = new EntropyEncoderHevc(State.m_contextState_0, State.m_fifo);
			//cpu.codeCoeffNxN(pcCoef_16_cpu, 1);
			cpu.codeCoeffNxN(pcCoef_32_cpu, 0);
			
			
			
			int groupNum = width*width/4/4;
			TEncSbacV1 fpga = new TEncSbacV1();
			int[] uiSigCoeffGroupFlag = new int[groupNum];//顺序扫描
			int uiLog2BlockWidth = (int)(Math.log(width)/Math.log(2));
			int scanFirstPos_32 = ((int)((lastScanPos_32) / 16)) * 16 + 15;
			ArrayList<Long> buffer_fpga = new ArrayList<>();
			
			for (int i = 0; i < (lastScanPos_32) / 16 + 1; i++) {
				
				int posLast = scan[scanFirstPos_32];// posLast是顺序扫描的位置
				int uiPosY = posLast >> uiLog2BlockWidth;// uiPosY是垂直方向的位置
				int uiPosX = posLast - (uiPosY << uiLog2BlockWidth);// uiPosX是水平方向的位置
				int groupPosY = uiPosY >> 2;
				int groupPosX = uiPosX >> 2;
				int uiBlkIdx = (width/4 * (uiPosY >> 2)) + (uiPosX >> 2);// uiBlkIdx是4*4大小的块在32*32的TU当中顺序扫描的位置
				
				
				int sigRight = groupPosX >= width/4-1 ? 0 : uiSigCoeffGroupFlag[uiBlkIdx+1];
				int sigLower = groupPosY >= width/4-1 ? 0 : uiSigCoeffGroupFlag[uiBlkIdx + width/4];
				
				fpga.codeCoeff4x4(pcCoef_32, i*16, 0, width, scanFirstPos_32, sigRight, sigLower, fpga.c1_buffer[0]);
				ArrayList<Long> fpgaOutput = fpga.getOutput(lastScanPos_32, scanFirstPos_32);
				
				
				uiSigCoeffGroupFlag[uiBlkIdx] = fpga.coded_sub_block_flag_buffer[1];// uiSigCoeffGroupFlag中的值是顺序扫描的
				scanFirstPos_32 -= 16;
				
				//ArrayList<Long> fpgaOutput = fpga.getOutput(255, 255);
				/*if(fpgaOutput.size() != cpu.buffer.size()){
					System.out.println(String.format("Size Diff: CPU=%d, FPGA=%d", cpu.buffer.size(), fpgaOutput.size() ));
				}*/
				for(int j=0;j<fpgaOutput.size();j++){
					if(!fpgaOutput.get(j).equals(cpu.buffer.get(j+buffer_fpga.size()))){
						System.out.println(String.format("[%d,%d] Val Diff: CPU=%d, FPGA=%d", i, j, cpu.buffer.get(j+buffer_fpga.size()), fpgaOutput.get(j) ));
					}
				}
				
				buffer_fpga.addAll(fpgaOutput);
			}			
			
			
			/*fpga.check(cpu.coded_sub_block_flag_buffer, fpga.coded_sub_block_flag_buffer, "coded_sub_block_flag");
			fpga.check(cpu.sig_coeff_flag_buffer, fpga.sig_coeff_flag_buffer, "sig_coeff_flag_buffer");
			fpga.check(cpu.coeff_abs_level_greater1_flag_buffer, fpga.coeff_abs_level_greater1_flag_buffer, "coeff_abs_level_greater1_flag_buffer");
			if(fpga.getNumNonZero(cpu.coeff_abs_level_greater1_flag_buffer) != 0){
				fpga.check(cpu.coeff_abs_level_greater2_flag_buffer, fpga.coeff_abs_level_greater2_flag_buffer, "coeff_abs_level_greater2_flag_buffer");
			}
			fpga.check(cpu.coeff_abs_level_remaining_buffer, fpga.coeff_abs_level_remaining_buffer, "coeff_abs_level_remaining_buffer");
			System.out.println("Done!");*/
			
			/*//get the compressed flag
			int[] coded_sub_block_flag_compressed = fpga.getCompressedFlag(fpga.coded_sub_block_flag_buffer);
			int[] sig_coeff_flag_compressed = fpga.getCompressedFlag(fpga.sig_coeff_flag_buffer);
			for(int i=0;i<sig_coeff_flag_compressed.length;i++){
				System.out.print(sig_coeff_flag_compressed[i]+", ");
			}
			int[] coeff_abs_level_greater1_flag_compressed = fpga.getCompressedFlag(fpga.coeff_abs_level_greater1_flag_buffer);
			for(int i=0;i<coeff_abs_level_greater1_flag_compressed.length;i++){
				System.out.print(coeff_abs_level_greater1_flag_compressed[i] +", ");
			}
			int[] coeff_abs_level_greater2_flag_compressed = fpga.getCompressedFlag(fpga.coeff_abs_level_greater2_flag_buffer);
			for(int i=0;i<coeff_abs_level_greater2_flag_compressed.length;i++){
				System.out.print(coeff_abs_level_greater2_flag_compressed[i] +", ");
			}
			int[] coeff_sign_flag_compressed = fpga.getCompressedFlag(fpga.coeff_sign_flag_buffer);
			for(int i=0;i<coeff_sign_flag_compressed.length;i++){
				System.out.print(coeff_sign_flag_compressed[i] +", ");
			}
			int[] coeff_abs_level_remaining_compressed = fpga.getCompressedFlag(fpga.coeff_abs_level_remaining_buffer);
			for(int i=0;i<coeff_abs_level_remaining_compressed.length;i+=2){
				long v = (((long)coeff_abs_level_remaining_compressed[i])* (1l<<32)+coeff_abs_level_remaining_compressed[i+1]);
				String str = Long.toString(v);
				System.out.print(String.format("%s,", str));
			}*/
			
			//check the whole output
			/*ArrayList<Long> fpgaOutput = buffer_fpga;
			//ArrayList<Long> fpgaOutput = fpga.getOutput(255, 255);
			if(fpgaOutput.size() != cpu.buffer.size()){
				System.out.println(String.format("Size Diff: CPU=%d, FPGA=%d", cpu.buffer.size(), fpgaOutput.size() ));
			}
			for(int i=0;i<fpgaOutput.size();i++){
				if(!fpgaOutput.get(i).equals(cpu.buffer.get(i))){
					System.out.println(String.format("[%d] Val Diff: CPU=%d, FPGA=%d", i, cpu.buffer.get(i), fpgaOutput.get(i) ));
				}
			}*/
			System.out.println("Done!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 将原始的4*4的编码过滤掉特殊情况，并合并所有的buffer的有效值
	 * 此时的仍然为encodeBins，还没有拆分开来
	 * 
	 * @param lastScanPos	 对角扫描的最后一个非零元素的位置，从左上开始		 
	 * @param scanFirstPos	 对角扫描的最后一个元素的位置，从左上开始, 16的倍数-1
	 * @return
	 */
	public ArrayList<Long> getOutput(int lastScanPos, int scanFirstPos){
		//get the compressed flag
		int[] coded_sub_block_flag_compressed = getCompressedFlag(coded_sub_block_flag_buffer);
		int[] sig_coeff_flag_compressed = getCompressedFlag(sig_coeff_flag_buffer);
		int[] coeff_abs_level_greater1_flag_compressed = getCompressedFlag(coeff_abs_level_greater1_flag_buffer);
		int[] coeff_abs_level_greater2_flag_compressed = getCompressedFlag(coeff_abs_level_greater2_flag_buffer);
		int[] coeff_sign_flag_compressed = getCompressedFlag(coeff_sign_flag_buffer);
		int[] coeff_abs_level_remaining_compressed = getCompressedFlag(coeff_abs_level_remaining_buffer);
		ArrayList<Long> res = new ArrayList<>();
		
		//过滤groupFlag，lastScanPos所在的4*4块，以及第一个4*4块的groupFlag不编码，默认为1
		boolean isLastBlock = lastScanPos >= scanFirstPos-15 && lastScanPos <= scanFirstPos;
		boolean defaultGroupFlagTrue = false;
		if(isLastBlock || scanFirstPos == 15){
			defaultGroupFlagTrue = true;
		}else{
			res.add((long) coded_sub_block_flag_compressed[0]);
		}
		
		if( (coded_sub_block_flag_compressed[0] & (1l << 8)) > 0l || defaultGroupFlagTrue){
			
			int startIdx = isLastBlock ? (scanFirstPos - lastScanPos) : 0;
			int numNonZero = 0;
			
			//过滤sigFlag， lastScanPos的sigFlag不编码
			for(int i=startIdx + (isLastBlock ? 1 : 0); i < 16; i++){
				res.add((long)sig_coeff_flag_compressed[i]);
				if((sig_coeff_flag_compressed[i] & (1 << 8)) != 0){
					numNonZero++;
				}
			}
			 
			//过滤greater1_flag
			int numGreater1Coded = 0;
			for(int i=startIdx; i < 16 && numGreater1Coded < 8; i++){
				if(sig_coeff_flag_compressed[i] != 0){
					res.add((long)coeff_abs_level_greater1_flag_compressed[i]);
					numGreater1Coded++;
				}
			}
			
			//过滤greater2_flag
			if(numGreater1Coded != 0){
				res.add((long)coeff_abs_level_greater2_flag_compressed[0]);
			}
			
			//过滤sign_flag
			res.add((long)coeff_sign_flag_compressed[0]);
			
			//过滤remaining
			if(numGreater1Coded > 1 || ((coeff_abs_level_greater2_flag_compressed[0] & (1 << 8)) != 0) || numNonZero>8)
			for(int i=startIdx*2;i < 16*2; i+=2){
				if(coeff_abs_level_remaining_compressed[i] != 0){
					res.add((long)coeff_abs_level_remaining_compressed[i] * (1l << 32) + coeff_abs_level_remaining_compressed[i+1]);
				}
			}
		}
		return  res;
	}
	
	public int[] getCompressedFlag(int[] buffer){
		int[] res = new int[buffer.length/3];
		for(int i=0;i<buffer.length/3;i++){
			int type = buffer[3*i] << 29;
			int val = buffer[3*i + 1] << 8;
			int ctx = buffer[3*i+2];
			res[i] = type | val | ctx; 
		}
		return res;
	}
	
/*	public boolean check(int[] arrayCpu, int[] arrayFpga, String arrayName){
		if(arrayCpu.length != arrayFpga.length){
			System.out.println(String.format("%s size Diff, CPU=%d, FPGA=%d", arrayName, arrayCpu.length, arrayFpga.length));
			return false;
		}
		for(int i=0;i<arrayCpu.length;i++){
			if(arrayCpu[i] != arrayFpga[i]){
				int type = i % 3;
				String typeStr = "";
				switch(type){
				case 0: typeStr = "function";break;
				case 1: typeStr = "val";break;
				case 2: typeStr = "ctx";break;
				}
				System.out.println(String.format("%s[%d] %s Diff, CPU=%d, FPGA=%d", arrayName, i/3, typeStr, arrayCpu[i], arrayFpga[i]));
				return false;
			}
		}
		return true;
	}*/
	
	public int getNumNonZero(int[] pcCoef){
		int cnt = 0;
		for(int i=0;i<pcCoef.length;i++){
			if(pcCoef[i] != 0){
				cnt++;
			}
		}
		return cnt;
	}

	final int DO_NOT_OUTPUT 			= 0;
	final int ENCODE_BIN				= 0;
	//final int ENCODE_BIN_EP				= 1;
	final int ENCODE_BINS_EP			= 1;
	
	//buffer
	int[] coded_sub_block_flag_buffer = 			new int[3];// the size should be narrowed down later, 3 ints are a unit:(function, val,param)
	int[] sig_coeff_flag_buffer = 					new int[3*16];
	int[] coeff_abs_level_greater1_flag_buffer = 	new int[3*16];//其实只有3*8是有效的，增加一倍是为了防止java代码数组越界
	//int[] escapeDataPresentInGroup_buffer = 		new int[3];// 在CPU中判断remaining的offset，所以输出这个
	int[] coeff_abs_level_greater2_flag_buffer = 	new int[3];
	int[] coeff_sign_flag_buffer = 					new int[3];
	int[] coeff_abs_level_remaining_buffer =		new int[3*2*16];
	int[] c1_buffer = 								{1};
	
	//32*32
	int[] scan_32 = new int[]{0,32,1,64,33,2,96,65,34,3,97,66,35,98,67,99,128,160,129,192,161,130,224,193,162,131,225,194,163,226,195,227,4,36,5,68,37,6,100,69,38,7,101,70,39,102,71,103,256,288,257,320,289,258,352,321,290,259,353,322,291,354,323,355,132,164,133,196,165,134,228,197,166,135,229,198,167,230,199,231,8,40,9,72,41,10,104,73,42,11,105,74,43,106,75,107,384,416,385,448,417,386,480,449,418,387,481,450,419,482,451,483,260,292,261,324,293,262,356,325,294,263,357,326,295,358,327,359,136,168,137,200,169,138,232,201,170,139,233,202,171,234,203,235,12,44,13,76,45,14,108,77,46,15,109,78,47,110,79,111,512,544,513,576,545,514,608,577,546,515,609,578,547,610,579,611,388,420,389,452,421,390,484,453,422,391,485,454,423,486,455,487,264,296,265,328,297,266,360,329,298,267,361,330,299,362,331,363,140,172,141,204,173,142,236,205,174,143,237,206,175,238,207,239,16,48,17,80,49,18,112,81,50,19,113,82,51,114,83,115,640,672,641,704,673,642,736,705,674,643,737,706,675,738,707,739,516,548,517,580,549,518,612,581,550,519,613,582,551,614,583,615,392,424,393,456,425,394,488,457,426,395,489,458,427,490,459,491,268,300,269,332,301,270,364,333,302,271,365,334,303,366,335,367,144,176,145,208,177,146,240,209,178,147,241,210,179,242,211,243,20,52,21,84,53,22,116,85,54,23,117,86,55,118,87,119,768,800,769,832,801,770,864,833,802,771,865,834,803,866,835,867,644,676,645,708,677,646,740,709,678,647,741,710,679,742,711,743,520,552,521,584,553,522,616,585,554,523,617,586,555,618,587,619,396,428,397,460,429,398,492,461,430,399,493,462,431,494,463,495,272,304,273,336,305,274,368,337,306,275,369,338,307,370,339,371,148,180,149,212,181,150,244,213,182,151,245,214,183,246,215,247,24,56,25,88,57,26,120,89,58,27,121,90,59,122,91,123,896,928,897,960,929,898,992,961,930,899,993,962,931,994,963,995,772,804,773,836,805,774,868,837,806,775,869,838,807,870,839,871,648,680,649,712,681,650,744,713,682,651,745,714,683,746,715,747,524,556,525,588,557,526,620,589,558,527,621,590,559,622,591,623,400,432,401,464,433,402,496,465,434,403,497,466,435,498,467,499,276,308,277,340,309,278,372,341,310,279,373,342,311,374,343,375,152,184,153,216,185,154,248,217,186,155,249,218,187,250,219,251,28,60,29,92,61,30,124,93,62,31,125,94,63,126,95,127,900,932,901,964,933,902,996,965,934,903,997,966,935,998,967,999,776,808,777,840,809,778,872,841,810,779,873,842,811,874,843,875,652,684,653,716,685,654,748,717,686,655,749,718,687,750,719,751,528,560,529,592,561,530,624,593,562,531,625,594,563,626,595,627,404,436,405,468,437,406,500,469,438,407,501,470,439,502,471,503,280,312,281,344,313,282,376,345,314,283,377,346,315,378,347,379,156,188,157,220,189,158,252,221,190,159,253,222,191,254,223,255,904,936,905,968,937,906,1000,969,938,907,1001,970,939,1002,971,1003,780,812,781,844,813,782,876,845,814,783,877,846,815,878,847,879,656,688,657,720,689,658,752,721,690,659,753,722,691,754,723,755,532,564,533,596,565,534,628,597,566,535,629,598,567,630,599,631,408,440,409,472,441,410,504,473,442,411,505,474,443,506,475,507,284,316,285,348,317,286,380,349,318,287,381,350,319,382,351,383,908,940,909,972,941,910,1004,973,942,911,1005,974,943,1006,975,1007,784,816,785,848,817,786,880,849,818,787,881,850,819,882,851,883,660,692,661,724,693,662,756,725,694,663,757,726,695,758,727,759,536,568,537,600,569,538,632,601,570,539,633,602,571,634,603,635,412,444,413,476,445,414,508,477,446,415,509,478,447,510,479,511,912,944,913,976,945,914,1008,977,946,915,1009,978,947,1010,979,1011,788,820,789,852,821,790,884,853,822,791,885,854,823,886,855,887,664,696,665,728,697,666,760,729,698,667,761,730,699,762,731,763,540,572,541,604,573,542,636,605,574,543,637,606,575,638,607,639,916,948,917,980,949,918,1012,981,950,919,1013,982,951,1014,983,1015,792,824,793,856,825,794,888,857,826,795,889,858,827,890,859,891,668,700,669,732,701,670,764,733,702,671,765,734,703,766,735,767,920,952,921,984,953,922,1016,985,954,923,1017,986,955,1018,987,1019,796,828,797,860,829,798,892,861,830,799,893,862,831,894,863,895,924,956,925,988,957,926,1020,989,958,927,1021,990,959,1022,991,1023};
	int[] scan_16 = new int[]{0,16,1,32,17,2,48,33,18,3,49,34,19,50,35,51,64,80,65,96,81,66,112,97,82,67,113,98,83,114,99,115,4,20,5,36,21,6,52,37,22,7,53,38,23,54,39,55,128,144,129,160,145,130,176,161,146,131,177,162,147,178,163,179,68,84,69,100,85,70,116,101,86,71,117,102,87,118,103,119,8,24,9,40,25,10,56,41,26,11,57,42,27,58,43,59,192,208,193,224,209,194,240,225,210,195,241,226,211,242,227,243,132,148,133,164,149,134,180,165,150,135,181,166,151,182,167,183,72,88,73,104,89,74,120,105,90,75,121,106,91,122,107,123,12,28,13,44,29,14,60,45,30,15,61,46,31,62,47,63,196,212,197,228,213,198,244,229,214,199,245,230,215,246,231,247,136,152,137,168,153,138,184,169,154,139,185,170,155,186,171,187,76,92,77,108,93,78,124,109,94,79,125,110,95,126,111,127,200,216,201,232,217,202,248,233,218,203,249,234,219,250,235,251,140,156,141,172,157,142,188,173,158,143,189,174,159,190,175,191,204,220,205,236,221,206,252,237,222,207,253,238,223,254,239,255};
	int[] notFirstGroupNeighbourhoodContextOffset  = {  3, 0};
	
	/**
	 * 
	 * @param pcCoef
	 * @param compId
	 * @param width
	 * @param scanFirstPos 	 对角扫描的最后一个元素的位置，从左上开始
	 * @param sigRight   		右边的4*4块是否存在非零元
	 * @param sigLower			下边的4*4块是否存在非零元
	 * @throws Exception
	 */
	
	public void codeCoeff4x4(int[] pcCoef, int pcCoefStartIdx, int compId,int width,int scanFirstPos, int sigRight, int sigLower, int preC1) throws Exception {
		// counter
		int inputCounter 					= 0;

		// loop variables
		int numNonZero 									= 0;//5 bits
		int c1 											= 1;//2 bits
		//int firstC2FlagIdx							= -1;
		//int escapeDataPresentInGroup					= 0;
		int hasOutput_coeff_abs_level_greater2_flag		= 0;//1 bit
		int coeffSigns									= 0;//16 bits
		int iFirstCoeff2								= 1;//1 bit
		int uiGoRiceParam 								= 0;//3 bits 0001000000000000000000100000

		for (int i = 0; i < 4 * 4; i++) {
			int input = pcCoef[i+pcCoefStartIdx];			
			int numNonZero_1 = input == 0 ? numNonZero : numNonZero + 1;
			
			//编码coded_sub_block_flag
			boolean isLastInput = inputCounter == 15;
			int base_coeff_group_ctx_idx = compId == 0 ? 42 : 44;
			int ui_ctx_sig = (sigRight + sigLower) != 0 ? 1 : 0;
			int coded_sub_block_flag_ctx = base_coeff_group_ctx_idx + ui_ctx_sig; 
			coded_sub_block_flag_buffer[0] = isLastInput ? ENCODE_BIN : DO_NOT_OUTPUT;
			coded_sub_block_flag_buffer[1] = isLastInput ? (numNonZero_1 > 0 ? 1: 0): DO_NOT_OUTPUT;
			coded_sub_block_flag_buffer[2] = isLastInput ? coded_sub_block_flag_ctx: DO_NOT_OUTPUT;

			//编码sig_coeff_flag
			int ui_sig = input != 0 ? 1 : 0;
			int base_ctx_idx = 46 + ((compId == 0)? 0: 28);
			int pattern_sig_ctx = sigRight + (sigLower << 1);
			int scan_current_pos = scanFirstPos - inputCounter;//当前的对角扫描的位置
			int rasterPosition = (width == 32) ? scan_32[scan_current_pos]: scan_16[scan_current_pos] ;//当前的顺序扫描的位置
			int uiLog2BlockWidth = (int)(Math.log(width)/Math.log(2));
			int pos_y = rasterPosition >> uiLog2BlockWidth;
			int pos_x = rasterPosition - (pos_y << uiLog2BlockWidth);
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
			int offset = (notFirstGroup ? notFirstGroupNeighbourhoodContextOffset[compId] : 0) + cnt;
			int firstSignificanceMapContext = width == 32 ? 21 : 12;
			int uiCtxSig1 = is_special_case_for_dc ? 0 : (firstSignificanceMapContext + offset);
			sig_coeff_flag_buffer[inputCounter * 3 + 0] = ENCODE_BIN;
			sig_coeff_flag_buffer[inputCounter * 3 + 1] = ui_sig;
			sig_coeff_flag_buffer[inputCounter * 3 + 2] = base_ctx_idx + uiCtxSig1;
			
			//编码coeff_abs_level_greater1_flag
			int absCoeff = (int)Math.abs(input);
			boolean isOutputCoeff_abs_level_greater1_flag = (absCoeff != 0 & numNonZero < 8);
			int uiSymbol = absCoeff > 1 ? 1 : 0;
			int notFirstSubsetOffset = (compId == 0 & (scanFirstPos > 15)) ? 2 : 0;
			int foundAGreaterThan1Offset = preC1 == 0 ? 1 : 0;
			int uiCtxSet = (compId==0?0:4) + notFirstSubsetOffset + foundAGreaterThan1Offset;
			int baseCtxMod = 150 + (4 * uiCtxSet);
			coeff_abs_level_greater1_flag_buffer[numNonZero * 3 + 0] = isOutputCoeff_abs_level_greater1_flag ? ENCODE_BIN : DO_NOT_OUTPUT;
			coeff_abs_level_greater1_flag_buffer[numNonZero * 3 + 1] = isOutputCoeff_abs_level_greater1_flag ? uiSymbol : DO_NOT_OUTPUT;
			coeff_abs_level_greater1_flag_buffer[numNonZero * 3 + 2] = isOutputCoeff_abs_level_greater1_flag ? baseCtxMod + c1 : DO_NOT_OUTPUT;
			boolean isBranch1 = (uiSymbol == 1) & isOutputCoeff_abs_level_greater1_flag;
			//boolean isBranch2 = isBranch1 & firstC2FlagIdx == -1;
			//boolean isBranch3 = isBranch1 & firstC2FlagIdx != -1;
			boolean isBranch4 = isOutputCoeff_abs_level_greater1_flag & (c1 < 3) & (c1 > 0);
			int c1_1 = isBranch1 ? 0 : (isBranch4 ? c1+1: c1);
			boolean firstC2Flag = isBranch1 ? true : false;
			//int escapeDataPresentInGroup_1 = isBranch3 ? 1 : escapeDataPresentInGroup;
			
			//编码coeff_abs_level_greater2_flag,是否存在这个编码取决于coeff_abs_level_greater1_flag是否存在1
			int symbol = absCoeff > 2 ? 1 : 0;
			boolean isBranch5 = (firstC2Flag & hasOutput_coeff_abs_level_greater2_flag == 0);
			//boolean isBranch6 = isBranch5 & (symbol != 0);
			int hasOutput_coeff_abs_level_greater2_flag_1 = (hasOutput_coeff_abs_level_greater2_flag == 1) ? 1 : isBranch5 ? 1 : 0;
			//int escapeDataPresentInGroup_2 = isBranch6 ? 1 : escapeDataPresentInGroup_1;
			int baseCtxMod_1 = 174 + (1 * uiCtxSet);
			coeff_abs_level_greater2_flag_buffer[0] = isBranch5 ? ENCODE_BIN : coeff_abs_level_greater2_flag_buffer[0];//DO_NOT_OUTPUT;
			coeff_abs_level_greater2_flag_buffer[1] = isBranch5 ? symbol : coeff_abs_level_greater2_flag_buffer[1];//DO_NOT_OUTPUT;
			coeff_abs_level_greater2_flag_buffer[2] = isBranch5 ? baseCtxMod_1 : coeff_abs_level_greater2_flag_buffer[2];//DO_NOT_OUTPUT;
			
			//输出escapeDataPresentInGroup
			//int escapeDataPresentInGroup_3 = is_input_end ? ((escapeDataPresentInGroup_2 == 1 | (num_non_zero_1 > 8)) ? 1 : 0) : escapeDataPresentInGroup_2;
			
			//编码coeff_sign_flag
			boolean inputNotZero = ui_sig == 1;
			int coeffSigns_1 = inputNotZero ? (2 * coeffSigns + ((input < 0) ? 1 : 0)) : coeffSigns;
			coeff_sign_flag_buffer[0] = isLastInput ? ENCODE_BINS_EP : DO_NOT_OUTPUT;
			coeff_sign_flag_buffer[1] = isLastInput ? coeffSigns_1 : DO_NOT_OUTPUT;
			coeff_sign_flag_buffer[2] = isLastInput ? numNonZero_1 : DO_NOT_OUTPUT;
			
			//编码coeff_abs_level_remaining
			int baseLevel  = (numNonZero < 8)? (2 + iFirstCoeff2 ) : 1;
			boolean isBranch7 = inputNotZero & absCoeff >= baseLevel;
			boolean isBranch8 = inputNotZero & absCoeff >= 2;
			boolean isBranch9 = isBranch7 & absCoeff > (3 << uiGoRiceParam);
			int escapeCodeValue = absCoeff - baseLevel;
			boolean isBranch10 = isBranch7 & (escapeCodeValue < (3 << uiGoRiceParam));
			boolean isBranch11 = isBranch7 & !(escapeCodeValue < (3 << uiGoRiceParam));
			int length = isBranch10 ? escapeCodeValue >> uiGoRiceParam : isBranch11 ? uiGoRiceParam : 0;
			int size = 7;
			int[] escapeCodeValueArray = new int[size];
			int[] lengthArray = new int[size];
			escapeCodeValueArray[0] = escapeCodeValue - ( 3 << uiGoRiceParam);
			lengthArray[0] = length;
			for(int j=0;j<size-1;j++){
				boolean isWhileTrue = escapeCodeValueArray[j] >= (1<<lengthArray[j]);
				lengthArray[j+1] = isWhileTrue ? lengthArray[j] + 1 : lengthArray[j];
				escapeCodeValueArray[j+1] = isWhileTrue ? escapeCodeValueArray[j] - (1<<(lengthArray[j])) : escapeCodeValueArray[j];
			}
			int coeff_abs_level_remaining_part1_val = isBranch10 ? (1<<(length+1))-2 : isBranch11 ? (1<<(3+lengthArray[size-1]+1-uiGoRiceParam))-2 : 0;
			int coeff_abs_level_remaining_part1_len = isBranch10 ? length+1 : isBranch11 ? 3+lengthArray[size-1]+1-uiGoRiceParam : 0;
			int coeff_abs_level_remaining_part2_val = isBranch10 ? (escapeCodeValue%(1<<uiGoRiceParam)) : isBranch11 ? escapeCodeValueArray[size-1] : 0;
			int coeff_abs_level_remaining_part2_len = isBranch10 ? uiGoRiceParam : isBranch11 ? lengthArray[size-1] : 0;
			boolean isOutput_coeff_abs_level_remaining = isBranch10 | isBranch11;
			coeff_abs_level_remaining_buffer[numNonZero * 6 + 0] = isOutput_coeff_abs_level_remaining ? ENCODE_BINS_EP : coeff_abs_level_remaining_buffer[numNonZero * 6 + 0];//DO_NOT_OUTPUT
			coeff_abs_level_remaining_buffer[numNonZero * 6 + 1] = isOutput_coeff_abs_level_remaining ? coeff_abs_level_remaining_part1_val : coeff_abs_level_remaining_buffer[numNonZero * 6 + 1];//DO_NOT_OUTPUT
			coeff_abs_level_remaining_buffer[numNonZero * 6 + 2] = isOutput_coeff_abs_level_remaining ? coeff_abs_level_remaining_part1_len : coeff_abs_level_remaining_buffer[numNonZero * 6 + 2];//DO_NOT_OUTPUT
			coeff_abs_level_remaining_buffer[numNonZero * 6 + 3] = isOutput_coeff_abs_level_remaining ? ENCODE_BINS_EP : coeff_abs_level_remaining_buffer[numNonZero * 6 + 3];//DO_NOT_OUTPUT
			coeff_abs_level_remaining_buffer[numNonZero * 6 + 4] = isOutput_coeff_abs_level_remaining ? coeff_abs_level_remaining_part2_val : coeff_abs_level_remaining_buffer[numNonZero * 6 + 4];//DO_NOT_OUTPUT
			coeff_abs_level_remaining_buffer[numNonZero * 6 + 5] = isOutput_coeff_abs_level_remaining ? coeff_abs_level_remaining_part2_len : coeff_abs_level_remaining_buffer[numNonZero * 6 + 5];//DO_NOT_OUTPUT
			int iFirstCoeff2_1 = isBranch8 ? 0 : iFirstCoeff2;
			int uiGoRiceParam_1 = isBranch9 ? Math.min((uiGoRiceParam + 1), 4) : uiGoRiceParam;
			
			//存储C1
			c1_buffer[0] = isOutputCoeff_abs_level_greater1_flag ? c1_1 : c1_buffer[0];  
			
			//循环利用的变量
			numNonZero = numNonZero_1;
			c1 = c1_1;
			hasOutput_coeff_abs_level_greater2_flag = hasOutput_coeff_abs_level_greater2_flag_1;
			//escapeDataPresentInGroup = escapeDataPresentInGroup_3;
			coeffSigns = coeffSigns_1;
			iFirstCoeff2 = iFirstCoeff2_1;
			uiGoRiceParam = uiGoRiceParam_1;
			inputCounter++;
		}

	}
}