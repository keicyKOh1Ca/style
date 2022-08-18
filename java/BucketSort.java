package sort.bucketsort;

import java.util.Arrays;

/**
 * バケットソート<br>
 * ※同数時にも対応<br>
 * </pre>
 */
public class Sorter
{
	/**
	 * メイン<br>
	 *
	 * @param args
	 */
	public static void main(String[] args)
	{
		int[] nums = {8, 1, 2, 6, 7, 5, 3, 8}; // ソート対象の数値
		
		// bucket準備
		int max = 0;
		
		for (int i = 0; i < nums.length; i++)
		{
			
			if (nums[max] < nums[i])
			{
				max = i;
			}
		}
		max = nums[max];
		
		Integer[][] bucketNum = new Integer[max][nums.length];
		//System.out.println(max);
		
		int sort_1 = 0;
		int sort_2 = 0;
		
		for (int j = 0; j < nums.length;)
		{
			sort_1 = nums[j] - 1;
			
			if (bucketNum[sort_1][sort_2] == null)
			{
				bucketNum[sort_1][sort_2] = nums[j];
				j++;
				sort_2 = 0;
			}
			else
			{
				sort_2++;
			}
		}
		println(bucketNum);
	}
	
	/**
	 * ソート後の中身を出力<br>
	 */
	private static void println(int[] nums)
	{
		System.out.println(Arrays.toString(nums));
	}
	
	/**
	 * ソート後の中身を出力<br>
	 */
	private static void println(Integer[][] nums)
	{
		
		for (Integer[] num : nums)
		{
			
			for (Integer n : num)
			{
				
				if (n != null)
				{
					System.out.print(n + " | ");
				}
			}
		}
	}
}
